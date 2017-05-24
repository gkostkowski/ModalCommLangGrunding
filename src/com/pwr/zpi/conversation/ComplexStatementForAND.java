package com.pwr.zpi.conversation;

import com.pwr.zpi.Agent;
import com.pwr.zpi.holons.NonBinaryHolon;
import com.pwr.zpi.language.*;


public class ComplexStatementForAND extends Statement {

    ComplexFormula formula;
    NonBinaryHolon holon;

    private final double MIN_POS = 0.1;
    private final double MAX_POS = 0.6;
    private final double MIN_BEL = 0.65;
    private final double MAX_BEL = 0.9;

    double pq;
    double npq;
    double pnq;
    double npnq;

    NonBinaryHolon.FormulaCase formulaCase;

   public ComplexStatementForAND(ComplexFormula formula, Agent agent, int timestamp, String name)
   {
       this.formula = formula;
       this.agent = agent;
       holon = (NonBinaryHolon)agent.getHolons().getHolon(formula, timestamp);
       this.name = name;
       formulaCase = formula.getFormulaCase();
       getTaoRatio();
   }

   public ComplexStatementForAND(ComplexFormula complexFormula, String name, double pq, double npq, double pnq, double npnq)
   {
       this.formula = complexFormula;
       formulaCase = formula.getFormulaCase();
       this.name = name;
       this.pq = pq;
       this.npq = npq;
       this.pnq = pnq;
       this.npnq = npnq;
   }

   private void getTaoRatio()
   {
       pq = holon.getTao().getPQ();
       npq = holon.getTao().getNPQ();
       pnq = holon.getTao().getPNQ();
       npnq = holon.getTao().getNPNQ();
   }

   public String generateStatement()
   {
       String answer;
       if(formulaCase== NonBinaryHolon.FormulaCase.PQ)
           answer = generateString(pq);
       else if(formulaCase== NonBinaryHolon.FormulaCase.NPQ)
           answer = generateString(npq);
       else if(formulaCase== NonBinaryHolon.FormulaCase.PNQ)
           answer = generateString(pnq);
       else answer = generateString(npnq);
       return answer;
   }

   private String generateString(double given)
   {
       String answer;
       String valP = (formulaCase== NonBinaryHolon.FormulaCase.PQ
               || formulaCase == NonBinaryHolon.FormulaCase.PNQ)? "" : "not ";
       String valQ = (formulaCase== NonBinaryHolon.FormulaCase.PQ
               || formulaCase == NonBinaryHolon.FormulaCase.NPQ)? "" : "not ";
       answer = checkForCertainty(given, valP, valQ);
       if(answer==null)
           answer = checkOthersForCertainty();
       if(answer==null)
           answer = checkForBel(given, valP, valQ);
       if(answer==null)
           answer = checkForPos(given, valP, valQ);
       if(answer==null)
           answer = "Sorry, I do not know what to say about it";
       return answer;
   }

   private String checkForCertainty(double given, String valP, String valQ)
   {
       if(given == 1)
           return "Yes, I am sure that " + name + " is " + valP +
                   formula.getTraits().get(0).getName() + formula.getOperator()
                   + formula.getTraits().get(1).getName();
       return null;
   }

   private String checkOthersForCertainty()
   {
       String answer = "No, but I am sure that ";
       if(pq == 1)
           return answer + getSentenceForPQ();
       else if(npnq==1)
           return answer + getSentenceForNPNQ();
       else if(npq==1)
           return answer + getSentenceForNPQ();
       else if(pnq==1)
           return answer + getSentenceForPNQ();
       else return null;
   }

