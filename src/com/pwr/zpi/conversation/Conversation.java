package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.language.ComplexFormula;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.SimpleFormula;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class Conversation implements Runnable {

    private final Agent agent;
    public String name;
    private int timestamp;

    private Queue<String> queue;

    private Thread thread;
    private boolean running = false;

    public Conversation(Agent agent, String name, int timestamp)
    {
        this.agent = agent;
        this.name = name;
        this.timestamp = timestamp;
        queue = new LinkedList<>();
    }

    public void setTimestamp(int time)
    {
        timestamp = time;
    }

    public void addQuestion(String question)
    {
        queue.add(question);
    }

    private String askQuestion(String question)
    {
        Question question1 = new Question(question, agent);
        try {
            Formula formula = question1.getFormula();
            return getAnswer(formula, question1.getName());
        } catch (InvalidQuestionException e) {
            if(e.getMistake()==InvalidQuestionException.NO_QUESTION) return e.getStringWithInfo();
            else return "I didn't understand the question, sorry. It was because there is " + e.getStringWithInfo();
        } catch (InvalidFormulaException e) {
            return "I couldn't create proper answer, I am really sorry";
        } catch (Exception e) {
            return  "Something terrible happened!";}
    }

    private String getAnswer(Formula formula, String name)
    {
        if(formula instanceof SimpleFormula)
        {
            SimpleStatement statement = new SimpleStatement((SimpleFormula)formula, agent, timestamp, name);
            return statement.generateStatement();
        }
        else
        {
            ComplexStatementForAND statement = new ComplexStatementForAND((ComplexFormula) formula, agent, timestamp, name);
            return statement.generateStatement();
        }

    }

    @Override
    public void run()
    {
        while(running)
        {
            if(!queue.isEmpty())
            {

                System.out.print(askQuestion(queue.remove()));
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
