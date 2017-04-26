package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;

import java.util.*;

/**
 *
 */
public class Grounder {

    public static final double MIN_POS = 0.1;  //todo set proper values
    public static final double MAX_POS = 0.6;
    public static final double MIN_BEL = 0.61;
    public static final double MAX_BEL = 0.99;
    private static final double KNOW = 1.0;
/*
    /**
     * Gives complete collection of grounding sets for certain formula. It supports simple and complex formulas.
     *
     * @param formula Considered formula.
     * @param time
     * @param all
     * @param states  Represents especially two states: IS and IS_NOT, which determines if simple formula, part of
     *                complex formula will require checking associated to observations described by trait or observations
     *                NOT described by trait.
     * @return Collection of grounding sets.
     */
    static Map<Formula, Set<BaseProfile>> getGroundingSets(Formula formula, int time, Set<BaseProfile> all) throws InvalidFormulaException {
        Observation o = formula.getObservation();
        List<Trait> traits = formula.getTraits();
        State [] states = new State[traits.size()];
        List<State> s = formula.getStates();
        states = s.toArray(states);
        if (o == null || traits == null || states == null)
            throw new InvalidFormulaException("Invalid formula");

        List<Formula> parts = new ArrayList<>();
        Map<Formula, Set<BaseProfile>> res = new HashMap<>();

        Operators.Type type = null;
        boolean isComplex = false;
        if (isComplex = formula instanceof ComplexFormula) {
            parts.addAll(((ComplexFormula) formula).getParts());
            type = ((ComplexFormula) formula).getOperator().getType();
        } else
            parts.add(formula);

        int fstStateCounter = 0,
                sndStateCounter = 0;
        for (Formula atomicFormula : parts) {
            for (State state : states) {
                List<State> statesSeq = Arrays.asList(states[fstStateCounter], states[sndStateCounter]);
                Formula mentalModel = isComplex ? new ComplexFormula(o, traits, statesSeq, type) : new SimpleFormula(o, traits, statesSeq);
                Set<BaseProfile> currSet = null;
                res.put(mentalModel, currSet = new HashSet<>());
                for (BaseProfile bp : all) {
                    if (isFulfilled(new ArrayList<>(traits), time, statesSeq, type, bp))
                        currSet.add(bp);
                }
                fstStateCounter = (fstStateCounter + 1) % states.length;
            }
            sndStateCounter = (sndStateCounter + 1) % states.length;
        }
        return res;
    }

    /**
     * Checks if condition (for sample or complex formula) is fulfilled. While building condition, takes into account order
     * of traits and states in given lists.
     * In most cases, it runs for one or two iterations (for complex formula): state1(trait1(o)) op state2(trait2(o)).
     *
     * @param traits
     * @param time
     * @param states Should be placed in certain positions, respectiovely to traits.
     * @param op
     * @param bp
     * @return
     */
    static private boolean isFulfilled( List<Trait> traits, int time, List<State> states, Operators.Type op, BaseProfile bp) {
        if (traits.size() != states.size())
            throw new IllegalStateException("Number of traits differs from amount of states.");

        if (op == null) //case for simple formula
            op = Operators.Type.AND;
        boolean res = op.equals(Operators.Type.AND) ? true : false; //so far, applicable only for AND or OR
        for (int i = 0; i < traits.size(); i++) {
            boolean curr = bp.DetermineIfSetHasTrait(traits.get(i), time);
            switch (op) {
                case AND:
                    res = res && curr;
                    break;
                case OR:
                    res = res || curr;
            }
        }
        return res;
    }
/*
    static Map<Formula, Set<BaseProfile>> getGroundingSets(Formula formula, int time, Set<BaseProfile> all) throws InvalidFormulaException {
        return getGroundingSets(formula, time, all);
    }
    */
/*

    */
/**
     * Defines grounded set A1(t) responsible for induction of mental model m1 connected to observation p and trait P
     * A1 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience observation o,having trait P
     *
     * @param o     Object observed by agent
     * @param time  Time taken into consideration when looking for expieriences
     * @param all   Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @return List of BaseProfiles which contain Positive Traits
     */

