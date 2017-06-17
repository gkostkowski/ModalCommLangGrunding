package com.pwr.zpi.conversation;

import java.util.LinkedList;

/**
 * Thread that extends Talking class and allows for printing answers onto system console
 * @author Weronika Wolska
 */
public class ConsoleTalking extends Talking implements Runnable {

    /**
     * starts the thread by initialising main fields
     */
    @Override
    public void start() {
        if(answers==null)
            answers = new LinkedList<>();
        if (thread == null) {
            thread = new Thread(this, "ConsoleTalking");
            RUNNING = true;
            thread.start();
        }
    }
    /**
     * stops the thread
     */
    @Override
    public void stop() {
        RUNNING = false;
    }

    /**
     * while thread is running it prints next answers onto console
     */
    @Override
    public void run() {
        while (RUNNING)
        {
            if(!answers.isEmpty())
                System.out.println(getAnswer());
        }
    }
}
