package com.pwr.zpi.language;


import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;

public class BinaryHolon extends Holon {

    protected Pair<Double, Double> Tao;
    protected Formula formula;

    public BinaryHolon(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        this.formula = dk.getFormula();
        update(dk);
    }

    // Odświeżenie Holonu odbywa się w pewnym momencie czasu (Nocy).
    //  I.Wersja 1 , Bierzemy wszystkie BaseProfile które pamiętamy ,zbieramy profile dla formuły i wrzucamy do Groundera,Tao odzwierciedla przekonanie na temat formuły. Dopóki nie uznamy,że czas minął
    // korzystamy ze starego Tao,do momentu update'u. Nawet jeśli nie odzwierciedla ostatnich obserwacji.
    //  II. Wersja 2 Bierzemy BaseProfile od danego momentu w czasie ,aby nie bawić się w najstarsze.Reszta jak w I.
    //  III. Wersja 3  Bierzemy BaseProfile dla wszystkich czasów(Od czasu ostatniego update'a). Dla każdego momentu czasu jaki rozważamy bierzemy profile i wrzucamy do groundera.Tao odzwierciedla np. 10 ostatnich obserwacji
    //  na temat formuły .Dopóki nie postanowimy zadać kolejnej sesji odświeżania korzystamy ze starego Tao. Podczas kolejnego odświeżenia najstarsze wpisy są zapominane. Holon nie jest updatowany na żywo.

    // Zapominanie do zaimplementowania
    // Jednak potrzebujemy wszystkich profili bazowych. ++
    // TaoList można wyrzucić

    //Distributed Knowledge <- I z nich wyciągamy profile bazowe <- które tworzymy z Agenta + formulę i hurrej
    // W agencie można zrobić metodę ,która tworzy distributed Knowledge

    //Pamięć przedświadoma
    //Pamięć świadoma

    public void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        System.out.println("//      HOLON       //");
        if (dk.getFormula().getType() != Formula.Type.SIMPLE_MODALITY) {
            throw new InvalidFormulaException();
        } else {
            double sumPositive = 0;
            double sumNegative = 0;
            if (((SimpleFormula) dk.getComplementaryFormulas().get(0)).isNegated()) {
                sumPositive += Grounder.determineFulfillmentDouble(dk, dk.getComplementaryFormulas().get(0));
                sumNegative += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(1));
            } else {
                sumNegative += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(1));
                sumPositive += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(0));
            }
            System.out.println(sumNegative + " " + sumPositive + " "  +  dk.getGroundingSet(dk.getFormula()).size());
            if (sumPositive != 0 && dk.getGroundingSet(dk.getFormula()).size() > 0) {
                sumPositive = sumPositive / dk.getGroundingSet(dk.getFormula()).size();
            }
            if (sumNegative !=0 && dk.getGroundingSet(dk.getFormula()).size() > 0) {
                sumNegative = sumNegative / dk.getGroundingSet(dk.getFormula()).size();
            }

                Tao = new Pair<Double, Double>(getCard(sumPositive / dk.getGroundingSet(dk.getFormula()).size(),
                        sumNegative / dk.getGroundingSet(dk.getFormula()).size()),
                        getCard(sumNegative / dk.getGroundingSet(dk.getFormula()).size(),
                                sumPositive / dk.getGroundingSet(dk.getFormula()).size()));
        }
    }


    public double getCard(double first, double sec) {
        return first / (sec + first);
    }

    public Formula getFormula() {
        return formula;
    }

    @Override
    public boolean isApplicable(Formula f) throws InvalidFormulaException {
        if(f.getType() != Formula.Type.SIMPLE_MODALITY){
            return false;
        }
        if(formula.getComplementaryFormulas().contains(formula)){return true;}
        return false;
    }

    public double getP() {
        return Tao.getK();
    }


    public double getnot_P() {
        return Tao.getV();
    }
    /*public com.pwr.zpi.language.Operators.Type getDominant(){
	return Tao.getP() > Tao.getnot_P() ? com.pwr.zpi.language.Operators.Type.KNOW :com.pwr.zpi.language.Operators.Type.NOT ;
	}*/

    @Override
    public com.pwr.zpi.language.Pair<Boolean, Double> getStrongest() {
        return Tao.getK() >= Tao.getV() ? (new Pair<Boolean, Double>(true, Tao.getK())) : (new Pair<Boolean, Double>(false, Tao.getV()));
    }

    @Override
    public com.pwr.zpi.language.Pair<Boolean, Double> getWeakest() {
        return Tao.getK() < Tao.getV() ? (new Pair<Boolean, Double>(true, Tao.getK())) : (new Pair<Boolean, Double>(false, Tao.getV()));
    }

    @Override
    public HolonKind getKind() {
        return Holon.HolonKind.Binary;
    }
}