    static Set<BaseProfile> getGroundingSetsPositiveTrait(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all) {
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
        for (BaseProfile bp : all) {
            if (bp.DetermineIfSetHasTrait(P, time)) {
                baseout.add(bp);
            }
        }
        return baseout;
    }

/**
     * Defines grounded set A2(t) responsible for induction of mental model m1 connected to observation p and trait P
     * A2 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience observation o,not having trait P
     *
     * @param o    Object observed by agent
     * @param P    Trait of observation
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @return Set of BaseProfiles which contain Positive Traits
     */


    static Set<BaseProfile> getGroundingSetsNegativeTrait(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all) {
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
        for (BaseProfile bp : all) {
            if (!bp.DetermineIfSetHasNotTrait(P, time)) {
                baseout.add(bp);
            }
        }
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
     * @param groundingSetPositive Set of Positive BaseProfiles
     * @param groundingSetNegative Set of Negative BaseProfiles
     * @param time                 given time
     * @return Cardinality (ratio) of Positive BaseProfiles to all
     */

    static double relativePositiveCard(Set<BaseProfile> groundingSetPositive, Set<BaseProfile> groundingSetNegative, int time) {
        if (groundingSetNegative.isEmpty()) {
            return 0;
        }
        return getCard(groundingSetPositive, time) / (getCard(groundingSetNegative, time) + getCard(groundingSetPositive, time));
    }

/**
     * Value of relative power of grounding lambda for base form not p(o)
     *
     * @param groundingSetPositive Set of Positive BaseProfiles
     * @param groundingSetNegative Set of Negative BaseProfiles
     * @param time                 Given time
     * @return Cardinality (ratio) of Negative BaseProfiles to all
     */

    static double relativeNegativeCard(Set<BaseProfile> groundingSetPositive, Set<BaseProfile> groundingSetNegative, int time) {
        if (groundingSetPositive.isEmpty()) {
            return 0;
        }
        return getCard(groundingSetPositive, time) / (getCard(groundingSetNegative, time) + getCard(groundingSetPositive, time));
    }

    public static double relativeCard(Map<Formula, Set<BaseProfile>> groundingSets, int time, Formula formula) throws NotApplicableException {
        if(formula.getType().equals(Formula.Type.SIMPLE_MODALITY)){

            switch(((ComplexFormula) formula).getOperator()){
                case AND:
                    if((boolean) ((ComplexFormula) formula).getLeftPart().isNegated && (boolean) ((ComplexFormula) formula).getRightPart().isNegated){
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().trait,((ComplexFormula) formula).getRightPart().trait ,time,groundingSets.get(formula),4);
                    }
                    if((boolean) !((ComplexFormula) formula).getLeftPart().isNegated && (boolean) ((ComplexFormula) formula).getRightPart().isNegated){
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().trait,((ComplexFormula) formula).getRightPart().trait ,time,groundingSets.get(formula),3);
                    }
                    if((boolean) ((ComplexFormula) formula).getLeftPart().isNegated && (boolean) !((ComplexFormula) formula).getRightPart().isNegated){
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().trait,((ComplexFormula) formula).getRightPart().trait ,time,groundingSets.get(formula),2);
                    }
                    if((boolean) ((ComplexFormula) formula).getLeftPart().isNegated && (boolean) ((ComplexFormula) formula).getRightPart().isNegated){
                        return relativeCardConunction(((ComplexFormula) formula).getLeftPart().trait,((ComplexFormula) formula).getRightPart().trait ,time,groundingSets.get(formula),1);
                    }
                    break;
                case OR:
                    break;
                default:
                    break;
            }
        }
        else{
            return getCard(groundingSets.get(formula),time);
        }
        return 0.0;
    }


/**
     * Builds distributed knowledge, which will be used to make respective mental models associated
     * with formulas. It can be used to build distribution of different mental models.
     * Builded distributed knowledge is related to certain moment in time.
     *
     * @param agent   The knowledge subject.
     * @param formula Formula which
     * @param time    Certain moment in time.
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
 */

    static DistributedKnowledge distributeKnowledge(Agent agent, Formula formula, int time) throws InvalidFormulaException {
        return new DistributedKnowledge(agent, formula, time);
    }

    static Operators.Type determineFulfillment(Agent agent, DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        Operators.Type res;
        for (Formula mentalModel : dk.getMentalModels()) {
            res = determineFulfillment(agent, dk, mentalModel);
        }
        return null; //todo ?
    }
