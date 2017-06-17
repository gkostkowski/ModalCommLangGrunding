package com.pwr.zpi.core.holons;


import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.core.episodic.DistributedKnowledge;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.core.holons.ContextJar.DistanceFunction;
import com.pwr.zpi.core.holons.ContextJar.DistanceFunctions.DistanceFunction1;
import com.pwr.zpi.core.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.*;
import com.pwr.zpi.util.Pair;

import java.util.HashMap;
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
    protected  Map<Formula, Set<BaseProfile>> currContext;
    protected Contextualisation context;
    protected int timestamp;
    protected DistributedKnowledge dk;

    public BinaryHolon(DistributedKnowledge dk, Contextualisation contextualisation) throws InvalidFormulaException, NotApplicableException {
        this.formula = dk.getComplementaryFormulas();
        DistanceFunction f1 = new DistanceFunction1();
        //Contextualisation cj = new CompositeContext(f1,3);
        //cj.setMaxThreshold(8);
        context = contextualisation;
        timestamp = dk.getTimestamp();
        this.dk = dk;
        update(dk);
    }


    /**
     * Updates holon's tao including observations taken until Distributed knowledge's timestamp
     * @param dk Distributed knowledge for respective grounding sets related with certain formula.
     * @throws InvalidFormulaException
     * @throws NotApplicableException
     */
    public boolean update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {

        this.dk=dk;
        if (dk.getFormula().getType() != Formula.Type.SIMPLE_MODALITY) {
            throw new InvalidFormulaException();
        } else {
            double sumPositive = 0;
            double sumNegative = 0;
            if (((SimpleFormula) dk.getComplementaryFormulas().get(0)).isNegated()) {
                if(currContext != null){
                currContext = context.performContextualisation(dk.mapOfGroundingSets());}
                sumPositive += Grounder.determineFulfillmentDouble(dk, dk.getComplementaryFormulas().get(0),currContext);
                sumNegative += Grounder.determineFulfillmentDouble(dk,  dk.getComplementaryFormulas().get(1),currContext);
            } else {
                if(currContext != null){
                currContext = context.performContextualisation(dk.mapOfGroundingSets());}
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
    public Pair<Boolean, Double> getStrongest() {
        return Tao.getK() >= Tao.getV() ? (new Pair<Boolean, Double>(true, Tao.getK())) : (new Pair<Boolean, Double>(false, Tao.getV()));
    }
    /**
     *
     * @return Returns Pair of weakest value in Tao,first part of pair is true when IS side is stronger,otherwise false
     */
    @Override
    public Pair<Boolean, Double> getWeakest() {
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
        update(dk);
    }

    @Override
    public Double getSummary(Formula formula) {
        if(((SimpleFormula)formula).isNegated()){
            return Tao.getV();
        }
        else{
            return Tao.getK();
        }
    }

    @Override
    public Map<Formula, Double> getSummaries()  {
        Map<Formula, Double> out = new HashMap<Formula, Double>();

            if(((SimpleFormula)formula.get(0)).isNegated()) {
                out.put(formula.get(0),Tao.getV() );
                out.put(formula.get(1),Tao.getK() );
            }
            else {
                out.put(formula.get(0), Tao.getK());
                out.put(formula.get(1), Tao.getV());
            }
        return out;
    }

    /**
     * Returns map of summaries only for given Formulas.
     *
     * @param selectedFormulas
     */
    @Override
    public Map<Formula, Double> getSummaries(List<Formula> selectedFormulas) {
        if(selectedFormulas.size()==1){
            HashMap<Formula, Double> out = new HashMap<Formula, Double>();
            out.put(selectedFormulas.get(0),getSummary(selectedFormulas.get(0)));
            return out;
        }
        else{
            return getSummaries();
        }
    }

    /**
     * Returns list of complementary formulas which were used when building this holon.
     */
    @Override
    public List<Formula> getAffectedFormulas() {
        return formula;
    }

    /**
     * Returns context which was used to build grounding sets for this holon.
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    public Contextualisation getContextualisation() {
        return context;
    }

/*
    public int compareTo(Holon o) {
        double res=0, res2=0;
        for (Formula f:formula) {
            res = f.hashCode() ;
        }

        for (Formula f:o.getFormula()) {
            res2 = f.hashCode() ;
        }
        return res > res2 ? 1: (res < res2 ? -1 : 0);
    }*/
}
