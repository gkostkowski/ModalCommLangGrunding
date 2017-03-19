package com.pwr.zpi.language;

import com.pwr.zpi.State;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public interface Formula {

    abstract State evaluate();
}