/**
 * Allows to perform multiple checking for epistemical condition fulfillment for same knowledge distribution and
 * different formulas (but associated with mental models used to generate this certain knowledge distribution).
 *
 * @param agent
 * @param dk
 * @param formulas
 * @return
 */

    static boolean determineFulfillments(Agent agent, DistributedKnowledge dk, Formula... formulas) throws InvalidFormulaException, NotApplicableException {
        List<Operators.Type> res = new ArrayList<>();
        for (Formula f : formulas) {
            res.add(determineFulfillment(agent, dk, f));
        }
        //do sth with results //todo
        return false;
    }

/**
     * Realizes verification of epistemic fulfillment relationship's conditions for formula given through
     * knowledge distribution. The given formula should be associated with given knowledge distribution.
     * Checks what type of extension of formula can occur. The following assumption was made: For any extended formula
     * (modal formula) there are only one modal operator which can be applied to this formula at once.
     * According to that, this method returns type of operator which can be used in formula without breaking
     * epistemic fulfillment relationship's conditions.
     * Timestamp is taken from given distribution of knowledge.
     *
     * @param agent Subject of knowledge.
     * @param dk    Distributed knowledge for respective grounding sets related with certain formula.
     * @return Type of operator which can be applied to formula given through distribution of knowledge.
     * @see DistributedKnowledge
     */

    static Operators.Type determineFulfillment(Agent agent, DistributedKnowledge dk, Formula formula) throws InvalidFormulaException, NotApplicableException {
        int timestamp = dk.getTimestamp();
        BaseProfile lmBp = new BaseProfile(dk.getTimestamp());
        BaseProfile wmBp = new BaseProfile(dk.getTimestamp());
        Set<Object> objects = new HashSet<>();

        Observation describedObj = formula.getObservation();
        List<Trait> describedTraits = formula.getTraits();
        List<State> states = formula.getStates();
        //mentalModel.
        //boolean isNegated = state.equals(State.IS) ? true : false;

        List<Set<Object>> objsWithClearState = new ArrayList<>();
/**
         * Represents objsWithPositiveState or objsWithNegativeState - depending on state value
         */

        List<Set<Object>> objsWithGivenState = new ArrayList<>();

        List<Set<Object>> indefiniteByTrait = new ArrayList<>();
        BPCollection.MemoryType selectedMemory = BPCollection.MemoryType.WM;


//        setCommonObjects(timestamp, agent, dk, lmBp, wmBp, observations, describedObj, describedTrait, objsWithClearState,
//                objsWithGivenState, indefiniteByTrait, isNegated);
        setCommonObjects(timestamp, agent, lmBp, wmBp, objects, describedObj, new HashSet<>(describedTraits), objsWithClearState,
                objsWithGivenState, indefiniteByTrait, states);

        Map<Formula, Set<BaseProfile>> groundingSets = dk.getGroundingSets();
        Set<BaseProfile> selsectedClass = dk.getDkClassByDesc(formula, selectedMemory);

        return checkEpistemicConditions(indefiniteByTrait, describedObj, groundingSets, selsectedClass, timestamp, objsWithGivenState, formula);
    }


    private static void setCommonObjects(int timestamp, Agent agent, BaseProfile lmBp, BaseProfile wmBp,
                                         Set<Object> objects, Object describedObj, Set<Trait> describedTraits,
                                         List<Set<Object>> objsWithClearState, //one set for each trait
                                         List<Set<Object>> objsWithGivenState,
                                         List<Set<Object>> indefiniteByTrait, List<State> states) throws InvalidFormulaException {
        lmBp.copy(agent.getKnowledgeBase().getBaseProfile(timestamp, BPCollection.MemoryType.LM));
        wmBp.copy(agent.getKnowledgeBase().getBaseProfile(timestamp, BPCollection.MemoryType.WM));
        objects.addAll(BaseProfile.getObjects(lmBp, wmBp));
//        describedObj.copy(dk.getObservation());
//        describedTrait.copy(dk.getTrait());

        if (describedTraits.size() != states.size())
            throw new com.pwr.zpi.exceptions.InvalidFormulaException("States doesn't match to given traits.");
        Iterator<State> stateIt = states.iterator();
        for (Trait t : describedTraits) {
            objsWithClearState.add(new HashSet<>(Observation.getObjects(
                    lmBp.getNotDescribedByTrait(t),
                    wmBp.getNotDescribedByTrait(t),
                    lmBp.getDescribedByTrait(t),
                    wmBp.getDescribedByTrait(t))));

            State state = stateIt.next();
            objsWithGivenState.add(new HashSet<>(Observation.getObjects(
                    lmBp.getByTraitState(t, state),
                    wmBp.getByTraitState(t, state))));

            indefiniteByTrait.add(new HashSet<Object>(objects));
            indefiniteByTrait.removeAll(objsWithClearState);
        }

    }

