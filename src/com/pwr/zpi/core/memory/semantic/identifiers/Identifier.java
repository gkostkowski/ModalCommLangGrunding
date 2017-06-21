package com.pwr.zpi.core.memory.semantic.identifiers;

import com.pwr.zpi.core.memory.semantic.ObjectType;
import com.pwr.zpi.exceptions.ObjectTypeNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that allows to use multiple identifiers at the same time.
 * NOTE: Children of this class must implement no-argument constructor.
 *
 * @author Mateusz Gawlowski
 */
public abstract class Identifier {

    String idNumber;

    public void setId(String idNumber){
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Checks whether given id number is member of identifier (id matches identifiers scheme).
     *
     * @return Is member (true/false).
     */
    abstract public boolean isIdMemberOf(String idNumber);

    /**
     * Matches the part of this id number with object type id and return this type.
     *
     * @return Type of object described by this identifier.
     */
    public ObjectType getType() {
        for(ObjectType objectType : ObjectType.getObjectTypes()){
            if(objectType.getTypeId().equals(getIdNumber().substring(0,2))) //substring(0,2) returns two first chars
                return objectType;
        }
        Logger.getAnonymousLogger().log(Level.WARNING, "Object type not found.", new ObjectTypeNotFoundException());
        return null;
    }

    /**
     * Checks whether this identifier is equal to another identifier.
     *
     * @param otherIdentifier Identifier that is being compared to.
     * @return Equal - true/false.
     */
    @Override
    public boolean equals(Object otherIdentifier) {
        return otherIdentifier.getClass() == this.getClass()
                && this.getIdNumber().equals(((Identifier)otherIdentifier).getIdNumber());
    }
}
