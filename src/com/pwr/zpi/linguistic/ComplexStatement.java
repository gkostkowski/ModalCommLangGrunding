package com.pwr.zpi.linguistic;

import com.pwr.zpi.language.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComplexStatement allows for building an answer in natural language (English) based on knowledge
 * which agent currently has on given ComplexFormula
 */
public class ComplexStatement extends Statement {

    /**
     * Formula which was asked in question
     */
    private ComplexFormula formula;

    /**
     * P represents first trait in ComplexFormula, and Q represents second Trait
     * If N precedes one of those letters, it means that the state of this trait is negated.
     */
    private enum FormulaCase {
        PQ, NPQ, PNQ, NPNQ
    }

    /**
     * groundedFormulas converted to map, where keys are instances of FormulaCase enum
     */
    private Map<FormulaCase, ModalOperator> groundedFormulaCases;
    /**
     * FormulaCase of formula
     */
    private FormulaCase questionCase;
    /**
     * Logic operator from formula
     */
    private LogicOperator operator;

    /**
     * Constructor of ComplexStatement
     *
     * @param formula          of asked question
     * @param groundedFormulas grounded formulas for given holon of the question
     * @param name             passed in order not to duplicate operations
     */
    public ComplexStatement(ComplexFormula formula, Map<Formula, ModalOperator> groundedFormulas, String name) {
        this.formula = formula;
        this.groundedFormulas = groundedFormulas;
        this.name = name;
        operator = formula.getOperator();
        setGroundedFormulaCases();
        setQuestionCase();
    }

    /**
     * @return builds an answer for complex formula question based on strategy described below.
     */
    public String generateStatement() {
        String answer = checkForKnow();
        if (answer != null)
            return answer;
        answer = checkForBel();
        if (answer != null)
            return answer;
        answer = checkForPos();
        if (answer != null)
            return answer;
        return "I do not know what to say about it";
    }

    /**
     * Method checks if any formula from groundedFormulas has modal operator of KNOW
     *
     * @return answer based on whether it matches formula in question ("Yes, I know it is...") or other formula in set
     * ("No, but I know it is..."). If neither of them is, returns null
     */
    private String checkForKnow() {
        if (groundedFormulaCases.containsValue(ModalOperator.KNOW))
            if (groundedFormulaCases.containsKey(questionCase))
                return "Yes, I know that " + generateRestOfSentence(questionCase, true);
            else return "No, but I know that " + generateRestOfSentence(getKey(ModalOperator.KNOW), true);
        else return null;
    }

