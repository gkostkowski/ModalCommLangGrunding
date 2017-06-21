package com.pwr.zpi.language;

import com.pwr.zpi.core.memory.semantic.IndividualModel;
import com.pwr.zpi.exceptions.InvalidFormulaException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class extends the Formula class. It creates an object which represents a complex formula in modal communication
 * language. A complex formula is a structure which consists of o subject of formula, two traits of this subject, states
 * of those traits (whether they are negated or not) and an operator which connects them. Operator can be one from
 * the LogicOperator enum class from this package.
 * @author Weronika Wolska
 * @author Grzegorz Kostkowski
 */
public class ComplexFormula extends Formula implements Comparable<ComplexFormula> {

    /**
     * todo
     */
    private static final int COMPLEMENTARY_CONJ_FORMULAS_NUMBER = 4;
    /**
     * Logic operator of this formula
     */
    private LogicOperator operator;
    /**
     * left part of this complex formula, which is a SimpleFormula where it's trait and state
     * are the same as the first trait and state of this complex formula
     */
    private SimpleFormula leftPart;
    /**
     * right part of this complex formula, which is a SimpleFormula where it's trait and state
     * are the same as the second trait and state of this complex formula
     */
    private SimpleFormula rightPart;
    /**
     * List of traits of the formula
     */
    private List<Trait> traits;
    /**
     * List of states of this formula, where first one is a state of first traits and second is
     * a state of the second trait
     */
    private List<State> states;
    /**
     * Subject of the formula in form of individual model
     */
    private IndividualModel individualModel;

    /**
     * Constructor of Complex formula, for now there is made an assumption that ComplexFormula consists of two SimpleFormulas
     * that are connected by either AND or OR operator and that share the same Object
     *
     * @param model     Object which is considered in a formula
     * @param traits    list of two traits, respectively for the left part and for the right part of the formula
     * @param statesSeq states for the two simple formulas, in same order as traits
     * @param op        Operator.Type which is used to connect two SimpleFormulas
     * @throws InvalidFormulaException when the sentence is being build improperly
     */
    public ComplexFormula(IndividualModel model, List<Trait> traits, List<State> statesSeq, LogicOperator op) throws InvalidFormulaException {
        if (model == null || traits == null || statesSeq == null || op == null)
            throw new NullPointerException("Cannot create formula - one or more parameteres are null");
        if (traits.size() != 2 || statesSeq.size() != 2 )
            throw new InvalidFormulaException("Either size of traits or states is not 2 or operator is not valid");
        this.individualModel = model;
        this.traits = traits;
        if (!checkTraits())
            throw new InvalidFormulaException("Cannot create formula - given traits don't describe type of the object");
        leftPart = new SimpleFormula(model, traits.subList(0, 1), statesSeq.subList(0, 1));
        rightPart = new SimpleFormula(model, traits.subList(1, 2), statesSeq.subList(1, 2));
        this.operator = op;
        this.states = statesSeq;

    }

    /**
     * Constructor just the same as above, only with the assumption that all states equal IS.
     * This constructor builds formula in standard version.
     *
     * @param model  Object which is considered in a formula
     * @param traits list of two traits, respectively for the left part and for the right part of the formula
     * @param op     Operator.Type which is used to connect two SimpleFormulas
     * @throws InvalidFormulaException when the sentence is being build improperly
     */
    public ComplexFormula(IndividualModel model, List<Trait> traits, LogicOperator op) throws InvalidFormulaException {
        this(model, traits, Arrays.asList(State.IS, State.IS), op);
    }

    /**
     * @return left part of this complex formula, which is a SimpleFormula where it's trait and state
     * are the same as the first trait and state of this complex formula
     */
    public SimpleFormula getLeftPart() {
        return leftPart;
    }
    /**
     * @return right part of this complex formula, which is a SimpleFormula where it's trait and state
     * are the same as the second trait and state of this complex formula
     */
    public SimpleFormula getRightPart() {
        return rightPart;
    }
    /**
     * Returns the information that the formula is modal conjunction
     *
     * @return the type of formula
     */
    public Formula.Type getType() {
        switch (operator) {
            case AND:
                return Type.MODAL_CONJUNCTION;
            case OR:
                return Type.MODAL_DISJUNCTION;
            case XOR:
                return Type.MODAL_EXCLUSIVE_DISJUNCTION;
            default:
                return null;
        }
    }
    /**
     * @return operator's type of the formula
     */
    public LogicOperator getOperator() {
        return operator;
    }

    /**
     * @return list of Traits of the formula, in order from left to right
     */
    public List<Trait> getTraits() {
        return traits;
    }

    /**
     * @return Object which is considered in formula
     */
    @Override
    public IndividualModel getModel() {
        return individualModel;
    }

    /**
     * @return list of States of traits in the formula, in order from left to right
     */
    @Override
    public List<State> getStates() {
        return states;
    }

    /**
     * Produces set of formulas which are complementary to this one. Amount and form of this formulas depends on
     * formula type. For convenience of use, given formula is also included in resulted collection.
     * For standard formula, produces list of formulas in following order:
     * IS, IS
     * IS_NOT, IS
     * IS, IS_NOT
     * IS_NOT, IS_NOT
     *
     * @return      list of complementary formulas
     */
    @Override
    public List<Formula> getComplementaryFormulas() throws InvalidFormulaException {
        List<Formula> res = new ArrayList<>();

        State[][] newStates = new State[][]{
                {states.get(0), states.get(1)},
                {states.get(0).not(), states.get(1)},
                {states.get(0), states.get(1).not()},
                {states.get(0).not(), states.get(1).not()}
        };
        for (int i = 0; i < COMPLEMENTARY_CONJ_FORMULAS_NUMBER; i++)
            res.add(new ComplexFormula(individualModel, traits, Arrays.asList(newStates[i]), operator));
        return res;
    }

