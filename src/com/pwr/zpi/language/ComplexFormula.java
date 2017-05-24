package com.pwr.zpi.language;

import com.pwr.zpi.semantic.IndividualModel;
import com.pwr.zpi.exceptions.InvalidFormulaException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.pwr.zpi.holons.NonBinaryHolon.FormulaCase;

/**
 *
 */
public class ComplexFormula extends Formula implements Comparable<ComplexFormula> {

    private static final int COMPLEMENTARY_FORMULAS_NUMBER = 4;
    private LogicOperator operator;
    private SimpleFormula leftPart, rightPart;
    private List<Trait> traits;
    private List<State> states;
    private IndividualModel individualModel;
    private FormulaCase formulaCase;

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
            throw new NullPointerException("One or more parameteres are null");
        if (traits.size() != 2 || statesSeq.size() != 2 || (op != LogicOperator.AND && op != LogicOperator.OR))
            throw new InvalidFormulaException("Either size of traits or states is not 2 or operator is not valid");
        this.individualModel = model;
        this.traits = traits;
        if (!checkTraits())
            throw new InvalidFormulaException("Given traits don't describe type of the object");
        leftPart = new SimpleFormula(model, traits.subList(0, 1), statesSeq.subList(0, 1));
        rightPart = new SimpleFormula(model, traits.subList(1, 2), statesSeq.subList(1, 2));
        if (leftPart.isNegated()) {
            if (rightPart.isNegated()) {
                formulaCase = FormulaCase.NPNQ;
            } else {
                formulaCase = FormulaCase.NPQ;
            }
        } else {
            if (rightPart.isNegated()) {
                formulaCase = FormulaCase.PNQ;
            } else {
                formulaCase = FormulaCase.PQ;
            }
        }
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
     * @return Collection of SimpleFormulas, in order of left part and the right part of formula
     */
    public Collection<Formula> getParts() {
        return Arrays.asList(leftPart, rightPart);
    }

    /**
     * Returns the information that the formula is modal conjunction
     *
     * @return the type of formula
     */
    public Formula.Type getType() {
        return Type.MODAL_CONJUNCTION;
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
     *
     * @return
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
        for (int i = 0; i < COMPLEMENTARY_FORMULAS_NUMBER; i++)
            res.add(new ComplexFormula(individualModel, traits, Arrays.asList(newStates[i]), operator));
        return res;
    }





    /**
     * evaluates the formula, for now only for the AND and OR operators
     *
     * @return State of the whole formula
     */

    public boolean equals(Formula other) {
        if (other instanceof ComplexFormula)
            if (individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if (compareTraitsAndStates((ComplexFormula) other))
                    if (operator.equals(((ComplexFormula) other).getOperator()))
                        return true;
        return false;
    }

    private boolean compareTraitsAndStates(ComplexFormula formula) {
        if (traits.get(0).equals(formula.getTraits().get(0)) && states.get(0).equals(formula.getStates().get(0)))
            if (traits.get(1).equals(formula.getTraits().get(1)) && states.get(1).equals(formula.getStates().get(1)))
                return true;
            else return false;
        else if (traits.get(0).equals(formula.getTraits().get(1)) && states.get(0).equals(formula.getStates().get(1)))
            if (traits.get(1).equals(formula.getTraits().get(0)) && states.get(1).equals(formula.getStates().get(0)))
                return true;
        return false;
    }

    /**
     * Checks if given formula regards the same object and same traits
     * @param other
     * @return
     */
    public boolean isFormulaSimilar(Formula other)
    {
        if (other instanceof ComplexFormula)
            if (individualModel.getIdentifier().equals(other.getModel().getIdentifier()))
                if (compareTraits((ComplexFormula) other))
                    if (operator.equals(((ComplexFormula) other).getOperator()))
                        return true;
        return false;
    }

    private boolean compareTraits(ComplexFormula formula) {
        if (traits.get(0).equals(formula.getTraits().get(0)))
            if (traits.get(1).equals(formula.getTraits().get(1)))
                return true;
            else return false;
      /*  else if (traits.get(0).equals(formula.getTraits().get(1)))  // na przyszłość być może
            if (traits.get(1).equals(formula.getTraits().get(0)))
                return true;*/
        return false;
    }

    public SimpleFormula getLeftPart() {
        return leftPart;
    }

    public SimpleFormula getRightPart() {
        return rightPart;
    }

    private boolean checkTraits() {
        boolean response;
        if (traits.get(0).equals(traits.get(1))) response = false;
        else response = individualModel.checkIfContainsTraits(traits);
        return response;
    }

    public FormulaCase getFormulaCase() {
        return formulaCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexFormula that = (ComplexFormula) o;

        if (getOperator() != that.getOperator()) return false;
        if (!getLeftPart().equals(that.getLeftPart())) return false;
        return getRightPart().equals(that.getRightPart());
    }

    @Override
    public int hashCode() {
        int result = getOperator().hashCode();
        result = 31 * result + getLeftPart().hashCode();
        result = 31 * result + getRightPart().hashCode();
        return result;
    }
    public void setpq(){
        if(leftPart.isNegated())leftPart.negate();
        if(rightPart.isNegated())rightPart.negate();
    }
    public void setnpq(){
        if(!leftPart.isNegated())leftPart.negate();
        if(rightPart.isNegated())rightPart.negate();
    }
    public void setpnq(){
        if(leftPart.isNegated())leftPart.negate();
        if(!rightPart.isNegated())rightPart.negate();
    }
    public void setnpnq(){
        if(!leftPart.isNegated())leftPart.negate();
        if(!rightPart.isNegated())rightPart.negate();
    }
    public ComplexFormula copy() throws InvalidFormulaException{
        return new ComplexFormula(individualModel,traits,states,operator);
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

    @Override
    public String toString() {
        return "ComplexFormula{" + individualModel.getIdentifier()+": "+
                states.get(0).name()+" "+traits.get(0) + " "+operator+" "+
                states.get(1).name()+" "+traits.get(1) +
                '}';
    }


    @Override
    public int compareTo(ComplexFormula o) {
        int val1 = states.hashCode() + traits.hashCode() + individualModel.hashCode();
        int val2 = o.states.hashCode() + o.traits.hashCode() + o.individualModel.hashCode();
        return val1 > val2 ? 1 : ( val1 < val2 ? -1 : 0);
    }
}