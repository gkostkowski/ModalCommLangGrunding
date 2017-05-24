package com.pwr.zpi.language;

import com.pwr.zpi.BaseProfile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jarema on 5/22/2017.
 */
public class ContectverJar {

    public class StrategyA{
        double dMax= 4;
        BaseProfile current;
        double currentval;
        DistributedKnowledge dk;
        List<Formula> complementFormulas;


        public StrategyA(DistributedKnowledge dk,BaseProfile current){
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
    }

    public class StrategyB{
        double dMax= 4;
        BaseProfile current;
        double currentval;
        DistributedKnowledge dk;
        List<Formula> complementFormulas;

        public StrategyB(Set<BaseProfile> bpSet,BaseProfile current){
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
                Ai = clasterFun(cmpf);
                for(BaseProfile bp : clasterFun()) {
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
        public Set<BaseProfile> clasterFun(Formula f){
            return null;
        }
    }

    public class StrategyC{
        double dMax= 4;
        BaseProfile current;
        double currentval;
        DistributedKnowledge dk;
        List<Formula> complementFormulas;


        public StrategyC(DistributedKnowledge dk,BaseProfile current){
            this.current = current;
            currentval = distanceFuzzyFun(current);
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
                    if (distanceFuzzyFun(bp) + dMax < currentval) {
                        bpSetRemove.add(bp);
                    }
                }
                Ai.removeAll(bpSetRemove);
                Gi.add(Ai);
            }
            return Gi;
        }

        public double distanceFuzzyFun(BaseProfile bp){
            return 0.0;
        }
    }

}
