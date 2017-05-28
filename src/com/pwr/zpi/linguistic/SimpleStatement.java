package com.pwr.zpi.linguistic;

import com.pwr.zpi.language.*;
import com.pwr.zpi.language.SimpleFormula;

import java.util.Map;

/**
 * SimpleStatement allows for building an answer in natural language (English) based on knowledge
 * which agent currently has on given SimpleFormula
 */
public class SimpleStatement extends Statement {

    /**
     * Formula representing question in consideration
     */
    private SimpleFormula simpleFormula;
    /**
     * Case of formula complementary to the one in question (second key)
     */
    private SimpleFormula complementaryFormula;



    /**
     * Constructor of SimpleStatement
     *
     * @param formula          of asked question
     * @param groundedFormulas grounded formulas for given holon of the question
     * @param name             passed in order not to duplicate operations
     */
    public SimpleStatement(SimpleFormula formula, Map<Formula, ModalOperator> groundedFormulas, String name) {
        this.groundedFormulas = groundedFormulas;
        this.simpleFormula = formula;
        this.name = name;
        questionKey = getFormula(true);
        complementaryFormula = getFormula(false);
    }

    /**
     * @return builds an answer for simple formula question based on strategy described below.
     */
    public String generateStatement() {
        String answer;
        answer = getAnswerIfKnow();
        if (answer == null)
            answer = getAnswerIfBel();
        if (answer == null)
            answer = getAnswerIfPos();
        if (answer == null)
            answer = "I really do not know what to say about it";
        return answer;
    }

    /**
     * Method checks if any formula from groundedFormulas has modal operator of KNOW
     *
     * @return answer based on whether it matches formula in question ("Yes, I know it is...") or other formula in set
     * ("No, but I know it is..."). If neither of them is, returns null
     */
    private String getAnswerIfKnow() {
        if (groundedFormulas.containsValue(ModalOperator.KNOW)) {
            if (questionKey!=null)
                return "Yes, I know that " + getRestOfSentece((SimpleFormula)questionKey);
            else return "No, I know " + getRestOfSentece(complementaryFormula);
        }
        return null;
    }


    /**
     * Strategy of answering when one of grounded modal operators is BEL in simple statements is as follows:
     * All grounded operators are used in sentence, which means if complementary formula was grounded with either BEL
     * or POS agent uses it in an answer. However in the beginning of sentence there is always operator
     * regarding asked formula, except when asked formula was not grounded at all, then agent will first say what he
     * believes and later that he does not know what to say about formula asked.
     *
     * @return built sentence or null if there was no formula grounded with BEL
     */
    private String getAnswerIfBel() {
        if (groundedFormulas.containsValue(ModalOperator.BEL)) {
            if (questionKey!=null &&
                    groundedFormulas.get(questionKey).equals(ModalOperator.BEL)) {
                if (complementaryFormula!=null)
                    return "Yes, I believe " + getRestOfSentece((SimpleFormula)questionKey)
                            + ", but it possible that it is"
                            + ((complementaryFormula.isNegated()) ? " not" : "");
                else return "Yes, I believe " + getRestOfSentece((SimpleFormula)questionKey);
            } else {
                if (questionKey!=null && groundedFormulas.get(questionKey).equals(ModalOperator.POS))
                    return "I think it is possible, that " + getRestOfSentece((SimpleFormula)questionKey)
                            + ", however I rather believe " + getRestOfSentece(complementaryFormula);
                else return "Well, I believe " + getRestOfSentece(complementaryFormula)
                        + ", I do not know enough about the asked state";
            }
        }
        return null;
    }

    /**
     * Strategy of answering when at least one of grounded modal operators is POS in simple statements is as follows:
     * all grunded operators are used, but the one of asked formula always comes first. If asked formula was not grounded
     * it was not grounded, there is an information about not knowing what to say about it.
     *
     * @return built sentence or null if there was no POS operator
     */
    private String getAnswerIfPos() {
        if (questionKey!=null)
            if (complementaryFormula!=null)
                return "It is possible that " + getRestOfSentece((SimpleFormula)questionKey)
                        + ", but it is also possible that it is"
                        + ((complementaryFormula.isNegated()) ? " not" : "");
            else return "It is possible that " + getRestOfSentece((SimpleFormula)questionKey);
        else if (groundedFormulas.containsKey(complementaryFormula))
            return "I do not know enough about asked state, I only think it is possible that "
                    + getRestOfSentece(complementaryFormula);
        return null;
    }

    /**
     * @param formula indicates which state of formula is described
     * @return rest of sentence with proper state ([name of model] [state] [trait])
     */
    private String getRestOfSentece(SimpleFormula formula) {
        return name + (!formula.isNegated()? " is " : " is not ") + simpleFormula.getTrait().getName();
    }

    /**
     * returns key from grounded formulas
     * @param isQuestion indicates which key is being looked for, the one describing question or opposite state
     * @return key if found or null if there is not one which is wanted
     */
    private SimpleFormula getFormula(boolean isQuestion)
    {
        for (Map.Entry<Formula, ModalOperator> entry : groundedFormulas.entrySet())
            if (entry.getKey().equals(simpleFormula)==isQuestion)
                return (SimpleFormula)entry.getKey();
        return null;
    }
}




