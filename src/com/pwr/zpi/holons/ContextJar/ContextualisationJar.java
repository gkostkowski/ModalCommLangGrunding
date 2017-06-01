package com.pwr.zpi.holons.ContextJar;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.Contextualisation;
import com.pwr.zpi.language.Formula;

import java.util.*;

/**
 * Created by Jarema on 5/24/2017.
 */
public class ContextualisationJar implements Contextualisation {
    DistanceFunction df;
    double dMax = 3.0;
    BaseProfile first;
    int mode;
    public ContextualisationJar(DistanceFunction df, int mode){
        this.df = df;
        this.mode = mode;
    }

    @Override
    public Map<Formula, Set<BaseProfile>> performContextualisation(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        Map<Formula, Set<BaseProfile>> out = new HashMap<>();
        for(Formula f:namedGroundingSets.keySet()){
            if(mode==1) {
                first = namedGroundingSets.get(f).toArray(new BaseProfile[namedGroundingSets.size()])[namedGroundingSets.get(f).size() - 1];
                Set<BaseProfile> littleOut = new HashSet<>();
                for (BaseProfile bp : namedGroundingSets.get(f)) {
                    if (df.implementation(first, bp) < dMax) {
                        littleOut.add(bp);
                    }
                }
                out.put(f, littleOut);
            }
            if(mode == 2){
                Set<BaseProfile> threeLast = new HashSet<BaseProfile>();
                BaseProfile[] bpArray = namedGroundingSets.get(f).toArray(new BaseProfile[namedGroundingSets.get(f).size()]);
                threeLast.add(bpArray[namedGroundingSets.get(f).size()-1]);
                threeLast.add(bpArray[namedGroundingSets.get(f).size()-2]);
                threeLast.add(bpArray[namedGroundingSets.get(f).size()-3]);
                Set<BaseProfile> littleOut = new HashSet<>();
                for (BaseProfile bp : namedGroundingSets.get(f)) {
                    boolean goneThrough = true;
                    for (BaseProfile oneOfThree : namedGroundingSets.get(f)) {
                        if (df.implementation(oneOfThree, bp) > dMax) {
                            goneThrough = false;
                        }
                    }
                    if(goneThrough){littleOut.add(bp);}
                }
                out.put(f, littleOut);
            }
        }
        return out;
    }

    @Override
    public void setMaxThreshold(double threshold) {
        dMax=threshold;
    }

    /*
    public class getContextualisation{



        public Contextualisation StrategyA(DistributedKnowledge dk, BaseProfile current){
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
