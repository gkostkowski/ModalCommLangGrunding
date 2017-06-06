package com.pwr.zpi.life_cycle;

import com.pwr.zpi.Agent;
import com.pwr.zpi.conversation.Talking;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.language.*;
import com.pwr.zpi.linguistic.ComplexStatement;
import com.pwr.zpi.linguistic.Question;
import com.pwr.zpi.linguistic.SimpleStatement;
import com.pwr.zpi.linguistic.Statement;

import java.util.Map;

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
