package com.pwr.zpi;

/**
 * Created by Grzesiek on 2017-03-16.
 */

import java.util.Collection;
import java.util.Map;

/**
 * Represents state of world from agent perspective. It's established for certain moment in time.
 */
public class BaseProfile extends World {
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that all objects in that collections have this trait.
     */
    protected Map<Trait, Collection<Object>> describedByTraits;
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that all objects in that collections DON'T HAVE this trait.
     */
    protected Map<Trait, Collection<Object>> notDescribedByTraits;
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that state of having this trait by all objects is unknown.
     */
    protected Map<Trait, Collection<Object>> indefiniteByTraits;
    protected int timestamp;

    public Map<Trait, Collection<Object>> getDescribedByTraits() {
        return describedByTraits;
    }

    public void setDescribedByTraits(Map<Trait, Collection<Object>> describedByTraits) {
        this.describedByTraits = describedByTraits;
    }

    public Map<Trait, Collection<Object>> getNotDescribedByTraits() {
        return notDescribedByTraits;
    }

    public void setNotDescribedByTraits(Map<Trait, Collection<Object>> notDescribedByTraits) {
        this.notDescribedByTraits = notDescribedByTraits;
    }

    public Map<Trait, Collection<Object>> getIndefiniteByTraits() {
        return indefiniteByTraits;
    }

    public void setIndefiniteByTraits(Map<Trait, Collection<Object>> indefiniteByTraits) {
        this.indefiniteByTraits = indefiniteByTraits;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }



    boolean isPAdequate(Collection<NamedCollection<Name, Object>> PSwT, Trait examinedTrait) {
        return false;
    }



    /**
     *
     * @return all described objects
     */
    public Collection<NamedCollection<Names, Object>> giveMeWorld(){
        return describedObjects;
    }
}
