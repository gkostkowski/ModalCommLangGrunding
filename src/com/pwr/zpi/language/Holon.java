package com.pwr.zpi.language;


import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;

import java.util.List;

/**
 * Holon is representation of agent's reflections on gathered observations. Agent is suppoused to form Holons based
 *  on Traits or Formulas. Holon returns Operator(BEL,POS,KNOW,NOT) ,which has been most frequent in most observations.
 *
 */



public abstract class Holon{
    public abstract void update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException;
    public abstract Pair getStrongest();
    public abstract Pair getWeakest();
    public abstract HolonKind getKind();
    public abstract List<Formula> getFormula();
    public abstract boolean isApplicable(Formula f) throws InvalidFormulaException;
    public enum HolonKind{
        Binary,
        Non_Binary
    }

}