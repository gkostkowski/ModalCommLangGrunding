package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-16.
 */
public class World {
    protected int time=0;
    protected Set<Object> objects;
    /**
     * Keeps references to Objects included in world, perceived as containing certain trait (NamedCollection<Names, Object>),
     * each Collection for respective moments in time.
     */
    protected NamedCollection<Names, Collection<NamedCollection<Names, Object>>> describedObjects;
}
