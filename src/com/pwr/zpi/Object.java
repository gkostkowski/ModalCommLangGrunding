package com.pwr.zpi;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes Objects included in World, which have traits assumed as objective, consistent with real world.
 */
public class Object {
    protected int identifier;
    protected String type;
    private Set<Trait> traits; //won't be visible in subclasses

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;
    }

    /**
     * Checks whether two objects are equal.
     * Equal when - same types, same traits with same states.
     *
     * @param other Other object.
     * @return      true/false
     */
    public boolean equals(Object other) {
        boolean contains = false;

        // ???????????????????????????????????
        if(type.equals(other.getType()))
            return false;

        if(traits.size() != other.getTraits().size())
            return false;

        for (Trait t1 : traits) {
            for (Trait t2 : other.getTraits()) {
                if(t1.equals(t2)){
                    contains = true;
                    break;
                }
            }
            if(!contains)
                return false;
        }
        return true;
    }

    /**
     * Checks if object has given trait and return it's state.
     *
     * @param trait Given trait.
     * @return      State of trait or null when object does'nt has trait.
     */
    public State hasTrait(Trait trait) {
        for (Trait t : traits) {
            if (t.getName() == trait.getName())
                return t.stateOfTrait();
        }
        return null;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Simply gives set of all objects included in given list of sets.
     *
     * @param sets  Array of sets of objects.
     * @return      Set of objects.
     */
    public static Set<Object> getObjects(Set<Object> ... sets) {
        Set<Object> res = new HashSet<>();
        for (Set<Object> set :sets)
            res.addAll(set);
        return res;
    }
}
