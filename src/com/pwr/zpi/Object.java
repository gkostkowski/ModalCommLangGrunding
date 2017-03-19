package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-16.
 */
/**
 * Describes Objects included in World, which have traits assumed as objective, consistent with real world.
 */
public class Object {
    protected int identifier;
    protected String type;

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

    private Set<Trait> traits; //won't be visible in subclasses

    @Override
    public boolean equals(Object o) {
        return false; //TODO
    }

    State hasTrait(Trait t){} //todo

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;
    }

    @Override

    public int hashCode() {
        return getIdentifier();
    }
}
