package com.pwr.zpi.core.behaviours;

import com.pwr.zpi.core.Agent;

/**
 * Thread that realises update of agent's memory by getting new observations from database.
 *
 * @author Mateusz Gawlowski
 */
public class UpdateThread implements Runnable {

    Agent agent;
    CommonResources commonResources;

    public UpdateThread(Agent agent, CommonResources commonResources)
    {
        this.agent = agent;
        this.commonResources = commonResources;
    }

    @Override
    public void run() {
        commonResources.acquire(true);
        agent.updateMemory();
        commonResources.release(true);
    }
}
