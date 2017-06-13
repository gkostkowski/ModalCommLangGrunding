package com.pwr.zpi.conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import org.json.*;

/**
 * Thread that connects with application that synthesis to voice the given message.
 * Acts as a server for a client application with voice synthesis engine
 */
public class Talking implements Runnable {

    /**
     * The questions Queue which stores answers
     */
    private Queue<String> answers;
    /**
     * The thread Thread is an instance of this thread
     */
    private Thread thread;
    /**
     * The RUNNING boolean allows for constant passing the answers while it is true.
     */
    private boolean RUNNING;
    /**
     * Refernce to Listening, used to be able to stop listening service for the moment of giving answer
     */
    private Listening listeningThread;
    /**
     * The talkingServer ServerSocket allows for connection with outside application that receives answers
     */
    private ServerSocket talkingServer;
    /**
     * The talkingClient Socket is a connection between this server and talkingApp
     */
    private Socket talkingClient;
    /**
     * The talkingApp Process - client application with voice synthesis engine
     */
    private Process talkingApp;
    /**
     * The printWriter PrintWriter object allows for sending JSON through socket
     */
    PrintWriter printWriter;

    /**
     * Method which puts next answer to the queue of answers
     * @param answer    String with given answer
     */
    public void addAnswer(String answer)
    {
        synchronized (answers)
        {
            answers.add(answer);
        }
    }

    public Talking(Listening listening)
    {
        this.listeningThread = listening;
    }

    /**
     * method starts the thread, starts the talkingApp, establishes connection between those
     */
    public void start()
    {
        if(answers ==null)
            answers = new LinkedList<>();
        try
        {
            talkingServer = new ServerSocket(6667);
            System.out.println("Talking server up");
            talkingApp = new ProcessBuilder("voice/Talking/Talking.exe", "6667").start();
            talkingClient = talkingServer.accept();
            System.out.println("Talking client connected");
            printWriter = new PrintWriter(talkingClient.getOutputStream(), true);
            if(thread == null)
            {
                thread = new Thread(this, "Talking");
                RUNNING = true;
                thread.start();
            }
        } catch (IOException e) { System.out.print("Could not start talking service");}
    }

    /**
     * method stops the thread, closes connection between server and client application, closes printWriter
     * and shuts the talkingApp
     */
    public void stop()
    {
        RUNNING = false;
        try {
            talkingClient.close();
            talkingServer.close();
            talkingApp.destroy();
            System.out.println("Talking service stopped");
        } catch (IOException e) {}
    }
    /**
     * thread runs while RUNNING is true and sends given answers through socket in form of JSON object
     */
    public void run()
    {
        while(RUNNING)
        {
            try {
                while(answers.isEmpty())
                    Thread.sleep(500);
                JSONObject object = new JSONObject();
                listeningThread.shouldStopListening(true);
                object.put("message", answers.peek());
                printWriter.println(object.toString());
                printWriter.flush();
                Thread.sleep(getTimeToWait(answers.remove().length()));
                listeningThread.shouldStopListening(false);
            } catch(InterruptedException e) {}
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Counts rounded time needed to say the message
     * @param length
     * @return
     */
    private int getTimeToWait(int length)
    {
        return (length/50+1)*3000;
    }
}