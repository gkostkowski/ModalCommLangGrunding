package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes object's observation with it's id, type and set of traits.
 * Represents objects placed in real world, witnessed by agent.
 */
public class Observation {
    protected int identifier;
    protected String type;
    private Set<Trait> traits; //won't be visible in subclasses

    public Observation(int identifier, String type, Set<Trait> traits){
        this.identifier = identifier;
        this.type = type;
        this.traits = traits;
    }

    public Observation() {

    }

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
     * Equal when they have same types, same traits with same states.
     *
     * @param other Other object.
     * @return      true/false
     */
    public boolean equals(Observation other) {
        boolean contains = false;

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
     * @return      State of trait or null when object doesn't has trait.
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
    public static Set<Observation> getObjects(Set<Observation> ... sets) {
        Set<Observation> res = new HashSet<>();
        for (Set<Observation> set :sets)
            res.addAll(set);
        return res;
    }

    /**
     * Copies all attributes from other object.
     */
    public void copy(Observation other) {
        setIdentifier(other.getIdentifier());
        setType(other.getType());
        setTraits(other.getTraits());
    }

    /*public boolean isTypeOf(ObjectType objType) {
        return
    }*/
}
