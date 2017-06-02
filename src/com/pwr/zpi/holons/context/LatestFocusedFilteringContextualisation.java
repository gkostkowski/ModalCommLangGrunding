/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.measures.FocusedMeasure;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Trait;

import java.util.*;

/**
 * Class provides realisation of FilteringContextualisation strategy with assumption that set of representative BPs contains
 * one BP which is the latest one. The latest observation is treated as most representative, but only when focusing on
 * particular traits, defined in constructor.
 */
public class LatestFocusedFilteringContextualisation extends LatestFilteringContextualisation {

    private static final String NATURAL_LANG_NAME = "Latest Focused Filtering Contextualisation";
    private List<Trait> traits;
    private Formula formula;

    public LatestFocusedFilteringContextualisation(Collection<Trait> focusedTraits, FocusedMeasure measure, double maxValue) {
        super(measure, maxValue);
    }

    public LatestFocusedFilteringContextualisation(Collection<Trait> focusedTraits, FocusedMeasure measure) {
        super(measure);
    }


    /**
     * This is key method in determining what base profiles should be used in holon building process.
     * This method can be overrided to change way of base profiles selection.
     *
     * @param representative This is representative base profile.
     * @param examined       This base profile is examined if meets criteria.
     * @return true if given examined base profile met criteria; false otherwise.
     */
    @Override
    public boolean determineFulfillment(BaseProfile representative, BaseProfile examined) {
        return ((FocusedMeasure)measure).count(representative, examined, traits) <= maxValue;
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
