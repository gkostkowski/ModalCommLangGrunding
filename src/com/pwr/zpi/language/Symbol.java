package com.pwr.zpi.language;

/**
 * Created by Grzesiek on 2017-03-19.
 */

/**
 * Represents language symbol. Some Symbols can represent operators.
 * @param <O>
 */
public class Symbol<O> {
    int id; //if required
    String value; //form of symbol
    String meaning;


    /*<I, O> Symbol(Operator.Type operatorType, I input, O output) {
        switch (operatorType) {
            case BINARY:
                operator = new ConcreteBinaryOperator<I, O>(input, output);
                break;
            case UNARY:
                break;

        }
    }*/
}
