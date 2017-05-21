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
public class NewNonBinaryHolon implements NewHolon{
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
        summaries = new HashMap<>();
        update();
    }

    /**
     * Creates holon for formula associated with specified knowledge distribution.
     *
     * @param dk
     * @throws InvalidFormulaException
     */
    NewNonBinaryHolon(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
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
                    Grounder.relativeCard(dk.getGroundingSets(), dk.getTimestamp(), f));
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
}
