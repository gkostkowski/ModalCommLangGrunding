package com.pwr.zpi.holons.context.selectors;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.language.Formula;

import java.util.Map;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-06-03.
 */
public class LatestFocusedGroupSelector implements RepresentativesSelector {
    /**
     * Method provides set of base profiles (or only one base profile in particular case) which are known as representative.
     * These base profiles can be used in ContextBuilder class to produce context.
     *
     * @param namedGroundingSets
     * @return
     */
    @Override
    public Set<BaseProfile> select(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        return null;
    }
}
