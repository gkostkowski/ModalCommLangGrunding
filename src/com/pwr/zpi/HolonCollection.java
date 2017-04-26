package com.pwr.zpi;
import com.pwr.zpi.language.*;

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


    public static Holon addHolon(Formula formula)
    {
       // Holon holon = new Holon() todo
        return null;
    }

    public static Holon findHolon(Formula formula)
    {
        for(Holon holon : holonCollection)
            if(holon.getFormula().equals(formula))
                return holon;
        return addHolon(formula);
    }







}
