package com.pwr.zpi.conversation;

import java.util.Queue;

/**
 * Created by Weronika on 17.06.2017.
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
     * The listeningServer ServerSocket allows for connection with outside application that provides next questions
     */
    public void putQuestion(String question)
    {
        questions.add(question);
    }
    /**
     * @return first question bufferedReader queue or null if none was asked
     */
    public String getQuestion() {
        if (questions.isEmpty())
            return null;
        else return questions.remove();
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
