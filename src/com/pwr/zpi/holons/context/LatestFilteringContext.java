package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.ContextException;
import com.pwr.zpi.holons.context.measures.Measure;
import com.pwr.zpi.language.Formula;

import java.util.*;

/**
 * Class provides realisation of FilteringContext strategy with assumption that set of representative BPs contains
 * one BP which is the latest one. The latest observation is treated as most representative.
 */
public class LatestFilteringContext extends FilteringContext {

    private static final String NATURAL_LANG_NAME = "Latest Filtering Context";

    public LatestFilteringContext(Measure measure, double maxValue) {
        super(measure, maxValue);
    }

    public LatestFilteringContext(Measure measure) {
        super(measure);
    }

    @Override
    public Set<BaseProfile> selectRepresentativeBPs(Map<Formula, Set<BaseProfile>> namedGroundingSets) throws ContextException {
        if (namedGroundingSets == null || namedGroundingSets.isEmpty())
            throw new ContextException("Representative base profiles cannot be resolved.");
        Set<BaseProfile> all = new HashSet<>();
        namedGroundingSets.values().forEach(all::addAll);
        BaseProfile res = all.stream().max(Comparator.comparing(BaseProfile::getTimestamp)).get();
        this.representativeBPs = new HashSet<>();
        representativeBPs.add(res);
        return representativeBPs;
    }

    @Override
    public void setMaxThreshold(double threshold) {
        this.maxValue = threshold;
    }


    /**
     * This method must be defined to enable accessing this kind of contextualisation via voice commands.
     * Note: In addition, this method must be invoked in Context.getNaturalLanguageNames() method.
     * @return Name of contextualisation.
     */
    public static String getContextName() {
        return NATURAL_LANG_NAME;
    }
}
