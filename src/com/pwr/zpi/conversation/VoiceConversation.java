package com.pwr.zpi.conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class which allows sets a server communication with application that recognizes voice
 * and synthesis it. For voice communication between human and agent
 */
public class VoiceConversation implements Runnable {

    /**
     * String with current question. In theory should be processed by linguistic/Question
     */
    private String curentQuestion;
    /**
     * An answer to given question, should come from linguistic/Statement
     */
    private String currentAnswer;
    /**
     * Thread which listens to communication between server and client
     */
    Thread thread;

    /**
     * Boolean for running thread
     */
    private boolean running = false;

    /**
     * @return current question to agent;
     */
    public String getCurentQuestion() {
        String string = curentQuestion;
        currentAnswer = null;
        return string;
    }

    /**
     * method sets current answer on the previously asked question
     * @param currentAnswer
     */
    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    /**
     * thread waits for asked question and then waits till it is possible to send answer back
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.print("Serwer postawiony");
            new ProcessBuilder("voice/Voice_Test.exe", "6666", "voice/Grammar.xml").start();
            Socket clientSocket = serverSocket.accept();
            System.out.print("Klient pozyskany");
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (running) {
                curentQuestion = in.readLine();
                System.out.println(curentQuestion);
                while (currentAnswer == null)
                    Thread.sleep(500);
                out.println(currentAnswer);
                out.flush();
                currentAnswer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        }
    }

    /**
     * start the thread
     */
    public void start()
    {
        if(thread == null)
        {
            thread = new Thread(this);
            running = true;
            thread.start();
        }

    }

    /**
     * stop the thread
     */
    public void stop()
    {
        if(thread!=null)
            running = false;
    }



}



