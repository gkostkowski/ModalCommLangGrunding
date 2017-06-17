package com.pwr.zpi.linguistic;

import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.ModalOperator;

import java.util.Map;

/**
 * abstract class which must be extended by classes which provide transition from agent's knowledge
 * to natural language
 * @author Weronika Wolska
 */
public abstract class Statement {

    /**
     * List of all grounded formulas for given model and trait
     */
    protected Map<Formula, ModalOperator> groundedFormulas;

    /**
     * Key of formula in question from groundedFormulas
     */
    protected Formula questionKey;

    /**
     * Lexical name of object being regarded in formula
     */
    protected String name;

    /**
     * @return answer for the given question
     */
    public abstract String generateStatement();

}
