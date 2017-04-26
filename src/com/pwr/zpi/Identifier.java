package com.pwr.zpi;

import static com.pwr.zpi.Main.ObjectTypeCollection;

/**
 * Abstract class that allows to use multiple identifiers at the same time.
 */
public abstract class Identifier {

    /**
     * Simply name of the object that is understandable by both human and agent.
     */
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
        for(ObjectType objectType : ObjectTypeCollection){
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
}
