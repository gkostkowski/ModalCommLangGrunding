import java.util.List;
import java.util.Set;

import com.pwr.zpi.Agent;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.language.ComplexFormula.FormulaCase;

public class NonBinaryHolon extends Holon{
	
	protected List<Pair<ComplexFormula.FormulaCase,Double>> TaoList;
	protected Quadrilateral Tao;
	protected Formula formula;
	
	public NonBinaryHolon (Agent a,Set<BaseProfile> baseProfile,int time,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException{
		formula = dk.getFormula();
		update(a,baseProfile,time,dk);
	}
	
	public void update(Agent a,Set<BaseProfile> baseProfile,int time,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException{
		double update = Grounder.determineFulfillmentDouble(a, dk, formula);
		TaoList.add(new Pair<ComplexFormula.FormulaCase,Double>(((ComplexFormula) dk.getFormula()).getFormulaCase(),update));
	}
	
	public void forgetOldest(){
		//Uwazac na Concurrenta
		TaoList.remove(TaoList.get(TaoList.size()));
    }
	
	public void updateRatio(){
		Double[] temp = new Double[4];
		for(Pair<ComplexFormula.FormulaCase,Double> p:TaoList){
			if(p.Case==ComplexFormula.FormulaCase.PQ){temp[0] += p.Value;}
			else if(p.Case==ComplexFormula.FormulaCase.NPQ){temp[1] += p.Value;}
			else if(p.Case==ComplexFormula.FormulaCase.PNQ){temp[2] += p.Value;}
			else if(p.Case==ComplexFormula.FormulaCase.NPNQ){temp[3] += p.Value;}
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
	
	static class Quadrilateral {

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
}