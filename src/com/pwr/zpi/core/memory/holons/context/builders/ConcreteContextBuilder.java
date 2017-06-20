/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.holons.context.builders;

import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.core.memory.holons.context.Context;
import com.pwr.zpi.exceptions.InvalidContextException;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.core.memory.semantic.IndividualModel;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is concrete realisation of ContextBuilder.
 */
public class ConcreteContextBuilder implements ContextBuilder {

    public ConcreteContextBuilder() {
    }

    @Override
    public Context build(IndividualModel relatedObject, Collection<BaseProfile> representatives) {
        if (representatives.isEmpty()) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Empty collection of representatives.");
            return null;
        }
        if (representatives.size() == 1)
            return new Context(representatives.iterator().next().getRelatedTraits(relatedObject, State.IS),
                    representatives.iterator().next().getRelatedTraits(relatedObject, State.IS_NOT), relatedObject);

        Context res = null;
        try {
            res = new Context(partialBuild(representatives, relatedObject, State.IS),
                    partialBuild(representatives, relatedObject, State.IS_NOT), relatedObject);
        } catch (InvalidContextException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Built context is empty - provided set of base profiles " +
                    "is too diverse.", e);
        }
        if (res.isEmpty())
            Logger.getAnonymousLogger().log(Level.WARNING, "Built context is empty - provided set of base profiles " +
                    "is too diverse.");
        return res;
    }

    @Override
    public Context build(IndividualModel relatedObject, BaseProfile... representatives) {
        return build(relatedObject, Arrays.asList(representatives));
    }


    private List<Trait> partialBuild(Collection<BaseProfile> representatives, IndividualModel relatedObject, State state) throws InvalidContextException {
        List<Trait> common = new ArrayList<>();
        Iterator<BaseProfile> iterator = representatives.iterator();
        common.addAll(iterator.next().getRelatedTraits(relatedObject, state));
        while (iterator.hasNext()) {
            BaseProfile currentBP = iterator.next();
            List<Trait> relevant = currentBP.getRelatedTraits(relatedObject, state);

            for (Iterator<Trait> it = common.iterator(); it.hasNext() && !common.isEmpty(); ) {
                if (!relevant.contains(it.next()))
                    it.remove();
            }
        }
        return common;
    }
}
