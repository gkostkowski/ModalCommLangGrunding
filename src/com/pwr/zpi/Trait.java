package com.pwr.zpi;

/**
 * Describes trait, which can be used to describe object. Instances of this class
 * stands for traits signatures, so they don't have attached value.
 * Trait has its name.
 * There is assumption that all traits are binary.
 */
public class Trait {
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
     * method used to check if two traits are about same thing
     * @param otherName String representation of name of a trait
     * @return true if same, false if different
     */
    public boolean equals(String otherName)
    {
        return name.equalsIgnoreCase(otherName);
    }

    /**
     * method used to check if two Traits are about same thing
     * @param other second Trait
     * @return true if same, false otherwise
     */
    public boolean equals(Trait other)
    {
        return equals(other.getName());
    }





}
