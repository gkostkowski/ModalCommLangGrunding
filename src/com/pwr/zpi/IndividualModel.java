package com.pwr.zpi;

import java.util.Set;

/**
 * Individual model represents exact object witnessed by agent.
 * IndividualModel is based on one specific ObjectType,
 * which is a signature for that IndividualModel.
 * There is one individual model for each object.
 * It has a name which gives a way of communication between human and agent.
 */
public class IndividualModel {
    private Identifier identifier;
    private ObjectType type;
    private String name;

    public IndividualModel(Identifier identifier, ObjectType type) {
        this.identifier = identifier;
        this.type = type;
        name = identifier.getName();
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    /**
     *
     * @return String representation of models' name
     */
    public String getName()
    {
        return name;
    }


}
