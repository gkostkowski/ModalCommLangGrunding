/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.holons.context.contextualisation;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidContextualisationException;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.holons.context.builders.ContextBuilder;
import com.pwr.zpi.holons.context.measures.Measure;
import com.pwr.zpi.holons.context.selectors.RepresentativesSelector;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.semantic.IndividualModel;

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

    protected Measure measure;
    protected Context context;
    protected double maxValue;



    public FilteringContextualisation(Context context, Measure measure, double maxValue) throws InvalidContextualisationException {
        if (measure == null || context == null)
            throw new NullPointerException("One of parameters was not specified.");
        if (maxValue < 0)
            throw new InvalidContextualisationException("Not valid value for maximum threshold.");
        this.context = context;
        this.measure = measure;
        this.maxValue = maxValue;
    }

    /**
     * If max allowable value won't be provided then this value is fetched from measure object.
     */
    public FilteringContextualisation(Context context, Measure measure) throws InvalidContextualisationException {
        this(context, measure, measure.getMaxThreshold());
    }

    /**
     * This constructor allows to provide ContextBuilder with required arguments, which will be used to build context
     * instead of providing context, for convenience.
     *
     * @param contextBuilder ContextBuilder used to builde context.
     * @param measure Measure.
     * @param relatedObject   Individual model.
     * @param selector RepresentativesSelector used to gain representative base profiles.
     * @param namedGroundingSets map of grounding sets.
     */
    public FilteringContextualisation(ContextBuilder contextBuilder, Measure measure, IndividualModel relatedObject,
                                      RepresentativesSelector selector, Map<Formula, Set<BaseProfile>> namedGroundingSets)
            throws InvalidContextualisationException {
        this(contextBuilder.build(relatedObject, selector.select(namedGroundingSets)),
                measure,
                measure.getMaxThreshold());
    }

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

        for (Map.Entry<Formula, Set<BaseProfile>> entry : namedGroundingSets.entrySet()) {
            Set<BaseProfile> groundingSet = entry.getValue();
            res.put(entry.getKey(),
                    groundingSet
                            .stream()
                            .filter(bp -> {
                                try {
                                    return determineFulfillment(bp);
                                } catch (InvalidMeasureImplementation e) {
                                    e.printStackTrace();
                                    Logger.getAnonymousLogger().log(Level.WARNING, "Unable to determine fulfillment.", e);
                                    return false;
                                }
                            })
                            .collect(Collectors.toSet()));
        }
        return res;
    }

    /**
     * This is key method in determining which base profiles should be used in holon building process.
     * It uses already provided context to determine condition fulfillment.
     * This method can be override to change way of base profiles selection.
     *
     * @param examined This base profile is examined if meets criteria.
     * @return true if given examined base profile met criteria; false otherwise.
     */
    public boolean determineFulfillment(BaseProfile examined) throws InvalidMeasureImplementation {
        return measure.count(examined, context) <= maxValue;
    }

    @Override
    public void setMaxThreshold(double threshold) {
        maxValue = threshold;
    }
}
