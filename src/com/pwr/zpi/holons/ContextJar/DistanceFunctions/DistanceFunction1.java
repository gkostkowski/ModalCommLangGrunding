package com.pwr.zpi.holons.ContextJar.DistanceFunctions;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.holons.ContextJar.DistanceFunction;
import com.pwr.zpi.semantic.IndividualModel;

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
}
