package com.pwr.zpi.conversation;

import java.util.Queue;

/**
 * Abstract class which must be extended by class that enables user to ask the agent a question
 * @author Weronika Wolska
 */
public abstract class Listening implements Runnable{

    /**
     * The questions Queue which stores consecutive questions
     */
    protected Queue<String> questions;
    /**
     * The thread Thread is an instance of this thread
     */
    protected Thread thread;
    /**
     * The RUNNING boolean allows for constant listening for the questions while it is true.
     */
    protected boolean RUNNING;
    /**
     * Object used to synchronize access to questions queue;
     */
    private final Object foo = new Object();
    /**
     * The listeningServer ServerSocket allows for connection with outside application that provides next questions
     */
    void putQuestion(String question)
    {
        synchronized (foo){
            questions.add(question);
        }
    }
    /**
     * @return first question bufferedReader queue or null if none was asked
     */
    public String getQuestion() {
        synchronized (foo) {
            if (questions.isEmpty())
                return null;
            else return questions.remove();
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
