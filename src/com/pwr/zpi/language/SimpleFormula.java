package com.pwr.zpi.language;

import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;

/**
 * SimpleFormula is designed to be used as part of ComplexFormula.
 */
public class SimpleFormula extends Formula {

    Object object;
    Trait trait;
    boolean isNegated;

    public SimpleFormula(Object object, Trait trait, boolean isNegated) {
        this.object = object;
        this.trait = trait;
        this.isNegated = isNegated;
    }

    public SimpleFormula(Object object, Trait trait) {
        this(object, trait, false);
    }

    /**
     * Used to determine whether given object has given trait
     * by returning its State - trait Is or Is_Not occurring in object.
     *
     * @return State of trait's occurrence in object (Is, Is_Not).
     */
    public State evaluate() {
        State result = object.hasTrait(trait);

        if(isNegated)  // when isNegated is true reverse result
            switch (result) {
                case Is:
                    return State.Is_Not;
                case Is_Not:
                    return State.Is;
            }

        return result;
    }

}
