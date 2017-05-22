package com.pwr.zpi.episodic;

import com.pwr.zpi.semantic.IndividualModel;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.language.Formula;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This class keeps collection of BaseProfiles split into working memory(obszar swiadomy/PR)
 * and long-term memory(obszar przedswiadomy/PT) and provides complete set of operations on stored base profiles.
 * Contains knowledge collected till moment in time described by timestamp. Timestamp is updated according to last recent
 * timestamp specified among entire base profiles.
 * All operations should be
 */
public class BPCollection {

    private static final int INIT_TIMESTAMP = 0;
    /**
     * Determines what should be happen in case of adding base profile with timestamp, which is already noticed. If true
     * then old base profile will be overridden.
     */
    private static final boolean DEFAULT_OVERRIDE_IF_EXISTS = true;
    private static final int MAX_WM_CAPACITY = 100;

    /**
     * By default, inserts to bp located in wm
     * @param newObservation
     */
    public void includeNewObservation(Observation newObservation, IndividualModel individualModel) {
        int newTimestamp = newObservation.getTimestamp();
        BaseProfile alreadyExisting = null;
        if ((alreadyExisting=getBaseProfile(newTimestamp, MemoryType.WM)) == null) {
            alreadyExisting = new BaseProfile(newObservation.getTimestamp());
             addToMemory(alreadyExisting);
        }

        for (Trait trait: newObservation.getValuedTraits().keySet())
            alreadyExisting.addObservationByValue(individualModel, trait, newObservation.getValuedTraits().get(trait));
    }

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
    protected Set<BaseProfile> workingMemory;
    /**
     * Map of BaseProfile representing long-term memory - each for successive moments in time - from beginning till
     * current timestamp set for this BPCollection.
     */
    protected Set<BaseProfile> longTermMemory;
    private int timestamp;

    /**
     * Simple constructor to initialize empty BPCollection with initials values. Sets timestamp to default value.
     */
    public BPCollection() {
        this.workingMemory = new HashSet<>();
        this.longTermMemory = new HashSet<>();
        this.timestamp = INIT_TIMESTAMP;
    }

    /**
     * Constructor for complete initialization with data given as parameters.
     *
     * @param workingMemory
     * @param longTermMemory
     */
    public BPCollection(Set<BaseProfile> workingMemory, Set<BaseProfile> longTermMemory) {
        if (workingMemory == null || longTermMemory == null)
            throw new NullPointerException("One of parameters is null.");
        this.workingMemory = workingMemory;
        this.longTermMemory = longTermMemory;
        this.timestamp = spotLastTimestamp();
    }

    /**
     * This method infers actual timestamp, basing on the greatest value of timestamp among added base profiles.
     *
     * @return
     */
    private int spotLastTimestamp() {
        Set<BaseProfile> storedBPs = getBaseProfiles(Integer.MAX_VALUE);
        int mostRecent = INIT_TIMESTAMP;
        if (storedBPs != null && !storedBPs.isEmpty())
            mostRecent = storedBPs.stream().mapToInt(BaseProfile::getTimestamp).max().getAsInt();
        return mostRecent;
    }


    /**
     * Adds given new base profile to specified memory type, according to given base profile timestamp.
     * If there is already specified base profile with same timestamp, then two actions are possible:
     * -override old one (default action).
     * -update old one with observations in new one.
     *
     * @param newBPs
     * @param type
     * @param overrideIfExisting
     */
    public void addToMemory(MemoryType type, boolean overrideIfExisting, BaseProfile ... newBPs) {
        for (BaseProfile newBP : newBPs) {
            Set<BaseProfile> affectedMemory = getMemoryContainer(type);
            BaseProfile alreadyExisted = getBaseProfile(newBP.getTimestamp());
            if (alreadyExisted != null) {
                /*if (!overrideIfExisting)
                    BaseProfile.joinBaseProfiles(newBP, alreadyExisted);*/
                affectedMemory.remove(alreadyExisted);
            }
            if (workingMemory.size() == MAX_WM_CAPACITY)
                shiftBaseProfile(MemoryType.WM, MemoryType.LM, getOldestBP(MemoryType.WM));
            affectedMemory.add(newBP);
            updateTimestamp(newBP);
        }
    }

    private BaseProfile getOldestBP(MemoryType memoryType) {
        return getMemoryContainer(memoryType).stream().min(Comparator.comparing(BaseProfile::getTimestamp)).get();
    }

    /**
     * By default, adds new bp to working memory.
     *
     * @param newBPs
     */
    public void addToMemory(BaseProfile ... newBPs) {
        addToMemory(MemoryType.WM, newBPs);
    }

    /**
     * Performs adding with default behaviour for base profiles with already noticed timestamp.
     *
     * @param newBPs
     * @param type
     */
    public void addToMemory(MemoryType type, BaseProfile ... newBPs) {
        addToMemory(type, DEFAULT_OVERRIDE_IF_EXISTS, newBPs);
    }

