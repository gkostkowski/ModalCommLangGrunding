
package com.pwr.zpi.conversation;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.language.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class letting generate formula out of text
 */

public class Question {

    private Agent agent;
    private String[] parts;
    private int length;
    private int index;

    private IndividualModel individualModel;
    private String name;

    public Question(String question, Agent agent)
    {
        this.agent = agent;
        parts = question.split(" ");
        length = parts.length;
    }

    /**
     * @return formula from string with question
     * @throws InvalidQuestionException
     * @throws InvalidFormulaException
     */
    public Formula getFormula() throws InvalidQuestionException, InvalidFormulaException
    {
        if(length<2)
            throw new InvalidQuestionException(InvalidQuestionException.NO_QUESTION);
        List<Trait> traits = new ArrayList<>();
        List<State> states = new ArrayList<>();
        findIndividualModel();
        if(parts[index].equalsIgnoreCase("not"))
        {
            states.add(State.IS_NOT);
            index++;
        }
        else states.add(State.IS);
        traits.add(findTraits(1));
        if(index<length)
        {
            LogicOperator op;
            switch (parts[index].toLowerCase())
            {
                case "and": op = LogicOperator.AND; break;
                default: throw new InvalidQuestionException(InvalidQuestionException.NO_OPERATOR);
            }
            index++;
            if(parts[index].equalsIgnoreCase("not")) {
                states.add(State.IS_NOT);
                index++;
            }
            else states.add(State.IS);
            traits.add(findTraits(2));
            return new ComplexFormula(individualModel, traits, states, op);
        }
        else return new SimpleFormula(individualModel, traits, states);
    }

    public String getName()
    {
        return name;
    }

    /**
     * method looks for a model represented by name given in question
     * @throws InvalidQuestionException if no match was found
     */
    private void findIndividualModel() throws InvalidQuestionException
    {
        String name = parts[1];
        individualModel = agent.getModels().getRepresentationByName(name);
        index=2;
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
     * @throws InvalidQuestionException if no trait was found
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
}

