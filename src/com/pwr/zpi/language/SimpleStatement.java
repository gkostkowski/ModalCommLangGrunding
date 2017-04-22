package com.pwr.zpi.language;

import com.pwr.zpi.HolonCollection;

public class SimpleStatement extends Statement{

    private Operators.Type levelOfCertainty;


    public SimpleStatement(NonBinaryHolon holon, SimpleFormula formula)
    {
        this.formula = formula;
        holon = HolonCollection.findNonBinaryHolon(
                formula.getObservation(), formula.getTraits().get(0));
    }

    public String generateStatement()
    {
        String answer = "I";
        StringBuilder sb = new StringBuilder(answer);
        if(holon == null)
            sb.append(" do not know about this object, sorry");
        else
        {
            if(levelOfCertainty==Operators.Type.Not_Know)
            {
                sb.append(" know that its' ");
                sb.append(formula.getTraits().get(0).getName().toString());
                sb.append((((SimpleFormula)formula).isNegated==true)? "is" : " is not ");
                sb.append(formula.getTraits().get(0).getValue().toString());
            }
            if(levelOfCertainty==Operators.Type.POS) {
                sb.append(" think it is possible that its' ");
                sb.append(formula.getTraits().get(0).getName().toString());
                sb.append((((SimpleFormula) formula).isNegated == false) ? "is" : " is not ");
                sb.append(formula.getTraits().get(0).getValue().toString());
            }
            if(levelOfCertainty==Operators.Type.BEL)
            {
                sb.append(" believe that its' ");
                sb.append(formula.getTraits().get(0).getName().toString());
                sb.append((((SimpleFormula)formula).isNegated==false)? "is" : " is not ");
                sb.append(formula.getTraits().get(0).getValue().toString());
            }
            if(levelOfCertainty==Operators.Type.KNOW)
            {
                sb.append("know that its' ");
                sb.append(formula.getTraits().get(0).getName().toString());
                sb.append((((SimpleFormula)formula).isNegated==false)? "is" : " is not ");
                sb.append(formula.getTraits().get(0).getValue().toString());
            }
        }
        return answer;
    }



    private void getLevelOfCertaintity()
    {
        // todo holon ma zwracać operator dla cechy

    }





}
