package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.ContextException;
import com.pwr.zpi.holons.context.measures.Measure;
import com.pwr.zpi.language.Formula;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class provides realisation of FilteringContextualisation strategy with assumption that set of representative BPs contains
 * group of BPs which are one of the latest ones. The latest observations are treated as most representative.
 */
public class LatestGroupFilteringContextualisation extends FilteringContextualisation {

    private static final int DEF_GROUP_SIZE = 5;
    private static final String NATURAL_LANG_NAME = "Latest Group Filtering Contextualisation";

    private int groupSize;

    public LatestGroupFilteringContextualisation(Measure measure, double maxValue) {
        super(measure, maxValue);
    }

    public LatestGroupFilteringContextualisation(Measure measure) {
        super(measure);
    }

    /**
     * This constructor allows to define maximal allowed value for measure and size of group of representative BPs.
     * If none is specified, then default values will be used.
     * @param measure
     * @param maxValue
     * @param groupSize
     */
    public LatestGroupFilteringContextualisation(Measure measure, double maxValue, int groupSize) {
        super(measure, maxValue);
        this.groupSize = groupSize;
    }


    @Override
    public Set<BaseProfile> selectRepresentativeBPs(Map<Formula, Set<BaseProfile>> namedGroundingSets) throws ContextException {
        int groupSize = this.groupSize != 0 ? this.groupSize : DEF_GROUP_SIZE;
        if (namedGroundingSets == null || namedGroundingSets.isEmpty()) {
            throw new ContextException("Representative base profiles cannot be resolved.");
        }
        Set<BaseProfile> all = new HashSet<>();
        namedGroundingSets.values().forEach(all::addAll);
        representativeBPs = all.stream()
                .sorted(Comparator.comparingInt(BaseProfile::getTimestamp).reversed())
                .limit(groupSize)
                .collect(Collectors.toSet());
        return representativeBPs;
    }

    @Override
    public void setMaxThreshold(double threshold) {
        this.maxValue = threshold;
    }

    /**
     * This method must be defined to enable accessing this kind of contextualisation via voice commands.
     * Note: In addition, this method must be invoked in Contextualisation.getNaturalLanguageNames() method.
     * @return Name of contextualisation.
     */
    public static String getContextName() {
        return NATURAL_LANG_NAME;
    }

    @Override
    public String toString() {
        return NATURAL_LANG_NAME;
    }
}
