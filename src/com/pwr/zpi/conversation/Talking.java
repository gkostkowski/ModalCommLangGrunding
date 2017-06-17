package com.pwr.zpi.conversation;

import java.util.Queue;

/**
 * Created by Weronika on 17.06.2017.
 */
public abstract class Talking implements Runnable {
    /**
     * The questions Queue which stores answers
     */
    protected Queue<String> answers;
    /**
     * The thread Thread is an instance of this thread
     */
    protected Thread thread;
    /**
     * The RUNNING boolean allows for constant passing the answers while it is true.
     */
    protected boolean RUNNING;
    /**
     * Refernce to VoiceListening, used to be able to stop listening service for the moment of giving answer
     */
    public void addAnswer(String answer)
    {
        synchronized (answers)
        {
            answers.add(answer);
        }
    }
    /**
     * method starts the thread
     */
    abstract public void start();
    /**
     * method stops the thread
     */
    abstract public void stop();

}
