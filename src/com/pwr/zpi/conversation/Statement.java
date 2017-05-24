package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;

/**
 * Created by Weronika on 25.04.2017.
 */
public abstract class Statement {

    Agent agent;
    int timestamp;
    String name;

    public abstract String generateStatement();

}
