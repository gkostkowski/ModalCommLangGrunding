package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-16.
 */
public class World {
    //protected int time=0;
    protected Set<Object> objects;
    /**
     * Keeps references to Objects included in world, perceived as containing certain trait (NamedCollection<Name, Object>),
     * each Collection for respective moments in time.
     * @param
     * @return
     */
    protected NamedCollection<Name, Collection<NamedCollection<Name, Object>>> describedObjects;
    //Zrób to na parach.
}