    /**
     * Removes one or more base profiles from specified memory.
     *
     * @param oldBPs Instances of BaseProfile.
     * @param type
     * @return Boolean informs if profile was removed. False if all base profiles weren't present in specified memory.
     */
    public boolean deleteFromMemory(MemoryType type, List<BaseProfile> oldBPs) {
        if (type == null || oldBPs == null)
            throw new NullPointerException("One or more parameters are nulls.");
        boolean res = false;
        Set<BaseProfile> affectedMemory = getMemoryContainer(type);
        for (BaseProfile bp : oldBPs)
            res = affectedMemory.remove(bp) || res;
        return res;
    }

    public boolean deleteFromMemory(MemoryType type, BaseProfile... oldBPs) {
        return deleteFromMemory(type, Arrays.asList(oldBPs));
    }

    /**
     * Removes one or more base profiles from specified memory according to given timestamps.
     */
    public boolean deleteFromMemory(MemoryType type, int... oldBPTimestamps) {
        List<BaseProfile> baseProfilesCollection = Arrays.stream(oldBPTimestamps).mapToObj(t -> getBaseProfile(t, type))
                .collect(Collectors.toList());
        if (baseProfilesCollection != null && !baseProfilesCollection.isEmpty())
            return deleteFromMemory(type, baseProfilesCollection);
        return false;
    }

    /**
     * Updates timestamp if required and return boolean informed if it was performed.
     * The main difference between this method and spotLastTimestamp is that this method assumes that so far
     * current timestamp has been set properly and takes into consideration only  base profiles
     * pointed as new (provided as parameter).
     *
     * @param newBPs
     * @return
     */
    private boolean updateTimestamp(BaseProfile... newBPs) {
        if (newBPs == null || newBPs.length == 0)
            throw new IllegalStateException("Array not specified or empty.");
        boolean needUpdate = false;
        int mostRecentTimestamp;

        if (newBPs.length == 1)
            mostRecentTimestamp = newBPs[0].timestamp;
        else mostRecentTimestamp = Arrays.stream(newBPs).mapToInt(BaseProfile::getTimestamp).max().getAsInt();

        if (mostRecentTimestamp > this.timestamp) {
            needUpdate = true;
            this.timestamp = mostRecentTimestamp;
        }
        return needUpdate;
    }


    /**
     * Returns base profile from any memory related with exact timestamp, given as parameter.
     * Useful when checking if base profile for given timestamp exists.
     *
     * @param timestamp Moment in time.
     * @return Base profile from pointed memory related with given timestamp.
     */
    public BaseProfile getBaseProfile(int timestamp) {
        BaseProfile res = getBaseProfile(timestamp, MemoryType.WM);
        return res != null ? res : getBaseProfile(timestamp, MemoryType.LM);
    }

    /**
     * Returns base profile from pointed memory related with exact timestamp, given as parameter.
     *
     * @param timestamp Moment in time.
     * @param memType   Specifies type of memory.
     * @return Base profile from pointed memory related with given timestamp.
     */
    public BaseProfile getBaseProfile(int timestamp, MemoryType memType) {
        if (timestamp < 0)
            throw new IllegalStateException("Not valid timestamp.");
        if (memType == null)
            throw new NullPointerException("Memory type not specified.");
        Set<BaseProfile> affectedMemory = getMemoryContainer(memType);
        Optional<BaseProfile> potentialRes = affectedMemory.stream().filter(bp -> bp.getTimestamp() == timestamp).findAny();
        return potentialRes.isPresent() ? potentialRes.get() : null;
    }


    /**
     * Returns set of ALL base profiles from both memories which are associated with
     * moment in time from range [start, given endTimestamp]
     *
     * @param endTimestamp
     * @return Set of base profiles.
     * @throws IllegalStateException
     */
    public Set<BaseProfile> getBaseProfiles(int endTimestamp) throws IllegalStateException {
        if (endTimestamp < 0)
            throw new IllegalStateException("Incorrect endTimestamp.");
        Set<BaseProfile> res = new HashSet<>();
        res.addAll(getBaseProfiles(endTimestamp, MemoryType.LM));
        res.addAll(getBaseProfiles(endTimestamp, MemoryType.WM));
        return res;
    }

    public Set<BaseProfile> getBaseProfiles() throws IllegalStateException {
        return getBaseProfiles(timestamp);
    }

    /**
     * Flatten given array of base profiles set to single set with base profiles.
     * @param bps
     * @return
     * @throws IllegalStateException
     */
    public static Set<BaseProfile> asBaseProfilesSet(Set<BaseProfile>... bps) throws IllegalStateException {
        if (bps == null)
            throw new NullPointerException("Array is null.");
        Set<BaseProfile> res = new HashSet<>();
        for (Set<BaseProfile> bp : bps)
            res.addAll(bp);
        return res;
    }


    /**
     * Returns set of base profiles which are associated with moment in time from range [beginning, given endTimestamp],
     * from specified memory.
     *
     * @param endTimestamp
     * @param memType
     * @return Set of base profiles from requested memory.
     * @throws IllegalStateException
     */
    public Set<BaseProfile> getBaseProfiles(int endTimestamp, MemoryType memType) throws IllegalStateException {
        if (endTimestamp < 0)
            throw new IllegalStateException("Incorrect endTimestamp.");
        Set<BaseProfile> affectedMemory = getMemoryContainer(memType);
        return affectedMemory.stream().filter(bp -> bp.timestamp <= endTimestamp).collect(Collectors.toSet());
    }

