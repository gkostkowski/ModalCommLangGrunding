package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.episodic.BPCollection;
import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.holons.Holon;
import com.pwr.zpi.semantic.IndividualModel;
import com.sun.istack.internal.Nullable;

import java.util.*;

/**
 * Class contains methods used in process of grounding natural language.
 */
public class Grounder {

    /**
     * Thresholds for simple modalities.
     */
    public static final double MIN_POS = 0.2;
    public static final double MAX_POS = 0.6;
    public static final double MIN_BEL = 0.7;
    public static final double MAX_BEL = 0.9;
    public static final double KNOW = 1.0;

    /**
     * Thresholds for modal conjunctions.
     */
    private static final double CONJ_MIN_POS = 0.1;
    private static final double CONJ_MAX_POS = 0.6;
    private static final double CONJ_MIN_BEL = 0.65;
    private static final double CONJ_MAX_BEL = 0.9;
    public static final double CONJ_KNOW = 1.0;

    /**
     * Arrays of thresholds for certain formula types. Order of elements in arrays is strictly defined and can't be other:
     * [MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW].
     */
    private final static double[] simpleThresholds = new double[]{MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW};
    private final static double[] conjThresholds = new double[]{CONJ_MIN_POS, CONJ_MAX_POS, CONJ_MIN_BEL, CONJ_MAX_BEL, CONJ_KNOW};
    private final static double[] disjThresholds = new double[]{}; //todo

    /**
     * Gives complete collection of grounding sets for certain formulas (in this context may be known as mental model).
     * It supports simple and complex formulas.
     *
     * @param formulas Considered formulas.
     * @param all      Sef of all available (for agent) base profiles, regardless memory type.
     * @return Map of grounding sets as values and respective formulas as keys.
     */
    public static Map<Formula, Set<BaseProfile>> getGroundingSets(Collection<Formula> formulas, Set<BaseProfile> all) throws InvalidFormulaException {
        if (formulas == null || all == null)
            throw new NullPointerException("One of parameters is null.");

        Map<Formula, Set<BaseProfile>> res = new HashMap<>();

        for (Formula f : formulas)
            res.put(f, getGroundingSet(f, all));
        return res;
    }

    public static Map<Formula, Set<BaseProfile>> getGroundingSets(Formula formula, Set<BaseProfile> all) throws InvalidFormulaException {
        return getGroundingSets(formula.getComplementaryFormulas(), all);
    }

    /**
     * Method produces grounding set for certain formula, basing on provided set of base profiles.
     *
     * @param formula
     * @param all
     * @return
     * @throws InvalidFormulaException
     */
    public static Set<BaseProfile> getGroundingSet(Formula formula, Set<BaseProfile> all) throws InvalidFormulaException {
        if (formula == null || all == null)
            throw new NullPointerException("One of parameters is null.");

        Set<BaseProfile> res = new HashSet<>();
        for (BaseProfile bp : all) {
            if (formula.isFormulaFulfilled(bp))
                res.add(bp);
        }
        return res;
    }


    /**
     * Method realises complete grounding of given formula. Formula is treated as standard formula (in non-negated form),
     * so according to complementarity of mental models, grounding is performed for all complementary formulas to this one.
     * The following assumption was made: For any extended formula (modal formula) there are only one modal operator
     * which can be applied to this formula at once. In the other hand, for given formula, formulas complementary to this
     * one can also be grounded.
     * So, according to above, for simple formula, there is at most two possible grounded forms of this formula,
     * for modal conjunction - four.
     * This method returns modal operators which can be used in certain complementary formulas without breaking
     * epistemic fulfillment relationship's conditions.
     * Timestamp is taken from given distribution of knowledge.
     *
     * @param agent
     * @param formula Formula which will be grounded.
     * @return Map of Formula and ModalOperator, where each entry (Formula, ModalOperator) is understood as form
     * of grounded formula with given modal operator.
     * @see DistributedKnowledge
     */
    public static Map<Formula, ModalOperator> performFormulaGrounding(Agent agent, Formula formula)
            throws InvalidFormulaException, NotApplicableException, NotConsistentDKException {

        DistributedKnowledge dk = agent.distributeKnowledge(formula, true);
        List<Formula> complementaryFormulas = formula.getStandardFormula().getComplementaryFormulas();
        Map<Formula, ModalOperator> res = new HashMap<>();
        ModalOperator currOperator = null;
        int timestamp = dk.getTimestamp();

        for (Formula currFormula : complementaryFormulas)
            if ((currOperator = checkEpistemicConditions(currFormula, dk, agent.getHolons().getHolon(currFormula, timestamp))) != null)
                res.put(currFormula, currOperator);

        return res;
    }


