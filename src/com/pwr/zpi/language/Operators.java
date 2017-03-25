package com.pwr.zpi.language;

import com.pwr.zpi.State;

/**
 * Describes language symbols ,which provide logica operations.
 */
public class Operators {

    enum Type {
        AND, OR, XOR,
        POS, BEL, KNOW
    }

    /**
     *
     * @param op1 evaluated State of first SimpleFormula or part of a ComplexFormula
     * @param op2 evaluated State of second SimpleFormula or part of a ComplexFormula
     * @return State which is a logical value of op1 ∨ op2
     */
    public static State XorY(State op1, State op2){
        return op1.or(op2);
    }

    /**
     *
     * @param op1 evaluated State of first SimpleFormula or part of a ComplexFormula
     * @param op2 evaluated State of second SimpleFormula or part of a ComplexFormula
     * @return State which is a logical value of op1 ∧ op2
     */
    public static State XandY(State op1, State op2){
        return op1.and(op2);
    }

    /**
     * Function returns exclusive disjunction of state op1 and op2 with the assumption that
     * if a value of at least one State equals Mayhaps, logical sense of the whole sentence also
     * equals Mayhaps
     * @param op1 evaluated State of first SimpleFormula or part of a ComplexFormula
     * @param op2 evaluated State of second SimpleFormula or part of a ComplexFormula
     * @return State which is a logical value of op1 ⊻ op2
     */
    public static State XxorY(State op1, State op2){
        State state = State.Is;
        if(op1 == State.Mayhaps || op2 == State.Mayhaps)
            state = State.Mayhaps;
        else if(op1 == op2)
            state = State.Is_Not;
        return state;
    }
    /**
     *
     * @param op1 SimpleFormula, first part of sentence
     * @param op2 SimpleFormula, second part of sentence
     * @return State which is a logical value of op1 ∨ op2
     */
    public static State XorY(SimpleFormula op1, SimpleFormula op2){
        return XorY(op1.evaluate(), op2.evaluate());
    }

    /**
     *
     * @param op1 SimpleFormula, first part of sentence
     * @param op2 SimpleFormula, second part of sentence
     * @return State which is a logical value of op1 ∧ op2
     */
    public static State XandY(SimpleFormula op1, SimpleFormula op2){
        return XandY(op1.evaluate(), op2.evaluate());
    }

    /**
     * Function returns exclusive disjunction of states op1 and op2 with the assumption that
     * if a value of at least one state equals Mayhaps, logical sense of the whole sentence also
     * equals Mayhaps
     * @param op1 SimpleFormula, first part of sentence
     * @param op2 SimpleFormula, second part of sentence
     * @return State which is a logical value of op1 ⊻ op2
     */
    public static State XxorY(SimpleFormula op1, SimpleFormula op2){
        State state = State.Is;
        State first = op1.evaluate();
        State second = op2.evaluate();
        if(first == State.Mayhaps || second == State.Mayhaps)
            state = State.Mayhaps;
        else if(first == second)
            state = State.Is_Not;
        return state;
    }


}
