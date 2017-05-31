package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.language.ComplexFormula;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Grounder;
import com.pwr.zpi.language.SimpleFormula;
import com.pwr.zpi.linguistic.ComplexStatement;
import com.pwr.zpi.linguistic.Question;
import com.pwr.zpi.linguistic.SimpleStatement;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class starting a thread which listens to questions to agent and lets it answer
 */
public class Conversation implements Runnable {

    private final Agent agent;
    private Context context;
    public String name;
    private int timestamp;

    /**
     * Queue of questions to process
     */
    private Queue<String> queue;

    private Thread thread;
    private boolean running = false;

    /**
     * Construction of Conversation
     * @param agent
     * @param name used only in case of handling multiple conversations, indicates to whom we refer
     * @param timestamp
     */
    public Conversation(Agent agent, String name, int timestamp, Context context)
    {
        this.agent = agent;
        this.name = name;
        this.timestamp = timestamp;
        queue = new LinkedList<>();
        this.context = context;
    }

    public void setTimestamp(int time)
    {
        timestamp = time;
    }

    public void addQuestion(String question)
    {
        queue.add(question);
    }


    /**
     * Method used to set answer for given question
     * @param question
     * @return String with answer or reason why can't it be given (in case of exception)
     */
    private String askQuestion(String question)
    {
        System.out.println("Question: " + question);
        Question question1 = new Question(question, agent);
        try {
            Formula formula = question1.getFormula();
            return getAnswer(formula, context, question1.getName());
        } catch (InvalidQuestionException e) {
            if(e.getMistake()==InvalidQuestionException.NO_QUESTION) return e.getStringWithInfo();
            else return "I didn't understand the question, sorry. It was because there is " + e.getStringWithInfo();
        } catch (InvalidFormulaException e) {
            return "I couldn't create proper answer, I am really sorry";
        } catch (Exception e) {
            e.printStackTrace();
            return  "Something terrible happened!";}
    }

    /**
     * Method which receives proper answer from specific Statement class
     * @param formula
     * @param name
     * @return
     */
    private String getAnswer(Formula formula, Context context, String name)
    {
        if(formula instanceof SimpleFormula)
        {
            SimpleStatement statement = null;
            try {
                statement = new SimpleStatement((SimpleFormula)formula, Grounder.performFormulaGrounding(agent, formula, context), name);
            } catch (InvalidFormulaException e) {
                e.printStackTrace();
            } catch (NotApplicableException e) {
                e.printStackTrace();
            } catch (NotConsistentDKException e) {
                e.printStackTrace();
            }
            return statement.generateStatement();
        }
        else
        {
            ComplexStatement statement = null;
            try {
                statement = new ComplexStatement((ComplexFormula)formula, Grounder.performFormulaGrounding(agent, formula, context), name);
            } catch (InvalidFormulaException e) {
                e.printStackTrace();
            } catch (NotApplicableException e) {
                e.printStackTrace();
            } catch (NotConsistentDKException e) {
                e.printStackTrace();
            }
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
          //      timestamp = agent.getEpisodicKnowledgeBase().getTimestamp();
                System.out.println(askQuestion(queue.remove()));
                System.out.println();
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
