package com.pwr.zpi;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by Grzesiek on 2017-03-17.
 */

/**
 * Collection of BaseProfiles split into working memory(obszar swiadomy/PR) and long-term memory(obszar przedswiadomy/PT).
 * Contains knowledge collected till moment in time described by timestamp.
 * All operations should be
 */
public class BPCollection {

    /**
     * Map of BaseProfile representing working memory - each for successive moments in time - from beginning till
     * current timestamp set for this BPCollection.
     */
    protected Map<Integer, BaseProfile> workingMemory;
    /**
     * Map of BaseProfile representing long-term memory - each for successive moments in time - from beginning till
     * current timestamp set for this BPCollection.
     */
    protected Map<Integer, BaseProfile> longTermMemory;
    //protected NamedCollection<Integer, BaseProfile> longTermMemory;
    private int timestamp;


    /**
     * Returns base profile from long-time memory related with given timestamp.
     * Ratains information about timestamp.
     * @param timestamp Moment in time.
     * @return Base profile from long time memory related with given timestamp.
     */
    public Pair<Integer, BaseProfile> getTimedBaseProfile(int timestamp) {
        return new Pair<>(timestamp, workingMemory.get(timestamp));
    }
    /**
     * Returns base profile from long time memory related with given timestamp.
     * @param timestamp Moment in time.
     * @return Base profile from long time memory related with given timestamp.
     */
    public BaseProfile getBaseProfile(int timestamp) {
        return workingMemory.get(timestamp);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setWorkingMemory(Map<Integer, BaseProfile> workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void setLongTermMemory(Map<Integer, BaseProfile> longTermMemory) {
        this.longTermMemory = longTermMemory;
    }

    public Map<Integer, BaseProfile> getWorkingMemory() {
        return workingMemory;
    }

    public Map<Integer, BaseProfile> getLongTermMemory() {
        return longTermMemory;
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
    void shiftBaseProfile(Map<Integer, BaseProfile> from, Map<Integer, BaseProfile>  to) {} //todo

}
