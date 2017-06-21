/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.holons.context.measures;

import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidMeasureException;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.core.memory.holons.context.Context;
import com.pwr.zpi.io.Configuration;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.core.memory.semantic.IndividualModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Class implements distance measure between base profile and context. If distance is equal 0, then it means that given
 * base profile perfectly match to context. The greater value of distance, the smaller similarity between of base
 * profile to the context.
 */
public class Distance implements Measure {

    private static final double MAX_THRESHOLD = Configuration.MAX_THRESHOLD;
    double maxThreshold;

    public Distance(double maxThreshold) throws InvalidMeasureException {
        if (maxThreshold < 0)
            throw new InvalidMeasureException("Invalid value of maximum threshold was passed.");
        this.maxThreshold = maxThreshold;
    }

    public Distance() {
        this.maxThreshold = MAX_THRESHOLD;
    }

    /**
     * Method counts distance measure between base profile and context.
     * Note: Distance is a difference between number of traits present in context and number of traits not presents in
     * base profile <em> plus difference between number of traits present in base profile and number of traits not presents in
     * context</em>. Further, result is a sum of above applied for described and not described traits.
     *
     * @param bp Examined base profile.
     * @param context Current context.
     * @return value of distance. 0 means that given base profile perfectly match to context.
     */
    @Override
    public double count(BaseProfile bp, Context context) throws InvalidMeasureImplementation {
        if (context == null || bp == null)
            throw new NullPointerException();
        IndividualModel object = context.getRelatedObject();
        return partialDifference(bp.getRelatedTraits(object, State.IS), context.getObservedTraits())
                + partialDifference(bp.getRelatedTraits(object, State.IS_NOT), context.getNotObservedTraits());
    }

    /**
     * Counts difference between two lists.
     * @param fromBP
     * @param fromContext
     * @return number of differences.
     */
    protected int partialDifference(List<Trait> fromBP, List<Trait> fromContext) {
        List<Trait> common = new ArrayList<>(fromBP);
        int fromBPSize = fromBP.size(),
                fromContextSize = fromContext.size();
        common.retainAll(fromContext);
        return fromContextSize - common.size()
                + fromBPSize - common.size();
    }

    /**
     * Returns maximal allowed value for this measure, for whom similarity expressed through this measure can be
     * understood as big enough.
     * Note: this threshold
     */
    @Override
    public double getMaxThreshold() {
        return maxThreshold;
    }

    @Override
    public void setMaxThreshold(double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public static String getContextName() {
        throw new NotImplementedException();
    }
}
