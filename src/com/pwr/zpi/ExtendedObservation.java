package com.pwr.zpi;

import java.util.Set;

/**
 *
 */

/**
 * Intended to use in representation of agent knowledge.
 */
public class ExtendedObservation extends Observation {
    private Set<Trait> positive; //traits which were observed in object
    private Set<Trait> negative; //traits which were not observed in object
    private Set<Trait> unknown; //traits which state couldn't be established
}
