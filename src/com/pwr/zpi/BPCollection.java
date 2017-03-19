package com.pwr.zpi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-17.
 */

/**
 * Collection of BaseProfiles splited into working memory(obszar swiadomy/PR) and long-term memory(obszar przedswiadomy/PT).
 * Contains knowledge collected till moment in time described by currTime.
 * All operations should be
 */
public class BPCollection {
    protected Set<NamedCollection<Names, Object>> workingMemory;
    protected Set<NamedCollection<Names, Object>> longTermMemory;
    private int currTime;


    Set<NamedCollection<Names, Object>> getBPCollection() {
        Set<NamedCollection<Names, Object>> res =new HashSet<NamedCollection<Names, Object>>(workingMemory);
        res.addAll(longTermMemory);
        return res;
    }

    void moveToWM(NamedCollection toMove){}
    void moveToLM(NamedCollection toMove) {}

    /**
     * Moves Existing Base Profile from one memory region to another.
     * Assumes two possibles scenarios: shifting from working memory to long-term memory or
     * from long-term memory to working memory.
     * @param from
     * @param to
     */
    void shiftBaseProfile(Set<NamedCollection<Names, Object>> from, Set<NamedCollection<Names, Object>> to) {}

}
