package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public class Grounder {

    public static final double MIN_POS =
    public static final double MAX_POS =
    public static final double MIN_BEL =
    public static final double MAX_BEL =

    static Set<BaseProfile> getGroundingSetsPositiveTrait(Object o, Trait<> trait){} //todo
    static Set<BaseProfile> getGroundingSetsNegativeTrait(Object o, Trait<> trait){} //todo

    static int getCardPositive(Set<BaseProfile> groundingSet){} //todo
    static int getCardNegative(){} //todo

    static double relativeCard(Formula formula, Set<BaseProfile> groundingSetPositive, Set<BaseProfile> groundingSetNegative, int time){}

    /**
     * todo opis zeby nie zapomniec co to robi
     * @param workingMemory
     * @param time
     * @return
     */
    static Set<NamedCollection<Name, Object>> positiveInWM (Collection<BaseProfile> workingMemory, int time) {

    }
    static Set<NamedCollection<Name, Object>> negativeInWM (Collection<BaseProfile> workingMemory, int time)
    static Set<NamedCollection<Name, Object>> positiveInLM(Collection<BaseProfile> longTimeMemory, int time)
    static Set<NamedCollection<Name, Object>> negativeInLM(Collection<BaseProfile> longTimeMemory, int time)

        //todo sprawdzic 3.9 i 3.10 zachodzi (s. 64)

    /**
     * Builds distributed knowledge, which will be used to make mental models m^a_1 and m^a_2 associated
     * with formulas. Formulas are given according to provided object and trait. This formulas are: trait(object) and
     * not(trait(object)).
     * Builded distributed knowledge is related to certain moment in time.
     * @param agent The knowledge subject.
     * @param trait Certain trait included in formulas.
     * @param obj Certain object included in formulas.
     * @param time Certain moment in time.
     * @return Distribution of knowledge.
     */
    DistributedKnowledge distributeKnowledge(Agent agent, Trait trait, Object obj, int time) {
        return new DistributedKnowledge(agent, trait, obj, time);
    }

    /**
     * Builds distributed knowledge, which will be used to make mental models m^a_1 and m^a_2 associated
     * with given formulas: baseFormula and its opposite - negBaseFormula.
     * @param agent The knowledge subject.
     * @param baseFormula Formula which is the base of resulted mental model m^a_1.
     * @param negBaseFormula Formula which is the base of resulted mental model m^a_2. Opposition of baseFormula.
     * @param time Certain moment in time.
     * @return Distribution of knowledge.
     */
    DistributedKnowledge distributeKnowledge(Agent agent, Formula baseFormula, Formula negBaseFormula, int time) {
        return new DistributedKnowledge(agent, baseFormula, negBaseFormula, time);
    }
    /**
     * Determines if any of "extended" operators will apply // todo opisac
     * @return
     */
    static Operations.Type determinePositive(Object o, DistributedKnowledge ds, int time) //todo
    static Operations.Type determineNegative(Object o, DistributedKnowledge ds, int time) //todo
}
