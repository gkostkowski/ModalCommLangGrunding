package com.pwr.zpi.language;


import com.pwr.zpi.Agent;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import java.util.*;
public class NonBinaryHolon extends Holon{

	protected Quadrilateral Tao;
	protected ComplexFormula formula;

	public NonBinaryHolon (DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        formula = (ComplexFormula) dk.getFormula();
        //Wywalić TaoList, ogarnąć BP
        //Enumik przeszedł tutaj,poprawić. Najlepiej jednak go wyjąć bo Weronika chce się nim bawić.

    }
	public void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException{
		if (dk.getFormula().getType() != Formula.Type.MODAL_CONJUNCTION) {
			throw new InvalidFormulaException();
		}else{
        double pq = 0;
        double npq = 0;
        double pnq = 0;
        double npnq = 0;

		List<Formula> temp = getComplementaryFormulasv2((ComplexFormula) dk.getFormula());

		pq = Grounder.determineFulfillmentDouble(dk,temp.get(0));
		npq = Grounder.determineFulfillmentDouble(dk,temp.get(1));
		pnq = Grounder.determineFulfillmentDouble(dk,temp.get(2));
		npnq = Grounder.determineFulfillmentDouble(dk,temp.get(3));

        if(pq!= 0){pq = pq/dk.getGroundingSet(dk.getFormula()).size();}
        if(npq!= 0){npq = npq/dk.getGroundingSet(dk.getFormula()).size();}
        if(pnq!= 0){pnq = pnq/dk.getGroundingSet(dk.getFormula()).size();}
        if(npnq!= 0){npnq = npnq/dk.getGroundingSet(dk.getFormula()).size();}
        Tao = new Quadrilateral(pq,npq,pnq,npnq);}
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

	@Override
	public boolean isApplicable(Formula f) throws InvalidFormulaException {
		if(f.getType() != Formula.Type.MODAL_CONJUNCTION){
			return false;
		}
		if(getComplementaryFormulasv2(formula).contains(f)){return true;}
		return false;
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
	    public Double getNPQ(){
	    	return NPQ;
	    }
	    public Double getPNQ(){
	    	return PNQ;
	    }
	    public Double getNPNQ(){
	    	return NPNQ;
	    }
	}
	public enum FormulaCase {
		PQ,
		PNQ,
		NPQ,
		NPNQ
	}

	public static List<Formula> getComplementaryFormulasv2(ComplexFormula f) throws InvalidFormulaException {
		List<Formula> res = new ArrayList<>();
		ComplexFormula pq = f.copy();pq.setpq();
		ComplexFormula npq = f.copy();npq.setpnq();
		ComplexFormula pnq = f.copy();pnq.setnpq();
		ComplexFormula npnq = f.copy();npnq.setnpnq();

		res.add(pq);
		res.add(pnq);
		res.add(npq);
		res.add(npnq);
		return res;
	}
}