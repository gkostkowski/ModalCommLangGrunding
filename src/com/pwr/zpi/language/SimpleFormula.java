package com.pwr.zpi.language;

import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.language.Formula;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public class SimpleFormula implements Formula {

    Object object;
    Trait trait;
    State isNegated;

    public SimpleFormula(Object operand, Trait trait, State isNegated) {
        this.object = operand;
        this.trait = trait;
        this.isNegated = isNegated;
    }
    public SimpleFormula(Object operand, Trait trait) {
        this(operand, trait, false);
    }

    @Override
    public State evaluate() {
        return object.hasTrait(trait) && !isNegated;
    }

    getState
}
