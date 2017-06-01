package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.semantic.IndividualModel;

import java.util.Collection;

/**
 * This interface provides way to define context builders, which will be responsible for providing context basing on
 * some set of representative base profiles.
 * This is functional interface and thus can be used in lambda expressions.
 */
public interface ContextBuilder {
    Context build(Collection<BaseProfile> representatives, IndividualModel relatedObject);
}
