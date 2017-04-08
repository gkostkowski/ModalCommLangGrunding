package com.pwr.zpi;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents types of objects existed in world, which observations are processed in system.
 * It determines allowed set of traits for certain object (and its observation).
 */
public class ObjectType {
    String id;
    LinkedList<TraitSignature> traits;

    public ObjectType(String id, LinkedList<TraitSignature> traits) {
        this.id = id;
        this.traits = traits;
    }

    public String getId() {
        return id;
    }

    public LinkedList<TraitSignature> getTraits() {
        return traits;
    }


}