    /**
     * Realizes verification of epistemic fulfillment relationship's conditions for provided formula.
     * The given formula should be associated with given knowledge distribution and it's treated as "exact" formula
     * (namely, processed as it was given).
     * Method decides which modal operator can occur for given formula. If none of possible, then null is returned.
     * This is an exact part of grounding process.
     * Note: Modal operators such BEL or KNOW may only occur if last related observation didn't provide sufficient
     * information about state of given trait in object (namely, state was MAYHAPS).
     *
     * @param formula
     * @param dk
     * @param timestamp
     * @return
     */
    @Nullable
    public static ModalOperator checkEpistemicConditions(Formula formula, DistributedKnowledge dk, Holon holon,
                                                         int timestamp) throws NotApplicableException {
        boolean hasLastClearState = true;
        boolean amongClearStateObjects = true;

        BaseProfile lastBP = dk.getRelatedObservationsBase()
                .getBaseProfile(timestamp, BPCollection.MemoryType.WM);
        for (Trait selectedTrait :formula.getTraits()) {  //supports complex formulas
            hasLastClearState = hasLastClearState &&
                    lastBP.isContainingClearDescriptionFor(formula.getModel(), selectedTrait);
        }
        /*for (int i = 0; i < formula.getTraits().size(); i++) {  //supports complex formulas
            Set<IndividualModel> clearStatesObjects = dk.getRelatedObservationsBase()
                    .getIMsByTraitStates(formula.getTraits().get(i), new State[]{State.IS, State.IS_NOT}, timestamp);
            hasLastClearState = hasLastClearState && !new ArrayList<>(clearStatesObjects).contains(formula.getModel());

            Set<IndividualModel> selectedStatesObjects = dk.getRelatedObservationsBase()
                    .getIMsByTraitState(formula.getTraits().get(i), formula.getStates().get(i), timestamp);
            amongClearStateObjects = amongClearStateObjects && new ArrayList<>(selectedStatesObjects).contains(formula.getModel());
        }*/

        boolean isPresentInWM = !dk.getDkClassByDesc(formula, BPCollection.MemoryType.WM).isEmpty();
        ModalOperator res = null;

        if (!hasLastClearState) {
//            double currRelCard = relativeCard(dk.getGroundingSets(), timestamp, formula);
            double currRelCard = holon.getSummary(formula);
            ModalOperator[] checkedOps = {ModalOperator.POS, ModalOperator.BEL, ModalOperator.KNOW};
            double[] appropriateTresholds = getThresholds(formula);
            for (int i = 0; i < checkedOps.length && res == null; i++)
                res = checkEpistemicCondition(true, isPresentInWM, currRelCard,
                        checkedOps[i], appropriateTresholds);
        } else
            res = formula.isFormulaFulfilled(lastBP) ? ModalOperator.KNOW : null;

        return res;
    }

    /**
     * Returns array of appropriate thresholds for formula with specified type.
     * @param formula
     * @return
     */
    private static double[] getThresholds(Formula formula) {
        switch (formula.getType()) {
            case SIMPLE_MODALITY:
                return simpleThresholds;
            case MODAL_CONJUNCTION:
                return conjThresholds;
            case MODAL_DISJUNCTION:
                return disjThresholds;
            default:
                return null;
        }
    }

    public static ModalOperator checkEpistemicConditions(Formula formula, DistributedKnowledge dk, Holon holon)
            throws NotApplicableException {
        return checkEpistemicConditions(formula, dk, holon, dk.getTimestamp());
    }

