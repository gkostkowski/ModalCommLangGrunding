package com.pwr.zpi.conversation;

import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Holon;

import java.util.List;

/**
 * Created by Weronika on 25.04.2017.
 */
public abstract class Statement {

    Formula formula;
    Holon holon;
    List<String> info;

    public abstract String generateStatement();

}
