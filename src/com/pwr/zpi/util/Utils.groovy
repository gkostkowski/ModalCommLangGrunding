package com.pwr.zpi.util

/**
 * Created by Grzesiek on 2017-04-25.
 */
class Utils { //todo napisz w javie
    static String makePath() {
        String [] parts = System.getProperty("user.dir").split("\\\\");
        parts = parts.dropRight(1)
        def path = parts.join("\\")+ '\\config\\types_def.xml'
    }
}