    /**
     * Performs checking for single epistemic condition according to provided thresholds. Thresholds determine way of grounding
     * modal operators - simple modalities or modal conjunctions/alternatives thresholds can be provided.
     * Note: this metod determines modal operator in uncertainity conditions, so it won't work for know when amongNoClearStateObjects ==false.
     *
     * @param amongNoClearStateObjects Flag determining if related individual model is among other individual models
     *                                 neither described as having trait(s) (listed in formula) nor not having mentioned trait(s).
     * @param isPresentInWM            Flag determining weather desired observation (agreeable with formula) is present
     *                                 in one of distributed knowledge classes related with working memory.
     * @param relativeCard             Relative cardinality for given formula.
     * @param inspectedOperator        Modal operator which possibility of occurrence is examined.
     * @param thresholds               Array of doubles which should contains thresholds in following order: [MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW].
     * @return Given modal operator if it is applicable or null in other way.
     */
    public static ModalOperator checkEpistemicCondition(boolean amongNoClearStateObjects, boolean isPresentInWM,
                                                         double relativeCard, ModalOperator inspectedOperator, double[] thresholds) {
        if (thresholds.length != 5)
            throw new IllegalStateException("Invalid thresholds.");
        double minRange, maxRange;
        switch (inspectedOperator) {
            case POS:
                minRange = thresholds[0];
                maxRange = thresholds[1];
                break;
            case BEL:
                minRange = thresholds[2];
                maxRange = thresholds[3];
                break;
            default:
                minRange = maxRange = thresholds[4];
        }
        return amongNoClearStateObjects && isPresentInWM && relativeCard >= minRange && relativeCard < maxRange ?
                inspectedOperator : null;
    }


    /**
     * Defines grounded set A1(t) responsible for induction of mental model m1 connected to individualModel p and trait P
     * A1 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience individualModel o,having trait P
     *
     * @param o    Object observed by agent
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set of BaseProfiles gives us set from which we'll evaluate those which contain Positive Traits
     * @return List of BaseProfiles which contain Positive Traits
     */

    static Set<BaseProfile> getGroundingSetsPositiveTrait(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all) {
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
        /*for (BaseProfile bp : all) {
            if (bp.DetermineIfSetHasTrait(P, time)) {
                baseout.add(bp);
            }
        }*/
        return baseout;
    }

    /**
     * Defines grounded set A2(t) responsible for induction of mental model m1 connected to individualModel p and trait P
     * A2 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience individualModel o,not having trait P
     *
     * @param o    Object observed by agent
     * @param P    Trait of individualModel
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set of BaseProfiles gives us set from which we'll evaluate those which contain Positive Traits
     * @return Set of BaseProfiles which contain Positive Traits
     */


    static Set<BaseProfile> getGroundingSetsNegativeTrait(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all) {
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
      /*  for (BaseProfile bp : all) {
            if (!bp.DetermineIfSetHasNotTrait(P, time)) {
                baseout.add(bp);
            }
        }*/
        return baseout;
    }

/**
 * Inductive cardinality GAi grounding set A1
 *
 * @param groundingSet set of Base Profiles which cardinality we desire to know
 * @param t            given time
 * @return Positive Cardinality of Set
 */


    /**
     * Value of relative power of grounding lambda for base form p(o)
     *
     * @param time given time
     * @return Cardinality (ratio) of Positive BaseProfiles to all
     */

    public static double relativePositiveCard(Set<BaseProfile> groundingSetPositive, Set<BaseProfile> groundingSetNegative, int time) {

        if (groundingSetNegative.isEmpty()) {
            return 0;
        }
        return getCard(groundingSetPositive, time) / (getCard(groundingSetNegative, time) + getCard(groundingSetPositive, time));
    }

    /**
     * Value of relative power of grounding lambda for base form not p(o)
     *
     * @param time Given time
     * @return Cardinality (ratio) of Negative BaseProfiles to all
     */

    static double relativeNegativeCard(Set<BaseProfile> groundingSetPositive, Set<BaseProfile> groundingSetNegative, int time) {
        if (groundingSetPositive.isEmpty()) {
            return 0;
        }
        return getCard(groundingSetPositive, time) / (getCard(groundingSetNegative, time) + getCard(groundingSetPositive, time));
    }

