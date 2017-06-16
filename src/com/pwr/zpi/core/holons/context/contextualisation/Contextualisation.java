/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.holons.context.contextualisation;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.language.Formula;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.Set;

/**
 * Interface defines contract for all concrete realisations of holon's contextualisation. Contextualisation specifies a way of
 * selecting base profiles according to some adequacy-based measures. The aim of contextualisation is to make agent's
 * answer more accurate.
 */
public interface Contextualisation {
    String DEFAULT_CONTEXT = "default context";

    /**
     * This is exact method for performing contextualisation. Imposes exact Contextualisation instances to receive map of
     * grounding sets (with respective formulas) as keys, and to produce map with same form as result.
     * @param namedGroundingSets Map of grounding sets and related formulas.
     * @return Map of grounding sets filtered in certain manner.
     */
    Map<Formula, Set<BaseProfile>> performContextualisation(Map<Formula, Set<BaseProfile>> namedGroundingSets);

    /**
     * This method provides names of all concrete implementations of this interface spoken in natural language.
     * Every contextualisation method which should be accessed via voice command must be included in resulted map.
     * @return Map of string representing natural language name and respective class.
     */
    @Deprecated
    static Map<String, Class> getNaturalLanguageNames() {
        throw new NotImplementedException();
        /*return new HashMap<String, Class>(){{
            put(DEFAULT_CONTEXT, null);
            put(LatestFilteringContextualisation.getContextName(), LatestFilteringContextualisation.class);
            put(LatestFocusedFilteringContextualisation.getContextName(), LatestFocusedFilteringContextualisation.class);
            put(LatestGroupFilteringContextualisation.getContextName(), LatestGroupFilteringContextualisation.class);
        }};*/

    }

    void setMaxThreshold(double threshold);
}
