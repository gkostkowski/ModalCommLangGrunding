package com.pwr.zpi.Conversation;

import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Holon;

/**
 * Created by Weronika on 25.04.2017.
 */
public abstract class Statement {

    Formula formula;
    Holon holon;

    public abstract String generateStatement();



}
