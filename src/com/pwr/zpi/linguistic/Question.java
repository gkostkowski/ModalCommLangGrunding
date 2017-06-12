
package com.pwr.zpi.linguistic;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.language.*;
import com.pwr.zpi.semantic.IndividualModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class allowing to generate formula out of string of text
 */

public class Question {

    public static final String DEFAULT_FAILURE_ANSWER = "Something terrible happened";
    /**
     * Instance of agent which memory will be searched through in order to build proper answer
     */
    private Agent agent;
    /**
     * parts of question, divided in order to process them
     */
    private String[] parts;
    /**
     * length of parts
     */
    private int length;
    /**
     * current index of parts
     */
    private int index;

    /**
     * IndividualModel which will be the subject of formula
     */
    private IndividualModel individualModel;
    /**
     * Name of the model which was used to describe it
     */
    private String name;

    /**
     * Constructor of Question
     *
     * @param question string representation of question in form of
     *                 "Is [name of model] [not - if state is negated] [trait]" for simpleFormula
     *                 or "Is [name of model] [not - if first state is negated] [trait] [operator]
     *                 [not - if second state is negated] [trait]" for ComplexFormula
     *
     * @param agent agent to which question was asked
     */
    public Question(String question, Agent agent)
    {
        this.agent = agent;
        parts = question.split(" ");
        length = parts.length;
    }

    /**
     * @return Formula from question
     * @throws InvalidQuestionException with proper number when there was something wrong with question
     * @throws InvalidFormulaException when a formula cannot be built
     */
    public Formula getFormula() throws InvalidQuestionException, InvalidFormulaException
    {
        if(length<5)
            throw new InvalidQuestionException(InvalidQuestionException.NO_QUESTION);
        List<Trait> traits = new ArrayList<>();
        List<State> states = new ArrayList<>();
        boolean isXOR = false;
        findIndividualModel();
        if(parts[index].equalsIgnoreCase("not"))
        {
            states.add(State.IS_NOT);
            index++;
        }
        else states.add(State.IS);
        if(parts[index].equalsIgnoreCase("either"))
        {
            isXOR = true;
            index++;
        }
        traits.add(findTraits(1));
        if(index<length)
        {
            LogicOperator op;
            switch (parts[index].toLowerCase())
            {
                case "and": op = LogicOperator.AND; break;
                case "or": op = (isXOR)? LogicOperator.XOR : LogicOperator.OR; break;
                default: throw new InvalidQuestionException(InvalidQuestionException.NO_OPERATOR);
            }
            index++;
            if(parts[index].equalsIgnoreCase("not")) {
                states.add(State.IS_NOT);
                index++;
            }
            else states.add(State.IS);
            traits.add(findTraits(2));
            if(checkValidityOfStatesInAltenrative(op, states))
                return new ComplexFormula(individualModel, traits, states, op);
            else throw new InvalidQuestionException(InvalidQuestionException.WRONG_STATES);
        }
        if(isXOR)
            throw new InvalidQuestionException(InvalidQuestionException.WRONG_STRUCTURE);
        else return new SimpleFormula(individualModel, traits, states);
    }

    public String getName()
    {
        return name;
    }

    /**
     * method looks for a model represented by name given in question
     * @throws InvalidQuestionException if no match in memory of agent was found
     */
    private void findIndividualModel() throws InvalidQuestionException
    {
        index = 3;
        String name = parts[index];
        individualModel = agent.getModels().getRepresentationByName(name);
        index++;
        while(index<length-1 && individualModel==null)
        {
            name += " " + parts[index];
            individualModel = agent.getModels().getRepresentationByName(name);
            index++;
        }
        this.name = name;
        if(individualModel==null)
            throw new InvalidQuestionException(InvalidQuestionException.NO_OBJECT);
    }

    /**
     * method looks for a trait represented by name given in question
     * @param which indicates which trait (first or second in sentence)
     * @return found Trait
     * @throws InvalidQuestionException if no matching trait was found
     */
    private Trait findTraits(int which) throws InvalidQuestionException
    {
        String name = parts[index];
        index++;
        Trait trait = individualModel.getType().findTraitByName(name);
        while(index<length && trait == null)
        {
            name +=parts[index];
            trait = individualModel.getType().findTraitByName(name);
            index++;
        }
        if(trait == null)
            if(which==1)
                throw new InvalidQuestionException(InvalidQuestionException.NO_FIRST_TRAIT);
            else throw new InvalidQuestionException(InvalidQuestionException.NO_SECOND_TRAIT);
        return trait;
    }

    private boolean checkValidityOfStatesInAltenrative(LogicOperator op, List<State> states)
    {
        if(op==LogicOperator.OR || op==LogicOperator.XOR)
            if(states.get(0)==State.IS_NOT || states.get(1)==State.IS_NOT)
                return false;
        return true;
    }

}

