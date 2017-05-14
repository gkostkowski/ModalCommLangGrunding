package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.sun.istack.internal.Nullable;

import java.util.*;

public class Grounder {

    public static final double MIN_POS = 0.2;
    public static final double MAX_POS = 0.6;
    public static final double MIN_BEL = 0.7;
    public static final double MAX_BEL = 0.9;
    public static final double KNOW = 1.0;


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



    static ModalOperator determineFulfillment(Agent agent, DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        ModalOperator res;
        for (Formula mentalModel : dk.getComplementaryFormulas()) {
            res = determineFulfillment(dk, mentalModel);
        }
        return null; //todo ?
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
     * @param dk    Distributed knowledge for respective grounding sets related with certain formula.
     * @return Type of operator which can be applied to formula given through distribution of knowledge.
     * @see DistributedKnowledge
     */
    public static ModalOperator determineFulfillment(DistributedKnowledge dk, Formula formula) throws InvalidFormulaException, NotApplicableException {
        if (!dk.isDkComplex() && !dk.getFormula().equals(formula)
                || dk.isDkComplex() && !dk.getComplementaryFormulas().contains(formula))
            throw new NotApplicableException("Given formula is not related to specified knowledge distribution.");

        return checkEpistemicConditions(formula, dk);
    }


    /**
     * Decides which modal operator can occur for given formula. If none of possible, then null is returned.
     *
     * @param formula
     * @param dk
     * @param timestamp
     * @return
     */
    @Nullable
    public static ModalOperator checkEpistemicConditions(Formula formula, DistributedKnowledge dk,
                                                          int timestamp) throws NotApplicableException {
        boolean amongNoClearStateObjects = true;
        boolean amongClearStateObjects = true;

        for (int i = 0; i < formula.getTraits().size(); i++) {  //supports complex formulas
            Set<IndividualModel> clearStatesObjects = dk.getRelatedObservationsBase()
                    .getIMsByTraitStates(formula.getTraits().get(i), new State[]{State.IS, State.IS_NOT}, timestamp);
            amongNoClearStateObjects = amongNoClearStateObjects && !new ArrayList<>(clearStatesObjects).contains(formula.getModel());

            Set<IndividualModel> selectedStatesObjects = dk.getRelatedObservationsBase()
                    .getIMsByTraitState(formula.getTraits().get(i), formula.getStates().get(i), timestamp);
            amongClearStateObjects = amongClearStateObjects && new ArrayList<>(selectedStatesObjects).contains(formula.getModel());
        }

        boolean isPresentInWM = !dk.getDkClassByDesc(formula, BPCollection.MemoryType.WM).isEmpty();
        ModalOperator res = null;

        if (amongNoClearStateObjects) {
            double currRelCard = relativeCard(dk.getGroundingSets(), timestamp, formula);
            ModalOperator[] checkedOps = {ModalOperator.POS, ModalOperator.BEL, ModalOperator.KNOW};
            for (int i = 0; i < checkedOps.length && res == null; i++)
                res = checkEpistemicCondition(true, isPresentInWM, currRelCard, checkedOps[i]);
        } else if (amongClearStateObjects)
            res = ModalOperator.KNOW;

        return res;
    }

    public static ModalOperator checkEpistemicConditions(Formula formula, DistributedKnowledge dk)
            throws NotApplicableException {
        return checkEpistemicConditions(formula, dk, dk.getTimestamp());
    }

    /**
     * Performs checking for single epistemic condition.
     *
     * @param amongNoClearStateObjects Flag determining if related individual model is among other individual models
     *                                 neither described as having trait(s) (listed in formula) nor not having mentioned trait(s).
     * @param isPresentInWM            Flag determining weather desired observation (agreeable with formula) is present
     *                                 in one of distributed knowledge classes related with working memory.
     * @param relativeCard             Relative cardinality for given formula.
     * @param inspectedOperator        Modal operator which possibility of occurrence is examined.
     * @return Given modal operator if it is applicable or null in other way.
     */
    private static ModalOperator checkEpistemicCondition(boolean amongNoClearStateObjects, boolean isPresentInWM,
                                                          double relativeCard, ModalOperator inspectedOperator) {
        double minRange, maxRange;
        switch (inspectedOperator) {
            case POS:
                minRange = MIN_POS;
                maxRange = MAX_POS;
                break;
            case BEL:
                minRange = MIN_BEL;
                maxRange = MAX_BEL;
                break;
            default:
                minRange = maxRange = KNOW;
        }
        return amongNoClearStateObjects && isPresentInWM && relativeCard >= minRange && relativeCard <= maxRange ?
                inspectedOperator : null;
    }


    /**
     * Defines grounded set A1(t) responsible for induction of mental model m1 connected to individualModel p and trait P
     * A1 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience individualModel o,having trait P
     *
     * @param o    Object observed by agent
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
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
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
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

    public static double relativeCard(Map<Formula, Set<BaseProfile>> groundingSets, int time, Formula formula) throws NotApplicableException {
        if (formula.getType().equals(Formula.Type.SIMPLE_MODALITY)) {

            switch (((ComplexFormula) formula).getOperator()) {
                case AND:
                    if ((boolean) ((ComplexFormula) formula).getLeftPart().isNegated() && (boolean) ((ComplexFormula) formula).getRightPart().isNegated()) {
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().getTrait(), ((ComplexFormula) formula).getRightPart().getTrait(), time, groundingSets.get(formula), 4);
                    }
                    if ((boolean) !((ComplexFormula) formula).getLeftPart().isNegated() && (boolean) ((ComplexFormula) formula).getRightPart().isNegated()) {
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().getTrait(), ((ComplexFormula) formula).getRightPart().getTrait(), time, groundingSets.get(formula), 3);
                    }
                    if ((boolean) ((ComplexFormula) formula).getLeftPart().isNegated() && (boolean) !((ComplexFormula) formula).getRightPart().isNegated()) {
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().getTrait(), ((ComplexFormula) formula).getRightPart().getTrait(), time, groundingSets.get(formula), 2);
                    }
                    if ((boolean) ((ComplexFormula) formula).getLeftPart().isNegated() && (boolean) ((ComplexFormula) formula).getRightPart().isNegated()) {
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().getTrait(), ((ComplexFormula) formula).getRightPart().getTrait(), time, groundingSets.get(formula), 1);
                    }
                    break;
                case OR:
                    break;
                default:
                    break;
            }
        } else {
            return getCard(groundingSets.get(formula), time);
        }
        return 0.0;
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
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @param i    indicator,indicating which case we'd like to use
     * @return
     */


    static Set<BaseProfile> getGroundingSetsConjunction(Trait P, Trait Q, int time, Set<BaseProfile> all,
                                                        int i) {
        /*Set<BaseProfile> out = new HashSet<BaseProfile>();
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
        }*/
        return null;
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
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
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
     * @param dk    Distributed knowledge for respective grounding sets related with certain formula.
     * @return Double value of Type of operator which can be applied to formula given through distribution of knowledge.
     * @see DistributedKnowledge
     */
    static double determineFulfillmentDouble(DistributedKnowledge dk, Formula formula) throws InvalidFormulaException, NotApplicableException {
        if (!dk.isDkComplex() && !dk.getFormula().equals(formula)
                || dk.isDkComplex() && !dk.getComplementaryFormulas().contains(formula))
            throw new NotApplicableException("Given formula is not related to specified knowledge distribution.");

        return checkEpistemicConditionsDouble(formula, dk,dk.getTimestamp());
    }


    /**
     * Returns value of fullfilment of epistemic condition. If none of possible, then null is returned.
     *
     * @param formula
     * @param dk
     * @param timestamp
     * @return
     */
    @Nullable
    public static Double checkEpistemicConditionsDouble(Formula formula, DistributedKnowledge dk,
                                                         int timestamp) throws NotApplicableException {
        boolean amongNoClearStateObjects = true;
        boolean amongClearStateObjects = true;

        for (int i = 0; i < formula.getTraits().size(); i++) {
            Set<IndividualModel> clearStatesObjects = dk.getRelatedObservationsBase()
                    .getIMsByTraitStates(formula.getTraits().get(i), new State[]{State.IS, State.IS_NOT}, timestamp);
            amongNoClearStateObjects = amongNoClearStateObjects && !new ArrayList<>(clearStatesObjects).contains(formula.getModel());

            Set<IndividualModel> selectedStatesObjects = dk.getRelatedObservationsBase()
                    .getIMsByTraitState(formula.getTraits().get(i), formula.getStates().get(i), timestamp);
            amongClearStateObjects = amongClearStateObjects && new ArrayList<>(selectedStatesObjects).contains(formula.getModel());
        }

        if (amongNoClearStateObjects) {
            return relativeCard(dk.getGroundingSets(), timestamp, formula);
        } else if (amongClearStateObjects)
            return 1.0;
        return 0.0;
    }

}
