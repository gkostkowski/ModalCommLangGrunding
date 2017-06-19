package com.pwr.zpi.core.behaviours;

import com.pwr.zpi.core.Agent;

public class UpdateThread implements Runnable {

    Agent agent;

    public UpdateThread(Agent agent)
    {
        System.out.print("Bla");
        this.agent = agent;
    }

    @Override
    public void run() {
        agent.updateMemory();
        Statics.release(true);
    }
}
