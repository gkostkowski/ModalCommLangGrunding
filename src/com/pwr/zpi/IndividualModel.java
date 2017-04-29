package com.pwr.zpi;


import java.util.List;

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
     * checks if given list of traits contains in models' traits.
     * @param traits List of TraitSignatures that are supposed to be checked
     * @return true if all traits describe model, false otherwise
     */
    public boolean checkIfContainsTraits(List<TraitSignature> traits)
    {
        for(TraitSignature trait: traits)
            if(!checkIfContainsTrait(trait))
                return false;
        return true;
    }

    /**
     * method checks if given TraitSignature describes model
     * @param trait TraitSignature to be checked
     * @return true if TraitSignature describes model, false otherwise
     */
    public boolean checkIfContainsTrait(TraitSignature trait)
    {
        for(TraitSignature traitSignature: type.getTraits())
            if(trait.equals(traitSignature))
                return true;
        return false;
    }

}
