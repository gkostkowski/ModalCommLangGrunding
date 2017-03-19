package com.pwr.zpi.language;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public interface Operator<O> {
    O execute();

    enum Type {
        UNARY, BINARY
    }
}
