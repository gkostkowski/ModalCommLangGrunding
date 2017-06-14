package com.pwr.zpi.exceptions;

/**
 * Exception thrown when asked question has wrong structure
 */
public class InvalidQuestionException extends Exception {

    public static final int NO_OBJECT = 1;
    public static final int NO_FIRST_TRAIT = 2;
    public static final int NO_SECOND_TRAIT = 3;
    public static final int NO_OPERATOR = 4;
    public static final int NO_QUESTION = 5;
    public static final int WRONG_STRUCTURE = 6;

    private int mistake = 0;

    public InvalidQuestionException()
    {
        super();
    }

    public InvalidQuestionException(int i)
    {
        super();
        mistake = i;
    }

    public InvalidQuestionException(String message)
    {
        super(message);
    }

    public int getMistake() {
        return mistake;
    }

    /**
     * @return String with information what kind of mistake was in the question
     */
    public String getStringWithInfo()
    {
        switch (mistake)
        {
            case NO_OBJECT: return "No such object in memory";
            case NO_FIRST_TRAIT: return "No such trait as the first one in this object";
            case NO_SECOND_TRAIT: return "No such trait as the second one in this object";
            case NO_OPERATOR: return "No operator";
            case NO_QUESTION: return "When you finally decide what you want to ask, then come again";
            case WRONG_STRUCTURE: return "question cannot be asked that way";
            case 0: return "Something wrong";
            default: return null;
        }
    }
}