    /**
     * Shifts given BaseProfile from source memory to destination memory.
     * Note: adding to destination will be performed even if given base profile is wasn't present in source to
     * keep state after operation consistent.
     *
     * @param toMove
     */
    void shiftBaseProfile(MemoryType src, MemoryType dest, BaseProfile toMove) {
        if (!src.equals(dest)) {
            getMemoryContainer(src).remove(toMove);
            addToMemory(dest, true, toMove);
        }
    }

    /**
     * Duplicate given BaseProfile placed in source memory to destination memory.
     * Throws exception if source doesn't contain given base profile.
     *
     * @param toDuplicate
     */
    public void duplicateBaseProfile(MemoryType src, MemoryType dest, BaseProfile toDuplicate) {
        if (!src.equals(dest))
            addToMemory(dest, true, toDuplicate);
        if (!new ArrayList<>(getMemoryContainer(src)).contains(toDuplicate))
            throw new IllegalStateException("Memory specified as source doesn't contain given base profile.");
    }

    /**
     * Returns memory which contains given base profile.
     *
     * @param bp
     * @return
     */
    public Set<BaseProfile> determineIncludingMemory(BaseProfile bp) {
        Set<BaseProfile> res = null;
        if (new ArrayList<>(workingMemory).contains(bp))
            res = workingMemory;
        else if (new ArrayList<>(longTermMemory).contains(bp))
            res = longTermMemory;
        return res;
    }

    /**
     * Returns memory container specified by memory type.
     * @param type
     * @return
     */
    public Set<BaseProfile> getMemoryContainer(MemoryType type) {
        switch (type) {
            case LM:
                return longTermMemory;
            case WM:
                return workingMemory;
            default:
                return null;
        }
    }

    /**
     * Gives Individual models from specified memory sources which contains given trait and value of this trait is
     * same as required. Timestamp is used to takes into consideration only this observations which are related
     * with time range: [initial time, endTimestamp].
     *
     * @param trait
     * @param state
     * @param endTimestamp
     * @param affectedMemories
     * @return
     */
    public Set<IndividualModel> getIMsByTraitState(Trait trait, State state, int endTimestamp, MemoryType... affectedMemories) {
        if (affectedMemories == null)
            throw new NullPointerException("Memory sources not specified.");
        if (endTimestamp < 0)
            throw new IllegalStateException("Not valid endTimestamp.");

        Set<IndividualModel> res = new HashSet<>();
        for (MemoryType mem : affectedMemories) {
            Set<BaseProfile> observations = getBaseProfiles(endTimestamp, mem);
            for (BaseProfile bp : observations) {
                Set<IndividualModel> matchedIMs = bp.getIMsByTraitState(trait, state);
                if (matchedIMs != null && !matchedIMs.isEmpty())
                    res.addAll(matchedIMs);
            }

        }
        return res;
    }

    public double getMayhapsNumber(int endTimestamp,Formula formula,int i){
        Set<BaseProfile> out = new HashSet<>();
        Set<BaseProfile> observations = getBaseProfiles(endTimestamp, MemoryType.LM);
        Set<BaseProfile> observations2 = getBaseProfiles(endTimestamp, MemoryType.WM);
        observations.addAll(observations2);
        double suma = 0;
        for (BaseProfile bp : observations) {
            if(bp.checkIfObserved(formula.getModel(), formula.getTraits().get(i), State.MAYHAPS)){
                suma++;
            }
        }

        System.out.println("Suma " +suma + " obser " + observations.size() + " / "  + suma/(observations.size()) );
        if(suma>0){
        return suma/observations.size();}
        else return 0.0;
    }

    public double getCompleteSize(int endTimestamp) {
        Set<BaseProfile> observations = getBaseProfiles(endTimestamp, MemoryType.LM);
        Set<BaseProfile> observations2 = getBaseProfiles(endTimestamp, MemoryType.WM);
        observations.addAll(observations2);
        return observations.size();
    }
    public Set<IndividualModel> getIMsByTraitState(Trait trait, State state, int endTimestamp) {
        return getIMsByTraitState(trait, state, endTimestamp, MemoryType.WM, MemoryType.LM);
    }

    public Set<IndividualModel> getIMsByTraitStates(Trait trait, State[] states, int endTimestamp) {
        Set<IndividualModel> res = new HashSet<>();
        for (State s : states)
            res.addAll(getIMsByTraitState(trait, s, endTimestamp, MemoryType.WM, MemoryType.LM));
        return res;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setWorkingMemory(Set<BaseProfile> workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void setLongTermMemory(Set<BaseProfile> longTermMemory) {
        this.longTermMemory = longTermMemory;
    }

    public Set<BaseProfile> getWorkingMemory() {
        return workingMemory;
    }

    public Set<BaseProfile> getLongTermMemory() {
        return longTermMemory;
    }

}
