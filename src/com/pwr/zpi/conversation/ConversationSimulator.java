package com.pwr.zpi.conversation;

import com.pwr.zpi.core.Agent;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.language.*;
import com.pwr.zpi.linguistic.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class allows for a simulation of conversation based on scenario, without the need to start
 * a new life cycle of an agent. It is also a merely simulation in that it doesn't leave the agent any way of
 * deciding what to do with the incoming question, as it only starts a thread which listens to questions
 * and answers them
 *
 * @author Weronika Wolska
 */
public class ConversationSimulator implements Runnable {

    /**
     * Reference to agent with whom the conversation is held
     */
    private final Agent agent;
    /**
     * Queue of questions to process
     */
    private Queue<String> questions;
    /**
     * The thread Thread is an instance of this thread
     */
    private Thread thread;
    /**
     * The RUNNING boolean allows for constant listening for the questions while it is true.
     */
    private boolean running = false;

    /**
     * Construction of ConversationSimulator
     *
     * @param agent instance of agent with whom the conversation is held
     */
    public ConversationSimulator(Agent agent) {
        this.agent = agent;
        questions = new LinkedList<>();
    }
    /**
     * Method adds a new question to the questions queue
     *
     * @param question question to agent in form of String
     */
    public void addQuestion(String question) {
        questions.add(question);
    }
    /**
     * Method used to establish answer for given question
     *
     * @param question question removed from questions queue
     * @return String with answer or reason why can't it be given (in case of exception)
     */
    private String askQuestion(String question) {
        System.out.println("Question: " + question);
        Question question1 = new Question(question, agent);
        try {
            Formula formula = question1.getFormula();
            return setAnswer(formula, question1.getName());
        } catch (InvalidQuestionException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Ivalid question", e);
            if (e.getMistake() == InvalidQuestionException.NO_QUESTION) return e.getStringWithInfo();
            else return "I didn't understand the question, sorry. It was because there is " + e.getStringWithInfo();
        } catch (InvalidFormulaException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Invalid formula", e);
            return "I couldn't create proper answer, I am really sorry";
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Some error", e);
            return "Something terrible happened!";
        }
    }
    /**
     * Method which receives proper answer from specific Statement class
     * based on grounded formulas
     *
     * @param formula Formula used in grounding process
     * @param name    Name of the object from question
     * @return String with generated answer
     */
    private String setAnswer(Formula formula, String name) {
        Statement statement;
        Map<Formula, ModalOperator> map;
        try {
            map = Grounder.performFormulaGrounding(agent, formula);
            if (formula instanceof ComplexFormula)
                statement = new ComplexStatement((ComplexFormula) formula, map, name);
            else statement = new SimpleStatement((SimpleFormula) formula, map, name);
            return statement.generateStatement();
        } catch (InvalidFormulaException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Invalid formula in setAnswer while running ConversationSimulator", e);
            return e.getMessage();
        } catch (NotApplicableException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Not Applicable exception in setAnswer while running ConversationSimulator", e);
            return e.getMessage();
        } catch (NotConsistentDKException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "Not consistent DK in setAnswer while running ConversationSimulator", e);
            return e.getMessage();
        }
    }
    /**
     * While thread is running it checks whether a new question was asked
     * and gives an answer for it
     */
    @Override
    public void run() {
        while (running) {
            if (!questions.isEmpty()) {
                System.out.println();
                System.out.println(askQuestion(questions.remove()));
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "Interrupted sleep in ConversationSimulator");
            }
        }
    }
    /**
     * Starts a new instance of this thread
     */
    public void start() {
        System.out.println(agent + " is listening");
        if (thread == null) {
            thread = new Thread(this);
            running = true;
            thread.start();
        }

    }
    /**
     * Stops this instance of thread
     */
    public void stop() {
        if (thread != null)
            running = false;
    }


}
