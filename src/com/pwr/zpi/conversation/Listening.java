package com.pwr.zpi.conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import org.json.*;

/**
 * Thread that connects with application that listens to voice questions and provides with strings with those.
 * Acts as a server for a client application with voice recognition engine.
 */
public class Listening implements Runnable {

    /**
     * The questions Queue which stores consecutive questions
     */
    private Queue<String> questions;
    /**
     * The thread Thread is an instance of this thread
     */
    private Thread thread;
    /**
     * The RUNNING boolean allows for constant listening for the questions while it is true.
     */
    private boolean RUNNING;
    /**
     * The listeningServer ServerSocket allows for connection with outside application that provides next questions
     */
    private ServerSocket listeningServer;
    /**
     * The listeningClient Socket is a connection between this server and listeningApp
     */
    private Socket listeningClient;
    /**
     * The listeningApp Process - client application with voice recognition engine
     */
    private Process listeningApp;
    /**
     * The bufferedReader BufferedReader reads for information passed from listeningApp
     */
    private BufferedReader bufferedReader;
    /**
     * The printWriter PrintWriter object allows for sending JSON through socket
     */
    PrintWriter printWriter;

    /**
     * @return first question bufferedReader queue or null if none was asked
     */
    public String getQuestion() {
        if (questions.isEmpty())
            return null;
        else return questions.remove();
    }

    /**
     * method starts the thread, starts the listeningApp, establishes connection between those
     */
    public void start() {
        if (questions == null)
            questions = new LinkedList<>();
        try {
            listeningServer = new ServerSocket(6666);
            System.out.println("Listening server up");
            listeningApp = new ProcessBuilder("voice/Listening/Listening.exe", "6666", "voice/Listening/Grammar.xml").start();
            listeningClient = listeningServer.accept();
            System.out.println("Listening client connected");
            bufferedReader = new BufferedReader(new InputStreamReader(listeningClient.getInputStream()));
            printWriter = new PrintWriter(listeningClient.getOutputStream(), true);
            if (thread == null) {
                thread = new Thread(this, "Listening");
                RUNNING = true;
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Could not start listening service");
        }
    }

    /**
     * method stops the thread, closes connection between server and client application, closes bufferedReader
     * and shuts the listeningApp
     */
    public void stop() {
        RUNNING = false;
        try {
            listeningClient.close();
            listeningServer.close();
            listeningApp.destroy();
            System.out.println("Listening service stopped");
        } catch (IOException e) {}
    }

    /**
     * thread runs while RUNNING is true and reads next asked questions from sent JSON object
     * putting it into the queue
     */
    public void run() {
        while (RUNNING) {
            try {
                String question = bufferedReader.readLine();
                JSONObject object = new JSONObject(question);
                questions.add(object.getString("message"));
                System.out.println(questions.peek());
            } catch (IOException e) { }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void shouldStopListening(boolean shouldListen)
    {
        JSONObject object1 = new JSONObject();
        try {
            object1.put("stop", shouldListen);
            printWriter.println(object1.toString());
            printWriter.flush();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
