package com.pwr.zpi.language;

import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.core.semantic.IndividualModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SimpleFormula is understood as atomic formula and it's used as part of ComplexFormula.
 *
 * @author Weronika Wolska
 * @author Grzegorz Kostkowski
 */
public class SimpleFormula extends Formula implements Comparable<SimpleFormula>{

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

    public void negate() {isNegated = !isNegated;}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleFormula that = (SimpleFormula) o;

        if (isNegated() != that.isNegated()) return false;
        if (!individualModel.equals(that.individualModel)) return false;
        return getTrait().equals(that.getTrait());
    }

    @Override
    public int hashCode() {
        int result = individualModel.hashCode();
        result = 31 * result + getTrait().hashCode();
        result = 31 * result + (isNegated() ? 1 : 0);
        return result;
    }

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

    @Override
    public int compareTo(SimpleFormula o) {
        int val1 = getStates().hashCode() + getTrait().hashCode() + individualModel.hashCode();
        int val2 = o.getStates().hashCode() + o.getTrait().hashCode() + o.individualModel.hashCode();
        return val1 > val2 ? 1 : (val1 < val2 ? -1 : 0);
    }
}
