package com.pwr.zpi;

import java.util.Set;

/**
 *
 */
public class Agent extends Observation {
    BPCollection knowledgeBase;

    public Agent(int identifier, String type, Set<Trait> traits, BPCollection knowledgeBase) {
        super(identifier, type, traits);
        this.knowledgeBase = knowledgeBase;
    }

    public BPCollection getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }


}
