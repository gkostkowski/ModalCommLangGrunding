package com.pwr.zpi.language;

import com.pwr.zpi.Observation;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ComplexFormula extends Formula {

    private Operators.Type operator;
    private SimpleFormula leftPart, rightPart;
    private List<Trait> traits;
    private List<State> states;
    private Observation observation;


    /**
     * Constructor of Complex formula, for now there is made an assumption that ComplexFormula consists of two SimpleFormulas
     * that are connected by either AND or OR operator and that share the same Object
     * @param o Object which is considered in a formula
     * @param traits list of two traits, respectively for the left part and for the right part of the formula
     * @param statesSeq states for the two simple formulas, in same order as traits
     * @param op Operator.Type which is used to connect two SimpleFormulas
     * @throws InvalidSentenceFormulaException when the sentence is being build improperly
     */
    public ComplexFormula(Observation o, List<Trait> traits, List<State> statesSeq, Operators.Type op) throws InvalidSentenceFormulaException {
        if(traits.size() != 2 || traits.size() != statesSeq.size() || op != Operators.Type.AND || op != Operators.Type.OR)
            throw new InvalidSentenceFormulaException();
        leftPart = new SimpleFormula(o, traits.subList(0,1), statesSeq.subList(0, 1));
        rightPart = new SimpleFormula(o, traits.subList(1,2), statesSeq.subList(1, 2));
        this.operator = op;
        this.traits = traits;
        this.states = statesSeq;
        this.observation = o;
    }

    /**
     * Constructor just the same as above, only with the assumption that all states equal IS
     * @param o Object which is considered in a formula
     * @param traits list of two traits, respectively for the left part and for the right part of the formula
     * @param op Operator.Type which is used to connect two SimpleFormulas
     * @throws InvalidSentenceFormulaException when the sentence is being build improperly
     */
    public ComplexFormula(Observation o, List<Trait> traits, Operators.Type op) throws InvalidSentenceFormulaException
    {
        if(traits.size() != 2 || op != Operators.Type.AND || op != Operators.Type.OR)
            throw new InvalidSentenceFormulaException();
        leftPart = new SimpleFormula(o, traits.subList(0,1), Arrays.asList(State.IS));
        rightPart = new SimpleFormula(o, traits.subList(1,2), Arrays.asList(State.IS));
        this.operator = op;
        this.traits = traits;
        this.states = Arrays.asList(State.IS, State.IS);
        this.observation = o;
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
    public List<Trait> getTraits()
    {
        return traits;
    }

    /**
     *
     * @return Object which is considered in formula
     */
    @Override
    public Observation getObservation() {
        return observation;
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
    public State evaluate()
    {
        if(operator == Operators.Type.AND)
            return Operators.XandY(leftPart, rightPart);
        else if(operator == Operators.Type.OR)
            return Operators.XorY(leftPart, rightPart);

        return null;
    }

    public boolean equals(Formula other)
    {
        if(other instanceof ComplexFormula)
            if(observation.getIdentifier().equals(other.getObservation().getIdentifier()))
                if(compareTraits(other.getTraits()))
                    if(operator.equals(((ComplexFormula) other).getOperator()))
                        return true;
        return false;
    }

    private boolean compareTraits(List<Trait> otherTraits)
    {
        if(traits.get(0).equals(otherTraits.get(0)))
            if(traits.get(1).equals(otherTraits.get(1)))
                return true;
            else return false;
        else if(traits.get(0).equals(otherTraits.get(1)))
                if(traits.get(1).equals(otherTraits.get(0)))
                    return true;
        return false;
    }























    /**
     * It is a reference to the root of the complex formula
     */
   // private SubFormula subFormula;

    /**
     * Constructor of ComplexFormula for creating sentence with two operannds and an operator
     * @param po1 SimpleFormula or ComplexFormula which is left side of sentence
     * @param operator Operator.Type which connects both Subformulas
     * @param po2 SimpleFormula or ComplexFormula which is right side of sentence
     * @throws InvalidSentenceFormulaException
     */
    /*public ComplexFormula(Formula po1, Operators.Type operator, Formula po2)  throws InvalidSentenceFormulaException
    {
        if(operator!=Operators.Type.NOT) {
            if(po1 instanceof ComplexFormula)
                po1 = ((ComplexFormula) po1).subFormula;
            if(po2 instanceof ComplexFormula)
                po2 = ((ComplexFormula) po2).subFormula;
            subFormula = new SubFormula(po1, operator, po2);
        }else throw new InvalidSentenceFormulaException();
    }*/

    /**
     * Constructor of sentence adding operator NOT
     * @param operator must be an operator NOT
     * @param po1 formula that is supposed to be negated
     * @throws InvalidSentenceFormulaException
     */
   /* public ComplexFormula(Operators.Type operator, Formula po1) throws InvalidSentenceFormulaException
    {
        if(operator==Operators.Type.NOT)
        {
            if(po1 instanceof ComplexFormula)
                po1 = ((ComplexFormula) po1).subFormula;
            subFormula = new SubFormula(po1, operator, null);
        } else throw new InvalidSentenceFormulaException();
    }*/

    /**
     * Not vialable, but created anyway
     * Constructor creating sentence consisting of only SimpleFormula
     * @param po1 SimpleFormula which is supposed to be in sentence
     * @throws InvalidSentenceFormulaException
     */
    /*public ComplexFormula(SimpleFormula po1) throws InvalidSentenceFormulaException
    {
        subFormula = new SubFormula(po1, null, null);
    }
*/

    /**
     * method adding another part of sentence
     * @param operator Operator.Type which connects two sides of the sentence, cannot be NOT
     * @param formula Formula which is added to sentence
     * @return true if succeeded or false if not
     * @throws InvalidSentenceFormulaException when sentence couldn't be created
     */
   /* public boolean addFormula(Operators.Type operator, Formula formula) throws InvalidSentenceFormulaException
    {
        try
        {
            if(formula instanceof ComplexFormula)
                formula = ((ComplexFormula) formula).subFormula;
            subFormula = new SubFormula(subFormula, operator, formula); return true;
        } catch (InvalidSentenceFormulaException e) {return false;}
    }*/

    /**
     * method negating the complexFormula
     * @return true if succeeded or false if not
     */
    /*public boolean negateFormula()
    {
        try
        {
            subFormula = new SubFormula(subFormula, Operators.Type.NOT, null);
            return true;
        } catch (InvalidSentenceFormulaException e) {return false;}
    }*/

    /**
     * method printing the whole complexFormula
     */
  /*  public void printFormula()
    {
        subFormula.printSubFormula(subFormula);
    }*/


    /**
     * Method return evaluation of ComplexFormula if such is possible
     * @return State of evalueted ComplexFormula
     */
/*    public State evaluate()
    {
        try {
            Stack<State> value = evaluate(new Stack<State>(), subFormula);
            System.out.println("\nIle: " + value.size());
            if (value.size() == 1)
                return value.pop();
        } catch (InvalidSentenceFormulaException isf) {}
        return null;
    }
*/
    /**
     * Method using reverse Polish notation to evaluate value of complex formula
     * @param values Stack which is used to store temporary values of states
     * @param formula Formula which is being evaluated
     * @return Stack of current values
     * @throws InvalidSentenceFormulaException when ComplexFormula was not created properly and cannot be evaluated
     */
/*    private Stack<State> evaluate(Stack<State> values, Formula formula) throws InvalidSentenceFormulaException
    {
        if(formula == null)
            return values;
        if(formula instanceof SimpleFormula)
            values.push(formula.evaluate());
        if(formula instanceof SubFormula) {
            evaluate(values, ((SubFormula) formula).getFormula1());
            evaluate(values, ((SubFormula) formula).getFormula2());
            if (((SubFormula) formula).getOperator() == Operators.Type.NOT && values.size() > 0)
                values.push(values.pop().not());
            else if (values.size() > 1 && ((SubFormula) formula).getOperator() != Operators.Type.NOT)
                switch (((SubFormula) formula).getOperator()) {
                    case AND:
                        values.push(Operators.XandY(values.pop(), values.pop()));
                        break;
                    case OR:
                        values.push(Operators.XorY(values.pop(), values.pop()));
                        break;
                    case XOR:
                        values.push(Operators.XxorY(values.pop(), values.pop()));
                        break;
                }
            else throw new InvalidSentenceFormulaException();
        }
        return values;
    }

    public Collection<Formula> getParts() {
        return Arrays.asList(subFormula.getFormula1(), subFormula.getFormula2());
    }
    */

}