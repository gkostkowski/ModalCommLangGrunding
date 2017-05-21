package com.pwr.zpi.language;

import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Grzesiek on 2017-05-19.
 */
public class NewNonBinaryHolon implements Holon{
    /**
     * Note: formula is given in standard form, namely: without any negations.
     */
    Formula relatedFormula;
    Map<Formula, Double> summaries;
    DistributedKnowledge dk;

    NewNonBinaryHolon(Formula formula, DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        if (formula != null && !dk.isRelated(formula))
            throw new IllegalStateException("Given formula is not related to specified knowledge distribution.");
        relatedFormula = formula != null ? formula : dk.getFormula();
        this.dk = dk;
        summaries = new HashMap<>();
        update();
    }

    /**
     * Creates holon for formula associated with specified knowledge distribution.
     *
     * @param dk
     * @throws InvalidFormulaException
     */
    public NewNonBinaryHolon(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        this(null, dk);
    }

    /**
     * Builds holon for specified formula.
     */
    @Override
    public void update() throws InvalidFormulaException, NotApplicableException {
        List<Formula> complFormulas = relatedFormula.getComplementaryFormulas();
        for (Formula f : complFormulas) {
            summaries.put(f,
                    Grounder.relativeCard(dk, f));
        }

    }

    public Formula getRelatedFormula() {
        return relatedFormula;
    }

    @Override
    public Map<Formula, Double> getSummaries() {
        return summaries;
    }

    @Override
    public Double getSummary(Formula formula) {
        return summaries.get(formula);
    }

    @Override
    public HolonKind getKind() {
        return HolonKind.Non_Binary;
    }

    /**
     * Returns list of complementary formulas which were used when building this holon.
     */
    @Override
    public List<Formula> getAffectedFormulas() {
        return new ArrayList<>(summaries.keySet());
    }

    /**
     * Checks if this holon is related to given formula.
     *
     * @param formula
     * @return
     * @throws InvalidFormulaException
     */
    @Override
    public boolean isApplicable(Formula formula) throws InvalidFormulaException {
        return getAffectedFormulas().contains(formula);
    }

    @Override
    public String toString() {
        return "NewNonBinaryHolon{" +
                "summaries=" + summaries +
                '}';
    }

    @Override
    public void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {

    }

    @Override
    public Pair getStrongest() {
        return null;
    }

    @Override
    public Pair getWeakest() {
        return null;
    }

    @Override
    public List<Formula> getFormula() {
        return null;
    }
}
