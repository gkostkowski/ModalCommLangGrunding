package com.pwr.zpi.language;


/**
 * Holon is representation of agent's reflections on gathered observations. Agent is suppoused to form Holons based
 *  on Traits or Formulas. Holon returns Operator(BEL,POS,KNOW,NOT) ,which has been most frequent in most observations.
 *
 */
abstract class Holon {

    enum Type {
        BINARY,
        NON_BINARY
    }
    abstract com.pwr.zpi.language.Operators.Type getDominant();
}