    /**
     * Method checks if any formula from groundedFormulas has modal operator of BEL
     *
     * @return answer based on following strategy: first always comes answer directly to the question, it doesn't
     * matter if it is with operator BEL, POS or it was not grounded at all. Later comes formula grounded with operator BEL,
     * if any exists. Later it is checked if any formula with at least partial connection to asked formula was grounded
     * and those are mentioned. At the end comes completely opposite formula. If one of formulas was not grounded it won't
     * be mentioned, unless it is the asked formula.
     */
    private String checkForBel() {
        if (groundedFormulaCases.containsValue(ModalOperator.BEL)) {
            if (groundedFormulaCases.containsKey(questionCase))
            {
                if (groundedFormulaCases.get(questionCase).equals(ModalOperator.BEL)) {
                    String answer = "Yes, I believe that " + generateRestOfSentence(questionCase, true);
                    if (groundedFormulaCases.size() == 4)
                        answer += " however all other states are also a little bit possible";
                    if (groundedFormulaCases.size() == 3) {
                        List<FormulaCase> keys = getKeysForPOS();
                        if (keys.get(0).equals(getOpposite(questionCase)))
                            answer += ", but it is also possible that " + generateRestOfSentence(keys.get(1), false)
                                    + " or even quite the opposite";
                        else if (keys.get(1).equals(getOpposite(questionCase)))
                            answer += ", but it is also possible that " + generateRestOfSentence(keys.get(0), false)
                                    + " or even quite the opposite";
                        else answer += ", but it is also possible at given time only one part is true";
                            /*answer += ", but it is also possible that " + generateRestOfSentence(keys.get(0), false)
                                    + " or " + generateRestOfSentence(keys.get(1), false);*/
                    }
                    if (groundedFormulaCases.size() == 2) {
                        if (groundedFormulaCases.containsKey(getOpposite(questionCase)))
                            answer += ", but it is possible that it is just the opposite";
                        else
                            answer += ", but it is also possible that " + generateRestOfSentence(getKey(ModalOperator.POS), false);
                    }
                    return answer;
                } else if (groundedFormulaCases.get(questionCase).equals(ModalOperator.POS)) {
                    String answer = "I think it is possible that " + generateRestOfSentence(questionCase, true) +
                            ", however I believe that " + generateRestOfSentence(getKey(ModalOperator.BEL), false);
                    if (groundedFormulaCases.size() == 4)
                        answer += " and it is even possible that other states are true";
                    else if (groundedFormulaCases.size() == 3) {
                        List<FormulaCase> keys = getKeysForPOS();
                        keys.remove(questionCase);
                        if (keys.get(0).equals(getOpposite(questionCase))) answer += " and it is also possible " +
                                "that it is the opposite";
                        else answer += " and it is possible that " + generateRestOfSentence(keys.get(0), false);
                    }
                    return answer;
                }
            }
            else {
                String answer = "I do not know what to say about those states, however I believe that "
                        + generateRestOfSentence(getKey(ModalOperator.BEL), true);
                if (groundedFormulaCases.size() == 3)
                    answer += " and it is possible that other states are true";
                else if (groundedFormulaCases.size() == 2)
                    answer += ", and it is possible that " + generateRestOfSentence(getKey(ModalOperator.POS), false);
                return answer;
            }
        }
        return null;
    }

    /**
     * Method checks if any formula from groundedFormulas has modal operator of POS
     *
     * @return answer based on following strategy: first always comes answer directly to the question, it doesn't
     * matter if it is with operator POS or it was not grounded at all. Later come formulas with at least partial
     * connection to asked formula. At the end comes completely opposite formula. If one of formulas was not grounded it won't
     * be mentioned, unless it is the asked formula.
     */
    private String checkForPos() {
        if (groundedFormulaCases.containsValue(ModalOperator.POS))
            if (groundedFormulaCases.containsKey(questionCase)) {
                String answer = "It is possible that " + generateRestOfSentence(questionCase, true);
                if (groundedFormulas.size() == 4)
                    return answer + ", but other options are also possible";
                else if (groundedFormulas.size() == 3) {
                    List<FormulaCase> keys = getKeysForPOS();
                    keys.remove(questionCase);
                    if (keys.get(0).equals(getOpposite(questionCase)))
                        answer += ", but it is also possible that " + generateRestOfSentence(keys.get(1), false)
                                + " or even quite the opposite";
                    else if (keys.get(1).equals(getOpposite(questionCase)))
                        answer += ", but it is also possible that " + generateRestOfSentence(keys.get(0), false)
                                + " or even quite the opposite";
                    else answer += ", but it is also possible at given time only one part is true";
                            /*answer += ", but it is also possible that " + generateRestOfSentence(keys.get(0), false)
                                    + " or " + generateRestOfSentence(keys.get(1), false);*/
                } else if (groundedFormulas.size() == 2) {
                    List<FormulaCase> keys = getKeysForPOS();
                    keys.remove(questionCase);
                    answer += ", but it is also possible that " + generateRestOfSentence(keys.get(0), false);
                }
                return answer;
            } else {
                String answer = "I do not know what to say about these states, I only think it is possible that ";
                if (groundedFormulas.size() == 1)
                    answer += generateRestOfSentence(getKey(ModalOperator.POS), false);
                else if (groundedFormulas.size() == 2) {
                    List<FormulaCase> keys = getKeysForPOS();
                    if (keys.get(0).equals(getOpposite(questionCase)))
                        answer += generateRestOfSentence(keys.get(1), false) + " or even the opposite";
                    else if (keys.get(1).equals(getOpposite(questionCase)))
                        answer += generateRestOfSentence(keys.get(0), false) + " or even the opposite";
                    else answer += "at given time only one part of it is true";
                } else answer += "other options are possible";
                return answer;
            }
        return null;
    }

