package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Set of four classes RA1 established for given moment in time, for certain base formula and associated object.
 */
public class DistributedKnowledge {
    /**
     * Represents set of all base profiles, which are included in working memory and presents object as described by
     * given trait.
     */
    private Set<BaseProfile> RA1;
    /**
     * Represents set of all base profiles, which are included in long-term memory and presents object as described by
     * given trait.
     */
    private Set<BaseProfile> TA1;
    /**
     * Represents set of all base profiles, which are included in working memory and presents object as NOT described by
     * given trait.
     */
    private Set<BaseProfile> RA2;
    /**
     * Represents set of all base profiles, which are included in long-term memory and presents object as NOT described by
     * given trait.
     */
    private Set<BaseProfile> TA2;
    /**
     * Grounding set related with m^a_1 mental model.
     */
    private Set<BaseProfile> A1;
    /**
     * Grounding set related with m^a_2 mental model.
     */
    private Set<BaseProfile> A2;

    private int timestamp;
    private final Object obj;
    private final Trait trait;


    public DistributedKnowledge(Agent agent, Trait trait, Object obj, int time) {
        this.timestamp = time;
        this.trait = trait;
        this.obj = obj;

        Map<Integer, BaseProfile> inLM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.LM);
        Map<Integer, BaseProfile> inWM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.WM);
        A1 =  Grounder
                .getGroundingSetsPositiveTraitSet(obj, trait, time, agent.getKnowledgeBase().getBaseProfiles(time));
        A2 =  Grounder
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


    public Set<BaseProfile> getRA1() {
        return RA1;
    }

    public Set<BaseProfile> getTA1() {
        return TA1;
    }

    public Set<BaseProfile> getRA2() {
        return RA2;
    }

    public Set<BaseProfile> getTA2() {
        return TA2;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Object getObj() {
        return obj;
    }

    public Trait getTrait() {
        return trait;
    }
    public Set<BaseProfile> getA1() {
        return A1;
    }

    public Set<BaseProfile> getA2() {
        return A2;
    }
}
