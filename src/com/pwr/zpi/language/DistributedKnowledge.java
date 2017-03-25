package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Set of four classes RA1 established for certain base formula and timestamp.
 */
public class DistributedKnowledge {
    /**
     *
     */
    Set<BaseProfile> RA1;
    Set<BaseProfile> TA1;
    Set<BaseProfile> RA2;
    Set<BaseProfile> TA2;



    public DistributedKnowledge(Agent agent, Trait trait, Object obj, int time) {
        Map<Integer, BaseProfile> inLM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.LM);
        Map<Integer, BaseProfile> inWM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.WM);
        Set<BaseProfile> A1 =  Grounder
                .getGroundingSetsPositiveTraitSet(obj, trait, time, agent.getKnowledgeBase().getBaseProfiles(time));
        Set<BaseProfile> A2 =  Grounder
                .getGroundingSetsNegativeTraitSet(obj, trait, time, agent.getKnowledgeBase().getBaseProfiles(time));
        RA1 = new HashSet<>(inWM.values());
        RA1.retainAll(A1);
        TA1 = new HashSet<>(inLM.values());
        TA1.retainAll(A1);
        RA2 = new HashSet<>(inWM.values());
        RA2.retainAll(A2);
        TA2 = new HashSet<>(inLM.values());
        TA2.retainAll(A2);


        //important checking
        Set<BaseProfile> check3_9_1 = new HashSet<BaseProfile>(RA1);
        check3_9_1.retainAll(TA1);
        Set<BaseProfile> check3_9_2 = new HashSet<BaseProfile>(RA2);
        check3_9_1.retainAll(TA2);
        assert check3_9_1.isEmpty()&& check3_9_2.isEmpty();

        Set<BaseProfile> check3_10_1 = new HashSet<BaseProfile>(RA1);
        check3_10_1.addAll(TA1);
        Set<BaseProfile> check3_10_2 = new HashSet<BaseProfile>(RA2);
        check3_10_2.addAll(TA2);
        assert check3_10_1.equals(A1) && check3_10_2.equals(A2);
    }



}
