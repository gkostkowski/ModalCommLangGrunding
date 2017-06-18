package com.pwr.zpi.conversation;

import java.util.Queue;

/**
 * Abstract class which must be extended by class that enables agent to pass it's answer to user
 * @author Weronika Wolska
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
     * Object used to synchronize access to questions queue;
     */
    private final Object foo = new Object();

    /**
     * synchronizes method that adds a new answer to answers queue
     * @param answer    answer in form of String
     */
    public void addAnswer(String answer)
    {
        synchronized (foo)
        {
            answers.add(answer);
        }
    }
    /**
     * synchronizes method that obtains and removes first answer from answers queue
     */
    protected String getAnswer()
    {
        synchronized (foo)
        {
            return answers.remove();
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
