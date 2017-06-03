package com.pwr.zpi.exceptions;


public class InvalidMeasureImplementation extends Throwable {
    private static final String MSG = "Normalised distance returned value from allowed range.";

    /**
     * Constructs a new throwable with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     * <p>
     * <p>The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created throwable.
     *
     */
    public InvalidMeasureImplementation() {
        super(MSG);
    }
}
