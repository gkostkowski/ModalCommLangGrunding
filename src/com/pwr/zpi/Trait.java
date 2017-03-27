package com.pwr.zpi;

/**
 * Trait - pair of name and value. Defines object's characteristics.
 */
public class Trait <K, V> {
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
        return this.name == other.getName() && this.stateOfTrait() == other.stateOfTrait();
    }

    /**
     * Used to determine state of given trait based on it's value.
     *
     * @return State of trait.
     */
    public State stateOfTrait() {
        int val = (int)this.value;
        if(val > 70) {
            return State.Is;
        }
        else {
            if (val < 30)
                return State.Is_Not;
            else    // <30,70>
                return State.Mayhaps;
        }
    }
}
