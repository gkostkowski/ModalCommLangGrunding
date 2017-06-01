package com.pwr.zpi.holons;


import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.holons.context.Contextualisation;
import com.pwr.zpi.language.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents belief on simple formula.
 *
 */
public class BinaryHolon implements Holon {
    /**
     * Represents ratio of IS,Is_Not and Mayhaps observations
     */
    protected Pair<Double, Double> Tao;
    protected List<Formula> formula;

    public BinaryHolon(DistributedKnowledge dk, Contextualisation contextualisation) throws InvalidFormulaException, NotApplicableException {
        this.formula = dk.getComplementaryFormulas();
        Map<Formula, Set<BaseProfile>> contextualisedGroundedSets = contextualisation.performContextualisation(dk.mapOfGroundingSets());
        //update(contextualisedGroundedSets); //todo
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


    /**
     * Updates holon's tao including observations taken until Distributed knowledge's timestamp
     * @param dk Distributed knowledge for respective grounding sets related with certain formula.
     * @throws InvalidFormulaException
     * @throws NotApplicableException
     */
    public void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {

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
            //System.out.println(sumNegative + " " + sumPositive + " " + dk.getRelatedObservationsBase().getCompleteSize(dk.getTimestamp()));
            Tao = new Pair<Double, Double>(sumPositive,sumNegative);
        }
    }


    /**
     *
     * @return Complementary Formula regarding this specific Holon
     */
    public List<Formula> getFormula() {
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
     * @return Returns IS side of Tao
     */
    public double getP() {
        return Tao.getK();
    }

    /**
     *
     * @return Returns IS_NOT side of Tao
     */
    public double getnot_P() {
        return Tao.getV();
    }
    /*public com.pwr.zpi.language.Operators.Type getDominant(){
	return Tao.getP() > Tao.getnot_P() ? com.pwr.zpi.language.Operators.Type.KNOW :com.pwr.zpi.language.Operators.Type.NOT ;
	}*/

    /**
     *
     * @return Returns Pair of strongest value in Tao,first part of pair is true when IS side is stronger,otherwise false
     */
    @Override
    public com.pwr.zpi.language.Pair<Boolean, Double> getStrongest() {
        return Tao.getK() >= Tao.getV() ? (new Pair<Boolean, Double>(true, Tao.getK())) : (new Pair<Boolean, Double>(false, Tao.getV()));
    }
    /**
     *
     * @return Returns Pair of weakest value in Tao,first part of pair is true when IS side is stronger,otherwise false
     */
    @Override
    public com.pwr.zpi.language.Pair<Boolean, Double> getWeakest() {
        return Tao.getK() < Tao.getV() ? (new Pair<Boolean, Double>(true, Tao.getK())) : (new Pair<Boolean, Double>(false, Tao.getV()));
    }

    /**
     *
     * @return HolonKind,Binary in this situation.
     */
    @Override
    public HolonKind getKind() {
        return Holon.HolonKind.Binary;
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
    public Contextualisation getContextualisation() {
        return null; //todo
    }
}
