package com.pwr.zpi.language;

import com.pwr.zpi.State;

/**
 * Describes language symbols ,which provide logica operations.
 */
public class Operators {

    private static Type type;



    public enum Type {
        AND, OR, XOR, NOT,
        POS, BEL, KNOW
        public Type getType() {
            return type;
        }
    }

    /**
     *
     * @param op1 evaluated State of first SimpleFormula or part of a ComplexFormula
     * @param op2 evaluated State of second SimpleFormula or part of a ComplexFormula
     * @return State which is a logical value of op1 OR op2
     */
    public static State XorY(State op1, State op2){
        return op1.or(op2);
    }

    /**
     *
     * @param op1 evaluated State of first SimpleFormula or part of a ComplexFormula
     * @param op2 evaluated State of second SimpleFormula or part of a ComplexFormula
     * @return State which is a logical value of op1 AND op2
     */
    public static State XandY(State op1, State op2){
        return op1.and(op2);
    }

    /**
     * Function returns exclusive disjunction of state op1 and op2 with the assumption that
     * if a value of at least one State equals MAYHAPS, logical sense of the whole sentence also
     * equals MAYHAPS
     * @param op1 evaluated State of first SimpleFormula or part of a ComplexFormula
     * @param op2 evaluated State of second SimpleFormula or part of a ComplexFormula
     * @return State which is a logical value of op1 XOR op2
     */
    public static State XxorY(State op1, State op2){
        State state = State.IS;
        if(op1 == State.MAYHAPS || op2 == State.MAYHAPS)
            state = State.MAYHAPS;
        else if(op1 == op2)
            state = State.IS_NOT;
        return state;
    }
    /**
     *
     * @param op1 SimpleFormula, first part of sentence
     * @param op2 SimpleFormula, second part of sentence
     * @return State which is a logical value of op1 OR op2
     */
    public static State XorY(SimpleFormula op1, SimpleFormula op2){
        return XorY(op1.evaluate(), op2.evaluate());
    }

    /**
     *
     * @param op1 SimpleFormula, first part of sentence
     * @param op2 SimpleFormula, second part of sentence
     * @return State which is a logical value of op1 AND op2
     */
    public static State XandY(SimpleFormula op1, SimpleFormula op2){
        return XandY(op1.evaluate(), op2.evaluate());
    }

    /**
     * Function returns exclusive disjunction of states op1 and op2 with the assumption that
     * if a value of at least one state equals MAYHAPS, logical sense of the whole sentence also
     * equals MAYHAPS
     * @param op1 SimpleFormula, first part of sentence
     * @param op2 SimpleFormula, second part of sentence
     * @return State which is a logical value of op1 XOR op2
     */
    public static State XxorY(SimpleFormula op1, SimpleFormula op2){
        State state = State.IS;
        State first = op1.evaluate();
        State second = op2.evaluate();
        if(first == State.MAYHAPS || second == State.MAYHAPS)
            state = State.MAYHAPS;
        else if(first == second)
            state = State.IS_NOT;
        return state;
    }


}
