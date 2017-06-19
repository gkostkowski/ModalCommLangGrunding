package com.pwr.zpi.conversation;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread that extends Listening and connects with application that listens to voice
 * questions and provides with strings with those.
 * Acts as a server for a client application with voice recognition engine.
 * @author Weronika Wolska
 */
public class VoiceListening extends Listening implements Runnable {

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
    private PrintWriter printWriter;

    /**
     * method starts the thread, starts the listeningApp, establishes connection between those
     */
    public void start() {
        if (questions == null)
            questions = new LinkedList<>();
        try {
            listeningServer = new ServerSocket(6666);
            Logger.getAnonymousLogger().log(Level.INFO, "Listening server up");
            listeningApp = new ProcessBuilder("voice/Listening/Listening.exe", "6666", "voice/Listening/Grammar.xml").start();
            listeningClient = listeningServer.accept();
            Logger.getAnonymousLogger().log(Level.INFO, "Listening client connected");
            bufferedReader = new BufferedReader(new InputStreamReader(listeningClient.getInputStream()));
            printWriter = new PrintWriter(listeningClient.getOutputStream(), true);
            if (thread == null) {
                thread = new Thread(this, "VoiceListening");
                RUNNING = true;
                thread.start();
            }
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Could not start listening service", e);
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
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "IOException while stopping VoiceListening", e);
        }
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
                putQuestion(object.getString("message"));
            } catch (IOException e) {  Logger.getAnonymousLogger().log(Level.WARNING,
                    "IOException while running VoiceListening", e);}
            catch (JSONException e) {
                Logger.getAnonymousLogger().log(Level.WARNING,
                        "JSONException while running VoiceListening", e);
            }
        }
    }
    /**
     * Method stops the voice recognition application for the moment of giving the answer by agent,
     * if it is answering using voice or starts it again
     * @param shouldListen  boolean indicating if the application should listen (true) or not (false)
     */
    void shouldStopListening(boolean shouldListen)
    {
        JSONObject object1 = new JSONObject();
        try {
            object1.put("stop", shouldListen);
            printWriter.println(object1.toString());
            printWriter.flush();
        } catch (JSONException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,
                    "JSONException while running VoiceListening", e);
        }
    }

}
