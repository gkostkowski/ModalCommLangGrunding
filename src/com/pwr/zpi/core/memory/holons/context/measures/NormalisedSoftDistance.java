/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.holons.context.measures;

import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.core.memory.holons.context.Context;
import com.pwr.zpi.exceptions.InvalidMeasureException;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Class implements distance measure between base profile and context. If distance is equal 0, then it means that given
 * base profile perfectly match to context. This measure is normalised, namely it returns values form range [0, 1].
 * It uses soft distance.
 */
public class NormalisedSoftDistance extends SoftDistance implements Measure {

    private static final double DEF_MAX_THRESHOLD = 0.4;
    private static final String NATURAL_LANG_NAME = "normalised distance";
    double maxThreshold;

    public NormalisedSoftDistance(double maxThreshold) throws InvalidMeasureException {
        if (maxThreshold < 0)
            throw new InvalidMeasureException("Invalid value of maximum threshold was passed.");
        this.maxThreshold = maxThreshold;
    }

    public NormalisedSoftDistance() {
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
        State[] availableStates = new State[]{State.IS, State.IS_NOT, State.MAYHAPS};
        int noOfUnique = (int)Stream.of(availableStates)
                .map(s -> new HashSet(bp.getRelatedTraits(context.getRelatedObject(), s)))
                .flatMap(s -> s.stream())
                .count();
        double res = differences / (noOfUnique*2.0);
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
