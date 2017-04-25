package com.pwr.zpi;
import com.pwr.zpi.Observation;
import com.pwr.zpi.Trait;
import com.pwr.zpi.language.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HolonCollection {

    private static Set<Holon> holonCollection;

    public HolonCollection()
    {

    }

    public HolonCollection(Set<Holon> holonCollection)
    {
        this.holonCollection = holonCollection;
    }


    public Holon addHolon(Formula formula)
    {
        //todo
        return null;
    }

    public static Holon findHolon(Formula formula)
    {
        for(Holon holon : holonCollection)
            if(holon.getFormula().equals(formula))
                return holon;
        return null;
    }







}
