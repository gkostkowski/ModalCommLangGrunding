package com.pwr.zpi.language;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public interface UnaryOperator<I, O> extends Operator<O>{
    abstract void setOperands(I operand);

    enum Type {
        NOT, POS, BEL, KNOW
    }
}
