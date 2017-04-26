package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.State;
import com.pwr.zpi.HolonCollection;
import com.pwr.zpi.language.*;

import java.util.List;

/**
 * Created by Weronika on 22.04.2017.
 */
public class ComplexStatement extends Statement {

    public ComplexStatement(ComplexFormula formula, Agent agent, int time, List<String> info)
    {
        this.formula = formula;
        holon = HolonCollection.findHolon(formula, agent, time);
        this.info = info;
    }

    public String generateStatement()
    {
        String answer = "";
        StringBuilder sb = new StringBuilder(answer);
        Operators.Type operator = holon.getTaoForFormula(formula);
        if(operator == Operators.Type.KNOW)
            sb.append("Yes, I am sure that ");
        if(operator == Operators.Type.BEL)
            sb.append("I believe that ");
        if(operator == Operators.Type.POS)
            sb.append("I think it is possible that ");
        sb.append(info.get(0) + "'s ");
        sb.append(info.get(1));
        if(formula.getStates().get(0)==State.IS)
            sb.append(" is ");
        else sb.append(" is not ");
        sb.append(sb.append(2));
        sb.append(" and ");
        sb.append(info.get(3));
        if(formula.getStates().get(1)==State.IS)
            sb.append(" is ");
        else sb.append(" is not ");
        sb.append(info.get(4));
        return null;
    }



}
