package com.pwr.zpi.language;

import com.pwr.zpi.State;
import com.pwr.zpi.Trait;

import java.util.Collection;

/**
 *
 */
public abstract class Formula {

    abstract State evaluate();
/*    abstract com.pwr.zpi.Object getAffectedObject();

    *//**
     * Returns traits affected in formula - only one trait in case of SimpleFormula; two or more in case of ComplexFormula.
     * @return Collections of affected traits.
     *//*
    abstract Collection<Trait> getAffectedTraits();*/
}
