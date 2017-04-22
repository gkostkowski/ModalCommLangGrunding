package com.pwr.zpi;

import java.util.Set;

/**
 *
 */
public class Agent {
    private BPCollection knowledgeBase;
    private ObservationCollection observations;
    private IMCollection models;

    public Agent(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public BPCollection getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public boolean addObservation(Observation observation) {
        if(!models.isObservationRepresented(observation)) {
            models.add(new IndividualModel(observation.getIdentifier(), observation.getType()));
        }
        return observations.add(observation);
    }

    public IndividualModel f(Identifier id) {
        return models.f(id);
    }

    public IndividualModel g(Observation observation) {
        return models.g(observation);
    }

}
