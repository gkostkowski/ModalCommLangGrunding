package com.pwr.zpi;

import java.util.*;

/**
 * Represents state of world from agent perspective. It's established for certain moment in time.
 */
public class BaseProfile extends World {
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that all objects in that collections have this trait.
     */
    protected static Map<Trait, Set<Observation>> describedByTraits;
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that all objects in that collections DON'T HAVE this trait.
     */
    protected Map<Trait, Set<Observation>> notDescribedByTraits;
    /**
     * Map of traits and related collections of objects. If some collection is related with certain trait, then
     * it mean that state of having this trait by all objects is unknown.
     */
    protected Map<Trait, Set<Observation>> indefiniteByTraits;
    protected int timestamp;

    public BaseProfile (Map<Trait, Set<Observation>> SetOTraits, int timestamp) {
        this.indefiniteByTraits = SetOTraits;
        this.timestamp = timestamp;
    }

    public BaseProfile() {

    }

    public Map<Trait, Set<Observation>> getDescribedByTraits() {
        return describedByTraits;
    }

    public void setDescribedByTraits(Map<Trait, Set<Observation>> describedByTraits) {
        this.describedByTraits = describedByTraits;
    }

    public Map<Trait, Set<Observation>> getNotDescribedByTraits() {
        return notDescribedByTraits;
    }

    public void setNotDescribedByTraits(Map<Trait, Set<Observation>> notDescribedByTraits) {
        this.notDescribedByTraits = notDescribedByTraits;
    }

    public Map<Trait, Set<Observation>> getIndefiniteByTraits() {
        return indefiniteByTraits;
    }

    public void setIndefiniteByTraits(Map<Trait, Set<Observation>> indefiniteByTraits) {
        this.indefiniteByTraits = indefiniteByTraits;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }



    public Set<Observation> getObjects(){
        return objects;
    }

    /**
     * Returns set of all objects included in given base profiles. Used when as an example merging base profiles
     * from working memory and long-term memory.
     * @param baseProfiles Set of base profiles.
     * @return Set of objects.
     */
    public static Set<Observation> getObjects(Set<BaseProfile> baseProfiles) {
        Set<Observation> res = new HashSet<>();
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
    public static Set<Observation> getObjects(BaseProfile ... baseProfiles) {
        return getObjects(new HashSet<BaseProfile>(Arrays.asList(baseProfiles)));
    }

    /**
     * Returns set of objects indicated with given trait.
     * @param trait
     * @return Set of objects.
     */
    public Set<Observation> getDescribedByTrait(Trait trait) {
        return describedByTraits.get(trait);
    }

    /**
     * Gives objects from appropriate sets. State describes which set should be selected: describedByTraits for State.IS etc.
     * @param trait
     * @return Set of objects.
     */
    public Set<Observation> getByTraitState(Trait trait, State state) {
        switch (state) {
            case IS: return describedByTraits.get(trait);
            case IS_NOT: return notDescribedByTraits.get(trait);
            default: return indefiniteByTraits.get(trait);
        }
    }

    public Set<Observation> getNotDescribedByTrait(Trait trait) {
        return notDescribedByTraits.get(trait);
    }

    public boolean DetermineIfSetHasTrait(Observation o, @SuppressWarnings("rawtypes") Trait P, int time, State stateOfDescription){
        Map<Trait, Set<Observation>> describedObjects = stateOfDescription.equals(State.IS) ?
                describedByTraits : (stateOfDescription.equals(State.IS_NOT) ?
                    notDescribedByTraits : indefiniteByTraits);
        return describedObjects.containsKey(P);

    }

    public void copy(BaseProfile other) {
        setDescribedByTraits(other.getDescribedByTraits());
        setNotDescribedByTraits(other.getNotDescribedByTraits());
        setIndefiniteByTraits(getIndefiniteByTraits());
        setTimestamp(other.getTimestamp());
        setObjects(other.getObjects());
    }
}
