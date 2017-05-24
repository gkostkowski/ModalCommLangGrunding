package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.measures.Measure;
import com.pwr.zpi.language.Formula;

import java.util.Map;
import java.util.Set;

/**
 * Class provides realisation of FilteringContext strategy with assumption that set of representative BPs contains
 * one BP which is the latest one. The latest observation is treated as most representative.
 */
public class LatestFilteringContext extends FilteringContext {

    public LatestFilteringContext(Measure measure, double maxValue) {
        super(measure, maxValue);
    }

    @Override
    public Set<BaseProfile> selectRepresentativeBPs(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        return null; //todo
    }
}
