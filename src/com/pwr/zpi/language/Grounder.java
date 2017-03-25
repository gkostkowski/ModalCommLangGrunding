package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;

import java.util.Collection;
import java.util.Set;
import java.util.*;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public class Grounder {

    public static final double MIN_POS =
    public static final double MAX_POS =
    public static final double MIN_BEL =
    public static final double MAX_BEL =

    /**
     * Returns every BaseProfile defined by agent from point of time t,and represent expierience
     * of Object o having trait P
     * @param o Object observed by agent
     * @param trait Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @return List of BaseProfiles which contain Positive Traits
     */

    static Set<BaseProfile> getGroundingSetsPositiveTrait(Object o, @SuppressWarnings("rawtypes") Trait P,int time,Set<BaseProfile> all){
        Set<BaseProfile> baseout = new HashSet<BaseProfile>();
        for(BaseProfile bp:all){
            if(DetermineIfSetHasTrait(o,P,time,bp)){
                baseout.add(bp);
            }
        }
        return baseout;
    }

    /**
     * Returns every BaseProfile defined by agent from point of time t,and represent expierience
     * of Object o having trait P
     * @param o Object observed by agent
     * @param P Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all Set<BaseProfile> gives us set from which we'll evaluate those which contain Positive Traits
     * @return Set of BaseProfiles which contain Positive Traits
     */

    static Set<BaseProfile> getGroundingSetsPositiveTraitSet(Object o, @SuppressWarnings("rawtypes") Trait P, int time, Set<BaseProfile> all){
        Set<BaseProfile> baseout = new HashSet<>();
        for(BaseProfile bp:all){
            if(DetermineIfSetHasTrait(o,P,time,bp)){
                baseout.add(bp);
            }
        }
        return baseout;
    }

    /**
     *
     * @param o obeject observed by agent
     * @param P Trait of object
     * @param time  Time taken into consideration when looking for expieriences
     * @param one BaseProfile in which we seek the object with given trait
     * @return  true if BaseProfile contains object with given trait,false otherwise
     */
    static boolean DetermineIfSetHasTrait(Object o, @SuppressWarnings("rawtypes") Trait P,int time,BaseProfile one){
        Collection<NamedCollection<Names, Object>> DescribedObjects = one.giveMeWorld();
        Iterator<NamedCollection<Names, Object>> DOIterator = DescribedObjects.iterator();
        while(DOIterator.hasNext()){
            NamedCollection<Names,Object> NamedCol = DOIterator.next();
            if(NamedCol.getList().contains(o)){
                if(NamedCol.getMember(o).hasTrait(P)==State.Is){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns every BaseProfile defined by agent from point of time t,and represent expierience
     * of Object o having trait P
     * @param o Object observed by agent
     * @param trait Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all Set<BaseProfile> gives us set from which we'll evaluate those which contain Negative Traits
     * @return List of BaseProfiles which contain Negative Traits
     */
    static List<BaseProfile> getGroundingSetsNegativeTrait(Object o,@SuppressWarnings("rawtypes") Trait P,int time,Set<BaseProfile> all){
        List<BaseProfile> baseout = new ArrayList<BaseProfile>();
        for(BaseProfile bp:all){
            if(!DetermineIfSetHasTrait(o,P,time,bp)){
                baseout.add(bp);
            }
        }
        return baseout;
    }
    /**
     * Returns every BaseProfile defined by agent from point of time t,and represent expierience
     * of Object o having trait P
     * @param o Object observed by agent
     * @param P Trait of object
     * @param time Time taken into consideration when looking for expieriences
     * @param all Set<BaseProfile> gives us set from which we'll evaluate those which contain Negative Traits
     * @return Set of BaseProfiles which contain Negative Traits
     */
    static Set<BaseProfile> getGroundingSetsNegativeTraitSet(Object o,@SuppressWarnings("rawtypes") Trait P,int time,Set<BaseProfile> all){
        Set<BaseProfile> baseout = new HashSet<>();
        for(BaseProfile bp:all){
            if(!DetermineIfSetHasTrait(o,P,time,bp)){
                baseout.add(bp);
            }
        }
        return baseout;
    }

    /**
     *
     * @param groundingSet  List of Base Profiles which cardinality we desire to know
     * @param t given time
     * @return Positive Cardinality of List
     */
    static double getCardPositive(List<BaseProfile> groundingSet,int t){
        return groundingSet.size();
    }
    /**
     *
     * @param groundingSet  List of Base Profiles which cardinality we desire to know
     * @param t given time
     * @return Cardinality of List
     */

    static double getCardNegative(List<BaseProfile> groundingSet,int t){
        return groundingSet.size();
    }

    /**
     *
     * @param formula na ciul tu to
     * @param groundingSetPositive List of Positive BaseProfiles
     * @param groundingSetNegative  List of Negative BaseProfiles
     * @param time given time
     * @return  Cardinality (ratio) of Positive BaseProfiles to all
     */
    static double relativePositiveCard(Formula formula, List<BaseProfile> groundingSetPositive, List<BaseProfile> groundingSetNegative, int time){
        return getCardPositive(groundingSetPositive,time)/(getCardNegative(groundingSetNegative,time)+getCardPositive(groundingSetPositive,time));
    }
    /**
     *
     * @param formula na ciul tu to
     * @param groundingSetPositive List of Positive BaseProfiles
     * @param groundingSetNegative  List of Negative BaseProfiles
     * @param time given time
     * @return  Cardinality (ratio) of Negative BaseProfiles to all
     */
    static double relativeNegativeCard(Formula formula, List<BaseProfile> groundingSetPositive, List<BaseProfile> groundingSetNegative, int time){
        return getCardNegative(groundingSetPositive,time)/(getCardNegative(groundingSetNegative,time)+getCardPositive(groundingSetPositive,time));
    }

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
    static DistributedKnowledge distributeKnowledge(Agent agent, Trait trait, Object obj, int time) {
        return new DistributedKnowledge(agent, trait, obj, time);
    }
/*  ==NOT APPLICABLE FOR COMPLEX FORMULA!==
    *//**
     * Builds distributed knowledge, which will be used to make mental models m^a_1 and m^a_2 associated
     * with given formulas: baseFormula and its opposite - negBaseFormula.
     * @param agent The knowledge subject.
     * @param baseFormula Formula which is the base of resulted mental model m^a_1.
     * @param negBaseFormula Formula which is the base of resulted mental model m^a_2. Opposition of baseFormula.
     * @param time Certain moment in time.
     * @return Distribution of knowledge.
     *//*
    static DistributedKnowledge distributeKnowledge(Agent agent, Formula baseFormula, Formula negBaseFormula, int time) {
        return new DistributedKnowledge(agent, baseFormula, negBaseFormula, time);
    }
    /**
     * Determines if any of "extended" operators will apply // todo opisac
     * @return
     */
    static Operators.Type determinePositive(Agent agent, DistributedKnowledge ds, int time) {
        agent.
    }
    static Operators.Type determineNegative(Object o, DistributedKnowledge ds, int time) //todo
}
