package com.pwr.zpi.holons.context;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.semantic.IndividualModel;

import java.util.Collection;

/**
 * Created by Grzesiek on 2017-06-01.
 */
public class ConcreteContextBuilder implements ContextBuilder {
    @Override
    public Context build(Collection<BaseProfile> representatives, IndividualModel relatedObject) {
        for (BaseProfile bp: representatives)
            bp.getIMDescription(relatedObject);
    }
}
