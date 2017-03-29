package com.pwr.zpi.language;

/**
 *
 */
public interface UnaryOperator<I, O> extends Operator<O>{
    abstract void setOperands(I operand);

    enum Type {
        NOT, POS, BEL, KNOW
    }
}
