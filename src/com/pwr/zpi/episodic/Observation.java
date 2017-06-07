package com.pwr.zpi.episodic;

import com.pwr.zpi.semantic.Identifier;
import com.pwr.zpi.semantic.ObjectType;
import com.pwr.zpi.language.Trait;

import java.util.Map;

/**
 * Describes object's observation with its timestamp, id and set of valuedTraits.
 * Represents observations placed in real world, witnessed by agent.
 *
 * @author Mateusz Gawłowski
 * @author Grzegorz Kostkowski
 */
public class Observation {
    private Identifier identifier;
    private int timestamp;
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

    @Override
    public String toString() {
        StringBuilder valTr = new StringBuilder();
        for (Trait t:valuedTraits.keySet()) {
            Boolean val = valuedTraits.get(t);
            valTr.append("\n\t\t");
            valTr.append(val == null ? "mayhaps " + t : (val ? "is " + t : "is not " + t));
        }

        return "Observation at "+timestamp+" {" +
                identifier +"as: "+
                valTr +
                '}';
    }
}
