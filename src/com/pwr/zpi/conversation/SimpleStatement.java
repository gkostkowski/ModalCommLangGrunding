package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.State;
import com.pwr.zpi.language.BinaryHolon;
import com.pwr.zpi.language.SimpleFormula;


public class SimpleStatement extends Statement{

    private double MIN_POS = 0.2;
    private double MAX_POS = 0.6;
    private double MIN_BEL = 0.7;
    private double MAX_BEL = 0.9;

    BinaryHolon holon;
    SimpleFormula simpleFormula;

    public SimpleStatement(SimpleFormula formula, Agent agent, int time, String name)
    {
        simpleFormula = formula;
        holon = (BinaryHolon)agent.getHolons().getHolon(formula, agent, time);

        this.name = name;
    }

    @Override
    public String generateStatement() {
        String answer;

        double p = holon.getP();
        double notP = holon.getnot_P();

        if(simpleFormula.isNegated())
            answer = getSentence(notP, p, State.IS_NOT);
        else answer = getSentence(p, notP, State.IS);

        return answer;
    }

    private String getSentence(double first, double alternative, State state)
    {
        String sf, sa;
        String answer;
        if(state == State.IS)
        {
            sf = "is ";
            sa = "is not ";
        } else
        {
            sa = "is ";
            sf = "is not ";
        }
        if (first == 1)
            answer = "Yes, I am sure that " + name + sf + simpleFormula.getTrait().getName();
        else if (withinBel(first) && !withinPos(alternative))
            answer = "Yes, I believe that " + name + sf + simpleFormula.getTrait().getName();
        else if (withinBel(first) && withinPos(alternative))
            answer = "I believe that " + name + sf + simpleFormula.getTrait().getName() + ", but it is also possible that it " + sa;
        else if (withinPos(first) && !withinBel(alternative))
            answer = "Well, it is possible that " + name + sf + simpleFormula.getTrait().getName();
        else if (withinPos(first) && withinBel(alternative))
            answer = "Well, it is possible that " + name + sf + simpleFormula.getTrait().getName() + ", but I believe  it " + sa;
        else if (!withinPos(first)&&withinBel(alternative))
            answer = "No, I believe that " + name + sa + simpleFormula.getTrait().getName();
        else if (alternative==1)
            answer = "No, I am sure that " + name + sa + simpleFormula.getTrait().getName();
        else answer = "I don't know what to think about it";
        return answer;
    }

    private boolean withinBel(double p)
    {
        return p>=MIN_BEL && p<MAX_BEL;
    }

    private boolean withinPos(double p)
    {
        return p>=MIN_POS && p<MAX_POS;
    }

}