/**
     * Decides for which modal operator, formula given through observations related to traits, can occur.
     *
     * @param indefiniteByTrait
     * @param describedObj
     * @param
     * @param timestamp
     * @param
     * @param
     * @return
     */

    private static Operators.Type checkEpistemicConditions(List<Set<Object>> indefiniteByTrait, Object describedObj,
                                                           Map<Formula, Set<BaseProfile>> groundingSets, Set<BaseProfile> selectedClass,
                                                           int timestamp, List<Set<Object>> objsWithGivenState,
                                                           Formula formula) throws NotApplicableException {
        if (eachContains(indefiniteByTrait, describedObj, Operators.Type.AND) && !selectedClass.isEmpty()) {
            double currRelCard = relativeCard(groundingSets, timestamp, formula);
            if (currRelCard >= MIN_POS && currRelCard < MAX_POS)
                return Operators.Type.POS;
            if (currRelCard >= MIN_BEL && currRelCard < MAX_BEL)
                return Operators.Type.BEL;
            if (currRelCard == KNOW)
                return Operators.Type.KNOW;
        } else {
            if (eachContains(objsWithGivenState, describedObj, Operators.Type.AND))
                return Operators.Type.KNOW;
            else {
/*can use AND, OR, XOR*/


            }
        }
        return null;
    }


    //todo move to utils
    private static <T extends Object> boolean eachContains(Collection<Set<T>> c, Object obj, Operators.Type op) {
        boolean res = op.equals(Operators.Type.AND), curr = false;
        for (Set<T> elem : c) {
            curr = elem.contains(obj);
            switch (op) {
                case AND: res = res &&  curr;
                case OR: res = res ||  curr;
            }
        }
        return res;
    }

/**
     * Defines grounded set Ai(t) responsible for induction of mental model mi connected to baseProfile which
     * involves connotations with both observations P,and Q.Depending on i
     * i=1 - Returns BaseProfiles where Object o has Trait P and has Trait Q
     * i=2 - Returns BaseProfiles where Object o has Trait P and does not have Trait Q
     * i=3 - Returns BaseProfiles where Object o does not have Trait P and has Trait Q
     * i=4 - Returns BaseProfiles where Object o does not have Trait P and does not have Trait Q
     *
     * @param P    Trait of observation
     * @param Q    Trait of observation
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @param i    indicator,indicating which case we'd like to use
     * @return
     */


    static Set<BaseProfile> getGroundingSetsConjunction( Trait P, Trait Q, int time, Set<BaseProfile> all,
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
     * @param o    Object observed by agent
     * @param P    Trait of observation
     * @param Q    Trait of observation
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @param i    indicator,indicating which case we'd like to use
     * @return
     */

    static double relativeCardConunction( Trait P, Trait Q, int time, Set<BaseProfile> all, int i) {
        Set<BaseProfile> Sum = new HashSet<BaseProfile>();

        //OgarnijAdAlla, nie dodaje tych samych obiektów
        Sum.addAll(getGroundingSetsConjunction( P, Q, time, all, 1));
        Sum.addAll(getGroundingSetsConjunction( P, Q, time, all, 2));
        Sum.addAll(getGroundingSetsConjunction( P, Q, time, all, 3));
        Sum.addAll(getGroundingSetsConjunction( P, Q, time, all, 4));
        return getCard(getGroundingSetsConjunction(P, Q, time, all, i), time) / getCard(Sum, time);
    }


}
