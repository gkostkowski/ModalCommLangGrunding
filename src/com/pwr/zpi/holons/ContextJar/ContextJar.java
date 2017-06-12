package com.pwr.zpi.holons.ContextJar;


import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.Formula;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jarema on 5/24/2017.
 * As observations are taken in real world, they might bring a context along with them,in this class we try to establish
 * context and use it in future to develop contextual understanding of the world
 */
public class ContextJar implements Contextualisation{
    DistanceFunction df;
    double dMax = 3.0;
    BaseProfile first;
    int mode;

    /**
     * Constructor of class
     * @param df    Distance function we desire to use in establishing context.
     * @param mode  one of modes,1 to establish context on last BaseProfile,2 to establish it on 3 last BaseProfiles
     */
    public ContextJar(DistanceFunction df,int mode){
        this.df = df;
        this.mode = mode;
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
            if(mode==1) {
                if(namedGroundingSets.get(f).size() >0){
                    first = namedGroundingSets.get(f).toArray(new BaseProfile[namedGroundingSets.size()])[namedGroundingSets.get(f).size()-1];
                    Set<BaseProfile> littleOut = new HashSet<>();
                    for (BaseProfile bp : namedGroundingSets.get(f)) {
                        if (df.implementation(first, bp) < dMax) {
                            littleOut.add(bp);
                        }
                    }
                    out.put(f, littleOut);}
            }
            if(mode == 2){
                Set<BaseProfile> threeLast = new HashSet<BaseProfile>();
                if(namedGroundingSets.get(f).size()>3){
                    BaseProfile[] bpArray = namedGroundingSets.get(f).toArray(new BaseProfile[namedGroundingSets.size()]);
                    threeLast.add(bpArray[namedGroundingSets.get(f).size()-1]);
                    threeLast.add(bpArray[namedGroundingSets.get(f).size()-2]);
                    threeLast.add(bpArray[namedGroundingSets.get(f).size()-3]);
                    Set<BaseProfile> littleOut = new HashSet<>();
                    for (BaseProfile bp : namedGroundingSets.get(f)) {
                        boolean goneThrough = true;
                        for (BaseProfile oneOfThree : threeLast) {
                            if (df.implementation(oneOfThree, bp) > dMax) {
                                goneThrough = false;
                            }
                        }
                        if(goneThrough){littleOut.add(bp);}
                    }
                    out.put(f, littleOut);}
                else{ System.out.println(" Requirements for contextualization based on 3 profiles haven't been met, behave yourself.");}
            }
        }
        return out;
    }

    /**
     * Sets maximal value we compare distance to
     * @param threshold
     */
    @Override
    public void setMaxThreshold(double threshold) {
        dMax = threshold;
    }

    /**
     *@return returns value of max distance
     */
    public double getMaxThreshold(){return dMax;}

    /**
     *
     * @return Last context we use to establish set of contextualised Base Profiles
     */
    public BaseProfile getActualContext(){return first;}
    /*
    public class getContext{



        public Context StrategyA(DistributedKnowledge dk, BaseProfile current){
            this.current = current;
            currentval = distanceFun(current);
            this.dk = dk;
            complementFormulas = dk.getComplementaryFormulas();
        }
        public Set<Set<BaseProfile>> getContextualNeighbours(){
            Set<Set<BaseProfile>> Gi = new HashSet<>();
            Set<BaseProfile> bpSetRemove = new HashSet<>();
            Set<BaseProfile> Ai;
            for(Formula cmpf: complementFormulas){
                Ai = dk.getGroundingSet(cmpf);
                for(BaseProfile bp : Ai) {
                    if (distanceFun(bp) + dMax < currentval) {
                        bpSetRemove.add(bp);
                    }
                }
                Ai.removeAll(bpSetRemove);
                Gi.add(Ai);
            }
            return Gi;
        }

        public double distanceFun(BaseProfile bp){
            return 0.0;
        }
    }*/
}
