package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes object's observation with it's time, id and set of traits.
 * Represents objects placed in real world, witnessed by agent.
 */
public class Observation {
    private Identifier identifier;
    private long time;
    private Set<Trait> traits;

    public Observation() {

    }

    public Observation(Identifier identifier, Set<Trait> traits){
        this.identifier = identifier;
        this.traits = traits;
        time = System.currentTimeMillis();
    }

    public Observation(Identifier identifier, Set<Trait> traits, long time){
        this.identifier = identifier;
        this.traits = traits;
        this.time = time;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
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

    /**
     * Sets traits which are correct for specific object
     * meaning that object is described by them and values are from domain.
     * @param traits Set of observed traits.
     */
    /*public void setTraits(Set<Trait> traits)
    {
        IndividualModel model = IMCollection.getRepresentationByIdentifier(identifier);
        for(Trait trait : traits)
        {
            if(model.getType().getSpecificTrait(trait.getName()).isInDomain(trait.getValue()))
                this.traits.remove(trait); // todo albo tak, albo zwracamy jakiś błąd, bo nie wiemy czy obserwacja była w takim razie prawdziwa
        }
        this.traits = traits;
    }*/

    /**
     * Checks if object has given trait and return it's state.
     * @param trait Given trait.
     * @return State of trait or null when object doesn't has trait.
     */
    public State hasTrait(Trait trait) {
        for (Trait t : traits) {
            if (t.getName().equals(trait.getName()))
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
     * @param sets Array of sets of objects.
     * @return Set of objects.
     */
    public static Set<Observation> getObjects(Set<Observation> ... sets) {
        Set<Observation> res = new HashSet<>();
        for (Set<Observation> set :sets)
            res.addAll(set);
        return res;
    }

    /**
     * Returns type of object that this observation concern.
     * @return Type of object.
     */
    public ObjectType getType(){
        return identifier.getType();
    }

}
