/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.holons.context.builders;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.semantic.IndividualModel;

import java.util.*;

/**
 * This is concrete realisation of ContextBuilder.
 */
public class ConcreteContextBuilder implements ContextBuilder {

    public ConcreteContextBuilder() {
    }

    @Override
    public Context build(IndividualModel relatedObject, Collection<BaseProfile> representatives) {
        if (representatives.size() == 1)
            return new Context(representatives.iterator().next().getRelatedTraits(relatedObject, State.IS),
                    representatives.iterator().next().getRelatedTraits(relatedObject, State.IS_NOT), relatedObject);
        return new Context(partialBuild(representatives, relatedObject, State.IS),
                partialBuild(representatives, relatedObject, State.IS_NOT), relatedObject);
    }

    @Override
    public Context build(IndividualModel relatedObject, BaseProfile... representatives) {
        return build(relatedObject, Arrays.asList(representatives));
    }


        private List<Trait> partialBuild(Collection<BaseProfile> representatives, IndividualModel relatedObject, State state) {
        List<Trait> common = new ArrayList<>();
        Iterator<BaseProfile> iterator = representatives.iterator();
        common.addAll(iterator.next().getRelatedTraits(relatedObject, state));
        while (iterator.hasNext()) {
            BaseProfile currentBP = iterator.next();
            List<Trait> relevant = currentBP.getRelatedTraits(relatedObject, state);

            for (Iterator<Trait> it = common.iterator(); it.hasNext() && !common.isEmpty();) {
                if (!relevant.contains(it.next()))
                    it.remove();
            }
        }
        return common;
    }
}
