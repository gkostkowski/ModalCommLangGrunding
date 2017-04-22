package com.pwr.zpi;

import java.util.Set;

/**
 * Collection of observations.
 */
public class ObservationCollection {
    private Set<Observation> observationSet;

    public boolean add(Observation observation) {
        return observationSet.add(observation);
    }
}
