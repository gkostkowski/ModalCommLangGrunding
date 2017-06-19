package com.pwr.zpi.core.behaviours;

import com.pwr.zpi.conversation.Talking;
import com.pwr.zpi.core.Agent;
import com.pwr.zpi.exceptions.*;
import com.pwr.zpi.language.*;
import com.pwr.zpi.linguistic.*;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread that processes the question asked to agent. There are allowed multiple instances of this thread during the same
 * time, however knowledge cannot be grounded if two questions consider same object, same traits and same context
 */
public class AnswerThread implements Runnable {

    /**
     * Reference to thread communicating with voice synthesis application
     */
    private Talking talkingThread;
    /**
     * String with a question asked to agent
     */
    private String question;
    /**
     * Instance of agent to which the question was directed
     */
    private Agent agent;

    /**
     * Constructor of AnswerThread
     * @param talking   reference to VoiceTalking thread instance
     * @param question  asked question
     */
    public AnswerThread(Talking talking, String question, Agent agent)
    {
        this.question = question;
        this.agent = agent;
        talkingThread = talking;
        Thread thread = new Thread(this, "AnswerThread");
        thread.start();
    }

    /**
     * Method tries to process asked question, starting from retrieving formula if it is possible,
     * then use it to perform grounding of the formula and finally generating answer to given question based on grounded
     * knowledge. Task of grounding formulas cannot be performed in two cases: if similar formula is currently being
     * processed or if memory is being updated
     */
    @Override
    public void run() {
        Question question1 = new Question(question, agent);
        Formula formula = null;
        try {
            Statement statement;
            formula = question1.getFormula();
            Statics.tryProccessingFormula(formula);
            Map<Formula, ModalOperator> map;
            Statics.acquire(false);
            map = Grounder.performFormulaGrounding(agent, formula);
            releaseResources(formula);
            if(formula instanceof ComplexFormula)
                statement = new ComplexStatement((ComplexFormula)formula, map, question1.getName());
            else statement = new SimpleStatement((SimpleFormula)formula, map, question1.getName());
            talkingThread.addAnswer(statement.generateStatement());
        } catch (InvalidQuestionException e) {
           talkingThread.addAnswer(e.getStringWithInfo());
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception while parsing question", e);
        } catch (InvalidFormulaException e) {
            talkingThread.addAnswer(e.getMessage());
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception while creating formulas", e);
        } catch (NotConsistentDKException | NotApplicableException e) {
            talkingThread.addAnswer(e.getMessage());
            releaseResources(formula);
            Logger.getAnonymousLogger().log(Level.WARNING, "Exception while grounding formulas", e);
        } catch (Exception e) {
            talkingThread.addAnswer("Something terrible happened");
            Logger.getAnonymousLogger().log(Level.WARNING, "not able to answer - unidentified exception", e);
            releaseResources(formula);}
    }


    private void releaseResources(Formula formula)
    {
        Statics.release(false);
        Statics.removeFromFormulasInProccess(formula);
    }
}
