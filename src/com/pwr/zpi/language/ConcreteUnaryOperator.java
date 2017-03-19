package com.pwr.zpi.language;

/**
 * Created by Grzesiek on 2017-03-19.
 */

/**
 * Special kind of language symbol, which is an unary operator.
 */
public class ConcreteUnaryOperator<I, O> extends Symbol implements UnaryOperator<I, O> {
    Operator<O> operator;

    @Override
    public O execute() {
        return null;
    }

    @Override
    public void setOperands(I operand) {

    }
}
