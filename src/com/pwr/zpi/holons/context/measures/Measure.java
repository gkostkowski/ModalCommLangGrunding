package com.pwr.zpi.holons.context.measures;

import com.pwr.zpi.episodic.BaseProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * This functional interface specifies measure which will be used in contextualisation process. This measure will be applied to
 * given two base profiles and should represent value of similarity between this two base profiles.
 *
 */
public interface Measure {

    String DEFAULT_MEASURE = "Default measure";

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
    void setMaxThreshold(double maxThreshold);

    /**
     * This method provides names of all concrete implementations of this interface spoken in natural language.
     * Every measure used with contextualisation method which should be accessed via voice command must
     * be included in resulted map.
     * Note: If measure won't be specified, when specifying contextualisation, then no contextualisation will be used.
     * @return Map of string representing natural language name and respective class.
     */
    static Map<String, Class> getNaturalLanguageNames() {
        return new HashMap<String, Class>(){{
            put(DEFAULT_MEASURE, Distance.class);
            put(Distance.getContextName(), Distance.class);
            put(FocusedDistance.getContextName(), FocusedDistance.class);
        }};
    }
}
