package com.pwr.zpi.language;

import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;

import java.util.*;

/**
 *
 */
public class ComplexFormula extends Formula {

    /**
     * It is a reference to the root of the complex formula
     */
    private SubFormula subFormula;
    private Operators operator;

    /**
     * Constructor of ComplexFormula for creating sentence with two operannds and an operator
     * @param po1 SimpleFormula or ComplexFormula which is left side of sentence
     * @param operator Operator.Type which connects both Subformulas
     * @param po2 SimpleFormula or ComplexFormula which is right side of sentence
     * @throws InvalidSentenceFormulaException
     */
    public ComplexFormula(Formula po1, Operators.Type operator, Formula po2)  throws InvalidSentenceFormulaException
    {
        if(operator!=Operators.Type.NOT) {
            if(po1 instanceof ComplexFormula)
                po1 = ((ComplexFormula) po1).subFormula;
            if(po2 instanceof ComplexFormula)
                po2 = ((ComplexFormula) po2).subFormula;
            subFormula = new SubFormula(po1, operator, po2);
        }else throw new InvalidSentenceFormulaException();
    }

    /**
     * Constructor of sentence adding operator NOT
     * @param operator must be an operator NOT
     * @param po1 formula that is supposed to be negated
     * @throws InvalidSentenceFormulaException
     */
    public ComplexFormula(Operators.Type operator, Formula po1) throws InvalidSentenceFormulaException
    {
        if(operator==Operators.Type.NOT)
        {
            if(po1 instanceof ComplexFormula)
                po1 = ((ComplexFormula) po1).subFormula;
            subFormula = new SubFormula(po1, operator, null);
        } else throw new InvalidSentenceFormulaException();
    }

    /**
     * Not vialable, but created anyway
     * Constructor creating sentence consisting of only SimpleFormula
     * @param po1 SimpleFormula which is supposed to be in sentence
     * @throws InvalidSentenceFormulaException
     */
    public ComplexFormula(SimpleFormula po1) throws InvalidSentenceFormulaException
    {
        subFormula = new SubFormula(po1, null, null);
    }

    public ComplexFormula(Object o, Set<Trait> traits, List<State> statesSeq, Operators.Type op) {
        /*build formula like: state1(trait1(o)) op state2(trait2(o))*/ //todo
    }

    /**
     * method adding another part of sentence
     * @param operator Operator.Type which connects two sides of the sentence, cannot be NOT
     * @param formula Formula which is added to sentence
     * @return true if succeeded or false if not
     * @throws InvalidSentenceFormulaException when sentence couldn't be created
     */
    public boolean addFormula(Operators.Type operator, Formula formula) throws InvalidSentenceFormulaException
    {
        try
        {
            if(formula instanceof ComplexFormula)
                formula = ((ComplexFormula) formula).subFormula;
            subFormula = new SubFormula(subFormula, operator, formula); return true;
        } catch (InvalidSentenceFormulaException e) {return false;}
    }

    /**
     * method negating the complexFormula
     * @return true if succeeded or false if not
     */
    public boolean negateFormula()
    {
        try
        {
            subFormula = new SubFormula(subFormula, Operators.Type.NOT, null);
            return true;
        } catch (InvalidSentenceFormulaException e) {return false;}
    }

    /**
     * method printing the whole complexFormula
     */
    public void printFormula()
    {
        subFormula.printSubFormula(subFormula);
    }

    /**
     * Method return evaluation of ComplexFormula if such is possible
     * @return State of evalueted ComplexFormula
     */
    public State evaluate()
    {
        try {
            Stack<State> value = evaluate(new Stack<State>(), subFormula);
            System.out.println("\nIle: " + value.size());
            if (value.size() == 1)
                return value.pop();
        } catch (InvalidSentenceFormulaException isf) {}
        return null;
    }

    /**
     * Method using reverse Polish notation to evaluate value of complex formula
     * @param values Stack which is used to store temporary values of states
     * @param formula Formula which is being evaluated
     * @return Stack of current values
     * @throws InvalidSentenceFormulaException when ComplexFormula was not created properly and cannot be evaluated
     */
    private Stack<State> evaluate(Stack<State> values, Formula formula) throws InvalidSentenceFormulaException
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

    public Operators getOperator() {
        return operator;
    }
}