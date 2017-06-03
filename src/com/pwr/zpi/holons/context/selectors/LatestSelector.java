package com.pwr.zpi.holons.context.selectors;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.ContextualisationException;
import com.pwr.zpi.language.Formula;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-06-03.
 */
public class LatestSelector implements RepresentativesSelector {
    /**
     * Method provides set of base profiles (or only one base profile in particular case) which are known as representative.
     * These base profiles can be used in ContextBuilder class to produce context.
     *
     * @param namedGroundingSets
     * @return
     */
    @Override
    public Set<BaseProfile> select(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        if (namedGroundingSets == null || namedGroundingSets.isEmpty())
            throw new NullPointerException("Representative base profiles cannot be resolved.");

        Set<BaseProfile> representativeBPs;
        Set<BaseProfile> all = new HashSet<>();
        namedGroundingSets.values().forEach(all::addAll);
        BaseProfile res = all.stream().max(Comparator.comparing(BaseProfile::getTimestamp)).get();
        representativeBPs = new HashSet<>();
        representativeBPs.add(res);
        return representativeBPs;
    }
}
