package com.pwr.zpi.language;

import com.pwr.zpi.State;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public class ComplexFormula implements Formula {
    SimpleFormula operand1, operand2;
    Operations operation;
    State isNegated;

    public ComplexFormula(SimpleFormula operand1, SimpleFormula operand2, Operations operation, State isNegated) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operation = operation;
        this.isNegated = isNegated;
    }

    @Override
    public State evaluate() {
        switch (operation) {
            case AND:
                break; //todo ...
        }
    }


}
