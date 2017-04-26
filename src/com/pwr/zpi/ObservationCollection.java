package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Collection of observations. Used to find or add new observations.
 */
public class ObservationCollection {
    private Set<Observation> observationSet;

    public ObservationCollection() {
        observationSet = new HashSet<>();
    }

    public ObservationCollection(Set<Observation> observationSet) {
        this.observationSet = observationSet;
    }

    /**
     * Simply adds new observation to collection.
     * @param observation New observation.
     * @return true/false (success of add operation)
     */
    public boolean add(Observation observation) {
        return observationSet.add(observation);
    }

    /**
     * Finds all observations of specific object based on identifier.
     * @param identifier Identifier of object.
     * @return Set of observations that matches the identifier.
     */
    public Set<Observation> getObservationsOfObject(Identifier identifier) {
        Set<Observation> foundObservations = new HashSet<>();

        for(Observation observation : observationSet) {
            if(observation.getIdentifier().equals(identifier))
                foundObservations.add(observation);
        }
        return foundObservations;
    }
}
