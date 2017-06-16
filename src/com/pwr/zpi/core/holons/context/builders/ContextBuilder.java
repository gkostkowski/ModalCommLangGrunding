/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.holons.context.builders;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.core.holons.context.Context;
import com.pwr.zpi.core.semantic.IndividualModel;

import java.util.Collection;

/**
 * This interface provides way to define context builders, which will be responsible for providing context basing on
 * some set of representative base profiles.
 * This is functional interface and thus can be used in lambda expressions.
 */
public interface ContextBuilder {

    /**
     * This method allows to build context from given representatives base profiles, according to relevant individual model.
     * @param relatedObject Relevant IndividualModel.
     * @param representatives Collection of BaseProfile. Can include only one element.
     * @return Generated Context.
     */
    Context build(IndividualModel relatedObject, Collection<BaseProfile> representatives);

    /**
     * This method allows to build context from given representatives, according to relevant individual model, but
     * offers convinience way to pass some certain base profiles or only one base profile.
     * @param relatedObject Relevant IndividualModel.
     * @param representatives Array of BaseProfile. Can include only one element.
     * @return Generated Context.
     * @see Context
     */
    Context build(IndividualModel relatedObject, BaseProfile... representatives);
}