    public static double relativeCard(DistributedKnowledge dk, Formula formula) throws NotApplicableException, InvalidFormulaException {
        if (!formula.getType().equals(Formula.Type.SIMPLE_MODALITY)) {

            switch (((ComplexFormula) formula).getOperator()) {
                case AND:
                    return complexFormulaFinalGrounder(formula,dk);
                case OR:
                    break;
                default:
                    break;
            }
        } else {
            return dk.getGroundingSet(formula).size();
        }
        return 0.0;
    }


    public static double relativeCard(Map<Formula, Set<BaseProfile>> groundingSets, Formula formula)
            throws NotApplicableException, InvalidFormulaException {
        return 0.0;//relativeCard() todo Jarema
    }

        /**
         * Defines grounded set Ai(t) responsible for induction of mental model mi connected to baseProfile which
         * involves connotations with both observations P,and Q.Depending on i
         * i=1 - Returns BaseProfiles where Object o has Trait P and has Trait Q
         * i=2 - Returns BaseProfiles where Object o has Trait P and does not have Trait Q
         * i=3 - Returns BaseProfiles where Object o does not have Trait P and has Trait Q
         * i=4 - Returns BaseProfiles where Object o does not have Trait P and does not have Trait Q
         *
         * @param P    Trait of individualModel
         * @param Q    Trait of individualModel
         * @param time Time taken into consideration when looking for expieriences
         * @param all  Set of BaseProfiles gives us set from which we'll evaluate those which contain Positive Traits
         * @param i    indicator,indicating which case we'd like to use
         * @return
         */


    static Set<BaseProfile> getGroundingSetsConjunction(Trait P, Trait Q, int time, Set<BaseProfile> all,
                                                        int i) {
        Set<BaseProfile> out = new HashSet<BaseProfile>();
        switch (i) {
            case 1:
                for (BaseProfile bp : all) {
                    if (bp.DetermineIfSetHasTrait(P, time) && bp.DetermineIfSetHasTrait(Q, time)) out.add(bp);
                }
                break;

            case 2:
                for (BaseProfile bp : all) {
                    if (bp.DetermineIfSetHasTrait(P, time) && !bp.DetermineIfSetHasTrait(Q, time))
                        out.add(bp);
                }
                break;

            case 3:
                for (BaseProfile bp : all) {
                    if (!bp.DetermineIfSetHasTrait(P, time) && bp.DetermineIfSetHasTrait(Q, time))
                        out.add(bp);
                }
                break;

            case 4:
                for (BaseProfile bp : all) {
                    if (!bp.DetermineIfSetHasTrait(P, time) && !bp.DetermineIfSetHasTrait(Q, time))
                        out.add(bp);
                }
                break;
        }
        return out;
    }

    /**
     * Inductive cardinality GAi grounding set Ai
     *
     * @param groundingSet Grounding set which cardinality we wish to know
     * @param t            Time taken into consideration when looking for expieriences
     * @return Cardinality of given set
     */


    static double getCard(Set<BaseProfile> groundingSet, int t) {
        return groundingSet.size();
    }

    /**
     * Value of relative power of grounding lambda for base form indicated by i
     * i = 1 p(o) and q(o)
     * i = 2 p(o) and not q(o)
     * i = 3 not p(o) and q(o)
     * i = 4 not p(o) and not q(o)
     *
     * @param P    Trait of individualModel
     * @param Q    Trait of individualModel
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set of BaseProfiles gives us set from which we'll evaluate those which contain Positive Traits
     * @param i    indicator,indicating which case we'd like to use
     * @return
     */

    static double relativeCardConunction(Trait P, Trait Q, int time, Set<BaseProfile> all, int i) {
        Set<BaseProfile> Sum = new HashSet<BaseProfile>();

        //OgarnijAdAlla, nie dodaje tych samych obiektów
        Sum.addAll(getGroundingSetsConjunction(P, Q, time, all, 1));
        Sum.addAll(getGroundingSetsConjunction(P, Q, time, all, 2));
        Sum.addAll(getGroundingSetsConjunction(P, Q, time, all, 3));
        Sum.addAll(getGroundingSetsConjunction(P, Q, time, all, 4));
        return getCard(getGroundingSetsConjunction(P, Q, time, all, i), time) / getCard(Sum, time);
    }

