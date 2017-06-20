package com.pwr.zpi.core.behaviours;

import com.pwr.zpi.core.Agent;

public class UpdateThread implements Runnable {

    Agent agent;
    CommonResources statics2;

    public UpdateThread(Agent agent, CommonResources statics2)
    {
        this.agent = agent;
        this.statics2 = statics2;
    }

    @Override
    public void run() {
        statics2.acquire(true);
        agent.updateMemory();
        statics2.release(true);
    }
}
