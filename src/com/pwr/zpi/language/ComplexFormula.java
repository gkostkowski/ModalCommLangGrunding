package com.pwr.zpi.language;

import com.pwr.zpi.IndividualModel;
import com.pwr.zpi.State;
import com.pwr.zpi.TraitSignature;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ComplexFormula extends Formula {

    private Operators.Type operator;
    private SimpleFormula leftPart, rightPart;
    private List<TraitSignature> traits;
    private List<State> states;
    private IndividualModel individualModel;


    /**
     * Constructor of Complex formula, for now there is made an assumption that ComplexFormula consists of two SimpleFormulas
     * that are connected by either AND or OR operator and that share the same Object
     * @param model Object which is considered in a formula
     * @param traits list of two traits, respectively for the left part and for the right part of the formula
     * @param statesSeq states for the two simple formulas, in same order as traits
     * @param op Operator.Type which is used to connect two SimpleFormulas
     * @throws InvalidFormulaException when the sentence is being build improperly
     */
    public ComplexFormula(IndividualModel model, List<TraitSignature> traits, List<State> statesSeq, Operators.Type op) throws InvalidFormulaException {
        if(model==null || traits == null || statesSeq == null || op == null)
            throw new NullPointerException("One or more parameteres are null");
        if(traits.size() != 2 || statesSeq.size() != 2 || (op != Operators.Type.AND && op != Operators.Type.OR))
            throw new InvalidFormulaException("Either size of traits or states is not 2 or operator is not valid");
        this.individualModel = model;
        this.traits = traits;
        if(!checkTraits())
            throw new InvalidFormulaException("Given traits don't describe type of the object");
        leftPart = new SimpleFormula(model, traits.subList(0,1), statesSeq.subList(0, 1));
        rightPart = new SimpleFormula(model, traits.subList(1,2), statesSeq.subList(1, 2));
        this.operator = op;
        this.states = statesSeq;

    }

    /**
     * Constructor just the same as above, only with the assumption that all states equal IS
     * @param model Object which is considered in a formula
     * @param traits list of two traits, respectively for the left part and for the right part of the formula
     * @param op Operator.Type which is used to connect two SimpleFormulas
     * @throws InvalidFormulaException when the sentence is being build improperly
     */
    public ComplexFormula(IndividualModel model, List<TraitSignature> traits, Operators.Type op) throws InvalidFormulaException
    {
        this(model, traits, Arrays.asList(State.IS, State.IS), op);
    }

    /**
     *
     * @return Collection of SimpleFormulas, in order of left part and the right part of formula
     */
    public Collection<Formula> getParts() {
        return Arrays.asList(leftPart, rightPart);
    }

    /**
     * Returns the information that the formula is modal conjunction
     * @return the type of formula
     */
    public Formula.Type getType()
    {
        return Type.MODAL_CONJUNCTION;
    }

    /**
     *
     * @return operator's type of the formula
     */
    public Operators.Type getOperator()
    {
        return operator;
    }

    /**
     *
     * @return list of Traits of the formula, in order from left to right
     */
    public List<TraitSignature> getTraits()
    {
        return traits;
    }

    /**
     *
     * @return Object which is considered in formula
     */
    @Override
    public IndividualModel getModel() {
        return individualModel;
    }

    /**
     *
     * @return list of States of traits in the formula, in order from left to right
     */
    @Override
    public List<State> getStates()
    {
        return states;
    }

    /**
     * evaluates the formula, for now only for the AND and OR operators
     * @return State of the whole formula
     */

    public boolean equals(Formula other)
    {
        if(other instanceof ComplexFormula)
            if(individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if(compareTraits((ComplexFormula) other))
                    if(operator.equals(((ComplexFormula) other).getOperator()))
                        return true;
        return false;
    }

    private boolean compareTraits(ComplexFormula formula)
    {
        if(traits.get(0).equals(formula.getTraits().get(0))&&states.get(0).equals(formula.getStates().get(0)))
            if(traits.get(1).equals(formula.getTraits().get(1))&&states.get(1).equals(formula.getStates().get(1)))
                return true;
            else return false;
        else if(traits.get(0).equals(formula.getTraits().get(1))&&states.get(0).equals(formula.getStates().get(1)))
            if(traits.get(1).equals(formula.getTraits().get(0))&&states.get(1).equals(formula.getStates().get(0)))
                return true;
        return false;
    }

    public SimpleFormula getLeftPart(){
        return leftPart;
    }

    public SimpleFormula getRightPart(){
        return rightPart;
    }

    private boolean checkTraits()
    {
        boolean response;
        if(traits.get(0).equals(traits.get(1))) response = false;
        else response = individualModel.checkIfContainsTraits(traits);
        return response;
    }

}