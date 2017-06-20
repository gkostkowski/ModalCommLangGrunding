package com.pwr.zpi.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains various methods used in application.
 */
public class Util {

    static public void setLogVisibilityLevel(Level level){
        Logger rootLog = Logger.getLogger("");
        rootLog.setLevel(level);
        rootLog.getHandlers()[0].setLevel(level);
    }
}
