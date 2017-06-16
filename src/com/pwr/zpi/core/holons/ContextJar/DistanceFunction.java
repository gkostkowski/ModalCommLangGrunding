package com.pwr.zpi.core.holons.ContextJar;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.language.Pair;
import com.pwr.zpi.language.Trait;

import java.util.Set;

/**
 * Created by Jarema on 5/24/2017.
 */
public abstract class DistanceFunction {

    public abstract double implementation(BaseProfile first,BaseProfile second);
    public abstract double composite(BaseProfile first,Pair<Set<Trait>,Set<Trait>> second);
}
