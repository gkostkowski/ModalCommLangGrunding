package com.pwr.zpi;

/**
 * Created by Mateo on 22.04.2017.
 */
public abstract class Identifier {

    abstract public String getId();

    abstract public ObjectType getType();

    public boolean equals(Identifier other_identifier) {
        if(other_identifier.getClass() != this.getClass())
            return false;
        else{
            if (this.getId().equals(other_identifier.getId())) {
                return true;
            }
            else{
                return false;
            }
        }
    }
}