    /**
     * Realizes verification of epistemic fulfillment relationship's conditions for provided formula.
     * The given formula should be associated with given knowledge distribution.
     * Checks what type of extension of formula can occur. The following assumption was made: For any extended formula
     * (modal formula) there are only one modal operator which can be applied to this formula at once.
     * According to that, this method returns type of operator which can be used in formula without breaking
     * epistemic fulfillment relationship's conditions.
     * Timestamp is taken from given distribution of knowledge.
     *
     * @param dk Distributed knowledge for respective grounding sets related with certain formula.
     * @return Double value of Type of operator which can be applied to formula given through distribution of knowledge.
     * @see DistributedKnowledge
     */
    public static double determineFulfillmentDouble(DistributedKnowledge dk, Formula formula) throws InvalidFormulaException, NotApplicableException {

        if (!dk.isDkComplex() && !dk.getFormula().equals(formula)
                || dk.isDkComplex() && !new ArrayList(dk.getComplementaryFormulas()).contains(formula))
            throw new NotApplicableException("Given formula is not related to specified knowledge distribution.");
        return checkEpistemicConditionsDouble(formula, dk, dk.getTimestamp());
    }


    /**
     * Returns value of fullfilment of epistemic condition. If none of possible, then 0.0 is returned.
     *
     * @param formula Considered Formula
     * @param dk      Distributed knowledge for respective grounding sets related with certain formula.
     * @return
     */
    @Nullable
    public static Double checkEpistemicConditionsDouble(Formula formula, DistributedKnowledge dk
            , int timestamp) throws NotApplicableException {
       /* double mayhapsD = 0;
        for (int i = 0; i < formula.getTraits().size(); i++) {  //supports complex formulas
            mayhapsD = dk.getRelatedObservationsBase().getMayhapsBP(timestamp,formula,i);
        }*/
        if (formula.getType() == Formula.Type.SIMPLE_MODALITY) {
            return simpleFormulaFinalGrounder(formula, dk);
        } else if (formula.getType() == Formula.Type.MODAL_CONJUNCTION) {
            return complexFormulaFinalGrounder(formula, dk);

        }
        return 0.0;
    }


    /**
     * Returns number of occurrences in grounded formulas for given formula.Case of simple formulas.
     *
     * @param formula Considered Formula
     * @param dk      Distributed knowledge for respective grounding sets related with certain formula.
     * @return
     */
    public static Double simpleFormulaFinalGrounder(Formula formula, DistributedKnowledge dk) {

        double sum = 0;
        System.out.println(formula.getTraits().get(0) + " " + dk.getTimestamp());
        for (BaseProfile bp : dk.getGroundingSet(formula)) {
            if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.IS) && !((SimpleFormula) formula).isNegated()) {
                sum++;
            } else if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.IS_NOT) && ((SimpleFormula) formula).isNegated()) {
                sum++;
            } else if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.MAYHAPS)) {
                sum++;
            }
        }

        if (sum != 0) {
            return sum / dk.getRelatedObservationsBase().getCompleteSize(dk.getTimestamp());
        }
        return 0.0;
    }

    /**
     * Returns number of occurrences in grounded formulas for given formula.Case of Complex formulas.
     *
     * @param formula Considered Formula
     * @param dk      Distributed knowledge for respective grounding sets related with certain formula.
     * @return
     */
    public static Double complexFormulaFinalGrounder(Formula formula, DistributedKnowledge dk) {

        double sum = 0;
        if (dk.getGroundingSet(formula).size() == 0) {
            return 0.0;
        }
        sum = dk.getGroundingSet(formula).size();

        if (sum != 0) {
            //System.out.println("sumsum" +sum + " " + formula);
            return sum / dk.getRelatedObservationsBase().getCompleteSize(dk.getTimestamp());
        }
        return 0.0;
    }

}