    /**
     * Allows for comparing this complex formula with another formula and checking if they are the same
     * without regarding order of traits
     * @param other     second formula
     * @return          true if formulas are same, false otherwise
     */
    public boolean equals(Formula other) {
        if (other instanceof ComplexFormula)
            if (individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if (compareTraitsAndStates((ComplexFormula) other))
                    if (operator.equals(((ComplexFormula) other).getOperator()))
                        return true;
        return false;
    }

    /**
     * Comapre states and traits of this formula and another without regarding order of traits
     * @param formula   second formula
     * @return          true if traits and states are same in both formulas, false otherwise
     */
    private boolean compareTraitsAndStates(ComplexFormula formula) {
        if (traits.get(0).equals(formula.getTraits().get(0)) && states.get(0).equals(formula.getStates().get(0)))
            return traits.get(1).equals(formula.getTraits().get(1)) && states.get(1).equals(formula.getStates().get(1));
        else if (traits.get(0).equals(formula.getTraits().get(1)) && states.get(0).equals(formula.getStates().get(1)))
            return traits.get(1).equals(formula.getTraits().get(0)) && states.get(1).equals(formula.getStates().get(0));
        return false;
    }

    /**
     * Checks if given formula regards the same object and same traits without checking their states
     *
     * @param other     Formula with which we compare this one
     * @return          true if they are similar or false otherwise
     */
    public boolean isFormulaSimilar(Formula other) {
        if (other instanceof ComplexFormula)
            if (individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if (compareTraits((ComplexFormula) other))
                    if (operator.equals(((ComplexFormula) other).getOperator()))
                        return true;
        return false;
    }

    /**
     * Compares traits of two formulas
     * @param formula   complex formula with which traits we compare this formula traits
     * @return          true if this formula traits are same with the other one's, false otherwise
     */
    private boolean compareTraits(ComplexFormula formula) {
        return traits.get(0).equals(formula.getTraits().get(0)) && traits.get(1).equals(formula.getTraits().get(1));
    }

    /**
     * @return      false if traits of formula are same or one of them does not describe the individual model
     */
    private boolean checkTraits() {
        return !traits.get(0).equals(traits.get(1)) && individualModel.checkIfContainsTraits(traits);
    }

    /**
     * Compares this formula with another object
     * @param o     object to compare
     * @return      true if the o was an identical complex formula, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexFormula that = (ComplexFormula) o;

        return getOperator() == that.getOperator() &&
                getLeftPart().equals(that.getLeftPart()) &&
                getRightPart().equals(that.getRightPart());
    }

    /**
     * @return hashcode of this complex formula
     */
    @Override
    public int hashCode() {
        int result = getOperator().hashCode();
        result = 31 * result + getLeftPart().hashCode();
        result = 31 * result + getRightPart().hashCode();
        return result;
    }

    /**
     * @return  copy of the complex formula
     * @throws InvalidFormulaException when the copy could not be created
     */
    public ComplexFormula copy() throws InvalidFormulaException {
        return new ComplexFormula(individualModel, traits, states, operator);
    }

    /**
     * Method returns standard version of this formula. Standard formula is known as formula without any negations.
     *
     * @return new formula which is standard formula.
     */
    @Override
    public Formula getStandardFormula() throws InvalidFormulaException {
        if (!getStates().contains(State.IS_NOT) && !getStates().contains(State.MAYHAPS))
            return this;
        else return new ComplexFormula(individualModel, traits, operator);
    }

    /**
     * @return String representation of the formula
     */
    @Override
    public String toString() {
        return "ComplexFormula{" + individualModel.getIdentifier() + ": " +
                states.get(0).name() + " " + traits.get(0) + " " + operator + " " +
                states.get(1).name() + " " + traits.get(1) +
                '}';
    }

    /**
     * Compares hashcode of this complex formula with the other one
     * @param o     second formula
     * @return      1 if hashcode of the first was bigger then second's, 0 if they
     *              were the same, and -1 if it was smaller
     */
    @Override
    public int compareTo(ComplexFormula o) {
        int val1 = states.hashCode() + traits.hashCode() + individualModel.hashCode();
        int val2 = o.states.hashCode() + o.traits.hashCode() + o.individualModel.hashCode();
        return val1 > val2 ? 1 : (val1 < val2 ? -1 : 0);
    }


    /**
     * Method is used to point out exact formulas which should be used in grounding process for formulas processed
     * in an indirect way. This situation takes place for disjunctions which use conjunctions.
     * Such formulas can be used for building grounding set or retrieving summarization.
     * In case of disjunction grounding sets, these are composed of more than one conjunctive grounding set.
     *
     * @return Array of bind formulas used in exact grounding.
     */
    @Override
    public List<Formula> getDependentFormulas() {

        if (getType().equals(Type.MODAL_CONJUNCTION))
            return Arrays.asList(new Formula[]{this});

        List<Formula> res;
        try {
            res = new ComplexFormula(individualModel, traits, states, LogicOperator.AND)
                    .getComplementaryFormulas().subList(0, 3);
        } catch (InvalidFormulaException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "The formula was not built correctly.", e);
            return null;
        }
        if (getType().equals(Type.MODAL_DISJUNCTION))
            return res;
        if (getType().equals(Type.MODAL_EXCLUSIVE_DISJUNCTION)) {
            res.remove(0);
            return res;
        }
        return null;
    }

}