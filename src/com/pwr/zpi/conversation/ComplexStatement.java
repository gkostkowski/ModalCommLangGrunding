package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.State;
import com.pwr.zpi.HolonCollection;
import com.pwr.zpi.language.*;
import sun.plugin.com.event.COMEventHandler;

import java.util.List;

public class ComplexStatement extends Statement {

    ComplexFormula formula;
    NonBinaryHolon holon;

    private final double MIN_POS = Grounder.MIN_POS;
    private final double MAX_POS = Grounder.MAX_POS;
    private final double MIN_BEL = Grounder.MIN_BEL;
    private final double MAX_BEL = Grounder.MAX_BEL;

   public ComplexStatement(ComplexFormula formula, Agent agent, int timestamp, String name)
   {
       this.formula = formula;
       this.agent = agent;
       holon = (NonBinaryHolon)agent.getHolons().getHolon(formula);
       if(holon==null)
           agent.getHolons().addHolon(formula, agent, timestamp);
       this.name = name;
   }

   public String generateStatement()
   {
       double pq = holon.getTao().getPQ();
       double npq = holon.getTao().getNPQ();
       double pnq = holon.getTao().getPNQ();
       double npnq = holon.getTao().getNPNQ();

       ComplexFormula.FormulaCase fCase = formula.getFormulaCase();

       return null;
   }


   private String sentence(ComplexFormula.FormulaCase fcase, double given, double pq, double npq, double pnq, double npnq)
   {
       if(fcase==ComplexFormula.FormulaCase.PQ)


       return null;
   }

   private String sentenceForPQ(ComplexFormula.FormulaCase fcase, double pq, double npq, double pnq, double npnq) {
       String valP = "not ", valQ = "not ", start;
       if (fcase == ComplexFormula.FormulaCase.PQ ||
               fcase == ComplexFormula.FormulaCase.PNQ)
           valP = "";
       if (fcase == ComplexFormula.FormulaCase.NPQ ||
               fcase == ComplexFormula.FormulaCase.PQ)
           valQ = "";
       String answer;
       if (pq == 1)
           return "I am sure that " + name + " is " + valP +
                   formula.getTraits().get(0).getName() + formula.getOperator()
                   + formula.getTraits().get(1).getName();
       else if (npnq == 1)
           return "I am sure that " + name + " is neither " +
                   formula.getTraits().get(0).getName() + formula.getOperator() + " nor "
                   + formula.getTraits().get(1).getName();
       else if (withinBel(pq)) {
           if (fcase == ComplexFormula.FormulaCase.PQ)
               start = "Yes, ";
           else start = "No, ";
           if (!withinPos(npq) && !withinPos(pnq) && withinPos(npnq))
               return start + "I believe that " + name + " is " +
                       formula.getTraits().get(0).getName() + formula.getOperator()
                       + formula.getTraits().get(1).getName();
           else if (withinBel(pq) && withinPos(npq))
               return start + "I believe that " + name + " is " +
                       formula.getTraits().get(0).getName() + formula.getOperator()
                       + formula.getTraits().get(1).getName() + ", but it might not be"
                       + formula.getTraits().get(0).getName();
           else if (withinBel(pq) && withinPos(pnq))
               return start + "I believe that " + name + " is " +
                       formula.getTraits().get(0).getName() + formula.getOperator()
                       + formula.getTraits().get(1).getName() + ", but it might not be"
                       + formula.getTraits().get(1).getName();
           else if (withinBel(pq) && withinPos(npnq))
               return start + "I believe that " + name + " is " +
                       formula.getTraits().get(0).getName() + formula.getOperator()
                       + formula.getTraits().get(1).getName() + ", but it might be other way";

       }
   }

/*


       else if(withinBel(pq) && !withinPos(npq) && !withinPos(pnq) &&withinPos(npnq))
           return "I believe that " + name + " is " +
                   formula.getValuedTraits().get(0).getName() + formula.getOperator()
                   + formula.getValuedTraits().get(1).getName();
       else if(withinBel(pq) && withinPos(npq)) {
           if (fcase == ComplexFormula.FormulaCase.PQ)
               start = "Yes, ";
           else start = "No, ";
           return start + "I believe that " + name + " is " +
                   formula.getValuedTraits().get(0).getName() + formula.getOperator()
                   + formula.getValuedTraits().get(1).getName() + ", but it might not be"
                   + formula.getValuedTraits().get(0).getName();
       }
       else if(withinBel(pq) && withinPos(pnq))
           return "I believe that " + name + " is " +
                   formula.getValuedTraits().get(0).getName() + formula.getOperator()
                   + formula.getValuedTraits().get(1).getName() + ", but it might not be"
                   + formula.getValuedTraits().get(1).getName();
       else if(withinBel(pq) && withinPos(npnq))
           return "I believe that " + name + " is " +
                   formula.getValuedTraits().get(0).getName() + formula.getOperator()
                   + formula.getValuedTraits().get(1).getName() + ", but it might be any other way";
       else if(withinPos(pq) && withinPos(npq) && withinPos(pnq) && withinPos(npnq))
           return "Well, it might " + valP + "be " + formula.getValuedTraits().get(0).getName() + formula.getOperator() + valQ
                   + formula.getValuedTraits().get(1).getName() + ", but I would not exclude other options";
       else if()
   }
*/

   

   private boolean withinBel(double p)
    {
        return p>=MIN_BEL && p<MAX_BEL;
    }

   private boolean withinPos(double p)
    {
        return p>=MIN_POS && p<MAX_POS;
    }



}
