package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 * Describes Objects included in World, which have traits assumed as objective, consistent with real world.
 */
public class Object {
    protected int identifier;
    protected String type;
    private Set<Trait> traits; //won't be visible in subclasses

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        return false; //TODO
    }

    public boolean hasTrait(Trait t){return true;} //todo

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
