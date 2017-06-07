/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.exceptions;

/**
 * It's used in situation when some values are passed to method and tried to use in a way which is not appropriate
 * for that kind of values.
 */
public class NotApplicableException extends Exception {
    public NotApplicableException() {super();}

    public NotApplicableException(String message) {super(message);}
}
