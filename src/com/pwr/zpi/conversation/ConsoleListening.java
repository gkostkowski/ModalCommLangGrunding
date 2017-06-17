package com.pwr.zpi.conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread extending Listening allowing for reading next question from console
 * @author Weronika Wolska
 */
public class ConsoleListening extends Listening implements Runnable {

    /**
     *  bufferedReader for reading input from console
     */
    private BufferedReader bufferedReader;

    /**
     * starts the thread by initialising main fields
     */
    @Override
    public void start() {
        if (questions == null)
            questions = new LinkedList<>();
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        if (thread == null) {
            thread = new Thread(this, "ConsoleListening");
            RUNNING = true;
            thread.start();
        }
    }
    /**
     * stops the thread and closes the bufferedReader
     */
    @Override
    public void stop() {
        RUNNING = false;
        try {
            bufferedReader.close();
        } catch (IOException e) { Logger.getAnonymousLogger().log(Level.WARNING,
                "IOException while stopping ConsoleListening", e);}
    }

    /**
     * while thread is running it reads next questions from console and puts them in the question queue
     */
    @Override
    public void run() {
        while(RUNNING)
        {
            try {
                putQuestion(bufferedReader.readLine());
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, "IOException in ConsoleListening readLine in run", e);
            }
        }
    }
}
