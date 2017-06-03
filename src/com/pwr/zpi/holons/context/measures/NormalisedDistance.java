/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.holons.context.measures;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;

/**
 * Class implements distance measure between base profile and context. If distance is equal 0, then it means that given
 * base profile perfectly match to context. This measure is normalised, namely it returns values form range [0, 1].
 */
public class NormalisedDistance extends Distance implements Measure {

    private static final double DEF_MAX_THRESHOLD = 0.4;
    private static final String NATURAL_LANG_NAME = "normalised distance";
    double maxThreshold;

    public NormalisedDistance(double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public NormalisedDistance() {
        this.maxThreshold = DEF_MAX_THRESHOLD;
    }

    /**
     * Method counts normalised distance measure between base profile and context.
     *
     * @param bp
     * @param context Current context.
     * @return value of distance from range [0, 1], where zero mean that given base profile perfectly match to context
     * and one mean the opposite.
     */
    @Override
    public double count(BaseProfile bp, Context context) throws InvalidMeasureImplementation {
        double differences = super.count(bp, context);
        Set<Trait> positive = new HashSet<>();
        positive.addAll(bp.getRelatedTraits(context.getRelatedObject(), State.IS));
        positive.addAll(context.getObservedTraits());
        double allUnique = positive.size();
        positive = new HashSet<>();
        positive.addAll(bp.getRelatedTraits(context.getRelatedObject(), State.IS_NOT));
        positive.addAll(context.getNotObservedTraits());
        allUnique += positive.size();
        double res = differences / allUnique;
        if (res > 1.0)
            throw new InvalidMeasureImplementation();
        return res;
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
        return NATURAL_LANG_NAME;
    }
}
