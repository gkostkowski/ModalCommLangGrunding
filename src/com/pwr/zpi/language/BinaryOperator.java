package com.pwr.zpi.language;

import com.pwr.zpi.language.Operator;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public interface BinaryOperator <I, O> extends Operator<O> {
    abstract void setOperands(I operand1, I operand2);

    enum Type {
        AND, OR, XOR
    }
}
