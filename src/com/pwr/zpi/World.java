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

}
