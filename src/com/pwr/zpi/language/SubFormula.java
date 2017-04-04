package com.pwr.zpi.language;
import com.pwr.zpi.Object;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;

import java.util.Collection;
import java.util.Set;
import java.util.Stack;


/**
 * Class representing parts of complex formula in a binary tree
 */
public class SubFormula extends Formula {

    private Formula formula1 = null;
    private Formula formula2 = null;
    private Operators.Type operator = null;

    /**
     * getter to left part of subformula
     * @return left part of formula, might be another Subformula or SimpleFormula
     */
    public Formula getFormula1() {  return formula1; }

    /**
     * getter to right part of subformula
     * @return right part of formula, might be another Subformula or SimpleFormula
     */
    public Formula getFormula2() {  return formula2; }

    /**
     * getter to operator of subformula
     * @return Operator connecting formulas
     */
    public Operators.Type getOperator() {   return operator;  }

    /**
     * Constructor of SubFormula, if the operator equals NOT, the po2 must be null,
     * otherwise both paramteres must not be null
     * @param po1 left formula, might be a SimpleFormula or antoher subformula
     * @param operator Operator.Type connceting parts of this subformula
     * @param po2 right formula, might be a SimpleFormula or antoher subformula
     * @throws InvalidSentenceFormulaException when there is an attempt to create logicaly wrong sentence
     */
    public SubFormula(Formula po1, Operators.Type operator, Formula po2) throws InvalidSentenceFormulaException
    {
        if(operator != Operators.Type.NOT && po2 == null)
            throw new InvalidSentenceFormulaException();
        if(operator == Operators.Type.NOT && po2 != null)
            throw new InvalidSentenceFormulaException();
        formula1 = po1;
        this.operator = operator;
        formula2 = po2;
    }

    /**
     * method that prints formula in a natural way for human being
     * @param formula SimpleFormula or SubFormula
     */
    public void printSubFormula(Formula formula)
    {
        if(formula == null)
            return;
        if(formula instanceof SimpleFormula)
            System.out.print((formula).evaluate());
        if(formula instanceof  SubFormula)
        {
            System.out.print("(");
            printSubFormula(((SubFormula) formula).formula1);
            System.out.print(operator + " ");
            printSubFormula(((SubFormula) formula).formula2);
            System.out.print(")");
        }
    }

    @Override
    public Set<Trait> getTraits() {
        return null;
    }

    @Override
    public Object getObject() {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    /**
     * implemented because there is not another way
     * @return
     */
    @Override
    State evaluate() {
        return null;
    }
}
