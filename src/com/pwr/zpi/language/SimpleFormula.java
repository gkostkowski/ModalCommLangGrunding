package com.pwr.zpi.language;

import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SimpleFormula is understood as atomic formula and it's used as part of ComplexFormula.
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
     * Constructor of SimpleFormula
     * @param o Object which is being considered in Formula
     * @param traits list of traits which should be size of 1,
     * @param statesSeq list of states which should be size of 1
     */
    public SimpleFormula(Object o, List<Trait> traits, List<State> statesSeq) throws InvalidSentenceFormulaException{
        /*build formula like: state1(trait1(o))*/ //todo
        if(traits.size() != 1 || traits.size() != statesSeq.size())
            throw new InvalidSentenceFormulaException();
        object = o;
        if(statesSeq.get(0) == State.IS)
            isNegated = false;
        else isNegated = true;
        trait = traits.get(0);
    }

    @Override
    public List<Trait> getTraits() {
        return Arrays.asList(trait);
    }

    @Override
    public Object getObject() {
        return object;
    }

    /**
     * Return information that formula is a simple modality
     * @return Type of formula
     */
    @Override
    public Type getType() {
        return Type.SIMPLE_MODALITY;
    }

    /**
     * Used to determine whether given object has given trait
     * by returning its State - trait IS or IS_NOT occurring in object.
     *
     * @return State of trait's occurrence in object (IS, IS_NOT).
     */
    public State evaluate() {
        State result = object.hasTrait(trait);

        if(isNegated)  // when isNegated is true reverse result
            switch (result) {
                case IS:
                    return State.IS_NOT;
                case IS_NOT:
                    return State.IS;
            }

        return result;
    }

}
