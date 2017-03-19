package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;

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
    static Set<NamedCollection<Names, Object>> positiveInWM (Set<NamedCollection<Names, Object>> workingMemory, int time) //todo
    static Set<NamedCollection<Names, Object>> negativeInWM (Set<NamedCollection<Names, Object>> workingMemory, int time)
    static Set<NamedCollection<Names, Object>> positiveInLM(Set<NamedCollection<Names, Object>> longTimeMemory, int time)
    static Set<NamedCollection<Names, Object>> negativeInLM(Set<NamedCollection<Names, Object>> longTimeMemory, int time)

        //todo sprawdzic 3.9 i 3.10 zachodzi (s. 64)

    /**
     * Determines if any of "extended" operators will apply // todo opisac
     * @return
     */
    static Operations.Type determinePositive(Object o, DistributedKnowledge ds, int time) //todo
    static Operations.Type determineNegative(Object o, DistributedKnowledge ds, int time) //todo
}
