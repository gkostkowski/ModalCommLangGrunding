package com.pwr.zpi;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Grzesiek on 2017-03-17.
 */

/**
 * Collection of BaseProfiles splited into working memory(obszar swiadomy) and long-term memory(obszar przedswiadomy)
 */
public class BPCollection {
    protected Set<NamedCollection<Names, Object>> workingMemory;
    protected Set<NamedCollection<Names, Object>> longTermMemory;
    private int currTime;

}
