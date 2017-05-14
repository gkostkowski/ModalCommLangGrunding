package com.pwr.zpi.language;


import com.pwr.zpi.Agent;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import java.util.*;
public class NonBinaryHolon extends Holon{

	protected Quadrilateral Tao;
	protected Formula formula;

	public NonBinaryHolon (Agent a,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        formula = dk.getFormula();
        //Wywalić TaoList, ogarnąć BP
        //Enumik przeszedł tutaj,poprawić. Najlepiej jednak go wyjąć bo Weronika chce się nim bawić.

    }
	public void update(Agent a,Set<BaseProfile> baseProfile,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException{
		double update = Grounder.determineFulfillmentDouble(dk, formula);
		//Przeprowadzic dla wszystko PQ,PNQ,NPQ,NPNQ,najlepiej dla komplementarnej formuły
        double pq = 0;
        double npq = 0;
        double pnq = 0;
        double npnq = 0;

            ((ComplexFormula) dk.getFormula()).setpq();
            pq += Grounder.determineFulfillmentDouble(dk,dk.getFormula());
            ((ComplexFormula) dk.getFormula()).setnpq();
            npq += Grounder.determineFulfillmentDouble(dk,dk.getFormula());
            ((ComplexFormula) dk.getFormula()).setpnq();
            pnq += Grounder.determineFulfillmentDouble(dk,dk.getFormula());
            ((ComplexFormula) dk.getFormula()).setnpnq();
            npnq += Grounder.determineFulfillmentDouble(dk,dk.getFormula());

        if(pq!= 0){pq = pq/dk.getGroundingSet(dk.getFormula()).size();}
        if(npq!= 0){npq = npq/dk.getGroundingSet(dk.getFormula()).size();}
        if(pnq!= 0){pnq = pnq/dk.getGroundingSet(dk.getFormula()).size();}
        if(npnq!= 0){npnq = npnq/dk.getGroundingSet(dk.getFormula()).size();}
        Tao = new Quadrilateral(pq,npq,pnq,npnq);
	}

	@Override
	public Pair<FormulaCase,Double> getStrongest() {
		return Tao.getStrongest();
	}

	@Override
	public Pair<FormulaCase,Double> getWeakest() {
		return Tao.getWeakest();
	}

	@Override
	public HolonKind getKind() {
		return Holon.HolonKind.Non_Binary;
	}

	public Formula getFormula(){
		return formula;
	}

	public Quadrilateral getTao() {
		return Tao;
	}

	public static class Quadrilateral {

	    private Double PQ;
	    private Double NPQ;
	    private Double PNQ;
	    private Double NPNQ;

	    public static  Quadrilateral createQuadrilateral(Double element0, Double element1,Double element2,Double element3) {
	        return new Quadrilateral(element0, element1,element2,element3);
	    }

	    public Quadrilateral(Double element0, Double element1,Double element2,Double element3) {
	        this.PQ = element0;
	        this.NPQ = element1;
	        this.PNQ = element2;
	        this.NPNQ = element3;
	    }
	    public Pair<FormulaCase,Double> getStrongest(){
	    	if(PQ>NPQ&&PQ>PNQ&&PQ>NPNQ){return new Pair<FormulaCase,Double>(FormulaCase.PQ,PQ);}
	    	else if(NPQ>=PQ&&NPQ>=PNQ&&NPQ>=NPNQ){return new Pair<FormulaCase,Double>(FormulaCase.NPQ,NPQ);}
	    	else if(PNQ>=NPQ&&PNQ>=PQ&&PNQ>=NPNQ){return new Pair<FormulaCase,Double>(FormulaCase.PNQ,PNQ);}
	    	else {return new Pair<FormulaCase,Double>(FormulaCase.NPNQ,NPNQ);}
	    }
	    public Pair<FormulaCase,Double> getWeakest(){
	    	if(PQ<NPQ&&PQ<PNQ&&PQ<NPNQ){return new Pair<FormulaCase,Double>(FormulaCase.PQ,PQ);}
	    	else if(NPQ<=PQ&&NPQ<=PNQ&&NPQ<=NPNQ){return new Pair<FormulaCase,Double>(FormulaCase.NPQ,NPQ);}
	    	else if(PNQ<=NPQ&&PNQ<=PQ&&PNQ<=NPNQ){return new Pair<FormulaCase,Double>(FormulaCase.PNQ,PNQ);}
	    	else {return new Pair<FormulaCase,Double>(FormulaCase.NPNQ,NPNQ);}
	    }
	    
	    public Double getPQ(){
	    	return PQ;
	    }
	    public void setPQ(Double var){
	    	PQ = var;
	    }
	    public Double getNPQ(){
	    	return NPQ;
	    }
	    public void setNPQ(Double var){
	    	NPQ = var;
	    }
	    public Double getPNQ(){
	    	return PNQ;
	    }
	    public void setPNQ(Double var){
	    	PNQ = var;
	    }
	    public Double getNPNQ(){
	    	return NPNQ;
	    }
	    public void setNPNQ(Double var){
	    	NPNQ = var;
	    }
	}
	public enum FormulaCase {
		PQ,
		PNQ,
		NPQ,
		NPNQ
	}
}