   private String checkForBel(double given, String valP, String valQ)
   {
       String answer;
       if(!withinBel(given)) return null;
       else answer = "Yes, I believe that " + name + " is " + valP +
               formula.getTraits().get(0).getName() + " and " + valQ
               + formula.getTraits().get(1).getName();
       if(checkIfRestIsInPos(formulaCase))
           return answer + ", but all other options are also a little bit possible";
       if(formulaCase== NonBinaryHolon.FormulaCase.PQ || formulaCase== NonBinaryHolon.FormulaCase.NPNQ)
       {
           if(withinPos(npq))
           {
               if(withinPos(pq)||withinPos(npnq))
                   return answer + ", but it is also possible that it is quite the opposite or " + getSentenceForNPQ();
               else if(withinPos(pnq))
                   return answer + ", but it is also possible that one of the traits " +
                           ((formulaCase== NonBinaryHolon.FormulaCase.NPNQ)? "actually occurs" : "does not occur");
               else return answer + ", but it is also possible that it is " + getSentenceForNPQ();
           }
           else if(withinPos(pnq))
           {
               if(withinPos(pq)||withinPos(npnq))
                   return answer + ", but it is also possible that it is quite the opposite or " + getSentenceForPNQ();
               else return answer + ", but it is also possible that " + getSentenceForPNQ();
           }
           else if(withinPos(npnq)||withinPos(pq))
               return answer + ", however it is also possible that it is quite the opposite";
       }
       else
       {
           if(withinPos(pq))
           {
               if(withinPos(npq) || withinPos(pnq))
                   return answer + ", but it is also possible that it is quite the opposite or " + getSentenceForPQ();
               else if(withinPos(npnq))
                   return answer + ", but it is also possible that " + getSentenceForNPNQ();
               else return answer + ", but it is also possible that " + getSentenceForPQ();
           }
           else if(withinPos(npnq))
           {
               if(withinPos(pnq) || withinPos(npq))
                   return answer + ", but it is also possible that it is quite the opposite or " + getSentenceForNPNQ();
               else return answer + " it is also possible that " + getSentenceForNPNQ();
           }
           else if(withinPos(npq)||withinPos(pnq))
               return answer + " it is also possible that it is quite the opposite";
       }
       return answer;
   }

   public String checkForPos(double given, String valP, String valQ)
   {
       String answer;
       if(!withinPos(given))
           return null;
       answer = "Yes, I think it is possible that " + name + " is "
               + valP + formula.getTraits().get(0).getName() + " and " + valQ + formula.getTraits().get(1).getName();
       if(withinBel(pq))
           return answer + ", but I rather believe " + getSentenceForPQ();
       else if(withinBel(npq))
           return answer + ", but I rather believe " + getSentenceForNPQ();
       else if(withinBel(pnq))
           return answer + ", but I rather believe " + getSentenceForPNQ();
       else if(withinBel(npnq))
           return answer + ", but I rather believe " + getSentenceForNPNQ();
       return answer;
   }

   private boolean checkIfRestIsInPos(NonBinaryHolon.FormulaCase formulaCase)
   {
       if(formulaCase== NonBinaryHolon.FormulaCase.PQ)
           return withinPos(npq) && withinPos(pnq) && withinPos(npnq);
       if(formulaCase== NonBinaryHolon.FormulaCase.NPQ)
           return withinPos(pq) && withinPos(pnq) && withinPos(npnq);
       if(formulaCase== NonBinaryHolon.FormulaCase.PNQ)
           return withinPos(pq) && withinPos(npq) && withinPos(npnq);
       else
           return withinPos(pq) && withinPos(npq) && withinPos(pnq);
   }


   private String getSentenceForPQ()
   {
       return "it is " + formula.getTraits().get(0).getName() + " and " + formula.getTraits().get(1).getName();
   }

   private String getSentenceForNPQ()
   {
       return "it is not " + formula.getTraits().get(0).getName() + " and " + formula.getTraits().get(1).getName();
   }

   private String getSentenceForPNQ()
   {
       return "it is " + formula.getTraits().get(0).getName() + " and not " + formula.getTraits().get(1).getName();
   }

    private String getSentenceForNPNQ()
    {
        return "it is not " + formula.getTraits().get(0).getName() + " and not " + formula.getTraits().get(1).getName();
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
