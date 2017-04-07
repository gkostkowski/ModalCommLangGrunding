package com.pwr.zpi.language;

import com.pwr.zpi.Observation;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;

import java.util.List;
import java.util.Set;

/**
 *
 */
public abstract class Formula {

    public abstract Set<Trait> getTraits();
    public abstract Observation getObject();

    public abstract Type getType();

    /**
     * Gives list of successive states. Classic case will contains states which describe whether parts of formula[traits]
     * (in case of complex formula - simple formula is special case and contains one part) occur with or without negation.
     * The order of returned states is respective to order of traits returned by getTraits().
     * @return List of states.
     */
    public List<State> getStates() {
        return null;
        //return states;
    }

    enum Type {
        SIMPLE_MODALITY,
        MODAL_CONJUNCTION
    }

    abstract State evaluate();
/*    abstract com.pwr.zpi.Observation getAffectedObject();

    *//**
     * Returns traits affected in formula - only one trait in case of SimpleFormula; two or more in case of ComplexFormula.
     * @return Collections of affected traits.
     *//*
    abstract Collection<Trait> getAffectedTraits();*/
}
