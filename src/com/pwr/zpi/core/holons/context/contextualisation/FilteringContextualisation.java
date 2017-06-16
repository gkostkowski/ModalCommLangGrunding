/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.holons.context.contextualisation;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidContextualisationException;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.core.holons.context.Context;
import com.pwr.zpi.core.holons.context.builders.ContextBuilder;
import com.pwr.zpi.core.holons.context.measures.Measure;
import com.pwr.zpi.core.holons.context.selectors.RepresentativesSelector;
import com.pwr.zpi.language.Formula;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This strategy of contextualisation performs filtering of base profiles stored in each grounding sets. It includes
 * in result sets only this base profiles, which value of specified Measure (according to second base profile or
 * set of base profiles understood as representative) is in allowed range. Currently established context is used
 * to determine which base profiles are enough relevant.
 * This class is realisation of Strategy A from paper: "Soft Computing Approach to Contextual Determination
 * of Grounding Sets for Simple Modalities".
 */
public class FilteringContextualisation implements Contextualisation {

    protected final RepresentativesSelector selector;
    private final ContextBuilder contextBuilder;
    protected Measure measure;


    /**
     * This constructor allows to provide ContextBuilder with required arguments, which will be used to build
     * concrete context.
     *
     * @param contextBuilder ContextBuilder used to builde context.
     * @param measure Measure.
     * @param selector RepresentativesSelector used to gain representative base profiles.
     */
    public FilteringContextualisation(ContextBuilder contextBuilder, RepresentativesSelector selector, Measure measure)
            throws InvalidContextualisationException {
        if (measure == null || contextBuilder == null || selector == null)
            throw new NullPointerException("One of parameters was not specified.");

        this.contextBuilder = contextBuilder;
        this.selector = selector;
        this.measure = measure;
    }
/*
    *//**
     * This constructor provide convenience way to build Contextualisation of holons. This contextualisation is prepared
     * according to base profiles based on given formula. Source of base profiles used to prepare grounding sets
     * is constituted through given agent's episodic memory.
     * @param agent Agent, which collection of base profiles will be used to build groundig sets.
     * @param measure Measure for determining base profiles matching the context.
     * @param baseOfContextualisation Formula, which is a base to constitute base profiles.
     * @throws InvalidContextualisationException
     *//*
    public FilteringContextualisation(Agent agent, Measure measure, Formula baseOfContextualisation,
                                      ContextBuilder contextBuilder, RepresentativesSelector selector)
            throws InvalidContextualisationException {

        this(contextBuilder.build(
                baseOfContextualisation.getModel(), selector.select(agent.makeGroundingSets(baseOfContextualisation))),
                measure,
                measure.getMaxThreshold());
    }*/

    /**
     * This is exact method for performing contextualisation. Imposes exact Contextualisation instances to receive map of
     * grounding sets (with respective formulas) as keys, and to produce map with same form as result.
     *
     * @param namedGroundingSets Map of grounding sets and related formulas.
     * @return Map of grounding sets filtered in certain manner.
     */
    @Override
    public Map<Formula, Set<BaseProfile>> performContextualisation(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        Map<Formula, Set<BaseProfile>> res = new TreeMap<>();
        Context currentContext = buildContext(namedGroundingSets);
        for (Map.Entry<Formula, Set<BaseProfile>> entry : namedGroundingSets.entrySet()) {
            Set<BaseProfile> groundingSet = entry.getValue();
            res.put(entry.getKey(),
                    groundingSet
                            .stream()
                            .filter(bp -> {
                                try {
                                    return determineFulfillment(bp, currentContext);
                                } catch (InvalidMeasureImplementation e) {
                                    Logger.getAnonymousLogger().log(Level.WARNING, "Unable to determine fulfillment.", e);
                                    return false;
                                }
                            })
                            .collect(Collectors.toSet()));
        }
        return res;
    }

    /**
     * Retireves one of complementary formulas from provided map of grounding sets. It is not important which one it
     * will be exactly.
     * @param namedGroundingSets
     * @return
     */
    private Formula provideFormula(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        return namedGroundingSets.keySet().iterator().next();
    }

    /**
     * Method creates context for given grounding sets.
     * @param namedGroundingSets
     * @return
     */
    private Context buildContext(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        Formula formula = provideFormula(namedGroundingSets);
        return contextBuilder.build(formula.getModel(), selector.select(namedGroundingSets));
    }

    /**
     * This is key method in determining which base profiles should be used in holon building process.
     * It uses provided context to determine condition fulfillment.
     * This method can be override to change way of base profiles selection.
     *
     * @param examined This base profile is examined if meets criteria.
     * @param context Context which will be used to perform comparision.
     * @return true if given examined base profile met criteria; false otherwise.
     */
    public boolean determineFulfillment(BaseProfile examined, Context context) throws InvalidMeasureImplementation {
        System.out.println(measure.count(examined, context));
        return measure.count(examined, context) <= measure.getMaxThreshold();
    }

    @Override
    public void setMaxThreshold(double threshold) {
        measure.setMaxThreshold(threshold);
    }
}
