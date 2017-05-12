package com.pwr.zpi.language;


import com.pwr.zpi.Agent;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import java.util.*;
public class NonBinaryHolon extends Holon{

	protected List<Pair<FormulaCase,Double>> TaoList;
	protected Quadrilateral Tao;
	protected Formula formula;

	public NonBinaryHolon (Agent a,int time) throws InvalidFormulaException, NotApplicableException {
		formula = null;//todo dk.getFormula();
		//Wywalić TaoList, ogarnąć BP
		//Enumik przeszedł tutaj,poprawić. Najlepiej jednak go wyjąć bo Weronika chce się nim bawić.

	}
	public NonBinaryHolon (Agent a,int time,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException{
		formula = dk.getFormula();
		update(a,a.getKnowledgeBase().getBaseProfiles(time),time,dk);
	}
	
	public void update(Agent a,Set<BaseProfile> baseProfile,int time,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException{
		double update = 0.0;//Grounder.determineFulfillmentDouble(a, dk, formula);
		//Przeprowadzic dla wszystko PQ,PNQ,NPQ,NPNQ,najlepiej dla komplementarnej formuły
		TaoList.add(new Pair<FormulaCase,Double>(((ComplexFormula) dk.getFormula()).getFormulaCase(),update));
	}
	
	public void forgetOldest(){
		//Uwazac na Concurrenta
		TaoList.remove(TaoList.get(TaoList.size()));
    }
	
	public void updateRatio(){
		Double[] temp = new Double[4];
		for(Pair<FormulaCase,Double> p:TaoList){
			if(p.Case==FormulaCase.PQ){temp[0] += p.Value;}
			else if(p.Case==FormulaCase.NPQ){temp[1] += p.Value;}
			else if(p.Case==FormulaCase.PNQ){temp[2] += p.Value;}
			else if(p.Case==FormulaCase.NPNQ){temp[3] += p.Value;}
		}
		for(int i =0;i<=temp.length;i++){temp[i] = temp[i]/TaoList.size();}
		Tao = new Quadrilateral(temp[0],temp[1],temp[2],temp[3]);
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
	enum FormulaCase {
		PQ,
		PNQ,
		NPQ,
		NPNQ
	}
}