package com.pwr.zpi;

import java.util.Map;

/**
 * Describes object's observation with it's timestamp, id and set of valuedTraits.
 * Represents observations placed in real world, witnessed by agent.
 */
public class Observation {
    private Identifier identifier;
    private int timestamp;
    //private Set<Trait> valuedTraits;
    private Map<Trait, Boolean> valuedTraits;


    public Observation(Identifier identifier, Map<Trait, Boolean> traits){
        this.identifier = identifier;
        this.valuedTraits = traits;
        timestamp = (int)System.currentTimeMillis();
    }

    public Observation(Identifier identifier, Map<Trait, Boolean> traits, int timestamp){
        this(identifier, traits);
        this.timestamp = timestamp;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Map<Trait, Boolean> getValuedTraits() {
        return valuedTraits;
    }

    public void setTraits(Map<Trait, Boolean> traits)
    {
        this.valuedTraits = traits;
    }

    /**
     * Returns type of object that this observation concern through finding its individual model.
     * @return Type of object or null when model not found.
     */
    public ObjectType getType(){
        return identifier.getType();
    }

    public String toString(){
        return "Observation(time: " + timestamp
                + "; id: " + identifier.getIdNumber()
                + "; traits: " + valuedTraits;
    }
}
