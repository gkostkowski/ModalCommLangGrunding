package com.pwr.zpi.conversation;

import com.pwr.zpi.HolonCollection;
import com.pwr.zpi.State;
import com.pwr.zpi.language.Operators;
import com.pwr.zpi.language.SimpleFormula;

import java.util.List;

/**
 * Created by Weronika on 25.04.2017.
 */
public class SimpleStatement extends Statement{


    public SimpleStatement(SimpleFormula formula, List<String> info)
    {
        this.formula = formula;
        holon = HolonCollection.findHolon(formula);
        this.info = info;
    }

    @Override
    public String generateStatement() {
        Operators.Type operator = holon.getTaoForFormula(formula);
        String answer = "";
        if(operator == Operators.Type.POS)
            answer = "It is possible that ";
        if(operator == Operators.Type.BEL)
            answer = "I believe that ";
        if(operator == Operators.Type.KNOW)
            answer = "I know that ";
        StringBuilder sb = new StringBuilder(answer);
        sb.append(info.get(0) + "'s ");
        sb.append(info.get(1));
        if(formula.getStates().get(0) == State.IS)
            sb.append(" is ");
        else sb.append(" is not ");
        sb.append(info.get(2));

        return answer;
    }
}
