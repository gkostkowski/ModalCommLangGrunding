package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.measures.Measure;
import com.pwr.zpi.language.Formula;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * This strategy of contextualisation performs filtering of base profiles stored in each grounding sets. It includes
 * in result sets only this base profiles, which value of specified Measure (according to second base profile or
 * set of base profiles understood as representative) is in allowed range.
 * This class is realisation of Strategy A from paper: "Soft Computing Approach to Contextual Determination
 * of Grounding Sets for Simple Modalities".
 */
public abstract class FilteringContext implements Context {

    protected Measure measure;
    protected double maxValue;

    /**
     * In standard verison, it's the single BP built basing on the last observations (according to all observations
     * with latest timestamp).
     */
    Set<BaseProfile> representativeBPs;

    public FilteringContext(Measure measure, double maxValue) {
        if (measure == null)
            throw new NullPointerException("Measure wasn't specified.");
        this.measure = measure;
        this.maxValue =maxValue;
    }

    /**
     * If max allowable value won't be provided then this value is fetched from measure object.
     */
    public FilteringContext(Measure measure) {
        this.measure = measure;
        this.maxValue =measure.getMaxThreshold();
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
        representativeBPs = selectRepresentativeBPs(namedGroundingSets);
        for (Map.Entry<Formula, Set<BaseProfile>> entry: namedGroundingSets.entrySet()) {
            Set<BaseProfile> groundingSet = entry.getValue();
            res.put(entry.getKey(),
                    groundingSet
                            .stream()
                            .filter(bp -> isMeetingCondition(bp))
                            .collect(Collectors.toSet()));
        }
        return res;
    }

    /**
     * Method checks if there is any representative BP, for whom similarity requirement is not fulfilled. If there is any,
     * then given BP doesn't meet condition. Here specified measure is used.
     * @param examined Base profile from input grounding set which will be examined.
     * @return
     */
    public boolean isMeetingCondition(BaseProfile examined) {
        return representativeBPs
                .stream()
                .filter(bp -> determineFulfillment(bp, examined))
                .collect(Collectors.toList())
                .isEmpty();
    }

    /**
     * This is key method in determining what base profiles should be used in holon building process.
     * This method can be overrided to change way of base profiles selection.
     * @param representative This is representative base profile.
     * @param examined This base profile is examined if meets criteria.
     * @return true if given examined base profile met criteria; false otherwise.
     */
    public boolean determineFulfillment(BaseProfile representative, BaseProfile examined) {
        return measure.count(representative, examined) <= maxValue;
    }

    public abstract Set<BaseProfile> selectRepresentativeBPs(Map<Formula, Set<BaseProfile>> namedGroundingSets);


}
