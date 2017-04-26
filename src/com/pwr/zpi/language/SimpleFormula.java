package com.pwr.zpi.language;

import com.pwr.zpi.IndividualModel;
import com.pwr.zpi.Observation;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;

import java.util.Arrays;
import java.util.List;

/**
 * SimpleFormula is understood as atomic formula and it's used as part of ComplexFormula.
 */
public class SimpleFormula extends Formula {

    IndividualModel observation;
    Trait trait;
    boolean isNegated;

    public SimpleFormula(IndividualModel observation, Trait trait, boolean isNegated) {
        this.observation = observation;
        this.trait = trait;
        this.isNegated = isNegated;
    }

    public SimpleFormula(IndividualModel observation, Trait trait) {
        this(observation, trait, false);
    }

    /**
     * Constructor of SimpleFormula
     * @param o Object which is being considered in Formula
     * @param traits list of traits which should be size of 1,
     * @param statesSeq list of states which should be size of 1
     */
    public SimpleFormula(IndividualModel o, List<Trait> traits, List<State> statesSeq) throws InvalidSentenceFormulaException{
        /*build formula like: state1(trait1(o))*/ //todo
        if(traits.size() != 1 || traits.size() != statesSeq.size())
            throw new InvalidSentenceFormulaException();
        observation = o;
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
    public IndividualModel getModel() {
        return observation;
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
     * Used to determine whether given observation has given trait
     * by returning its state (trait IS, IS_NOT or MAYHAPS is occurring in observation).
     *
     * @return State of trait's occurrence in observation.
     */
    public State evaluate() {
        State result = State.IS; // todo observation.hasTrait(trait);

        if(isNegated)  // when isNegated is true reverse result
            switch (result) {
                case IS:
                    return State.IS_NOT;
                case IS_NOT:
                    return State.IS;
            }

        return result;
    }

    public boolean equals(Formula other)
    {
        if(other instanceof SimpleFormula)
            if(observation.getIdentifier().equals(other.getModel().getIdentifier()))
                if(trait.equals(((SimpleFormula) other).trait))
                        return true;
        return false; //todo czy sprawdzać stan isNegated
    }

}
