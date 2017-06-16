package com.pwr.zpi.core.holons.context.selectors;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.language.Formula;

import java.util.Map;
import java.util.Set;


public interface RepresentativesSelector {

    /**
     * Method provides set of base profiles (or only one base profile in particular case) which are known as representative.
     * These base profiles can be used in ContextBuilder class to produce context.
     *
     * @param namedGroundingSets
     * @return
     */
    Set<BaseProfile> select(Map<Formula, Set<BaseProfile>> namedGroundingSets);
}
