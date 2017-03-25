package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;

import java.util.Map;
import java.util.Set;

/**
 * Set of four classes RA1 established for certain base formula and timestamp.
 */
public class DistributedKnowledge {
    Set<BaseProfile> RA1, TA1, RA2, TA2;

    Set<NamedCollection<Name, Object>> positiveCurrentlyInWM, negativeCurrentlyInWM,positiveCurrentlyInLM, negativeCurrentlyInLM;

    public DistributedKnowledge(Agent agent, Formula baseFormula, Formula negBaseFormula, int time) {
        Map<Integer, BaseProfile> inLM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.LM);
        Map<Integer, BaseProfile> inWM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.WM);
        RA1 = Grounder.getGroundingSetsNegativeTrait()
        RA2 =
        TA1 =
        TA2 =
    }

    //todo gettery, settery, konstr
}
