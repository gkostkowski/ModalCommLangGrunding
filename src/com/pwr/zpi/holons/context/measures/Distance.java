package com.pwr.zpi.holons.context.measures;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.LatestFilteringContext;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.semantic.IndividualModel;

import java.util.*;

/**
 * Class implements distance measure between two given base profiles. If distance is equal 0, then it means that given
 * two base profiles are identical. The greater value of distance, the smaller similarity between this two base profiles.
 */
public class Distance implements Measure {

    private static final double DEF_MAX_THRESHOLD = 5;
    private static final String NATURAL_LANG_NAME = "distance";
    double maxThreshold;

    public Distance(double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public Distance() {
        this.maxThreshold = DEF_MAX_THRESHOLD;
    }

    /**
     * Method counts distance between given two base profiles. Distance is a number of all differences in particular
     * maps of traits and IMs.
     * @param first
     * @param second
     * @return value of distance. 0 mean that two base profiles are identical.
     */
    @Override
    public double count(BaseProfile first, BaseProfile second) {
        int differentIMs=0;

            List<Map<Trait, Set<IndividualModel>>> mapsToProcess = new ArrayList<Map<Trait, Set<IndividualModel>>>(){{
                add(first.getDescribedByTraits());
                add(first.getNotDescribedByTraits());
                add(first.getIndefiniteByTraits());
            }};
            List<Map<Trait, Set<IndividualModel>>> mapsToCompare = new ArrayList<Map<Trait, Set<IndividualModel>>>(){{
                add(second.getDescribedByTraits());
                add(second.getNotDescribedByTraits());
                add(second.getIndefiniteByTraits());
            }};

            for (int i=0; i< mapsToProcess.size(); i++) {
                Map<Trait, Set<IndividualModel>> currMap = mapsToProcess.get(i);
                Map<Trait, Set<IndividualModel>> comparedMap = mapsToCompare.get(i);
                for (Trait trait : currMap.keySet()) {
                    if (comparedMap.get(trait) == null)
                        differentIMs += currMap.get(trait).size();
                    else {

                        Set<IndividualModel> minus=new HashSet<>(currMap.get(trait));
                        minus.removeAll(comparedMap.get(trait));
                        differentIMs+=minus.size();
                        minus=new HashSet<>(comparedMap.get(trait));
                        minus.removeAll(currMap.get(trait));
                        differentIMs+=minus.size();
                    }
                }
            }
        return differentIMs;
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
