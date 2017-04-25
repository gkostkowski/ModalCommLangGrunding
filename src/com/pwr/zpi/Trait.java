package com.pwr.zpi;

import java.util.*;

/**
 * Trait - pair of name and value. Defines object's characteristics.
 */
public class Trait <K, V> {
    public static final int UPPER_BOUND = 70;
    public static final int LOWER_BOUND = 30;

    private String name;
    private V value;

    public Trait(String name,V value){
        this.name = name;
        this.value = value;
    }

    public Trait() {

    }

    public Trait(Trait<K,V> other) {
        this.setValue(other.getValue());
        this.setName(other.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Check if traits are equal (same names and same state).
     *
     * @return true/false
     */
    public boolean equals(Trait other){
        return name.equalsIgnoreCase(other.getName()) && value.equals(other.getValue()) && stateOfTrait() == other.stateOfTrait();
    }

    /**
     * Used to determine state of given trait based on it's value.
     *
     * @return State of trait.
     */
    public State stateOfTrait() {
        /*int val = (int)this.value;
        if(val > UPPER_BOUND) {
            return State.IS;
        }
        else {
            if (val < LOWER_BOUND)
                return State.IS_NOT;
            else    // <LOWER_BOUND,UPPER_BOUND>
                return State.MAYHAPS;
        }*/
        return State.MAYHAPS; //todo
    }

    /**
     * Copies all attributes from other trait.
     */
    public void copy(Trait<K, V> other) {
        setName(other.getName());
        setValue(other.getValue());
    }

    public TraitSignature<K, V> asTraitSignature() {
        return new TraitSignature<K, V>(name, value.getClass());
    }

    /**
     * Turns set of traits into equivalent set of traits signatures.
     * @param traits
     * @param <K> Key type of Trait.
     * @param <V> Value type of Trait.
     * @return
     */
    public static <K, V> Set<TraitSignature<K, V>> getSignatures(Set<Trait<K,V>> traits) {
        List<TraitSignature<K, V>> res = new ArrayList<>();
        for (Trait<K,V> trait :traits)
            res.add(trait.asTraitSignature());
        return new HashSet<>(res);
    }
}
