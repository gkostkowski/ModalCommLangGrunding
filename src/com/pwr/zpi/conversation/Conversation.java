package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.language.ComplexFormula;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.SimpleFormula;

/**
 * Created by Weronika on 25.04.2017.
 */
public class Conversation {

    Agent agent;

    public Conversation(Agent agent)
    {
        this.agent = agent;
    }
    
    private String giveAnswer(String myQuestion)
    {
        String answer = "";
        try {
            Question question = new Question(myQuestion);
            Formula formula = question.getFormula();
            if(formula instanceof SimpleFormula)
            {
                SimpleStatement statement = new SimpleStatement((SimpleFormula)formula, question.getInfo());
                answer = statement.generateStatement();
            } else
            {
                ComplexStatement statement = new ComplexStatement((ComplexFormula)formula, question.getInfo());
                answer = statement.generateStatement();
            }
        } catch (InvalidQuestionException e)
        {
            answer = e.getStringWithInfo();
        }

        return answer;
    }


    public void ask(String question)
    {
        System.out.println(giveAnswer(question));
    }







}
