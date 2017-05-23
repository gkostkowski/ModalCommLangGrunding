package com.pwr.zpi.holons.context.measures;

import com.pwr.zpi.episodic.BaseProfile;

/**
 * This functional interface specifies measure which will be used in contextualisation process. This measure will be applied to
 * given two base profiles and should represent value of similarity between this two base profiles.
 *
 */
public interface Measure {
    /**
     * Method counts value of implemented measure for given two base profiles.
     * @param first
     * @param second
     * @return Value for specified measure.
     */
    double count(BaseProfile first, BaseProfile second);

    /**
     * Returns maximal allowed value for this measure, for whom similarity expressed through this measure can be
     * understood as big enough.
     * Note: this threshold
     */
    double getMaxThreshold();
}
