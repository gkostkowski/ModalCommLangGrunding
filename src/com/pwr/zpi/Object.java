package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-16.
 */
/**
 * Describes Objects included in World, which have traits assumed as objective, consistent with real world.
 */
public class Object {
    protected int identifier;

    private Set<Trait> traits; //won't be visible in subclasses
}
