package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Agent is crucial class for project - it has members of collections
 * that hold all of data that we are processing.
 */
public class Agent {
    private BPCollection knowledgeBase;
    private ObservationCollection observations;
    private IMCollection models;
    public static Set<ObjectType> ObjectTypeCollection = new HashSet<>();

    public Agent(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
        observations = new ObservationCollection();
        models = new IMCollection();
    }

    public Agent(BPCollection knowledgeBase, ObservationCollection observations, IMCollection models) {
        this.knowledgeBase = knowledgeBase;
        this.observations = observations;
        this.models = models;
    }

    public BPCollection getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public ObservationCollection getObservations() {
        return observations;
    }

    public void setObservations(ObservationCollection observations) {
        this.observations = observations;
    }

    public IMCollection getModels() {
        return models;
    }

    public void setModels(IMCollection models) {
        this.models = models;
    }

    /**
     * Adds single observation to collection while ensuring that object of this observation
     * is amongst individual models, if not - add it.
     * @param observation Single observation of object.
     * @return Success - true/false.
     */
    public boolean addObservation(Observation observation) {
        if(!models.isObservationRepresented(observation)) {
            models.add(new IndividualModel(observation.getIdentifier(), observation.getType()));
        }
        return observations.add(observation);
    }

    /**
     * Adds multiple observations to collection.
     * @param observations Set of observations.
     * @return Success - true/false.
     */
    public boolean addObservations(Set<Observation> observations) {
        boolean success = true;
        for(Observation observation : observations) {
            if(!addObservation(observation))
                success = false;
        }
        return success;
    }

}
