package com.pwr.zpi.language;

import com.pwr.zpi.State;
import com.pwr.zpi.HolonCollection;

/**
 * Created by Weronika on 22.04.2017.
 */
public class ComplexStatement extends Statement {

    private Operators.Type operator;

    public ComplexStatement(Holon holon, ComplexFormula formula)
    {
        this.formula = formula;
        holon = HolonCollection.findBinaryHolon(
                formula.getObservation(), formula.getTraits());
    }


    public String generateStatement()
    {

        String answer = "I";
        StringBuilder sb = new StringBuilder(answer);
        if(holon == null)
            sb.append(" know nothing about this object, leave me alone");
        else
        {
            if(((ComplexFormula)formula).getOperator() == Operators.Type.AND)
                sb.append(generateConjuction());
            else if(((ComplexFormula)formula).getOperator() == Operators.Type.OR)
                sb.append(generateAleternative());
        }
        return answer;
    }

    private String generateConjuction()
    {
        String s = "";
        StringBuilder sb = new StringBuilder(s);
        String trait1 = formula.getTraits().get(0).toString();
        String trait2 = formula.getTraits().get(1).toString();
        String value1 = formula.getTraits().get(0).getValue().toString();
        String value2 = formula.getTraits().get(1).getValue().toString();
        if(operator==Operators.Type.Not_Know)
        {
            sb.append(" know that its' " );

        }
        else if(operator==Operators.Type.POS)
        {
            sb.append(" think that it is possible that its' " );
        }
        else if(operator==Operators.Type.BEL)
        {
            sb.append(" believe that its' " );
        }
        else if(operator==Operators.Type.KNOW)
        {
            sb.append(" am sure that its' " );
        }
        sb.append(trait1);
        sb.append((formula.getStates().get(0)== State.IS)? "is" : " is not ");
        sb.append(value1);
        sb.append(" and its' ");
        sb.append(trait2);
        sb.append((formula.getStates().get(1)== State.IS)? "is" : " is not ");
        sb.append(value2);

        return s;
    }

    private String generateAleternative()
    {
        String s = "";
        StringBuilder sb = new StringBuilder(s);
        String trait1 = formula.getTraits().get(0).toString();
        String trait2 = formula.getTraits().get(1).toString();
        String value1 = formula.getTraits().get(0).getValue().toString();
        String value2 = formula.getTraits().get(1).getValue().toString();
        if(operator==Operators.Type.Not_Know)
        {
            sb.append(" know that its' " );

        }
        else if(operator==Operators.Type.POS)
        {
            sb.append(" think that it is possible that its' " );
        }
        else if(operator==Operators.Type.BEL)
        {
            sb.append(" believe that its' " );
        }
        else if(operator==Operators.Type.KNOW)
        {
            sb.append(" am sure that its' " );
        }
        sb.append(trait1);
        sb.append((formula.getStates().get(0)== State.IS)? "is" : " is not ");
        sb.append(value1);
        sb.append(" or its' ");
        sb.append(trait2);
        sb.append((formula.getStates().get(1)== State.IS)? "is" : " is not ");
        sb.append(value2);

        return s;
    }

    private void setOperator()
    {
        // todo czekamy na holona
    }



}
