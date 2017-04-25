package com.pwr.zpi.Conversation;

import com.pwr.zpi.HolonCollection;
import com.pwr.zpi.State;
import com.pwr.zpi.language.Holon;
import com.pwr.zpi.language.Operators;
import com.pwr.zpi.language.SimpleFormula;

/**
 * Created by Weronika on 25.04.2017.
 */
public class SimpleStatement extends Statement{


    public SimpleStatement(SimpleFormula formula)
    {
        this.formula = formula;
        holon = HolonCollection.findHolon(formula);
        if(holon == null)
        {
            // holon = new Holon(formula, ) todo
        }
    }

    @Override
    public String generateStatement() {
        Operators.Type operator = holon.getTaoForFormula(formula);
        String answer = "";
        String name = String.valueOf(formula.getTraits().get(0).getName());
        if(operator == Operators.Type.POS)
            answer = "It is possible that its' ";
        if(operator == Operators.Type.BEL)
            answer = "I believe that its' ";
        if(operator == Operators.Type.KNOW)
            answer = "I know that its' ";
        StringBuilder sb = new StringBuilder(answer);
        sb.append(name);
        if(formula.getStates().get(0) == State.IS)
            sb.append("is ");
        else sb.append("is not ");
        String value = String.valueOf(formula.getTraits().get(0).getName());
        sb.append(value);

        return answer;
    }
}
