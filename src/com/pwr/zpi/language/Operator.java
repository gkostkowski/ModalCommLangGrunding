package com.pwr.zpi.language;

/**
 *
 */
public interface Operator<O> {
    O execute();

    enum Type {
        UNARY, BINARY
    }
}
