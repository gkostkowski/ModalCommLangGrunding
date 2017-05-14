package com.pwr.zpi;

import java.util.HashMap;

/**
 * Describes trait, which can be used to describe object. Instances of this class
 * stands for valuedTraits signatures, so they don't have attached value.
 * Trait has its name.
 * There is assumption that all valuedTraits are binary.
 */
public class Trait implements Comparable<Trait> {
    private String name;

    public Trait(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return name of the TraitSiganture
     */
    public String getName() {
        return name;
    }

    /**
     * method used to check if two valuedTraits are about same thing
     * @param otherName String representation of name of a trait
     * @return true if same, false if different
     */
    @Override
    public boolean equals(Object otherName)
    {
        return this.toString().equalsIgnoreCase(otherName.toString());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(Trait o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
