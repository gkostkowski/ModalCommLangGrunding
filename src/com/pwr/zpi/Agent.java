package com.pwr.zpi;

import java.util.Set;

/**
 *
 */
public class Agent {
    BPCollection knowledgeBase;
    ObservationCollection observations;
    IMCollection models;

    public Agent(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public BPCollection getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }


}
