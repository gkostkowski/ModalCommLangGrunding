package com.pwr.zpi;

import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-16.
 */
public class ExtendedObject extends Object {
    private Set<Trait> positive; //traits which were observed in object
    private Set<Trait> negative; //traits which were not observed in object
    private Set<Trait> unknown; //traits which state couldn't be established
}
