package com.pwr.zpi.core.holons.ContextJar;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.core.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.util.Pair;
import com.pwr.zpi.language.SimpleFormula;
import com.pwr.zpi.language.Trait;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jarema on 6/2/2017.
 * As observations are taken in real world, they might bring a context along with them,in this class we try to establish
 * context and use it in future to develop contextual understanding of the world
 * Composite Context is based on three last observations. Class extracts traits which are and are not in all of last
 * three(default,reality might vary) BaseProfiles, then builds context around this set of traits.
 */
public class CompositeContext implements Contextualisation {
    DistanceFunction df;
    BaseProfile[] contextArray ;
    Pair<Pair<Formula,Pair<Set<Trait>,Set<Trait>>>,Pair<Formula,Pair<Set<Trait>,Set<Trait>>>> contextTraits;
    double dMax;
    /**
     * Constructor
     * @param df Distance function we desire to use
     * @param takenIntoConsideration number of lastly observed BaseProfiles we wish to take into consideration
     */
    public  CompositeContext(DistanceFunction df,int takenIntoConsideration){
        this.df = df;
        contextArray = new BaseProfile[takenIntoConsideration];
    }

    /**
     *
     * Performs act of contextualisation under previously given circumstances
     * @param namedGroundingSets Map of grounding sets and related formulas.
     * @return Map of Sets of BaseProfiles considered as contextualised set of BP for given formula.
     */
    @Override
    public Map<Formula, Set<BaseProfile>> performContextualisation(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        Map<Formula, Set<BaseProfile>> out = new HashMap<>();
        for(Formula f:namedGroundingSets.keySet()){

            Set<BaseProfile> givenLast = new HashSet<>();
            BaseProfile[] bpArray = namedGroundingSets.get(f).toArray(new BaseProfile[namedGroundingSets.size()]);
            if(namedGroundingSets.get(f).size()>contextArray.length){
                for(int i = 0 ; i < contextArray.length;i++){
                    contextArray[i] = bpArray[namedGroundingSets.get(f).size()-i-1];
                }
                Set<BaseProfile> littleOut = new HashSet<>();
                Pair<Set<Trait>,Set<Trait>> lilContextTraits = establishCommonTraits(contextArray);
                for (BaseProfile bp : namedGroundingSets.get(f)) {

                    if(df.composite(bp,lilContextTraits)<dMax){
                        littleOut.add(bp);
                    }
                }
                out.put(f,littleOut);
                contextTraits = new Pair<>(null,null);
                if(((SimpleFormula)f).isNegated()){contextTraits.setV(new Pair<>(f,lilContextTraits));}
                else{contextTraits.setK(new Pair<>(f,lilContextTraits));}
            }
        }
        return out;
    }

    public Pair<Set<Trait>,Set<Trait>> establishCommonTraits(BaseProfile[] contextArray) {
        Set<Trait> positive =  new HashSet<>(contextArray[0].getDescribedByTraits().keySet());
        Set<Trait> negative = new HashSet<>(contextArray[0].getNotDescribedByTraits().keySet());
        if(contextArray.length>1){
            for(int i = 1;i<contextArray.length;i++){
                Set<Trait> remove = new HashSet<>();
                for(Trait t1:positive){
                    if(!contextArray[i].getDescribedByTraits().keySet().contains(t1)){
                        remove.add(t1);
                    }
                }positive.removeAll(remove);
                remove = new HashSet<>();
                for(Trait t1:negative){
                    if(!contextArray[i].getNotDescribedByTraits().keySet().contains(t1)){
                        remove.add(t1);
                    }
                }positive.removeAll(remove);
            }}
        return new Pair<>(positive,negative);
    }

    /**
     * Sets maximal value we compare distance to
     * @param threshold
     */
    @Override
    public void setMaxThreshold(double threshold) {
        this.dMax=threshold;
    }
    /**
     *@return returns value of max distance
     */
    public double getMaxThreshold(){return dMax;}

    /**
     *
     * @return Array of  BaseProfiles which we take into account;
     */
    public BaseProfile[] getActualContext(){return contextArray;}

    /**
     *
     * @return Pair of sets which we use to establish distance
     */
    public Pair<Pair<Formula,Pair<Set<Trait>,Set<Trait>>>,Pair<Formula,Pair<Set<Trait>,Set<Trait>>>> getContextTraits(){return contextTraits;}
}