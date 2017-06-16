package com.pwr.zpi.core.holons.ContextJar.DistanceFunctions;

import com.pwr.zpi.core.episodic.BaseProfile;
import com.pwr.zpi.core.holons.ContextJar.DistanceFunction;
import com.pwr.zpi.language.Pair;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.core.semantic.IndividualModel;

import java.util.Set;

/**
 * Created by Jarema on 5/24/2017.
 */
public class DistanceFunction1 extends DistanceFunction {


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