package com.pwr.zpi.language;


import java.util.List;
import java.util.Set;

import com.pwr.zpi.Agent;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import java.util.ArrayList;


public class BinaryHolon extends Holon{

    protected List<Pair<Double,Double>> TaoList = new ArrayList<Pair<Double,Double>>();
    protected Pair<Double,Double> Tao;
    protected Formula formula;

    public BinaryHolon (Formula formula, Agent a, int time) throws InvalidFormulaException{
        this.formula = formula;
        update(formula,a.getKnowledgeBase().getBaseProfiles(time),time);
    }

    // Odświeżenie Holonu odbywa się w pewnym momencie czasu (Nocy).
    //  I.Wersja 1 , Bierzemy wszystkie BaseProfile które pamiętamy ,zbieramy profile dla formuły i wrzucamy do Groundera,Tao odzwierciedla przekonanie na temat formuły. Dopóki nie uznamy,że czas minął
    // korzystamy ze starego Tao,do momentu update'u. Nawet jeśli nie odzwierciedla ostatnich obserwacji.
    //  II. Wersja 2 Bierzemy BaseProfile od danego momentu w czasie ,aby nie bawić się w najstarsze.Reszta jak w I.
    //  III. Wersja 3  Bierzemy BaseProfile dla wszystkich czasów(Od czasu ostatniego update'a). Dla każdego momentu czasu jaki rozważamy bierzemy profile i wrzucamy do groundera.Tao odzwierciedla np. 10 ostatnich obserwacji
    //  na temat formuły .Dopóki nie postanowimy zadać kolejnej sesji odświeżania korzystamy ze starego Tao. Podczas kolejnego odświeżenia najstarsze wpisy są zapominane. Holon nie jest updatowany na żywo.

    // Zapominanie do zaimplementowania
    // Jednak potrzebujemy wszystkich profili bazowych.
    // TaoList można wyrzucić

    //Distributed Knowledge <- I z nich wyciągamy profile bazowe <- które tworzymy z Agenta + formulę i hurrej
    // W agencie można zrobić metodę ,która tworzy distributed Knowledge

    //Pamięć przedświadoma
    //Pamięć świadoma

    public void update (Formula f,Set<BaseProfile> baseProfile,int time) throws InvalidFormulaException{
        if(f.getType()!= Formula.Type.SIMPLE_MODALITY){throw new InvalidFormulaException();}
        else{
            double sumPositive = 0;
            double sumNegative = 0;
            for(BaseProfile bp:baseProfile){
                sumPositive += Grounder.relativePositiveCard(bp.getIMsDescribedByTrait(((SimpleFormula) f).getTrait()),bp.getIMsNotDescribedByTrait(((SimpleFormula) f).getTrait()) , time);
                sumNegative += Grounder.relativeNegativeCard(bp.getIMsDescribedByTrait(((SimpleFormula) f).getTrait()),bp.getIMsNotDescribedByTrait(((SimpleFormula) f).getTrait()) , time);
            }
            if(sumPositive!= 0){
                sumPositive=sumPositive/baseProfile.size();}
            if(sumNegative!= 0){
                sumNegative=sumNegative/baseProfile.size();}
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
        if(allP!=0 && allNot_P !=0){
        Tao = new Pair<Double,Double>(getCard(allP/TaoList.size(),allNot_P/TaoList.size()),getCard(allNot_P/TaoList.size(),allP/TaoList.size()));}
    }

    public double getCard(double first,double sec){
        return first/(sec+first);
    }
    public Formula getFormula(){
        return formula;
    }
    public double getP(){
        return Tao.getK();
    }
    public double getnot_P(){
        return Tao.getV();
    }
	/*public com.pwr.zpi.language.Operators.Type getDominant(){
	return Tao.getP() > Tao.getnot_P() ? com.pwr.zpi.language.Operators.Type.KNOW :com.pwr.zpi.language.Operators.Type.NOT ;
	}*/

    @Override
    public com.pwr.zpi.language.Pair<Boolean,Double> getStrongest() {
        return Tao.getK() >= Tao.getV() ? (new Pair<Boolean,Double>(true,Tao.getK())) : (new Pair<Boolean,Double>(false,Tao.getV()));
    }

    @Override
    public com.pwr.zpi.language.Pair<Boolean,Double> getWeakest() {
        return Tao.getK() < Tao.getV() ? (new Pair<Boolean,Double>(true,Tao.getK())) : (new Pair<Boolean,Double>(false,Tao.getV()));
    }

    @Override
    public HolonKind getKind() {
        return Holon.HolonKind.Binary;
    }
}
