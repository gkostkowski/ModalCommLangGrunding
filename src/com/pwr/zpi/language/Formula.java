package com.pwr.zpi.language;

import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 */
public abstract class Formula {

    public abstract Set<Trait> getTraits();
    public abstract Object getObject();

    public abstract Type getType();

    enum Type {
        SIMPLE_MODALITY,
        MODAL_CONJUNCTION
    }

    abstract State evaluate();
/*    abstract com.pwr.zpi.Object getAffectedObject();

    *//**
     * Returns traits affected in formula - only one trait in case of SimpleFormula; two or more in case of ComplexFormula.
     * @return Collections of affected traits.
     *//*
    abstract Collection<Trait> getAffectedTraits();*/
}
