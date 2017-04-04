package com.pwr.zpi;

import javafx.util.Pair;

import java.util.*;

/**
 *
 */

/**
 * Collection of BaseProfiles split into working memory(obszar swiadomy/PR) and long-term memory(obszar przedswiadomy/PT).
 * Contains knowledge collected till moment in time described by timestamp.
 * All operations should be
 */
public class BPCollection {

    /**
     * Describes allowed types of agent's memory. According to the accepted theoretical model, there are two memory
     * levels: Working memory and long-term memory.
     */
    public enum MemoryType {
        WM, LM
    }


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
    private int timestamp;

    public BPCollection(Map<Integer, BaseProfile> workingMemory, Map<Integer, BaseProfile> longTermMemory, int timestamp) {
        this.workingMemory = workingMemory;
        this.longTermMemory = longTermMemory;
        this.timestamp = timestamp;
    }

    /**
     * Returns base profile from pointed memory related with given timestamp.
     * Ratains information about timestamp.
     *
     * @param timestamp Moment in time.
     * @return Base profile from pointed memory related with given timestamp.
     */
    public Pair<Integer, BaseProfile> getTimedBaseProfile(int timestamp, MemoryType memType) {
        switch (memType) {
            case WM:
                return new Pair<>(timestamp, workingMemory.get(timestamp));
            case LM:
                return new Pair<>(timestamp, longTermMemory.get(timestamp));
            default:
                return null;
        }
    }

    /**
     * Returns base profile from pointed memory related with given timestamp.
     *
     * @param timestamp Moment in time.
     * @param memType Specifies type of memory.
     * @return Base profile from pointed memory related with given timestamp.
     */
    public BaseProfile getBaseProfile(int timestamp, MemoryType memType) {
        switch (memType) {
            case WM:
                return workingMemory.get(timestamp);
            case LM:
                return longTermMemory.get(timestamp);
            default:
                return null;
        }
    }

    /**
     * Returns set of base profiles which are associated with moment in time from range [start, given timestamp].
     * @param timestamp
     * @return Map of base profiles.
     * @throws IllegalStateException
     */
    public Set<BaseProfile> getBaseProfiles(int timestamp) throws IllegalStateException{
        return new HashSet<BaseProfile>(getTimedBaseProfiles(timestamp).values());
    }

    /**
     * Returns map of base profiles which are associated with moment in time from range [start, given timestamp].
     * It retains information about associated timestamps.
     * @param timestamp
     * @return Map of base profiles.
     * @throws IllegalStateException
     */
    public Map<Integer, BaseProfile> getTimedBaseProfiles(int timestamp) throws IllegalStateException{
        Map<Integer, BaseProfile> res = new HashMap<>();
        res.putAll(getTimedBaseProfiles(timestamp, MemoryType.LM));
        res.putAll(getTimedBaseProfiles(timestamp, MemoryType.WM));
        return res;
    }

    /**
     * Returns map of base profiles which are associated with moment in time from range [start, given timestamp],
     * differentiated between momory type.
     * It retains information about associated timestamps.
     * @param timestamp
     * @param memType
     * @return Map of base profiles from requested memory.
     * @throws IllegalStateException
     */
    public Map<Integer, BaseProfile> getTimedBaseProfiles(int timestamp, MemoryType memType) throws IllegalStateException{
        if (timestamp <= 0 || timestamp > this.timestamp)
            throw new IllegalStateException("Incorrect timestamp.");
        Map<Integer, BaseProfile> res = new HashMap<>(),
                selectedMem = memType.equals(MemoryType.LM) ? longTermMemory : workingMemory;

        for (Map.Entry<Integer, BaseProfile> entry: selectedMem.entrySet()) {
            if (entry.getKey() <= timestamp)
                res.put(entry.getKey(), entry.getValue());
        }
        return res;
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


    void moveToWM(Map.Entry<Integer, BaseProfile> toMove) {
    }

    void moveToLM(Map.Entry<Integer, BaseProfile> toMove) {
    }

    /**
     * Moves Existing Base Profile from one memory region to another.
     * Assumes two possibles scenarios: shifting from working memory to long-term memory or
     * from long-term memory to working memory.
     *
     * @param from
     * @param to
     */
    void shiftBaseProfile(Map<Integer, BaseProfile> from, Map<Integer, BaseProfile> to) {
    } //todo

    /*public Set<Object> getAffectedObjects(int time) {
        Set<Object> res = new HashSet<>();
        res.addAll(getTimedBaseProfile(time, MemoryType.LM).getValue().)
    }*/

}
