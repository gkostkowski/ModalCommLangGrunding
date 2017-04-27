package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SimpleFormula is understood as atomic formula and it's used as part of ComplexFormula.
 */
public class SimpleFormula extends Formula {

    private IndividualModel individualModel;
    private TraitSignature trait;
    private boolean isNegated;

    public SimpleFormula(IndividualModel individualModel, TraitSignature trait, boolean isNegated) throws InvalidSentenceFormulaException
    {
        this.individualModel = individualModel;
        this.trait = trait;
        if(!checkTraits())
            throw new InvalidSentenceFormulaException();
        this.isNegated = isNegated;
    }

    public SimpleFormula(IndividualModel model, TraitSignature trait) throws InvalidSentenceFormulaException {
        this(model, trait, false);
    }

    /**
     * Constructor of SimpleFormula
     * @param model Object which is being considered in Formula
     * @param traits list of traits which should be size of 1,
     * @param statesSeq list of states which should be size of 1
     */
    public SimpleFormula(IndividualModel model, List<TraitSignature> traits, List<State> statesSeq) throws InvalidSentenceFormulaException{
        if(traits.size() != 1 || statesSeq.size() != 1)
            throw new InvalidSentenceFormulaException();
        individualModel = model;
        trait = traits.get(0);
        if(!checkTraits())
            throw new InvalidSentenceFormulaException();
        if(statesSeq.get(0) == State.IS)
            isNegated = false;
        else isNegated = true;

    }

    public SimpleFormula(IndividualModel model, List<TraitSignature> traits) throws InvalidSentenceFormulaException
    {
        this(model, traits, Arrays.asList(State.IS));
    }

    public TraitSignature getTrait()
    {
        return trait;
    }

    public boolean isNegated()
    {
        return isNegated;
    }


    @Override
    public List<TraitSignature> getTraits() {
        return Arrays.asList(trait);
    }

    @Override
    public IndividualModel getModel() {
        return individualModel;
    }

    /**
     * Return information that formula is a simple modality
     * @return Type of formula
     */
    @Override
    public Type getType() {
        return Type.SIMPLE_MODALITY;
    }

    public List<State> getStates()
    {
        List<State> states = new ArrayList<>();
        states.add((isNegated) ? State.IS_NOT : State.IS);
        return states;
    }


    /**
     * used to check if two Formulas are equal - concern same individual model,
     * have the same trait and state of this trait
     * @param other second formula
     * @return true if both are the same
     */
    public boolean equals(Formula other)
    {
        if(other instanceof SimpleFormula)
            if(individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if(trait.equals(((SimpleFormula) other).trait))
                        return true;
        return false; //todo czy sprawdzać stan isNegated
    }

    private boolean checkTraits()
    {
        return individualModel.checkIfContainsTrait(trait);
    }

}
