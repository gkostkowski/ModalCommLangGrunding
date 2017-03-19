package com.pwr.zpi.language;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public abstract class ConcreteBinaryOperator<I, O> extends Symbol implements BinaryOperator<I, O> {
    I operand1, operand2;
    O result;

    ConcreteBinaryOperator(I operand1, I operand2, O output) {
        super();
        setOperands(operand1, operand2);
    }

    @Override
    public abstract O execute();

    @Override
    public void setOperands(I operand1, I operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }
}
