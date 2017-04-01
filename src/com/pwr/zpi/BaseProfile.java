package com.pwr.zpi;

/**
 *
 */

import java.util.*;

/**
 * Represents state of world from agent perspective. It's established for certain moment in time.
 */
public class BaseProfile extends World {
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that all objects in that collections have this trait.
     */
    protected static Map<Trait, Set<Object>> describedByTraits;  //set
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that all objects in that collections DON'T HAVE this trait.
     */
    protected Map<Trait, Set<Object>> notDescribedByTraits;
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that state of having this trait by all objects is unknown.
     */
    protected Map<Trait, Set<Object>> indefiniteByTraits;
    protected int timestamp;

    public Map<Trait, Set<Object>> getDescribedByTraits() {
        return describedByTraits;
    }

    public void setDescribedByTraits(Map<Trait, Set<Object>> describedByTraits) {
        this.describedByTraits = describedByTraits;
    }

    public Map<Trait, Set<Object>> getNotDescribedByTraits() {
        return notDescribedByTraits;
    }

    public void setNotDescribedByTraits(Map<Trait, Set<Object>> notDescribedByTraits) {
        this.notDescribedByTraits = notDescribedByTraits;
    }

    public Map<Trait, Set<Object>> getIndefiniteByTraits() {
        return indefiniteByTraits;
    }

    public void setIndefiniteByTraits(Map<Trait, Set<Object>> indefiniteByTraits) {
        this.indefiniteByTraits = indefiniteByTraits;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }



    /*boolean isPAdequate(Collection<NamedCollection<Name, Object>> PSwT, Trait examinedTrait) {
        return false;
    }*/




    public Set<Object> getObjects(){
        return objects;
    }

    /**
     * Returns set of all objects included in given base profiles. Used when as an example merging base profiles
     * from working memory and long-term memory.
     * @param baseProfiles Set of base profiles.
     * @return Set of objects.
     */
    public static Set<Object> getObjects(Set<BaseProfile> baseProfiles) {
        Set<Object> res = new HashSet<>();
        for (BaseProfile bp :baseProfiles)
            res.addAll(bp.getObjects());
        return res;
    }
    /**
     * Returns set of all objects included in given base profiles. Used when as an example merging base profiles
     * from working memory and long-term memory.
     * @param baseProfiles Array of base profiles.
     * @return Set of objects.
     */
    public static Set<Object> getObjects(BaseProfile ... baseProfiles) {
        return getObjects(new HashSet<BaseProfile>(Arrays.asList(baseProfiles)));
    }

    /**
     * Returns set of objects indicated with given trait.
     * @param trait
     * @return Set of objects.
     */
    public Set<Object> getDescribedByTrait(Trait trait) {
        return describedByTraits.get(trait);
    }

    public Set<Object> getNotDescribedByTrait(Trait trait) {
        return notDescribedByTraits.get(trait);
    }

    public boolean DetermineIfSetHasTrait(Object o, @SuppressWarnings("rawtypes") Trait P,int time){
        Map<Trait, Set<Object>> describedObjects = describedByTraits;
        if(describedObjects.containsKey(P)){
            return true;
        }
        else{return false;}
    }

    public void copy(BaseProfile other) {
        setDescribedByTraits(other.getDescribedByTraits());
        setNotDescribedByTraits(other.getNotDescribedByTraits());
        setIndefiniteByTraits(getIndefiniteByTraits());
        setTimestamp(other.getTimestamp());
        setObjects(other.getObjects());
    }
}
