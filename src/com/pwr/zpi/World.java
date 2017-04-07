package com.pwr.zpi;

import java.util.Set;

/**
 *
 */
public class World {
    public Set<Observation> getObjects() {
        return objects;
    }

    public void setObjects(Set<Observation> objects) {
        this.objects = objects;
    }

    //protected int time=0;
    protected Set<Observation> objects;

}
