/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.holons.context.contextualisation;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.holons.context.builders.ContextBuilder;
import com.pwr.zpi.holons.context.measures.Measure;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.semantic.IndividualModel;

import java.util.Collection;
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
public abstract class FilteringContextualisation implements Contextualisation {

    protected Measure measure;
    protected Context context;
    protected double maxValue;

    /**
     * In standard verison, it's the single BP built basing on the last observations (according to all observations
     * with latest timestamp).
     */
    Set<BaseProfile> representativeBPs;

    public FilteringContextualisation(Context context, Measure measure, double maxValue) {
        if (measure == null || context == null)
            throw new NullPointerException("One of parameters was not specified.");
        this.measure = measure;
        this.maxValue = maxValue;
    }

    /**
     * If max allowable value won't be provided then this value is fetched from measure object.
     */
    public FilteringContextualisation(Context context, Measure measure) {
        this(context, measure, measure.getMaxThreshold());
    }

    /**
     * This constructor allows to provide ContextBuilder with required arguments, which will be used to build context
     * instead of providing context, for convenience.
     *
     * @param contextBuilder
     * @param measure
     * @param relatedObject   Individual model.
     * @param representatives Set of base profiles known as representatives.
     */
    public FilteringContextualisation(ContextBuilder contextBuilder, Measure measure,
                                      IndividualModel relatedObject, Collection<BaseProfile> representatives) {
        this(contextBuilder.build(relatedObject, representatives), measure, measure.getMaxThreshold());
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
     * Method checks if there is any representative BP, for whom similarity requirement is not fulfilled. If there is any,
     * then given BP doesn't meet condition. Here specified measure is used.
     *
     * @param examined Base profile from input grounding set which will be examined.
     * @return
     */
    @Deprecated
    public boolean isMeetingCondition(BaseProfile examined) {
        if (context == null)
            throw new NullPointerException("Context was not initialized.");
        return representativeBPs
                .stream()
                .filter(bp -> {
                    try {
                        return !determineFulfillment(bp);
                    } catch (InvalidMeasureImplementation e) {
                        Logger.getAnonymousLogger().log(Level.WARNING, "None .", e);
                        return false;
                    }
                })
                .collect(Collectors.toList())
                .isEmpty();
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

}
