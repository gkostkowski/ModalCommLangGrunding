package com.pwr.zpi.language;

import com.pwr.zpi.State;
import com.pwr.zpi.Trait;

import java.util.Collection;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public interface Formula {

    abstract State evaluate();
    abstract Object getAffectedObject();

    /**
     * Returns traits affected in formula - only one trait in case of SimpleFormula; two or more in case of ComplexFormula.
     * @return Collections of affected traits.
     */
    abstract Collection<Trait> getAffectedTraits();
}
