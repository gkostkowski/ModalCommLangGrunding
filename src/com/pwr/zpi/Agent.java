package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Agent is crucial class for project - it has members of collections
 * that hold all of data that we are processing.
 */
public class Agent {
    private BPCollection knowledgeBase;
    private IMCollection models;
    private HolonCollection holons;
    // W agencie można zrobić metodę ,która tworzy distributed Knowledge
    public static Set<ObjectType> ObjectTypeCollection = new HashSet<>();

    public Agent() {
        knowledgeBase = new BPCollection();
        models = new IMCollection();
    }

    public Agent(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
        models = new IMCollection();
    }

    public Agent(BPCollection knowledgeBase, IMCollection models) {
        this.knowledgeBase = knowledgeBase;
        this.models = models;
    }

    public BPCollection getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public IMCollection getModels() {
        return models;
    }

    public void setModels(IMCollection models) {
        this.models = models;
    }

}
