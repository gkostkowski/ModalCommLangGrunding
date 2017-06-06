package com.pwr.zpi.holons;


import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.holons.ContextJar.DistanceFunction;
import com.pwr.zpi.holons.ContextJar.DistanceFunctions.DistanceFunction1;
import com.pwr.zpi.holons.context.CompositeContext;
import com.pwr.zpi.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents belief on simple formula.
 *
 */
public class BinaryHolon implements Holon,Comparable<BinaryHolon> {
    /**
     * Represents ratio of IS,Is_Not and Mayhaps observations
     */
    protected Pair<Double, Double> Tao;
    protected List<Formula> formula;
    protected  Map<Formula, Set<BaseProfile>> currContext;
    protected Contextualisation context;
    protected int timestamp;

    public BinaryHolon(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        this.formula = dk.getComplementaryFormulas();
        DistanceFunction f1 = new DistanceFunction1();
        Contextualisation cj = new CompositeContext(f1,3);

        cj.setMaxThreshold(8);
        context = cj;
        timestamp = dk.getTimestamp();
        update(dk);
    }


    /**
     * Updates holon's tao including observations taken until Distributed knowledge's timestamp
     * @param dk Distributed knowledge for respective grounding sets related with certain formula.
     * @throws InvalidFormulaException
     * @throws NotApplicableException
     */
    public boolean update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {


        if (dk.getFormula().getType() != Formula.Type.SIMPLE_MODALITY) {
            throw new InvalidFormulaException();
        } else {
            double sumPositive = 0;
            double sumNegative = 0;
            if (((SimpleFormula) dk.getComplementaryFormulas().get(0)).isNegated()) {
                currContext = context.performContextualisation(dk.mapOfGroundingSets());
                sumPositive += Grounder.determineFulfillmentDouble(dk, dk.getComplementaryFormulas().get(0),currContext);
                sumNegative += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(1),currContext);
            } else {
                currContext = context.performContextualisation(dk.mapOfGroundingSets());
                sumNegative += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(1),currContext);
                sumPositive += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(0),currContext);
            }
            System.out.println(sumNegative + " " + sumPositive + " " + dk.getRelatedObservationsBase().getCompleteSize(dk.getTimestamp()));
            Tao = new Pair<Double, Double>(sumPositive,sumNegative);
        }
        return true;
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
     * Returns map of summaries only for given Formulas.
     *
     * @param selectedFormulas
     */
    @Override
    public Map<Formula, Double> getSummaries(List<Formula> selectedFormulas) {
        throw new NotImplementedException();
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
    public Integer getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(BinaryHolon o) {
        double res=0, res2=0;
        for (Formula f:formula) {
            res = f.hashCode() ;
        }

        for (Formula f:o.getFormula()) {
            res2 = f.hashCode() ;
        }
        return res > res2 ? 1: (res < res2 ? -1 : 0);}
    public Contextualisation getContextualisation() {
        return context;
    }
}
