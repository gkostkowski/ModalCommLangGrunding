/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.semantic;

import com.pwr.zpi.language.Trait;
import com.pwr.zpi.io.XMLDAO;

import java.util.Collection;
import java.util.List;

/**
 * Represents types of observations existed in world, which observations are processed in system.
 * Only objects with some of types defined in config file can exist in system.
 * It determines allowed set of valuedTraits for certain object (and its observation).
 *
 * @author Grzegorz Kostkowski
 */
public class ObjectType {
    private String typeId;
    private List<Trait> traits;

    public ObjectType(String id, List<Trait> traits) {
        this.typeId = id;
        this.traits = traits;
    }

    public String getTypeId() {
        return typeId;
    }

    public List<Trait> getTraits() {
        return traits;
    }


    /**
     * Method that return a trait based on a given name
     * @param name name of Trait
     * @return Trait with a given name or null if object doesn't contain such trait
     */
    public Trait findTraitByName(String name)
    {
        for(Trait trait: traits)
            if(trait.equals(name))
                return trait;
        return null;
    }

    /**
     * Method realizes loading objects types from config file and returns collection of such types.
     * @return
     */
    public static Collection<ObjectType> getObjectTypes() {
        return new XMLDAO<>().loadTypesDefinitions();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectType that = (ObjectType) o;

        if (!getTypeId().equals(that.getTypeId())) return false;
        return getTraits().containsAll(that.getTraits());
    }

    @Override
    public int hashCode() {
        int result = getTypeId().hashCode();
        result = 31 * result + getTraits().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ObjectType{" +
                typeId + ": " +
                traits +
                '}';
    }
}
