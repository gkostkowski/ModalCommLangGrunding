package com.pwr.zpi.language;

import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.core.memory.semantic.IndividualModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SimpleFormula is understood as atomic formula and it's used as part of ComplexFormula.
 *
 * @author Weronika Wolska
 * @author Grzegorz Kostkowski
 * @author Mateusz Gawłowski
 */
public class SimpleFormula extends Formula implements Comparable<SimpleFormula>{

    /**
     * Subject of the formula in form of individual model
     */
    private IndividualModel individualModel;
    /**
     * Trait od the formula
     */
    private Trait trait;
    /**
     * boolean if the state of the trait equals IS_NOT
     */
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
        isNegated = statesSeq.get(0) != State.IS;
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

    /**
     * @return trait of the formula
     */
    public Trait getTrait()
    {
        return trait;
    }

    /**
     * @return true if trait of formula equals IS_NOT, otherwise false
     */
    public boolean isNegated()
    {
        return isNegated;
    }
    /**
     * @return trait in form of list of Traits
     */
    @Override
    public List<Trait> getTraits() {
        return Arrays.asList(trait);
    }

    /**
     * @return individual model which is the subject of the formula
     */
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

    /**
     * State of the trait of the formula in form of list of states
     * @return
     */
    public List<State> getStates()
    {
        List<State> states = new ArrayList<>();
        states.add((isNegated) ? State.IS_NOT : State.IS);
        return states;
    }

    /**
     * Produces set of formulas which are complementary to this one. Amount and form of this formulas depends on
     * formula type. For convenience, given formula is also included in resulted collection.
     *
     * @return
     */
    @Override
    public List<Formula> getComplementaryFormulas() throws InvalidFormulaException {
        List<Formula> res = new ArrayList<>();
        res.add(this);
        res.add(new SimpleFormula(individualModel, trait, !isNegated));
        return res;
    }


    /**
     * used to check if two Formulas are equal - concern same individual model,
     * have the same trait and state of this trait
     * @param other second formula
     * @return true if both are the same
     */
    public boolean equals(Formula other)
    {
       if(isFormulaSimilar(other) && isNegated==((SimpleFormula)other).isNegated())
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

    /**
     * Checks if this object is equal to another one
     * @param o     another object
     * @return      true if they are same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleFormula that = (SimpleFormula) o;

        if (isNegated() != that.isNegated()) return false;
        if (!individualModel.equals(that.individualModel)) return false;
        return getTrait().equals(that.getTrait());
    }
    /**
     * @return hashcode of this formula
     */
    @Override
    public int hashCode() {
        int result = individualModel.hashCode();
        result = 31 * result + getTrait().hashCode();
        result = 31 * result + (isNegated() ? 1 : 0);
        return result;
    }

    /**
     * Checks if given formula regards the same object and same trait without checking their states
     *
     * @param other     Formula with which we compare this one
     * @return          true if they are similar or false otherwise
     */
    public boolean isFormulaSimilar(Formula other)
    {
        if(other instanceof SimpleFormula)
            if(individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if(trait.equals(((SimpleFormula) other).trait))
                    return true;
        return false;
    }

    /**
     * Method returns standard version of this formula. Standard formula is known as formula without any negations.
     *
     * @return new formula which is standard formula.
     */
    @Override
    public Formula getStandardFormula() throws InvalidFormulaException {
        if (!getStates().contains(State.IS_NOT))
            return this;
        else return new SimpleFormula(individualModel, trait, false);
    }

    /**
     * Method is used to point out exact formulas which should be used for building grounding set. It has application
     * in case of disjunction where grounding sets are composed of more than one conjunctive grounding set. In case
     * of simply modalities and conjunctions method should return formula which is provided as parameter.
     *
     * @return Array of partial formulas used in exact grounding.
     */
    @Override
    public List<Formula> getDependentFormulas() {
        return Arrays.asList(new Formula[]{this});
    }
    /**
     * Compares hashcode of this formula with another one
     * @param o     second formula
     * @return      1 if hashcode of the first was bigger then second's, 0 if they
     *              were the same, and -1 if it was smaller
     */
    @Override
    public int compareTo(SimpleFormula o) {
        int val1 = getStates().hashCode() + getTrait().hashCode() + individualModel.hashCode();
        int val2 = o.getStates().hashCode() + o.getTrait().hashCode() + o.individualModel.hashCode();
        return val1 > val2 ? 1 : (val1 < val2 ? -1 : 0);
    }
}
