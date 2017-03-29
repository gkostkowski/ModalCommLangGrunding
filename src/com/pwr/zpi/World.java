package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 *
 */
public class World {
    public Set<Object> getObjects() {
        return objects;
    }

    public void setObjects(Set<Object> objects) {
        this.objects = objects;
    }

    //protected int time=0;
    protected Set<Object> objects;
    /**
     * Keeps references to Objects included in world, perceived as containing certain trait (NamedCollection<Name, Object>),
     * each Collection for respective moments in time.
     * @param
     * @return
     */
//    protected NamedCollection<Name, Collection<NamedCollection<Name, Object>>> describedObjects;
    //Zrób to na parach.
}
