package com.pwr.zpi.life_cycle;

import com.pwr.zpi.Agent;
import com.pwr.zpi.conversation.Listening;
import com.pwr.zpi.conversation.Talking;
import com.pwr.zpi.language.Formula;

import java.util.ArrayList;
import java.util.List;

/**
 * life_cycle allows for starting new thread with life cycle of an agent, which consists of cyclic behaviours
 * that can trigger one-shot behaviours. It allows for start with a completely new agent or a copy of existing
 * agent. For now it implements periodical checking for new observations and new questions.
 */
public class LifeCycle implements Runnable {

    /**
     * The RUNNING boolean allows for constant looping through life cycle of the agent when it is true.
     */
    private boolean RUNNING = false;
    /**
     * The thread Thread is an instance of this thread
     */
    private Thread thread;
    /**
     * The listening thread is a thread that allows for receiving voice questions to agent
     */
    private Listening listeningThread;
    /**
     * The talkingThread is a thread that allows to send answer to program which will answer to question vocally
     */
    private Talking talkingThread;
    /**
     * static Object foo is used for synchronization between Threads //todo może jednak flagi
     */
    private int semaphore = 0;

    public static final Object foo = new Object();

    public static List<Formula> formulasInProcess;
    /**
     * Agent which life cycle concerns
     */
    private Agent agent;

    private Thread updateThread;

    /**
     * Constructor of LifeCycle, it creates a thread which will work on new agent
     */
    public LifeCycle()
    { }

    /**
     * Constructor of LifeCycle which allows to working with existing instance of agent
     * @param agent     copy of the agent
     */
    public LifeCycle(Agent agent)
    {
        this.agent = agent;
    }

    /**
     * main loop of the agent. Periodically checks for new observations and new questions
     */
    @Override
    public void run() {
        while(RUNNING)
        {
            if(checkIfNewObservations() && !updateThread.isAlive())
            {
                acquire(true);
                updateThread = new Thread(new UpdateThread(agent));
                updateThread.start();
                release(true);
            }
            String question = listeningThread.getQuestion();
            if(question!=null)
            {
                System.out.println(question);
                new AnswerThread(talkingThread, question, this, agent);
            }
        }
        System.out.println("Stopped - life cycle");
    }

    private boolean checkIfNewObservations() {
        return agent.isNewObservationInDatabase();
    }

    /**
     * starts new life cycle of a completly new Agent or resumes life cycle of agent. Also starts threads
     * that allows for voice communications
     */
    public void start()
    {
        if(agent==null)
            agent = new Agent.AgentBuilder().build();
        formulasInProcess = new ArrayList<>();
        listeningThread = new Listening();
        listeningThread.start();
        talkingThread = new Talking(listeningThread);
        talkingThread.start();
        updateThread = new Thread(new UpdateThread(agent));
        updateThread.start();
        if(thread==null)
        {
            thread = new Thread(this, "life cycle");
            RUNNING = true;
            thread.start();
        }
    }

    /**
     * stops life cycle of agent, but leaves options for resuming it in the future
     */
    public void stop()
    {
        RUNNING = false;
        if(thread!=null)
            thread = null;
        if(listeningThread!=null)
            listeningThread.stop();
        if(talkingThread!=null)
            talkingThread.stop();
    }

    /**
     * Method which checks if any similar formula is being processed at the moment, if none such was found it
     * adds given formula to list of ones in middle of processing
     * @param formula   to which currently processed formula are compared
     * @return true if none was found, false if similar was found
     */
    public static boolean canFormulaBeProccessed(Formula formula)
    {
        synchronized (formulasInProcess) {
            for (Formula f : formulasInProcess) {
                if (formula.isFormulaSimilar(f))
                    return false;
            }
            formulasInProcess.add(formula);
            return true;
        }
    }

    /**
     * method removes given formula from formulas being processed
     * @param formula   formula which was completely processed and is not used anymore
     */
    public void removeFromFormulasInProccess(Formula formula)
    {
        synchronized (formulasInProcess)
        {
            formulasInProcess.remove(formula);
            formulasInProcess.notifyAll();
        }
    }

    /**
     * method which blocks access for specific threads
     * @param isMemoryUpdateThread  true if blocking thread is the memory update thread
     */
    public void acquire(boolean isMemoryUpdateThread)
    {
        synchronized (foo) {
            if (isMemoryUpdateThread) {
                while (semaphore > 0)
                    try {
                        foo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                semaphore--;
            } else {
                while (semaphore < 0)
                    try {
                        foo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                semaphore++;
            }
        }
    }

    /**
     * method releases blockage and allows other threads to access resources
     * @param isMemoryUpdateThread  true if releasing thread is the memory update one
     */
    public void release(boolean isMemoryUpdateThread)
    {
        synchronized (foo) {
            if (isMemoryUpdateThread)
                semaphore++;
            else semaphore--;
            foo.notifyAll();
        }
    }

    /**
     * Method called when there is formula to process. It waits till no similar formula is being processed
     * @param formula
     */
    public void tryProccessingFormula(Formula formula)
    {
        while(!canFormulaBeProccessed(formula))
            synchronized (formulasInProcess)
            {
                try {
                    formulasInProcess.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }



}
