package com.pwr.zpi.linguistic;

import com.pwr.zpi.language.*;

import java.util.ArrayList;
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
     * List of keys extracted from groundedFormulas map
     */
    private List<Formula> keys;

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
        keys = extractKeys();
        sortKeys(formula.getOperator() == LogicOperator.XOR);
    }

    /**
     * Method that extracts keys from groundedFormulas map for future use
     *
     * @return extracted keys in form of list
     */
    private List<Formula> extractKeys() {
        List<Formula> formulas = new ArrayList<>();
        for (Formula f : groundedFormulas.keySet())
            formulas.add(f);
        return formulas;
    }

    /**
     * Method checks if given formula is present in list of keys
     *
     * @param formula formula to be checked
     * @return true if equal formula was found, false if not
     */
    private boolean checkIfKeyPresent(Formula formula) {
        for (Formula f : keys)
            if (formula.equals(f))
                return true;
        return false;
    }

    /**
     * Method generating string with answer if one of grounded formulas has he modal operator of KNOW
     *
     * @return String with proper answer to given question
     */
    private String checkForKnow() {
        if (groundedFormulas.containsValue(ModalOperator.KNOW))
            if (checkIfKeyPresent(formula))
                return "Yes, I know that " + generateRestOfsentence(formula, true, true);
            else return "No, but I know that " + generateRestOfsentence((ComplexFormula) keys.get(0), true, true);
        else return null;
    }

    /**
     * @return builds an answer for complex formula question based on strategy described below.
     * first always comes answer directly to the question, it doesn't
     * matter if it is with operator BEL, POS or it was not grounded at all. Later comes formula grounded with operator BEL,
     * if any exists. Later it is checked if any formula with at least partial connection to asked formula was grounded
     * and those are mentioned. At the end comes completely opposite formula. If one of formulas was not grounded it won't
     * be mentioned, unless it is the asked formula.
     */
    @Override
    public String generateStatement() {
        String answer = checkForKnow();
        if (answer != null)
            return answer;
        answer = "";
        StringBuilder sb = new StringBuilder(answer);
        if (keys.get(0).equals(formula)) {
            sb.append("Yes, ");
            sb.append(getBeginningOfSubsentence(groundedFormulas.get(keys.get(0))));
            sb.append(generateRestOfsentence(formula, true, true));
            keys.remove(0);
        } else {
            sb.append(getBeginningOfSubsentence(null));
            sb.append(" of " + name);
        }
        Formula nextWithBel = lookForBEl();
        boolean addAlso = false;
        if (nextWithBel != null) {
            sb.append(", however I believe that ");
            sb.append(generateRestOfsentence((ComplexFormula) nextWithBel, false, false));
            keys.remove(nextWithBel);
            addAlso = true;
        }
        if (!keys.isEmpty()) {
            sb.append(", but it is" + ((addAlso) ? " also" : "") + " possible that ");
        }
        String connecting = "";
        for (Formula f : keys) {
            sb.append(connecting);
            sb.append(generateRestOfsentence((ComplexFormula) f, false, false));
            connecting = " or ";
        }
        return sb.toString();
    }

    /**
     * Method checks if any formula left in keys list was grounded with the modal operator BEL
     *
     * @return null if no such formula was found, or formula with BEL operator
     */
    private Formula lookForBEl() {
        if (groundedFormulas.containsValue(ModalOperator.BEL))
            for (Formula f : keys)
                if (groundedFormulas.get(f) == ModalOperator.BEL)
                    return f;
        return null;
    }

    /**
     * @param op it is operator which indicates how the beginning of the subsentence of the answer
     *           should look like
     * @return first part of the subsentence
     */
    private String getBeginningOfSubsentence(ModalOperator op) {
        if (op == null)
            return "I don't know about those states";
        if (op == ModalOperator.BEL)
            return "I believe that ";
        if (op == ModalOperator.POS)
            return "it is possible that ";
        return null;
    }

    /**
     * Sorts keys in a way that allows for an easy implementation of strategy described above
     *
     * @param isXor for XOR question some of the keys should be removed from the list, this boolean indicates
     *              if this operation should be performed
     */
    private void sortKeys(boolean isXor) {
        List<Formula> temp = new ArrayList<>();
        Formula f1 = null;
        for (Formula f : keys)
            if (formula.equals(f)) {
                temp.add(f);
                f1 = f;
                break;
            }
        keys.remove(f1);
        for (Formula f : keys)
            if (f.getStates().get(0) == formula.getStates().get(0)) {
                temp.add(f);
                f1 = f;
                break;
            }
        keys.remove(f1);
        for (Formula f : keys)
            if (f.getStates().get(1) == formula.getStates().get(1)) {
                if (!isXor || (isXor && !isOppositeAlternative(temp, f)))
                    temp.add(f);
                f1 = f;
                break;
            }
        keys.remove(f1);
        if (!keys.isEmpty() && (!isXor || (isXor && !isOppositeAlternative(temp, keys.get(0)))))
            temp.add(keys.get(0));
        keys = temp;
    }

    /**
     * checks if in list of formulas is already one that is exactly opposite regarding states
     *
     * @param formulas list of previously added formulas
     * @param checkedF formula to be verified
     * @return true if there was an opposite formula, false otherwise
     */
    private boolean isOppositeAlternative(List<Formula> formulas, Formula checkedF) {
        for (Formula f : formulas)
            if (f.getStates().get(0) != checkedF.getStates().get(0) && f.getStates().get(1) != checkedF.getStates().get(1))
                return true;
        return false;
    }

    /**
     * Method allows to generate rest of sentence, so name of subject (or "it" when it is not important)
     * traits and their states
     *
     * @param formula     formula for generating rest of sentence
     * @param isName      if the name should be said
     * @param wholeNeeded if it set to false it allows for not generating whole sentence with traits and states,
     *                    but another description
     * @return String with the rest of sentence
     */
    private String generateRestOfsentence(ComplexFormula formula, boolean isName, boolean wholeNeeded) {
        if (!wholeNeeded && isOppositeFromFormula(formula))
            return "it is quite the opposite";
        String subject = (isName) ? name : "it";
        String isXOR = (formula.getOperator() == LogicOperator.XOR) ? "either " : "";
        if (formula.getStates().get(0) == State.IS && formula.getStates().get(1) == State.IS)
            return subject + " is " + isXOR + formula.getTraits().get(0).getName()
                    + formula.getOperator() + formula.getTraits().get(1).getName();
        if (formula.getStates().get(0) == State.IS_NOT && formula.getStates().get(1) == State.IS)
            return subject + " is " + isXOR + "not " + formula.getTraits().get(0).getName()
                    + formula.getOperator() + formula.getTraits().get(1).getName();
        if (formula.getStates().get(0) == State.IS && formula.getStates().get(1) == State.IS_NOT)
            return subject + " is " + isXOR + formula.getTraits().get(0).getName()
                    + formula.getOperator() + "not " + formula.getTraits().get(1).getName();
        if (formula.getStates().get(0) == State.IS_NOT && formula.getStates().get(1) == State.IS_NOT)
            return subject + " is " + isXOR + "not " + formula.getTraits().get(0).getName()
                    + formula.getOperator() + "not " + formula.getTraits().get(1).getName();
        return null;
    }

    /**
     * checks if given formula is opposite to asked formula regarding states
     *
     * @param f formula to be checked
     * @return true if it is exactly opposite, false otherwise
     */
    private boolean isOppositeFromFormula(Formula f) {
        if (f.getStates().get(0) != formula.getStates().get(0) && f.getStates().get(1) != formula.getStates().get(1))
            return true;
        return false;
    }
}
