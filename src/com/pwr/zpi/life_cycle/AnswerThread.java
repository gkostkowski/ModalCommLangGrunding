package com.pwr.zpi.life_cycle;

import com.pwr.zpi.Agent;
import com.pwr.zpi.conversation.Talking;
import com.pwr.zpi.exceptions.*;
import com.pwr.zpi.language.*;
import com.pwr.zpi.linguistic.*;

import java.util.Map;

/**
 * Thread that processes the question asked to agent. There are allowed multiple instances of this thread during the same
 * time, however knowledge cannot be grounded if two questions consider same object, same traits and same context
 */
public class AnswerThread implements Runnable {

    /**
     * Reference to thread communicating with voice synthesis application
     */
    Talking talkingThread;
    /**
     * Reference to main life cycle thread
     */
    LifeCycle lifeCycle;
    /**
     * String with a question asked to agent
     */
    String question;
    /**
     * Instance of agent to which the question was directed
     */
    Agent agent;

    /**
     * Constructor of AnswerThread
     * @param talking   reference to Talking thread instance
     * @param question  asked question
     * @param lifeCycle reference to life_cycle thread instance
     */
    public AnswerThread(Talking talking, String question, LifeCycle lifeCycle, Agent agent)
    {
        this.question = question;
        this.agent = agent;
        talkingThread = talking;
        this.lifeCycle = lifeCycle;
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
        try {
            Statement statement;
            Formula formula = question1.getFormula();
            lifeCycle.tryProccessingFormula(formula);
            Map<Formula, ModalOperator> map;
            lifeCycle.acquire(false);
            map = Grounder.performFormulaGrounding(agent, formula);
            lifeCycle.release(false);
            lifeCycle.removeFromFormulasInProccess(formula);
            if(formula instanceof ComplexFormula)
                statement = new ComplexStatement((ComplexFormula)formula, map, question1.getName());
            else statement = new SimpleStatement((SimpleFormula)formula, map, question1.getName());
            talkingThread.addAnswer(statement.generateStatement());
        } catch (InvalidQuestionException e) {
           talkingThread.addAnswer(e.getStringWithInfo());
        } catch (InvalidFormulaException e) {
            talkingThread.addAnswer(e.getMessage());
        } catch (NotConsistentDKException e) {
            talkingThread.addAnswer(e.getMessage());
        } catch (NotApplicableException e) {
            talkingThread.addAnswer(e.getMessage());
        }

    }
}
