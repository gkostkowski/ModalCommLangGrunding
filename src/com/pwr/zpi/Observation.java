package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes object's observation with it's time, id and set of traits.
 * Represents objects placed in real world, witnessed by agent.
 */
public class Observation {
    private String identifier;
    private long time;
    private Set<Trait> traits;

    public Observation(String identifier, Set<Trait> traits){
        this.identifier = identifier;
        this.traits = traits;
        time = System.currentTimeMillis();
    }

    public Observation(String identifier, Set<Trait> traits, long time){
        this.identifier = identifier;
        this.traits = traits;
        this.time = time;
    }

    public Observation() {

    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;
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
     * Determines type of given observation.
     *
     * @param observation Given observation of object.
     * @return Type of object.
     */
    public static ObjectType getType(Observation observation) {
        return null;    //todo
    }

    /**
     * Copies all attributes from other object.
     */
    public void copy(Observation other) {
        setIdentifier(other.getIdentifier());
        setTime(other.getTime());
        setTraits(other.getTraits());
    }

    /*public boolean isTypeOf(ObjectType objType) {
        return
    }*/

//    /**
//     * Checks whether two objects are equal.
//     * Equal when they have same types, same traits with same states.
//     *
//     * @param other Other object.
//     * @return      true/false
//     */
//    public boolean equals(Observation other) {
//        boolean contains = false;
//
//        if(type.equals(other.getType()))
//            return false;
//
//        if(traits.size() != other.getTraits().size())
//            return false;
//
//        for (Trait t1 : traits) {
//            for (Trait t2 : other.getTraits()) {
//                if(t1.equals(t2)){
//                    contains = true;
//                    break;
//                }
//            }
//            if(!contains)
//                return false;
//        }
//        return true;
//    }

}
