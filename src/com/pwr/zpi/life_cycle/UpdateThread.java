package com.pwr.zpi.life_cycle;

import com.pwr.zpi.core.Agent;

public class UpdateThread implements Runnable {

    Agent agent;

    public UpdateThread(Agent agent)
    {
        this.agent = agent;
    }

    @Override
    public void run() {
        agent.updateMemory();
    }
}
