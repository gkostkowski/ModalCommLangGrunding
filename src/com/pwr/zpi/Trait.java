package com.pwr.zpi;

/**
 * Trait - pair of name and value. Defines object's characteristics.
 */
public class Trait <K, V> {
    public static final int UPPER_BOUND = 70;
    public static final int LOWER_BOUND = 30;

    private K name;
    private V value;

    public K getName() {
        return name;
    }

    public void setName(K name) {
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
        return name == other.getName() && stateOfTrait() == other.stateOfTrait();
    }

    /**
     * Used to determine state of given trait based on it's value.
     *
     * @return State of trait.
     */
    public State stateOfTrait() {
        int val = (int)this.value;
        if(val > UPPER_BOUND) {
            return State.IS;
        }
        else {
            if (val < LOWER_BOUND)
                return State.IS_NOT;
            else    // <LOWER_BOUND,UPPER_BOUND>
                return State.MAYHAPS;
        }
    }

    public void copy(Trait<K, V> other) {
        setName(other.getName());
        setValue(other.getValue());
    }
}
