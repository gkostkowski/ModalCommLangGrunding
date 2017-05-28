package com.pwr.zpi.holons;


import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.language.*;

import java.util.*;
public class NonBinaryHolon implements Holon{

	protected Quadrilateral Tao;
	protected static List<Formula> formula;

	public NonBinaryHolon (DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        formula = dk.getComplementaryFormulas();
        //Wywalić TaoList, ogarnąć BP
        //Enumik przeszedł tutaj,poprawić. Najlepiej jednak go wyjąć bo Weronika chce się nim bawić.
        update(dk);
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
        Tao = new Quadrilateral(pq,npq,pnq,npnq);}
	}
	/**
	 *
	 * @return Returns Pair of strongest value in Tao,first part of pair is true when IS side is stronger,otherwise false
	 */
	@Override
	public Pair<FormulaCase,Double> getStrongest() {
		return Tao.getStrongest();
	}
	/**
	 *
	 * @return Returns Pair of weakest value in Tao,first part of pair is true when IS side is stronger,otherwise false
	 */
	@Override
	public Pair<FormulaCase,Double> getWeakest() {
		return Tao.getWeakest();
	}
	/**
	 *
	 * @return HolonKind,Non_Binary in this situation.
	 */
	@Override
	public HolonKind getKind() {
		return Holon.HolonKind.Non_Binary;
	}
	/**
	 *
	 * @return Complementary Formula regarding this specific Holon
	 */
	public List<Formula> getFormula(){
		return formula;
	}


	/**
	 *  Checks if given formula is one of complementary formulas of this Holon.
	 * @param f
	 * @return
	 * @throws InvalidFormulaException
	 */
	@Override
	public boolean isApplicable(Formula f) throws InvalidFormulaException {
		return formula.contains(f);
	}

	/**
	 *
	 * @return Quadrilateral containing all cases.
	 */
	public Quadrilateral getTao() {
		return Tao;
	}

	/**
	 * Inner class containing Tao for four variables.
	 */
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

	@Override
	public void update() throws InvalidFormulaException, NotApplicableException {

	}

	@Override
	public Double getSummary(Formula formula) {
		return null;
	}

	@Override
	public Map<Formula, Double> getSummaries() {
		return null;
	}

	/**
	 * Returns list of complementary formulas which were used when building this holon.
	 */
	@Override
	public List<Formula> getAffectedFormulas() {
		return null;
	}

	/**
	 * Returns context which was used to build grounding sets for this holon.
	 */
	@Override
	public Context getContext() {
		return null;
	}
}