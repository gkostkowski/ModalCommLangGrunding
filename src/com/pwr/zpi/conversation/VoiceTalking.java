package com.pwr.zpi.conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.*;

/**
 * Thread that connects with application that synthesis to voice the given message.
 * Acts as a server for a client application with voice synthesis engine
 * @author Weronika Wolska
 */
public class VoiceTalking extends Talking implements Runnable {

    /**
     * Refernce to VoiceListening, used to be able to stop listening service for the moment of giving answer
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
    private PrintWriter printWriter;

    /**
     * Constructor of the class
     * @param listening     reference to Listening thread, in case if there would be need
     *                      to stop it for a moment
     */
    public VoiceTalking(Listening listening)
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
                thread = new Thread(this, "VoiceTalking");
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
        } catch (IOException e) {Logger.getAnonymousLogger().log(Level.WARNING,
                "IOException in VoiceTalking", e);}
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
                if(listeningThread instanceof VoiceListening)
                    ((VoiceListening)listeningThread).shouldStopListening(true);
                String answer = getAnswer();
                object.put("message", answer);
                printWriter.println(object.toString());
                printWriter.flush();
                Thread.sleep(getTimeToSayTheAnswer(answer.length()));
                if(listeningThread instanceof VoiceListening)
                    ((VoiceListening)listeningThread).shouldStopListening(false);
            } catch(InterruptedException e) { Logger.getAnonymousLogger().log(Level.INFO,
                    "Interrupted sleep in VoiceTalking", e);}
            catch (JSONException e) {
               Logger.getAnonymousLogger().log(Level.WARNING,
                        "JSON exception in VoiceTalking", e);
            }
        }
    }
    /**
     * Counts rounded time needed to say the message
     * @param length    length of the message
     * @return          time it would likely take to say the message out loud
     */
    private int getTimeToSayTheAnswer(int length)
    {
        return (length/50+1)*3000;
    }
}
