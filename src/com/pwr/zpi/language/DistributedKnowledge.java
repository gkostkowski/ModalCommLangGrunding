package com.pwr.zpi.language;

import com.pwr.zpi.Agent;
import com.pwr.zpi.Name;
import com.pwr.zpi.NamedCollection;
import com.pwr.zpi.Object;

import java.util.Set;

/**
 * Set of four classes RA1, TA1, RA2, TA2 established for certain base formula
 */
public class DistributedKnowledge {
    Set<NamedCollection<Name, Object>> positiveCurrentlyInWM, negativeCurrentlyInWM,positiveCurrentlyInLM, negativeCurrentlyInLM;

    public DistributedKnowledge(Agent agent, Formula baseFormula, Formula negBaseFormula, int time) {
        agent.getKnowledgeBase().
    }

    //todo gettery, settery, konstr
}
