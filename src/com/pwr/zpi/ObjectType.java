package com.pwr.zpi;

import java.util.LinkedList;

/**
 * Represents types of objects existed in world, which observations are processed in system.
 * It determines allowed set of traits for certain object (and its observation).
 */
public class ObjectType {
    String typeId;
    LinkedList<TraitSignature> traits;

    public ObjectType(String id, LinkedList<TraitSignature> traits) {
        this.typeId = id;
        this.traits = traits;
    }

    public String getTypeId() {
        return typeId;
    }

    public LinkedList<TraitSignature> getTraits() {
        return traits;
    }


}
