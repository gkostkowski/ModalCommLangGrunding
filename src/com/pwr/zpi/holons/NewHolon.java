package com.pwr.zpi.holons;

import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.language.Formula;

import java.util.List;
import java.util.Map;

/**
 * Shared interface for holons which are placed inside agent and represent embedded summarization of empirical
 * episodic experiences gathered by this agent. Each holon relates to particular experiences with strictly defined objects.
 * Thus, there is different holon for each formula considered by agent.
 * Holons constitute layer in agent's architecture which works with semantic memory, episodic memory and linguistic module.
 * Its aim is to provide summarization for linguistic module which it to perform grounding of affected formula.
 * To build some hierarchy of knowledge according to some groups of associated observations, contextualisation of holons is introduced.
 */
public interface NewHolon {

    void update() throws InvalidFormulaException, NotApplicableException;

    Double getSummary(Formula formula);

    Map<Formula, Double> getSummaries();

    HolonKind getKind();

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
    enum HolonKind{
        Binary,
        Non_Binary
    }
}
