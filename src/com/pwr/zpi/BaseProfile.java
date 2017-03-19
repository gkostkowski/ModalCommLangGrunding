package com.pwr.zpi;

/**
 * Created by Grzesiek on 2017-03-16.
 */

import java.util.Collection;

/**
 * Represents state of world from agent perspective. It's established for certain moment in time.
 */
public class BaseProfile extends World {
    protected Collection<NamedCollection<Names, Object>> describedObjects; //hides field from superclass


    boolean isPAdequate(Collection<NamedCollection<Names, Object>> PSwT, Trait examinedTrait) {
        return false;
    }
}
