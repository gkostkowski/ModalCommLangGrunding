package com.pwr.zpi.holons.context.measures;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.LatestFilteringContext;

/**
 * Class implements distance measure between two given base profiles. If distance is equal 0, then it means that given
 * two base profiles are identical. The greater value of distance, the smaller similarity between this two base profiles.
 */
public class Distance implements Measure {

    private static final double DEF_MAX_THRESHOLD = 5;
    double maxThreshold;

    public Distance(double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public Distance() {
        this.maxThreshold = DEF_MAX_THRESHOLD;
    }

    @Override
    public double count(BaseProfile first, BaseProfile second) {
        return 0; //todo
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
}
