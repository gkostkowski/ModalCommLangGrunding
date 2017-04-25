package com.pwr.zpi;

/**
 * Created by Mateo on 22.04.2017.
 */
public abstract class Identifier {

    abstract public String getId();

    abstract public ObjectType getType();


    /**
     *
     * @return String representation of a name of that object which would be
     * common with agent and a human
     */
    abstract public String getName();

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
