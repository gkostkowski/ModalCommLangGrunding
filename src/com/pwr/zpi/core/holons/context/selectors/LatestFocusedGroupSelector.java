package com.pwr.zpi.core.holons.context.selectors;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.State;
import com.pwr.zpi.core.semantic.IndividualModel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Selector chooses base profiles which matches to the description provided through Formula object. Description provides
 * traits with their states and a way of fulfillment: all of traits (operator AND) or only one of them (OR).
 *
 */
public class LatestFocusedGroupSelector implements RepresentativesSelector {

    private Formula formula;
    private IndividualModel relatedObject;
    /**
     *
     * @param formula Description of base profile which can be used to build a context. If exemplary formula is
     *                <i>Is blue <em>and</em> not blinking</i> then returned set of base profiles will contain only this
     *                base profiles which presents given object as blue and not blinking.
     *
     */
    public LatestFocusedGroupSelector(Formula formula) {
        if (formula == null)
            throw new NullPointerException();
        this.formula = formula;
         relatedObject= formula.getModel();

    }

    /**
     * Method provides set of base profiles (or only one base profile in particular case) which are known as representative.
     * These base profiles can be used in ContextBuilder class to produce context.
     *
     * @param namedGroundingSets
     * @return
     */
    @Override
    public Set<BaseProfile> select(Map<Formula, Set<BaseProfile>> namedGroundingSets) {
        if (namedGroundingSets == null || namedGroundingSets.isEmpty()) {
            throw new NullPointerException("Representative base profiles cannot be resolved.");
        }

        Set<BaseProfile> representativeBPs;
        Set<BaseProfile> all = new HashSet<>();
        namedGroundingSets.values().forEach(all::addAll);
        representativeBPs = all.stream()
                .filter(this::isEligible)
                .collect(Collectors.toSet());
        return representativeBPs;
    }

    private boolean isEligible(BaseProfile bp) {
        return bp.getAffectedIMs(State.IS, State.IS_NOT).contains(relatedObject)
                && formula.isFormulaFulfilled(bp);
    }
}
