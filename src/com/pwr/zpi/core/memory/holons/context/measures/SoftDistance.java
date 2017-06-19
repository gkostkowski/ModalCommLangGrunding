/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.holons.context.measures;

import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.core.memory.holons.context.Context;
import com.pwr.zpi.core.memory.semantic.IndividualModel;
import com.pwr.zpi.exceptions.InvalidMeasureException;
import com.pwr.zpi.exceptions.InvalidMeasureImplementation;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implements kind of distance measure between base profile and context. If distance is equal 0, then it means that given
 * base profile perfectly match to context. The greater value of distance, the smaller similarity between of base
 * profile to the context.
 * This distance implementation is less strict than this in Distance class - it takes into consideration only this traits
 * which are present in context, what is more reasonable when context does not contain all existed traits.
 * Note: Distance is a number of traits presents in context with differ state values.
 */
public class SoftDistance extends Distance implements Measure {

    public SoftDistance(double maxThreshold) throws InvalidMeasureException {
        super(maxThreshold);
    }

    public SoftDistance() {
        super();
    }

    /**
     * Counts difference between two lists of traits.
     *
     * @param fromBP
     * @param fromContext
     * @return number of differences.
     */
    @Override
    protected int partialDifference(List<Trait> fromBP, List<Trait> fromContext) {
        List<Trait> common = new ArrayList<>(fromContext);
        int fromContextSize = fromContext.size();
        common.retainAll(fromBP);
        return fromContextSize - common.size();
    }
}
