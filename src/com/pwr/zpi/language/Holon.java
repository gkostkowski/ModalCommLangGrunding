package com.pwr.zpi.language;


/**
 * Holon is representation of agent's reflections on gathered observations. Agent is suppoused to form Holons based
 *  on Traits or Formulas. Holon returns Operator(BEL,POS,KNOW,NOT) ,which has been most frequent in most observations.
 *
 */



public abstract class Holon{

    public abstract Pair getStrongest();
    public abstract Pair getWeakest();
    public abstract HolonKind getKind();
    public abstract Formula getFormula();
    public enum HolonKind{
        Binary,
        Non_Binary
    }

}