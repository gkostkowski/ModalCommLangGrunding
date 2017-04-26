package com.pwr.zpi;


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

    public IndividualModel(Identifier identifier, ObjectType type) {
        this.identifier = identifier;
        this.type = type;
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
     * Simply returns name of the object that is understandable by both human and agent.
     * @return Name of object.
     */
    public String getName()
    {
        return identifier.getName();
    }


}
