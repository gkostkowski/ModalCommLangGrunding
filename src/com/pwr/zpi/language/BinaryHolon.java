package com.pwr.zpi.language;


import java.util.List;
import java.util.Set;

import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;

public class BinaryHolon extends Holon{

    protected List<Pair<Double,Double>> TaoList;
    protected Pair<Double,Double> Tao;
    protected Formula formula;

    public BinaryHolon (Formula formula,Agent a,int time) throws InvalidFormulaException{
        this.formula = formula;
        update(formula,a.getKnowledgeBase().getBaseProfiles(time),time);
    }

    public void update (Formula f,Set<BaseProfile> baseProfile,int time) throws InvalidFormulaException{
        if(f.getType()!= Formula.Type.SIMPLE_MODALITY){throw new InvalidFormulaException();}
        else{
            double sumPositive = 0;
            double sumNegative = 0;
            for(BaseProfile bp:baseProfile){
                sumPositive += Grounder.relativePositiveCard(bp.getDescribedByTrait(((SimpleFormula) f).getTrait()),bp.getNotDescribedByTrait(((SimpleFormula) f).getTrait()) , time);
                sumNegative += Grounder.relativeNegativeCard(bp.getDescribedByTrait(((SimpleFormula) f).getTrait()),bp.getNotDescribedByTrait(((SimpleFormula) f).getTrait()) , time);
            }
            sumPositive=sumPositive/baseProfile.size();
            sumNegative=sumNegative/baseProfile.size();
            TaoList.add(new Pair<Double,Double>(sumPositive,sumNegative));
            updateRatio();
        }
    }
    public void forgetOldest(){
        TaoList.remove(TaoList.get(TaoList.size()));
        updateRatio();
    }

    public void updateRatio(){
        Double allP = 0.0,allNot_P = 0.0;
        for (Pair<Double,Double> p:TaoList){
            allP += p.Case;
            allNot_P += p.Value;
        }
        Tao = new Pair<Double,Double>(getCard(allP/TaoList.size(),allNot_P/TaoList.size()),getCard(allNot_P/TaoList.size(),allP/TaoList.size()));
    }

    public double getCard(double first,double sec){
        return first/(sec+first);
    }
    public Formula getFormula(){
        return formula;
    }
    public double getP(){
        return Tao.getP();
    }
    public double getnot_P(){
        return Tao.getnot_P();
    }
	/*public com.pwr.zpi.language.Operators.Type getDominant(){
	return Tao.getP() > Tao.getnot_P() ? com.pwr.zpi.language.Operators.Type.KNOW :com.pwr.zpi.language.Operators.Type.NOT ;
	}*/

    @Override
    public com.pwr.zpi.language.Pair<Boolean,Double> getStrongest() {
        return Tao.getP() >= Tao.getnot_P() ? (new Pair<Boolean,Double>(true,Tao.getP())) : (new Pair<Boolean,Double>(false,Tao.getnot_P()));
    }

    @Override
    public com.pwr.zpi.language.Pair<Boolean,Double> getWeakest() {
        return Tao.getP() < Tao.getnot_P() ? (new Pair<Boolean,Double>(true,Tao.getP())) : (new Pair<Boolean,Double>(false,Tao.getnot_P()));
    }

    @Override
    public HolonKind getKind() {
        return Holon.HolonKind.Binary;
    }
}
