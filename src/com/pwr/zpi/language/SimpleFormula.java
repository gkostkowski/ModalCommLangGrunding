package com.pwr.zpi.language;

import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;

import java.util.Collection;

/**
 * SimpleFormula is designed to be used as part of ComplexFormula
 */
public class SimpleFormula implements Formula {

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
     * by returning its State - trait Is or Is_Not occurring in object
     *
     * @param  isNegated    used to return opposite result
     * @param  trait        trait that we look for in object
     * @param  object       being that is defined by set of traits
     * @return              State of trait's occurrence in object (Is, Is_Not)
     */
    public State evaluate() {
        State result = object.hasTrait(trait) ? State.Is : State.Is_Not;

        if(isNegated && result == State.Is)  // when isNegated is true reverse result
            result = State.Is_Not;
        else
            result = State.Is;

        return result;
    }

    @Override
    public Object getAffectedObject() {
        return object;
    }

    @Override
    public Collection<Trait> getAffectedTraits() {
        return List<Trait>;
    }
}