    /**
     * @param formulaCase formulacase for which opposite is being searched
     * @return opposite formulaCase
     */
    private FormulaCase getOpposite(FormulaCase formulaCase) {
        if (formulaCase.equals(FormulaCase.PQ))
            return FormulaCase.NPNQ;
        else if (formulaCase.equals(FormulaCase.NPQ))
            return FormulaCase.PNQ;
        else if (formulaCase.equals(FormulaCase.PNQ))
            return FormulaCase.NPQ;
        else return FormulaCase.PQ;
    }

    /**
     * Method looks for first entry with given value and returns key to it.
     * Works as intended only for cases when there is one instance of given operator in map
     *
     * @param operator value for which there is needed key
     * @return key or null if there was no match
     */
    private FormulaCase getKey(ModalOperator operator) {
        for (Map.Entry<FormulaCase, ModalOperator> entry : groundedFormulaCases.entrySet())
            if (entry.getValue().equals(operator))
                return entry.getKey();
        return null;
    }

    /**
     * Method looks through all entries to find the ones with value pos
     *
     * @return list of keys to values POS
     */
    private List<FormulaCase> getKeysForPOS() {
        List<FormulaCase> keys = new ArrayList<>();
        for (Map.Entry<FormulaCase, ModalOperator> entry : groundedFormulaCases.entrySet())
            if (entry.getValue().equals(ModalOperator.POS))
                keys.add(entry.getKey());
        return keys;
    }

    /**
     * Method allows to generate rest of sentence, so name of subject (or "it" when it is not important)
     * traits and their states
     * @param formulaCase indicates what states should be in sentence
     * @param isName      if the name should be said
     * @return String with sentence
     */
    private String generateRestOfSentence(FormulaCase formulaCase, boolean isName) {
        String subject = (isName) ? name : "it";
        String isXOR = (formula.getOperator()==LogicOperator.XOR)? "either " : "";
        if (formulaCase.equals(FormulaCase.PQ))
            return subject + " is " + isXOR + formula.getTraits().get(0).getName()
                    + formula.getOperator() + formula.getTraits().get(1).getName();
        if (formulaCase.equals(FormulaCase.NPQ))
            return subject + " is " + isXOR + "not " + formula.getTraits().get(0).getName()
                    + formula.getOperator() + formula.getTraits().get(1).getName();
        if (formulaCase.equals(FormulaCase.PNQ))
            return subject + " is " + isXOR + formula.getTraits().get(0).getName()
                    + formula.getOperator() + "not " + formula.getTraits().get(1).getName();
        if (formulaCase.equals(FormulaCase.NPNQ))
            return subject + " is " + isXOR + "not " + formula.getTraits().get(0).getName()
                    + formula.getOperator() + "not " + formula.getTraits().get(1).getName();
        return null;
    }

    /**
     * Method sets questionCase of formula to its corresponding FormulaCase
     */
    private void setQuestionCase() {
        if (formula.getStates().get(0).equals(State.IS))
            if (formula.getStates().get(1).equals(State.IS))
                questionCase = FormulaCase.PQ;
            else questionCase = FormulaCase.PNQ;
        else if (formula.getStates().get(1).equals(State.IS))
            questionCase = FormulaCase.NPQ;
        else questionCase = FormulaCase.NPNQ;
    }

    /**
     * Method creates groundedFormulaCases map out of groundedFormulas map
     */
    private void setGroundedFormulaCases() {
        groundedFormulaCases = new HashMap<>();
        for (Map.Entry<Formula, ModalOperator> entry : groundedFormulas.entrySet()) {
            if (entry.getKey().getStates().get(0) == State.IS)
                if (entry.getKey().getStates().get(1) == State.IS)
                    groundedFormulaCases.put(FormulaCase.PQ, entry.getValue());
                else groundedFormulaCases.put(FormulaCase.PNQ, entry.getValue());
            else if (entry.getKey().getStates().get(1) == State.IS)
                groundedFormulaCases.put(FormulaCase.NPQ, entry.getValue());
            else groundedFormulaCases.put(FormulaCase.NPNQ, entry.getValue());
        }
    }


}
