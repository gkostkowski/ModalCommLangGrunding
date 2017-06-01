package com.pwr.zpi.holons;


import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.holons.context.Contextualisation;
import com.pwr.zpi.language.DistributedKnowledge;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Pair;

import java.util.List;
import java.util.Map;

/**
 * Holon is representation of agent's reflections on gathered observations. Agent is suppoused to form Holons based
 *  on Traits or Formulas. Holon returns Operator(BEL,POS,KNOW,NOT) ,which has been most frequent in most observations.
 *
 */

/*


public abstract class Holon{
    public abstract void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException;
    public abstract Pair getStrongest();
    public abstract Pair getWeakest();
    public abstract HolonKind getKind();
    public abstract List<Formula> getFormula();
    public abstract boolean isApplicable(Formula f) throws InvalidFormulaException;
    public enum HolonKind{
        Binary,
        Non_Binary
    }

}

*/

/*Zmiany:
* -nie ma potrzeby uzycia klasy abstrakcyjnej bo wszystkie metody sa abstrakcyjne
* */

/**
 * Shared interface for holons which are placed inside agent and represent embedded summarization of empirical
 * episodic experiences gathered by this agent. Each holon relates to particular experiences with strictly defined objects.
 * Thus, there is different holon for each formula considered by agent.
 * Holons constitute layer in agent's architecture which works with semantic memory, episodic memory and linguistic module.
 * Its aim is to provide summarization for linguistic module which it to perform grounding of affected formula.
 * To build some hierarchy of knowledge according to some groups of associated observations, contextualisation of holons is introduced.
 */
public interface Holon {

    void update() throws InvalidFormulaException, NotApplicableException;

    Double getSummary(Formula formula);

    Map<Formula, Double> getSummaries();

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
    public abstract void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException;
    public abstract Pair getStrongest();
    public abstract Pair getWeakest();
    public abstract HolonKind getKind();
    public abstract List<Formula> getFormula();

    /** Returns context which was used to build grounding sets for this holon.*/
    Contextualisation getContextualisation();

    enum HolonKind{
        Binary,
        Non_Binary
    }
}