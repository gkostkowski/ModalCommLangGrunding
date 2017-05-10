package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.language.ComplexFormula;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.SimpleFormula;

public class Conversation implements Runnable {

    private final Agent agent;
    private int timestamp;

    private String question;

    private Thread thread;
    private boolean running = false;

    public Conversation(Agent agent, int timestamp)
    {
        this.agent = agent;
        this.timestamp = timestamp;
    }

    public void setTimestamp(int time)
    {
        timestamp = time;
    }

    public void setQuestion(String question)
    {
        if(question==null)
            this.question = question;
    }

    private String askQuestion(String question)
    {
        Question question1 = new Question(question, agent);
        try {
            Formula formula = question1.getFormula();
            return getAnswer(formula, question1.getName());
        } catch (InvalidQuestionException e) {
            return "I didn't understand the question, sorry. It was because there is " + e.getStringWithInfo();
        } catch (InvalidFormulaException e) {
            return "I couldn't create proper answer, I am really sorry";
        } catch (Exception e) {return  "Something terrible happened!";}
    }

    public String getAnswer(Formula formula, String name)
    {
        if(formula instanceof SimpleFormula)
        {
            SimpleStatement statement = new SimpleStatement((SimpleFormula)formula, agent, timestamp, name);
            return statement.generateStatement();
        }
        else
        {
            ComplexStatement statement = new ComplexStatement((ComplexFormula) formula, agent, timestamp, name);
            return statement.generateStatement();
        }

    }

    @Override
    public void run()
    {
        while(running)
        {
            if(question != null)
            {
                askQuestion(question);//todo komunikacja zewnętrzna
                question = null;
            }
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e){}
        }
    }

    public void start()
    {
        System.out.println(agent + " is listening");
        if(thread == null)
        {
            thread = new Thread(this);
            running = true;
            thread.start();
        }

    }

    public void stop()
    {
        if(thread!=null)
            running = false;
    }


}
