package com.pwr.zpi.core.memory.holons.ContextJar.DistanceFunctions;

import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.core.memory.holons.ContextJar.DistanceFunction;
import com.pwr.zpi.util.Pair;
import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.core.memory.holons.ContextJar.DistanceFunction;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.core.memory.semantic.IndividualModel;

import java.util.Set;

/**
 * Class containing firt implementation of Distance Function
 * Created by Jarema on 5/24/2017.
 */
public class DistanceFunction1 extends DistanceFunction {

    /**
     * Method counting distance between two Base Profiles
     * @param first
     * @param second
     * @return Number of Individual Models which appear in both Base Profiles.
     */
    @Override
    public double implementation(BaseProfile first,BaseProfile second) {
        double sum=0;

        for (IndividualModel im : first.getAffectedIMs()){
            for(IndividualModel im2:second.getAffectedIMs()){
                if(im.equals(im2)){
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * Method counting distance between Base Profile and Pair of two Sets of Traits
     * @param first
     * @param second
     * @return Number of Traits present in second parameter's SetK and first's observed Traits summed with
     * numer of Traits present in second parameter's SetV and first's Not observed Traits
     *
     */
    @Override
    public double composite(BaseProfile first, Pair<Set<Trait>, Set<Trait>> second) {
        double sum = 0;
        for(Trait t:first.getNotDescribedByTraits().keySet()){
            if(second.getV().contains(t)){
                sum++;
            }
        }
        for(Trait t:first.getDescribedByTraits().keySet()){
            if(second.getK().contains(t)){
                sum++;
            }
        }
        return sum ;
    }
}