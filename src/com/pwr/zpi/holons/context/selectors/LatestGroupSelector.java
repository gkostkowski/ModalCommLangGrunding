package com.pwr.zpi.holons.context.selectors;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.ContextualisationException;
import com.pwr.zpi.exceptions.InvalidGroupSelectorException;
import com.pwr.zpi.language.Formula;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Grzesiek on 2017-06-03.
 */
public class LatestGroupSelector implements RepresentativesSelector {

    private static final int DEF_GROUP_SIZE = 5;
    private int groupSize;

    public LatestGroupSelector(int groupSize) throws InvalidGroupSelectorException {
        if (groupSize < 1)
            throw new InvalidGroupSelectorException("Invalid size of representatives group was passed.");
        this.groupSize = groupSize;
    }

    /**
     * Method provides set of base profiles (or only one base profile in particular case) which are known as representative.
     * These base profiles can be used in ContextBuilder class to produce context.
     *
     * @param namedGroundingSets
     * @return
     */
    @Override
    public Set<BaseProfile> select(Map<Formula, Set<BaseProfile>> namedGroundingSets){
        int groupSize = this.groupSize != 0 ? this.groupSize : DEF_GROUP_SIZE;
        if (namedGroundingSets == null || namedGroundingSets.isEmpty()) {
            throw new NullPointerException("Representative base profiles cannot be resolved.");
        }

        Set<BaseProfile> representativeBPs;
        Set<BaseProfile> all = new HashSet<>();
        namedGroundingSets.values().forEach(all::addAll);
        representativeBPs = all.stream()
                .sorted(Comparator.comparingInt(BaseProfile::getTimestamp).reversed())
                .limit(groupSize)
                .collect(Collectors.toSet());
        return representativeBPs;
    }
}
