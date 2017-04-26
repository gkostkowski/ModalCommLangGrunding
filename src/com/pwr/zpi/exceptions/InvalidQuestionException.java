package com.pwr.zpi.exceptions;

/**
 * Created by Weronika on 25.04.2017.
 */
public class InvalidQuestionException extends Exception {

    public static final int NO_OBJECT = 1;
    public static final int NO_FIRST_TRAIT = 2;
    public static final int NO_SECOND_TRAIT = 3;
    public static final int NO_FIRST_VALUE = 4;
    public static final int NO_SECOND_VALUE = 5;

    private int mistake;

    public InvalidQuestionException(int i)
    {
        super();
        mistake = i;
    }

    public InvalidQuestionException(String message, int i)
    {
        super(message);
        mistake = i;
    }

    public int getMistake() {
        return mistake;
    }

    public String getStringWithInfo()
    {
        switch (mistake)
        {
            case NO_OBJECT: return "No such object in memory, sorry";
            case NO_FIRST_TRAIT: return "No such trait as the first one in the object, sorry";
            case NO_FIRST_VALUE: return "No such value of the first trait in the object, sorry";
            case NO_SECOND_TRAIT: return "No such trait as the second one in the object, sorry";
            case NO_SECOND_VALUE: return "No such value of the second trait in the object, sorry";
            default: return null;
        }
    }
}
