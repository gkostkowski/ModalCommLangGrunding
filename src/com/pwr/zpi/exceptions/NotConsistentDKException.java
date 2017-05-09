package com.pwr.zpi.exceptions;

public class NotConsistentDKException extends Exception {


    private static final String DEF_MESSAGE = "Distributed knowledge is not consistent.";

    public NotConsistentDKException()
    {
        super(DEF_MESSAGE);
    }

    public NotConsistentDKException(String message)
    {
        super(message);
    }

}
