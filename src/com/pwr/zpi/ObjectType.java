package com.pwr.zpi;

import java.util.List;

/**
 * Represents types of observations existed in world, which observations are processed in system.
 * It determines allowed set of traits for certain object (and its observation).
 */
public class ObjectType {
    private String typeId;
    private List<TraitSignature> traits;

    public ObjectType(String id, List<TraitSignature> traits) {
        this.typeId = id;
        this.traits = traits;
    }

    public String getTypeId() {
        return typeId;
    }

    public List<TraitSignature> getTraits() {
        return traits;
    }

}
