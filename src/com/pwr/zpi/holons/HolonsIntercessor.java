package com.pwr.zpi.holons;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.ComplexFormula;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.LogicOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class works with non-binary holon on determining exact relative grounding cardinality for each affected
 * complex formula.
 * In case of summaries for conjunctions, relatives grounding cardinality is fixed straight from respective conjunctive
 * grounding sets. In case of disjunctions, such values are determined based on relative grounding cardinality for
 * conjunctions.
 */
public class HolonsIntercessor {

    private static final LogicOperator HOLON_BASE_OPERATOR = LogicOperator.AND;
    Agent agent;
    Contextualisation contextualisation;
    HolonCollection holonsCollection;


    public HolonsIntercessor(Agent agent, Contextualisation contextualisation) {
        this.agent = agent;
        this.contextualisation = contextualisation;
        holonsCollection = new HolonCollection(agent, contextualisation);
    }


    public void updateBeliefs(int timestamp) throws InvalidFormulaException, NotApplicableException {
        for (Holon h : holonsCollection.getHolonCollection()) {
            h.update(agent.distributeKnowledge(h.getAffectedFormulas().get(0), timestamp, true));
        }
    }

    public Contextualisation getHolonsContextualisation(){
        return contextualisation;
    }

    public HolonCollection getHolonsCollection() {
        return holonsCollection;
    }

    /**
     * This method is used to retrieve summarization for any available formula. Due to differences in counting summaries
     * based on provided holon, this method was implemented to provide unified interface for different types of formula.
     * Method makes use of holon which is built during summaries retrieving process.
     * @param currFormula
     * @param timestamp
     * @return
     */
    public Map<Formula, Double> getSummaries(Formula currFormula, int timestamp) {

        if (currFormula.usesDirectHolonConstruction()) {
            return holonsCollection.getHolon(currFormula, timestamp).getSummaries();
        } else {
            try {
                ComplexFormula conjunction = currFormula.transformTo(HOLON_BASE_OPERATOR);
                Holon conjunctiveHolon = holonsCollection.getHolon(conjunction, timestamp);
                Map<Formula, Double> res = new HashMap<>();
                for (Formula complDisj: currFormula.getComplementaryFormulas())
                    res.put(complDisj, countRelevantSummary(conjunctiveHolon, complDisj));
                return res;
            } catch (InvalidFormulaException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, "Unable to retrieve summarization for given formula.", e);
                return null;
            }
        }
    }

    /**
     * This method counts summary (relative grounding cardinality) for given disjunctive formula with usage of provided holon.
     * This holon was built for conjunctive formula which corresponds to provided disjunction (only difference is used operator).
     *
     * @param conjunctiveHolon
     * @param compelDisj
     * @return
     */
    private Double countRelevantSummary(Holon conjunctiveHolon, Formula compelDisj) {
        List<Formula> selectedConjunctions=compelDisj.getDependentFormulas();
        Map<Formula, Double> summaries = conjunctiveHolon.getSummaries(selectedConjunctions);
        return summaries.values().stream().mapToDouble(Double::doubleValue).sum();
    }

}
