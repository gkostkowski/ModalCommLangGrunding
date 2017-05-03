package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SimpleFormula is understood as atomic formula and it's used as part of ComplexFormula.
 */
public class SimpleFormula extends Formula {

    private IndividualModel individualModel;
    private Trait trait;
    private boolean isNegated;

    /**
     * COnstructor of SimpleFormula
     * @param individualModel individual model which formula describes
     * @param trait Trait which describes given model
     * @param isNegated boolean value of whether the state of formula is negated or not
     * @throws InvalidFormulaException when given trait doesn't describe type of the model
     */
    public SimpleFormula(IndividualModel individualModel, Trait trait, boolean isNegated) throws InvalidFormulaException
    {
        if(individualModel==null || trait == null)
            throw new NullPointerException("One of the parameters is null");
        this.individualModel = individualModel;
        this.trait = trait;
        if(!checkTraits())
            throw new InvalidFormulaException("Trait doesn't describe type of the model");
        this.isNegated = isNegated;
    }

    /**
     * Generalised previous constructor, sets state of trait to IS
     * @param model individual model which formula describes
     * @param trait Trait which describes given model
     * @throws InvalidFormulaException when given trait doesn't describe type of the model
     */
    public SimpleFormula(IndividualModel model, Trait trait) throws InvalidFormulaException {
        this(model, trait, false);
    }

    /**
     * Constructor of SimpleFormula
     * @param model Object which is being considered in Formula
     * @param traits list of traits which should be size of 1,
     * @param statesSeq list of states which should be size of 1
     * @throws InvalidFormulaException when sizes of list of states and traits are not equal to 1 or when trait
     *      doesn't describe type of the model
     */
    public SimpleFormula(IndividualModel model, List<Trait> traits, List<State> statesSeq) throws InvalidFormulaException{
        if(model == null || traits == null || statesSeq == null)
            throw new NullPointerException("One of the parameters is null");
        if(traits.size() != 1 || statesSeq.size() != 1)
            throw new InvalidFormulaException("Number of traits or states is not equal to 1");
        individualModel = model;
        trait = traits.get(0);
        if(!checkTraits())
            throw new InvalidFormulaException("Trait doesn't describe type of the model");
        if(statesSeq.get(0) == State.IS)
            isNegated = false;
        else isNegated = true;
    }

    /**
     * Generalised constructor which sets state of trait to IS
     * @param model Object which is being considered in Formula
     * @param traits list of traits which size must be of 1
     * @throws InvalidFormulaException when sizes of list of states and traits are not equal to 1 or when trait
     *      doesn't describe type of the model
     */
    public SimpleFormula(IndividualModel model, List<Trait> traits) throws InvalidFormulaException
    {
        this(model, traits, Arrays.asList(State.IS));
    }

    public Trait getTrait()
    {
        return trait;
    }

    public boolean isNegated()
    {
        return isNegated;
    }


    @Override
    public List<Trait> getTraits() {
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
                    if(isNegated==((SimpleFormula) other).isNegated())
                        return true;
        return false;
    }

    /**
     * method checks if set trait desscribes type of the model
     * @return true if describes, false otherwise
     */
    private boolean checkTraits()
    {
        return individualModel.checkIfContainsTrait(trait);
    }

}
