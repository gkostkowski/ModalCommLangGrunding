package com.pwr.zpi.core.holons;


import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.core.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.core.episodic.DistributedKnowledge;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Pair;

import java.util.List;
import java.util.Map;

/**
 * Shared interface for holons which are placed inside agent and represent embedded summarization of empirical
 * episodic experiences gathered by this agent. Each holon relates to particular experiences with strictly defined objects.
 * Thus, there is different holon for each formula considered by agent.
 * Holons constitute layer in agent's architecture which works with semantic memory, episodic memory and linguistic module.
 * Its aim is to provide summarization for linguistic module which it to perform grounding of affected formula.
 * To build some hierarchy of knowledge according to some groups of associated observations, contextualisation of holons is introduced.
 * @author Grzegorz Kostkowski
 * @author Jarema Radom
 */
public interface Holon extends Comparable<Holon> {

    void update() throws InvalidFormulaException, NotApplicableException;

    Double getSummary(Formula formula);

    /**
     * Returns map of all summaries.
     */
    Map<Formula, Double> getSummaries();
    /**
     * Returns map of summaries only for given Formulas.
     */
    Map<Formula, Double> getSummaries(List<Formula> selectedFormulas);

    /**
     * Returns list of complementary formulas which were used when building this holon.
     */
    List<Formula> getAffectedFormulas();

    /**
     * Checks if this holon is related to given formula.
     * @param formula
     * @return
     * @throws InvalidFormulaException
     */
    boolean isApplicable(Formula formula) throws InvalidFormulaException;

    ///+ metody Jaremy

    /**
     * Method performs holon update if content of knowledge distribution contains new observations.
     * @param dk The newest version of knowledge distribution.
     * @return True if holon was updated; false if holon had up-to-date information and update was not needed.
     * @throws InvalidFormulaException
     * @throws NotApplicableException
     */
    boolean update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException;
    Pair getStrongest();
    Pair getWeakest();
    HolonKind getKind();
    List<Formula> getFormula();

    /** Returns context which was used to build grounding sets for this holon.*/
    Contextualisation getContextualisation();

    enum HolonKind{
        Binary,
        Non_Binary
    }

    @Override
    default int compareTo(Holon o) {
        double res=0, res2=0;
        for (Formula f:getSummaries().keySet()) {
            res = f.hashCode() + getSummaries().get(f);
        }

        for (Formula f:o.getSummaries().keySet()) {
            res2 = f.hashCode() + o.getSummaries().get(f);
        }
        return res > res2 ? 1: (res < res2 ? -1 : 0);
    }
}