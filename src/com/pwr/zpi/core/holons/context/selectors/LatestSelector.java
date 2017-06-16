package com.pwr.zpi.core.holons.context.selectors;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.State;
import com.pwr.zpi.core.semantic.IndividualModel;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Selector chooses last basing profile, according to timestamp.
 */
public class LatestSelector implements RepresentativesSelector {


    /**
     * Method provides way to determine representative base profile. In this implementation returned set contains
     * only one grounding set which is the latest one (according to timestamp). In case of more than one base profile
     * with same latest timestamp, one of them is selected - such selection is non-deterministic.
     * Resulted set of base profiles can be used in ContextBuilder class to produce context.
     * Note: selected base profile is returned in form of set of base profiles due to interface's agreement.
     *
     * @param namedGroundingSets
     * @return Set of BaseProfiles which contains latest one.
     */
    @Override
    public Set<BaseProfile> select(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        if (namedGroundingSets == null || namedGroundingSets.isEmpty())
            throw new NullPointerException("Representative base profiles cannot be resolved.");

        IndividualModel relatedObject = namedGroundingSets.keySet().iterator().next().getModel();

        Set<BaseProfile> representativeBPs;
        Set<BaseProfile> all = new HashSet<>();
        namedGroundingSets.values().forEach(all::addAll);
        BaseProfile res = all.stream()
                .filter(bp -> bp.getAffectedIMs(State.IS, State.IS_NOT).contains(relatedObject))
                .max(Comparator.comparing(BaseProfile::getTimestamp))
                .get();
        representativeBPs = new HashSet<>();
        representativeBPs.add(res);
        return representativeBPs;
    }
}
