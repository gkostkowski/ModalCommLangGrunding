package com.pwr.zpi.language;


import java.util.List;

public abstract class Statement {

    protected Formula formula;
    protected Holon holon;

    public abstract String generateStatement();

}
