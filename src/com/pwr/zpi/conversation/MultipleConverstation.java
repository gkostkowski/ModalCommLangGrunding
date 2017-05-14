package com.pwr.zpi.conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weronika on 14.05.2017.
 */
public class MultipleConverstation implements Runnable {

    List<Conversation> conversations = new ArrayList<>();
    Thread thread;
    boolean running = false;
    BufferedReader br;

    public void addConversation(Conversation conversation)
    {
        conversation.start();
        conversations.add(conversation);
    }

    private Conversation getConveration(String agent)
    {
        for(Conversation conversation: conversations)
            if(agent.equalsIgnoreCase(conversation.name))
                return conversation;
        return null;
    }

    public void stopAgent(String agent)
    {
        if(getConveration(agent)==null)
            System.out.print("Such agent is not listenig");
        else getConveration(agent).stop();
    }

    public void setQuestionInConversation(String question, String agent)
    {
        if(getConveration(agent)==null)
            System.out.print("Such agent is not listenig");
        else getConveration(agent).addQuestion(question);
    }

    public void stopAll()
    {
        for(Conversation conversation: conversations)
            conversation.stop();
    }

    @Override
    public void run() {
        while (running)
        {
            System.out.print("Who: ");
            try {
                String agent = br.readLine();
                if(agent.equalsIgnoreCase("stop"))
                {
                    stopAll();
                    stop();
                }
                else
                {
                    System.out.print("Question: ");
                    String question = br.readLine();
                    if(question.equalsIgnoreCase("stop"))
                        stopAgent(agent);
                    else setQuestionInConversation(question, agent);
                }
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    public void start()
    {
        if(thread == null)
        {
            thread = new Thread(this);
            running = true;
            br = new BufferedReader(new InputStreamReader(System.in));
            thread.start();
        }
    }

    public void stop()
    {
        if(thread!=null)
            running = false;
    }
}
