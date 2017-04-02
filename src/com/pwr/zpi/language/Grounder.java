package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;
import javafx.util.Pair;

import java.util.Set;
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

    /**
     * Gives complete collection of grounding sets for certain formula. It supports simple and complex formulas.
     *
     * @param formula Considered formula.
     * @param time
     * @param all
     * @param states  Represents especially two states: IS and IS_NOT, which determines if simple formula, part of
     *                complex formula will require checking associated to objects described by trait or objects
     *                NOT described by trait.
     * @return Collection of grounding sets.
     */
    static Map<Formula, Set<BaseProfile>> getGroundingSets(Formula formula, int time, Set<BaseProfile> all, State... states) throws InvalidSentenceFormulaException {
        Object o = formula.getObject();
        Set<Trait> traits = formula.getTraits();
        List<Formula> parts = new ArrayList<>();
        Map<Formula, Set<BaseProfile>> res = new HashMap<>();

        Operators.Type type = null;
        boolean isComplex=false;
        if (isComplex=formula instanceof ComplexFormula) {
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
                res.put(mentalModel, currSet=new HashSet<>());
                for (BaseProfile bp : all) {
                    if (isFulfilled(o, new ArrayList<>(traits), time, statesSeq, type, bp))
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
     * @param o
     * @param traits
     * @param time
     * @param states Should be placed in certain positions, respectiovely to traits.
     * @param op
     * @param bp
     * @return
     */
    static private boolean isFulfilled(Object o, List<Trait> traits, int time, List<State> states, Operators.Type op, BaseProfile bp) {
        if (traits.size() != states.size())
            throw new IllegalStateException("Number of traits differs from amount of states.");

        if (op == null) //case for simple formula
            op = Operators.Type.AND;
        boolean res = op.equals(Operators.Type.AND) ? true : false; //so far, applicable only for AND or OR
        for (int i = 0; i < traits.size(); i++) {
            boolean curr = bp.DetermineIfSetHasTrait(o, traits.get(i), time, states.get(i));
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

    static Map<Formula, Set<BaseProfile>> getGroundingSets(Formula formula, int time, Set<BaseProfile> all) throws InvalidSentenceFormulaException {
        return getGroundingSets(formula, time, all, State.IS, State.IS_NOT);
    }

    /**
     * Defines grounded set A1(t) responsible for induction of mental model m1 connected to object p and trait P
     * A1 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience object o,having trait P
     *
     * @param o     Object observed by agent
     * @param trait Trait of object
     * @param time  Time taken into consideration when looking for expieriences
     * @param all   Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @return List of BaseProfiles which contain Positive Traits
     */
    static Set<BaseProfile> getGroundingSetsPositiveTrait(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all) {
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
        for (BaseProfile bp : all) {
            if (bp.DetermineIfSetHasTrait(o, P, time)) {
                baseout.add(bp);
            }
        }
        return baseout;
    }

    /**
     * Defines grounded set A2(t) responsible for induction of mental model m1 connected to object p and trait P
     * A2 contains everu base profiledefining state of knowledge SW(t) recorded by agent to point of time t and
     * representing expierience object o,not having trait P
     *
     * @param o    Object observed by agent
     * @param P    Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @return Set of BaseProfiles which contain Positive Traits
     */

    static Set<BaseProfile> getGroundingSetsNegativeTrait(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all) {
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
        for (BaseProfile bp : all) {
            if (!bp.DetermineIfSetHasTrait(o, P, time)) {
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
    static double getCardPositive(Set<BaseProfile> groundingSet, int t) {
        return groundingSet.size();
    }

    /**
     * Inductive cardinality GAi grounding set A2
     *
     * @param groundingSet set of Base Profiles which cardinality we desire to know
     * @param t            given time
     * @return Cardinality of Set
     */

    static double getCardNegative(Set<BaseProfile> groundingSet, int t) {
        return groundingSet.size();
    }

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
        return getCardPositive(groundingSetPositive, time) / (getCardNegative(groundingSetNegative, time) + getCardPositive(groundingSetPositive, time));
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
        return getCardNegative(groundingSetPositive, time) / (getCardNegative(groundingSetNegative, time) + getCardPositive(groundingSetPositive, time));
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
     */
    static DistributedKnowledge distributeKnowledge(Agent agent, Formula formula, int time) {
        return new DistributedKnowledge(agent, formula, time);
    }

    /**
     * Realizes verification of epistemic fulfillment relationship's conditions for formula given through
     * knowledge distribution.
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
    static Operators.Type determineFulfillment(Agent agent, DistributedKnowledge dk) {
        Operators.Type res;
        for (Formula mentalModel : dk.getMentalModels()) {
            res = determineSingleFulfillment(agent, dk, mentalModel);
        }
        return null; //todo ?
    }


    static Operators.Type determineSingleFulfillment(Agent agent, DistributedKnowledge dk, Formula mentalModel) {
        int timestamp = dk.getTimestamp();
        BaseProfile lmBp = new BaseProfile();
        BaseProfile wmBp = new BaseProfile();
        Set<Object> objects = new HashSet<>();

        Object describedObj = mentalModel.getObject();
        Set<Trait> describedTrait = mentalModel.getTraits();
        //mentalModel.
        //boolean isNegated = state.equals(State.IS) ? true : false;

        Set<Object> objsWithClearState = new HashSet<>();
        /**
         * Represents objsWithPositiveState or objsWithNegativeState - depending on state value
         */
        Set<Object> objsWithGivenState = new HashSet<>();

        Set<Object> indefiniteByTrait = new HashSet<>();
        Formula formula = dk.getFormula();


//        setCommonObjects(timestamp, agent, dk, lmBp, wmBp, objects, describedObj, describedTrait, objsWithClearState,
//                objsWithGivenState, indefiniteByTrait, isNegated);
        setCommonObjects(timestamp, agent, lmBp, wmBp, objects, describedObj, describedTrait, objsWithClearState,
                objsWithGivenState, indefiniteByTrait, isNegated);

        return checkEpistemicalConditions(indefiniteByTrait, describedObj, dk, timestamp, objsWithGivenState, isNegated);
    }


    private static void setCommonObjects(int timestamp, Agent agent, BaseProfile lmBp, BaseProfile wmBp,
                                         Set<Object> objects, Object describedObj, Trait describedTrait,
                                         Set<Object> objsWithClearState, Set<Object> objsWithGivenState,
                                         Set<Object> indefiniteByTrait, boolean isNegated) {
        lmBp.copy(agent.getKnowledgeBase().getBaseProfile(timestamp, BPCollection.MemoryType.LM));
        wmBp.copy(agent.getKnowledgeBase().getBaseProfile(timestamp, BPCollection.MemoryType.LM));
        objects.addAll(BaseProfile.getObjects(lmBp, wmBp));
//        describedObj.copy(dk.getObject());
//        describedTrait.copy(dk.getTrait());

        objsWithClearState.addAll(Object.getObjects(
                lmBp.getNotDescribedByTrait(describedTrait),
                wmBp.getNotDescribedByTrait(describedTrait),
                lmBp.getDescribedByTrait(describedTrait),
                wmBp.getDescribedByTrait(describedTrait)));
        if (isNegated) {
            objsWithGivenState.addAll(Object.getObjects(
                    lmBp.getNotDescribedByTrait(describedTrait),
                    wmBp.getNotDescribedByTrait(describedTrait)));
        } else {

            objsWithGivenState.addAll(Object.getObjects(
                    lmBp.getDescribedByTrait(describedTrait),
                    wmBp.getDescribedByTrait(describedTrait)));
        }
        indefiniteByTrait.addAll(objects);
        indefiniteByTrait.removeAll(objsWithClearState);
    }

    private static Operators.Type checkEpistemicalConditions(Set<Object> indefiniteByTrait, Object describedObj, DistributedKnowledge dk,
                                                             int timestamp, Set<Object> objsWithSelectedState, boolean isNegated) {
        Set<BaseProfile> PBfromWM = isNegated ? dk.getRA2() : dk.getRA1();
        if (indefiniteByTrait.contains(describedObj) && !PBfromWM.isEmpty()) {
            double currRelCard = isNegated ? relativeNegativeCard(dk.getA1(), dk.getA2(), timestamp)
                    : relativePositiveCard(dk.getA1(), dk.getA2(), timestamp);
            if (currRelCard >= MIN_POS && currRelCard < MAX_POS)
                return Operators.Type.POS;
            if (currRelCard >= MIN_BEL && currRelCard < MAX_BEL)
                return Operators.Type.POS;
            if (currRelCard == KNOW)
                return Operators.Type.POS;
        } else if (objsWithSelectedState.contains(describedObj)) {
            return Operators.Type.POS;
        } else {
            /*can use AND, OR, XOR*/

        }
        return null;
    }

    /**
     * Defines grounded set Ai(t) responsible for induction of mental model mi connected to baseProfile which
     * involves connotations with both objects P,and Q.Depending on i
     * i=1 - Returns BaseProfiles where Object o has Trait P and has Trait Q
     * i=2 - Returns BaseProfiles where Object o has Trait P and does not have Trait Q
     * i=3 - Returns BaseProfiles where Object o does not have Trait P and has Trait Q
     * i=4 - Returns BaseProfiles where Object o does not have Trait P and does not have Trait Q
     *
     * @param o    Object observed by agent
     * @param P    Trait of object
     * @param Q    Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @param i    indicator,indicating which case we'd like to use
     * @return
     */

    static Set<BaseProfile> getGroundingSetsConjunction(Object o, Trait P, Trait Q, int time, Set<BaseProfile> all, int i) {
        Set<BaseProfile> out = new HashSet<BaseProfile>();
        switch (i) {
            case 1:
                for (BaseProfile bp : all) {
                    if (bp.DetermineIfSetHasTrait(o, P, time) && bp.DetermineIfSetHasTrait(o, Q, time)) out.add(bp);
                }
                break;

            case 2:
                for (BaseProfile bp : all) {
                    if (bp.DetermineIfSetHasTrait(o, P, time) && !bp.DetermineIfSetHasTrait(o, Q, time)) out.add(bp);
                }
                break;

            case 3:
                for (BaseProfile bp : all) {
                    if (!bp.DetermineIfSetHasTrait(o, P, time) && bp.DetermineIfSetHasTrait(o, Q, time)) out.add(bp);
                }
                break;

            case 4:
                for (BaseProfile bp : all) {
                    if (!bp.DetermineIfSetHasTrait(o, P, time) && !bp.DetermineIfSetHasTrait(o, Q, time)) out.add(bp);
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
     * @param P    Trait of object
     * @param Q    Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all  Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @param i    indicator,indicating which case we'd like to use
     * @return
     */
    static double relativeCardConunction(Object o, Trait P, Trait Q, int time, Set<BaseProfile> all, int i) {
        Set<BaseProfile> Sum = new HashSet<BaseProfile>();

        //OgarnijAdAlla, nie dodaje tych samych obiektów
        Sum.addAll(getGroundingSetsConjunction(o, P, Q, time, all, 1));
        Sum.addAll(getGroundingSetsConjunction(o, P, Q, time, all, 2));
        Sum.addAll(getGroundingSetsConjunction(o, P, Q, time, all, 3));
        Sum.addAll(getGroundingSetsConjunction(o, P, Q, time, all, 4));
        return getCard(getGroundingSetsConjunction(o, P, Q, time, all, i), time) / getCard(Sum, time);
    }

}
