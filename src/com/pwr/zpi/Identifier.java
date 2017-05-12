package com.pwr.zpi;

import java.util.Collection;

import static com.pwr.zpi.Agent.objectTypeCollection;

/**
 * Abstract class that allows to use multiple identifiers at the same time.
 */
public abstract class Identifier {

    /**
     * Simply returns the id number as String.
     * @return Id number.
     */
    abstract public String getIdNumber();

    /**
     * Matches the part of this id number with object type id and return this type.
     * @return Type of object described by this identifier.
     */
    public ObjectType getType() {
        for(ObjectType objectType : objectTypeCollection){
            if(objectType.getTypeId().equals(getIdNumber().substring(0,2))) //substring(0,2) returns two first chars
                return objectType;
        }
        return null;
    }

    /**
     * Checks whether this identifier is equal to another identifier.
     * @param other_identifier Identifier that is being compared to.
     * @return Equal - true/false.
     */
    public boolean equals(Identifier other_identifier) {
        if(other_identifier.getClass() != this.getClass())
            return false;
        else{
            if (this.getIdNumber().equals(other_identifier.getIdNumber())) {
                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * This method reads identifiers of objects which was observed in the world. Every unique identifier existence
     * points every single object (with specified type) existed in world, which is represented in system through
     * IndividualModel instances.
     * @return Collection of unique identifiers.
     */
    public static Collection<Identifier> readIdentifiers() {
        Collection<Identifier> res = null;
        //todo
        return res;
    }
}
