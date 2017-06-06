package com.pwr.zpi.language;

import com.pwr.zpi.Agent;
import com.pwr.zpi.episodic.BPCollection;
import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.holons.NonBinaryHolon;
import com.pwr.zpi.util.Permutation;
import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

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
     * Thresholds for modal disjunctions. //TODO rozwazyc inne wartosci
     */
    private static final double DISJ_MIN_POS = 0.4;
    private static final double DISJ_MAX_POS = 0.55;
    private static final double DISJ_MIN_BEL = DISJ_MAX_POS;
    private static final double DISJ_MAX_BEL = 1.0;
    public static final double DISJ_KNOW = 1.0;

    private static final double EX_DISJ_MIN_POS = 0.21;
    private static final double EX_DISJ_MAX_POS = 0.67;
    private static final double EX_DISJ_MIN_BEL = DISJ_MAX_POS;
    private static final double EX_DISJ_MAX_BEL = 1;
    public static final double EX_DISJ_KNOW = 1.0;

    /**
     * Arrays of thresholds for certain formula types. Order of elements in arrays is strictly defined and can't be other:
     * [MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW].
     */
    public final static double[] simpleThresholds = new double[]{MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW};
    public final static double[] conjThresholds = new double[]{CONJ_MIN_POS, CONJ_MAX_POS, CONJ_MIN_BEL, CONJ_MAX_BEL, CONJ_KNOW};
    public final static double[] disjThresholds = new double[]{DISJ_MIN_POS, DISJ_MAX_POS, DISJ_MIN_BEL, DISJ_MAX_BEL, DISJ_KNOW};
    public final static double[] exDisjThresholds = new double[]{EX_DISJ_MIN_POS, EX_DISJ_MAX_POS, EX_DISJ_MIN_BEL, EX_DISJ_MAX_BEL, EX_DISJ_KNOW};
    private static final double DEF_DISJ_EPS_MULTIPLIER = 1;
    private static final double DEF_EX_DISJ_EPS_MULTIPLIER = 0.25;
    private static final double EPS_FST_COEFFICIENT = 1 / 6;
    private static final double EPS_SND_COEFFICIENT = 1 / 2;
    private static final Double DEF_EPS_LOWER_RANGE = 0.0;

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
            res.put(f, composeGroundingSet(f, all));
        return res;
    }

    /**
     * Method intermediates between getGroundingSets() method and getGroundingSet() which provides concrete grounding
     * set. This method was developed to allow seamless grounding process for conjunctions and disjunctions which base on
     * particular conjunctive grounding sets.
     * Note: it also supports simple modalities.
     *
     * @param f   Formula which is used to generate appropriate grounding set. Supports simple modalities, modal
     *            conjunctions and modal disjunctions (regular and exclusive). There are some additional remarks related
     *            to complex formulas:
     *            <ul>
     *            <li>There is one conjunctive grounding set for each conjunctive formula</li>
     *            <li>There is one disjunctive grounding set for each disjunctive formula, which bases on some conjunctive
     *            grounding sets:
     *            <ul>
     *            <li>disjunctive grounding set for formula <i>P XOR Q</i> uses conjunctive grounding sets for
     *            <u>P AND NOT Q</u> and <u>NOT P AND Q</u>
     *            <li>disjunctive grounding set for formula <i>P OR Q</i> uses same conjunctive grounding sets as in
     *            case of <i>P XOR Q</i> and additionally grounding set for <u>P AND Q</u></li>
     *            </ul></li>
     *            </ul>
     * @param all Set of all available base profiles.
     * @return Grounding set for given formula: conjunctive or disjunctive.
     */
    public static Set<BaseProfile> composeGroundingSet(Formula f, Set<BaseProfile> all) throws InvalidFormulaException {
        Set<BaseProfile> res = new HashSet<>();

        for (Formula partialFormula : f.getDependentFormulas())
            res.addAll(getGroundingSet(partialFormula, all));
        return res;
    }

    public static Map<Formula, Set<BaseProfile>> getGroundingSets(Formula formula, Set<BaseProfile> all) throws InvalidFormulaException {
        return getGroundingSets(formula.getComplementaryFormulas(), all);
    }

    /**
     * Method produces grounding set for certain formula, basing on provided set of base profiles. Method supports
     * simple modalities and modal conjunctions. In order to build grounding set for disjunction. please use
     * <em>composeGroundingSet()</em> which uses this method in particular way to build desired grounding sets.
     *
     * @param formula Simple modality and modal conjunction.
     * @param all     Set of all available base profiles.
     * @return Grounding set for given formula.
     * @throws InvalidFormulaException
     */
    private static Set<BaseProfile> getGroundingSet(Formula formula, Set<BaseProfile> all) throws InvalidFormulaException {
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
            if ((currOperator = checkEpistemicConditions(currFormula, dk,
                    agent.getSummarization(currFormula, timestamp))) != null)
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
     * @param summarization
     * @return
     */
    @Nullable
    public static ModalOperator checkEpistemicConditions(Formula formula, DistributedKnowledge dk, Map<Formula, Double> summarization,
                                                         int timestamp) throws NotApplicableException {
        boolean hasLastClearState = true;

        BaseProfile lastBP = dk.getRelatedObservationsBase()
                .getBaseProfile(timestamp, BPCollection.MemoryType.WM);
        for (Trait selectedTrait : formula.getTraits()) {  //supports complex formulas
            hasLastClearState = hasLastClearState &&
                    lastBP.isContainingClearDescriptionFor(formula.getModel(), selectedTrait);
        }


        boolean isPresentInWM = !dk.getDkClassByDesc(formula, BPCollection.MemoryType.WM).isEmpty();
        ModalOperator res = null;

        if (!hasLastClearState) {
//            double currRelCard = holon.getSummary(formula);
            double currRelCard = summarization.get(formula);
            ModalOperator[] checkedOps = {ModalOperator.POS, ModalOperator.BEL, ModalOperator.KNOW};
            double[] appropriateTresholds = getThresholds(formula);
            for (int i = 0; i < checkedOps.length && res == null; i++)
                res = checkEpistemicCondition(true, isPresentInWM, currRelCard,
                        checkedOps[i], appropriateTresholds);
        } else
            res = formula.isFormulaFulfilled(lastBP) ? ModalOperator.KNOW : null;

        if (formula.needEpsilonConcentrationChecking()) {
            if(!isEpsilonConcentrated((ComplexFormula) formula, dk.getRelatedObservationsBase().getBaseProfiles()))
                res=null;
        }
        return res;
    }



    /**
     * Returns array of appropriate thresholds for specified type.
     *
     * @param type
     * @return
     */
    private static double[] getThresholds(Formula.Type type) {
        switch (type) {
            case SIMPLE_MODALITY:
                return simpleThresholds;
            case MODAL_CONJUNCTION:
                return conjThresholds;
            case MODAL_DISJUNCTION:
                return disjThresholds;
            case MODAL_EXCLUSIVE_DISJUNCTION:
                return exDisjThresholds;
            default:
                return null;
        }
    }

    /**
     * Returns array of appropriate thresholds for formula with specified type.
     *
     * @param formula
     * @return
     */
    private static double[] getThresholds(Formula formula) {
        return getThresholds(formula.getType());
    }

    public static ModalOperator checkEpistemicConditions(Formula formula, DistributedKnowledge dk, Map<Formula, Double> summarization)
            throws NotApplicableException {
        return checkEpistemicConditions(formula, dk, summarization, dk.getTimestamp());
    }

    /**
     * Inductive cardinality GAi grounding set A1
     *
     * @param groundingSet set of Base Profiles which cardinality we desire to know
     * @param t            given time
     * @return Positive Cardinality of Set
     */


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
        return amongNoClearStateObjects && isPresentInWM && relativeCard >= minRange && (relativeCard < maxRange || maxRange == minRange) ?
                inspectedOperator : null;
    }

    /**
     * Method counts relative cardinality of grounding sets. Method receives certain number of grounding sets and for
     * each of them counts relative cardinality.
     *
     * @param groundingSets
     * @return
     * @throws InvalidFormulaException
     */
    public static Map<Formula, Double> relativeCard_(Map<Formula, Set<BaseProfile>> groundingSets) throws InvalidFormulaException {
        Map<Formula, Double> res = new HashMap<>();
        int totalSize = groundingSets.entrySet().stream()
                .map(x -> x.getValue().size())
                .reduce(0, (s1, s2) -> s1 + s2);
        for (Formula f : groundingSets.keySet()) {
            Set<BaseProfile> currGroundingSet = groundingSets.get(f);
            if (f.getType().equals(Formula.Type.MODAL_DISJUNCTION) || f.getType().equals(Formula.Type.MODAL_EXCLUSIVE_DISJUNCTION))
                currGroundingSet = getGroundingSet(f, BPCollection.asBaseProfilesSet(groundingSets.values()));
            Double relativeCard = (double) currGroundingSet.size() / (double) totalSize;
            res.put(f, relativeCard);
        }
        return res;
    }


    /**
     * Method provides family of sets of (complex) formulas. Such family of sets is used to determine power of coincidence
     * among particular grounding sets when evaluating fulfillment of epistemic relation in case of disjunctions.
     *
     * @param protoform Formula which will be used to build relevant family of sets.
     * @return formulas sets family.
     */
    static List<Set<ComplexFormula>> getFormulasSetsFamily(ComplexFormula protoform) {
        List<Formula> complForm;
        try {
            complForm = protoform.getStandardFormula().getComplementaryFormulas();
        } catch (InvalidFormulaException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Unable to build family of sets for given formula.", e);
            return null;
        }
        return new ArrayList<Set<ComplexFormula>>() {{
            add(new HashSet<ComplexFormula>() {{
                add((ComplexFormula) complForm.get(0));
            }});
            add(new HashSet<ComplexFormula>() {{
                add((ComplexFormula) complForm.get(2));
            }});
            add(new HashSet<ComplexFormula>() {{
                add((ComplexFormula) complForm.get(1));
            }});
            add(new HashSet<ComplexFormula>() {{
                add((ComplexFormula) complForm.get(3));
            }});
            add(new HashSet<ComplexFormula>() {{
                add((ComplexFormula) complForm.get(1));
                add((ComplexFormula) complForm.get(2));
            }});
            add(new HashSet<ComplexFormula>() {{
                add((ComplexFormula) complForm.get(0));
                add((ComplexFormula) complForm.get(1));
                add((ComplexFormula) complForm.get(2));
            }});
        }};
    }

    /**
     * Method counts range of possible values of concentration-epsilon which is used to determine power of coincidence
     * among particular grounding sets.
     * @param formula Related formula.
     * @return Pair of double values where key is lower threshold and value is upper threshold.
     * @throws NotApplicableException
     */
    static javafx.util.Pair<Double, Double> getConcentrationEpsilonRange(ComplexFormula formula) throws NotApplicableException {
        switch (formula.getType()) {
            case MODAL_DISJUNCTION:
                return new javafx.util.Pair<>(DEF_EPS_LOWER_RANGE, countEpsilonConcentrationPartial(DEF_DISJ_EPS_MULTIPLIER));
            case MODAL_EXCLUSIVE_DISJUNCTION:
                return new javafx.util.Pair<>(DEF_EPS_LOWER_RANGE, countEpsilonConcentrationPartial(DEF_EX_DISJ_EPS_MULTIPLIER));
            default:
                throw new NotApplicableException("ConcentrationEpsilon cannot be counted for this type of formula.");
        }

    }

    static private double countEpsilonConcentrationPartial(double multiplier) {
        Formula.Type type = Formula.Type.MODAL_CONJUNCTION;
        double minPos = getThresholds(type)[0];
        double posBel = getThresholds(type)[1] == getThresholds(type)[2] ? getThresholds(type)[1] :
                (getThresholds(type)[1] + getThresholds(type)[2])/2.0;
        return multiplier*Math.min(EPS_FST_COEFFICIENT-minPos, posBel-EPS_SND_COEFFICIENT);
    }

    /**
     * By default, returns value from the middle.
     */
    static private double getConcentrationEpsilon(ComplexFormula formula) throws NotApplicableException {
        javafx.util.Pair<Double, Double> range = getConcentrationEpsilonRange(formula);
        return (range.getKey()+range.getValue())/2;
    }


    /**
     * Method performs checking of fourth epistemic condition dedicated for disjunctions. Condition determines if
     * set of respective disjunctive formulas is epsilon-concentrated according to given family of formula sets, epsilon
     * and with usage of relative grounding sets cardinality function.
     * @param formula
     * @param relevantFamily
     * @param dependentFormulas
     * @param concentrationEpsilon
     * @param episodicSet
     * @return
     */
    private static boolean isEpsilonConcentrated(ComplexFormula formula, List<Set<ComplexFormula>> relevantFamily,
                                                 Collection<Formula> dependentFormulas, double concentrationEpsilon, Set<BaseProfile> episodicSet) {
        boolean isMemberOfFamily=false;
        for (Set<ComplexFormula> l : relevantFamily)
            isMemberOfFamily = new ArrayList<>(l).containsAll(dependentFormulas) || isMemberOfFamily;
        if (!isMemberOfFamily) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Given dependentFormulas set does not belong to specified family of formulas");
            return false;
        }
        try {
            return countSetDiameter(formula, dependentFormulas, episodicSet) <= concentrationEpsilon
                    && isSetMinimal(formula, dependentFormulas, episodicSet, concentrationEpsilon);
        } catch (InvalidFormulaException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Cannot determine epsilon concentration.", e);
            return false;
        }
    }

    private static boolean isEpsilonConcentrated(ComplexFormula formula, Set<BaseProfile> episodicSet) {
        try {
            return isEpsilonConcentrated(formula, getFormulasSetsFamily(formula), formula.getDependentFormulas(),
                    getConcentrationEpsilon(formula), episodicSet);
        } catch (NotApplicableException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Epsilon-concentration checking was not performed.", e);
            return false;
        }
    }

    private static boolean isSetMinimal(ComplexFormula formula, Collection<Formula> formulasSet,
                                        Set<BaseProfile> episodicSet, double concentrationEpsilon) {
        if (formulasSet.isEmpty()){
                Logger.getAnonymousLogger().log(Level.WARNING, "Provided collection of formulas cannot be empty.");
                return false;
            }
        if (formulasSet.size() ==1)
            return true;
        else {
            List<ArrayList<Formula>> allPosMinimalSets = Permutation
                    .getAllPossiblePermutations(new ArrayList<Formula>(formulasSet), formula.comparator());
            for (List<Formula> minimalElem : allPosMinimalSets) {
                try {
                    if(countSetDiameter(formula, minimalElem, episodicSet) < concentrationEpsilon)
                        return false;
                } catch (InvalidFormulaException e) {
                    Logger.getAnonymousLogger().log(Level.WARNING, "Cannot determine IsMinimal condition.", e);
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Counts diameter for given set of formulas. This value indicates how much relative cardinality for grounding sets
     * (which are built according to formulas in provided set of formulas) differs.
     * @return
     */
    private static double countSetDiameter(ComplexFormula dependentConj, Collection<Formula> dependentFormulas,
                                           Set<BaseProfile> allBPs) throws InvalidFormulaException {
        DoubleStream relCards = relativeCard_(getGroundingSets(dependentConj, allBPs))
                .entrySet()
                .stream()
                .filter(entry -> dependentFormulas.contains(entry.getKey()))
                .mapToDouble(entry -> entry.getValue());
        return relCards.max().getAsDouble() - relCards.min().getAsDouble();
    }


    //===========

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
            if (bp.determineIfSetHasTrait(P, time)) {
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
            if (!bp.determineIfSetHasNotTrait(P, time)) {
                baseout.add(bp);
            }
        }*/
        return baseout;
    }

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
                    return complexFormulaFinalGrounder(formula, dk);
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


    /**
     * Returns relative cardinality of given map of sets.If last observation connected to given formula had state MAYHAPS , method deletes last observation from groundingSets and counts
     * cardinality only for those left.
     *
     * @param groundingSets Map containing Formulas as keys and Sets of BaseProfiles as variables
     * @param formula       Given formula for which we count cardinalit
     * @return Cardinality of formula in all given groundingSets
     * @throws NotApplicableException
     * @throws InvalidFormulaException
     */
    public static double relativeCard(Map<Formula, Set<BaseProfile>> groundingSets, Formula formula)
            throws NotApplicableException, InvalidFormulaException {
        if (formula.getType() == Formula.Type.SIMPLE_MODALITY && !groundingSets.get(formula).toArray
                (new BaseProfile[groundingSets.get(formula).size()])[groundingSets.get(formula).size() - 1]
                .isContainingClearDescriptionFor(formula.getModel(), formula.getTraits().get(0))) {
            groundingSets.get(formula).remove(groundingSets.get(formula).toArray()[groundingSets.get(formula).size()]);

            double out = 0;
            for (BaseProfile bp : groundingSets.get(formula)) {
                out += bp.getDescribedByTraits().size();
                //out += bp.getIndefiniteByTraits().size();
                out += bp.getNotDescribedByTraits().size();
            }
            return out;
        } else if (formula.getType() == Formula.Type.MODAL_CONJUNCTION && (!groundingSets.get(formula).toArray
                (new BaseProfile[groundingSets.get(formula).size()])[groundingSets.get(formula).size() - 1]
                .isContainingClearDescriptionFor(formula.getModel(), formula.getTraits().get(0)) ||
                !groundingSets.get(formula).toArray
                        (new BaseProfile[groundingSets.get(formula).size()])[groundingSets.get(formula).size() - 1]
                        .isContainingClearDescriptionFor(formula.getModel(), formula.getTraits().get(1)))) {
            groundingSets.get(formula).remove(groundingSets.get(formula).toArray()[groundingSets.get(formula).size()]);
            double out = 0;
            for (BaseProfile bp : groundingSets.get(formula)) {
                out += bp.getDescribedByTraits().size();
                //out += bp.getIndefiniteByTraits().size();
                out += bp.getNotDescribedByTraits().size();
            }
            return out;
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
                    if (bp.determineIfSetHasTrait(P, time) && bp.determineIfSetHasTrait(Q, time)) out.add(bp);
                }
                break;

            case 2:
                for (BaseProfile bp : all) {
                    if (bp.determineIfSetHasTrait(P, time) && !bp.determineIfSetHasTrait(Q, time))
                        out.add(bp);
                }
                break;

            case 3:
                for (BaseProfile bp : all) {
                    if (!bp.determineIfSetHasTrait(P, time) && bp.determineIfSetHasTrait(Q, time))
                        out.add(bp);
                }
                break;

            case 4:
                for (BaseProfile bp : all) {
                    if (!bp.determineIfSetHasTrait(P, time) && !bp.determineIfSetHasTrait(Q, time))
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
    public static double determineFulfillmentDouble(DistributedKnowledge dk, Formula formula,Map<Formula, Set<BaseProfile>>  context) throws InvalidFormulaException, NotApplicableException {

        if (!dk.isDkComplex() && !dk.getFormula().equals(formula)
                || dk.isDkComplex() && !new ArrayList(dk.getComplementaryFormulas()).contains(formula))
            throw new NotApplicableException("Given formula is not related to specified knowledge distribution.");
        return checkEpistemicConditionsDouble(formula, dk, dk.getTimestamp(), context);
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
            , int timestamp,Map<Formula, Set<BaseProfile>>  context) throws NotApplicableException, InvalidFormulaException {
       /* double mayhapsD = 0;
        for (int i = 0; i < formula.getTraits().size(); i++) {  //supports complex formulas
            mayhapsD = dk.getRelatedObservationsBase().getMayhapsBP(timestamp,formula,i);
        }*/
        if (formula.getType() == Formula.Type.SIMPLE_MODALITY) {
            return simpleFormulaFinalGrounder(formula, dk,context);
        } else if (formula.getType() == Formula.Type.MODAL_CONJUNCTION) {
            return complexFormulaFinalGrounder(formula, dk,context);

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
    public static Double simpleFormulaFinalGrounder(Formula formula, DistributedKnowledge dk,Map<Formula, Set<BaseProfile>>  context) throws InvalidFormulaException, NotApplicableException {

        double sum = 0;
        if(context.size()==0) {
            for (BaseProfile bp : dk.getGroundingSet(formula)) {
                if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.IS) && !((SimpleFormula) formula).isNegated()) {
                    sum++;
                } else if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.IS_NOT) && ((SimpleFormula) formula).isNegated()) {
                    sum++;
                } else if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.MAYHAPS)) {
                    sum++;
                }
            }
        }
        else{
            if(context.get(formula)!=null){
            for (BaseProfile bp : context.get(formula)) {
                if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.IS) && !((SimpleFormula) formula).isNegated()) {
                    sum++;
                } else if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.IS_NOT) && ((SimpleFormula) formula).isNegated()) {
                    sum++;
                } else if (bp.checkIfObserved(formula.getModel(), formula.getTraits().get(0), State.MAYHAPS)) {
                    sum++;
                }
            }}
        }
        if (sum != 0) {
            return sum /relativeCard(dk.mapOfGroundingSets(),formula);
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
    public static Double complexFormulaFinalGrounder(Formula formula, DistributedKnowledge dk,Map<Formula, Set<BaseProfile>>  context) throws InvalidFormulaException {

        double sum = 0;
        if(context.size()==0) {
            sum = context.get(formula).size();
            if (sum != 0) {
                return sum / getGroundingSetsForComplementaryFormula(dk, formula);
            }
        }
        else{
            sum = dk.getGroundingSet(formula).size();
            if (sum != 0) {
                return sum / getGroundingSetsForComplementaryFormula(dk, formula);
            }
        }
        return 0.0;
    }

    public static int getGroundingSetsForComplementaryFormula(DistributedKnowledge dk, Formula f) throws InvalidFormulaException {
        int size = 0;
        if (f.getType() == Formula.Type.SIMPLE_MODALITY) {
            for (Formula fi : ((SimpleFormula) f).getComplementaryFormulas()) {
                size += dk.getGroundingSet(fi).size();
            }
        } else {
            for (Formula fi : NonBinaryHolon.getComplementaryFormulasv2((ComplexFormula) f)) {
                size += dk.getGroundingSet(fi).size();
            }
        }
        return size;
    }
}
