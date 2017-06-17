/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.holons.context.measures;

import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.language.Trait;

import java.util.Collection;

/**
 * This functional interface specifies focused measure which will be used in contextualisation process. This measure will be applied to
 * given two base profiles and should represent value of similarity between this two base profiles.
 * The focused measure is known as a measure which is taking into account only particular traits in both base profiles.
 * Manner in which traits influences the resulted value should be explained in exact implementations.
 */
public interface FocusedMeasure extends Measure{

    /**
     * Method counts value of implemented measure for given two base profiles.
     * @param first
     * @param second
     * @return Value for specified measure.
     */
    double count(BaseProfile first, BaseProfile second, Collection<Trait> traits);

    /**
     * Returns maximal allowed value for this measure, for whom similarity expressed through this measure can be
     * understood as big enough.
     * Note: this threshold
     */
    double getMaxThreshold();
    void setMaxThreshold(double maxThreshold);
}
