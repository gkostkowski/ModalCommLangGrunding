package com.pwr.zpi.episodic;

import com.pwr.zpi.semantic.IndividualModel;
import com.pwr.zpi.language.State;
import com.pwr.zpi.language.Trait;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Represents state of world from agent perspective. It's established for certain moment in time.
 */
public class  BaseProfile {
    /**
     * Map of valuedTraits and related collections of individual models. If some collection is related with certain trait, then
     * it mean that all observations in that collections HAVE this trait.
     */
    protected Map<Trait, Set<IndividualModel>> describedByTraits;
    /**
     * Map of valuedTraits and related collections of individual models. If some collection is related with certain trait, then
     * it mean that all observations in that collections DON'T HAVE this trait.
     */
    protected Map<Trait, Set<IndividualModel>> notDescribedByTraits;
    /**
     * Map of valuedTraits and related collections of individual models. If some collection is related with certain trait, then
     * it mean that state of having this trait by all observations is unknown. They are neither described nor not described
     * by given trait.
     */
    protected Map<Trait, Set<IndividualModel>> indefiniteByTraits;
    protected int timestamp;

    //protected Set<Observation> observations;

    /**
     * Common constructor for initializing new empty base profile related with given timestamp.
     * @param timestamp
     */
    public BaseProfile(int timestamp) {
        if (timestamp < 0)
            throw new IllegalStateException("Not valid timestamp.");
        this.describedByTraits = new HashMap<>();
        this.notDescribedByTraits = new HashMap<>();
        this.indefiniteByTraits = new HashMap<>();
        this.timestamp = timestamp;
    }

    /**
     * Constructor used for creating complete base profile object.
     * @param baseProfileMaps Maps should be in following order: describedByTraits, notDescribedByTraits,
     *                        indefiniteByTraits
     * @param timestamp
     */
    private BaseProfile(List<Map<Trait, Set<IndividualModel>>> baseProfileMaps, int timestamp) {
        if (timestamp < 0)
            throw new IllegalStateException("Not valid timestamp.");
        if (baseProfileMaps == null || baseProfileMaps.size() < 3)
            throw new IllegalStateException("Not valid map of base profiles.");
        for (Map<Trait, Set<IndividualModel>> map : baseProfileMaps)
            if (map == null)
                throw new IllegalStateException("Not valid map of base profiles.");

        this.timestamp = timestamp;
        this.describedByTraits = baseProfileMaps.get(0);
        this.notDescribedByTraits = baseProfileMaps.get(1);
        this.indefiniteByTraits = baseProfileMaps.get(2);
    }


    public Map<Trait, Set<IndividualModel>> getDescribedByTraits() {
        return describedByTraits;
    }
    public Map<Trait, Set<IndividualModel>> getNotDescribedByTraits() {
        return notDescribedByTraits;
    }
    public Map<Trait, Set<IndividualModel>> getIndefiniteByTraits() {
        return indefiniteByTraits;
    }
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    public void setDescribedByTraits(Map<Trait, Set<IndividualModel>> describedByTraits) {
        this.describedByTraits = describedByTraits;
    }
    public void setNotDescribedByTraits(Map<Trait, Set<IndividualModel>> notDescribedByTraits) {
        this.notDescribedByTraits = notDescribedByTraits;
    }
    public void setIndefiniteByTraits(Map<Trait, Set<IndividualModel>> indefiniteByTraits) {
        this.indefiniteByTraits = indefiniteByTraits;
    }


    /**
     * Returns set of individual models related to this base profile.
     *
     * @return New set containing Individual models associated with describedByTraits, notDescribedByTraits,
     * indefiniteByTraits sets.
     */
    public Set<IndividualModel> getAffectedIMs() {
        Set<IndividualModel> res = new HashSet<IndividualModel>();
        getAffectedIMs(describedByTraits, res);
        getAffectedIMs(notDescribedByTraits, res);
        getAffectedIMs(indefiniteByTraits, res);
        return res;
    }

    /**
     * Returns set of individual models related to given map in this base profile.
     *
     * @return New set containing Individual models associated with one of following: describedByTraits, notDescribedByTraits
     * or indefiniteByTraits set.
     */
    private Set<IndividualModel> getAffectedIMs(Map<Trait, Set<IndividualModel>> relatedMap) {
        return getAffectedIMs(relatedMap, new HashSet<>());
    }

    /**
     * Returns set of individual models related to given map in this base profile.
     *
     * @param relatedMap One of following (for this base profile): describedByTraits, notDescribedByTrait
     *                   or indefiniteByTraits set.
     * @param resultedSet Set witch will be extended to resulted individual models.
     * @return Given as parameter set extended to set of Individual models associated with given map.
     */
    private Set<IndividualModel> getAffectedIMs(Map<Trait, Set<IndividualModel>> relatedMap, Set<IndividualModel> resultedSet) {
        for (Set<IndividualModel> set : relatedMap.values())
            resultedSet.addAll(set);
        return resultedSet;
    }

    /**
     * Returns set of all individual models included in given base profiles. Used when as an example merging base profiles
     * from working memory and long-term memory.
     *
     * @param baseProfileSet Set of base profiles.
     * @return Set of observations.
     */
    public static Set<IndividualModel> getAffectedIMs(Collection<BaseProfile> baseProfileSet) {
        Set<IndividualModel> res = new HashSet<>();
        for (BaseProfile bp : baseProfileSet)
            res.addAll(bp.getAffectedIMs());
        return res;
    }

    public static Set<IndividualModel> getAffectedIMs(BaseProfile ... baseProfileArr) {
        return getAffectedIMs(Arrays.asList(baseProfileArr));
    }


/*    public void setObservations(Set<Observation> observations) {
        this.observations = new HashSet<>(observations);
    }*/

    /**
     * Returns set of individual models described with given trait. Namely, resulted set contains individual models
     * which were observed as having given trait.
     *
     * @param trait
     * @return Set of Individual models.
     */
    public Set<IndividualModel> getIMsDescribedByTrait(Trait trait) {
        return describedByTraits.get(trait);
    }

    /**
     * Returns set of individual models not described with given trait. Namely, resulted set contains individual models
     * which were observed as not having given trait.
     *
     * @param trait
     * @return Set of Individual models.
     */
    public Set<IndividualModel> getIMsNotDescribedByTrait(Trait trait) {
        return notDescribedByTraits.get(trait);
    }

    /**
     * Returns set of individual models which weren't associated with given trait, so there are no reason to claim whether
     * they have or not have given trait.
     *
     * @param trait
     * @return Set of Individual models.
     */
    public Set<IndividualModel> getIMsIndefiniteByTrait(Trait trait) {
        return indefiniteByTraits.get(trait);
    }

    /**
     * Gives Individual models from appropriate sets. State indicates individual models trait's state, which is used
     * as selection criteria.
     * For given trait, state describes which individual models should be selected: describedByTraits for State.IS etc.
     *
     * @param trait
     * @return Set of observations.
     */
    public Set<IndividualModel> getIMsByTraitState(Trait trait, State state) {
        switch (state) {
            case IS:
                return describedByTraits.get(trait);
            case IS_NOT:
                return notDescribedByTraits.get(trait);
            default:
                return indefiniteByTraits.get(trait);
        }
    }

    /**
     * This method determines weather given object was included in this base profile observations and if
     * one of its trait's value is as expected. This expectation is expressed through state parameter.
     * @param object
     * @param trait
     * @param state
     * @return
     */
    public boolean checkIfObserved(IndividualModel object, Trait trait, State state) {
        if (object == null || trait == null || state == null)
            throw new NullPointerException("One of parameters is null.");
        switch (state) {
            case IS:
                return describedByTraits.containsKey(trait) && new ArrayList<>(describedByTraits.get(trait)).contains(object);
            case IS_NOT:
                return notDescribedByTraits.containsKey(trait) && new ArrayList<>(notDescribedByTraits.get(trait)).contains(object);
            case MAYHAPS:
                return indefiniteByTraits.containsKey(trait) && new ArrayList<>(indefiniteByTraits.get(trait)).contains(object);
            default: return false;
        }
    }

    public void addDescribedObservations(Set<IndividualModel> individualModels, Trait relatedTrait/*, int timestamp*/) {
        //if (timestamp == this.timestamp)
        if (individualModels == null || relatedTrait == null)
            throw new NullPointerException("One of parameters is null.");
        describedByTraits.put(relatedTrait, individualModels);
        //else throw new IllegalStateException("Given observation not belong to this BP.");
    }

    public void addNotDescribedObservations(Set<IndividualModel> individualModels, Trait relatedTrait) {
        if (individualModels == null || relatedTrait == null)
            throw new NullPointerException("One of parameters is null.");
        notDescribedByTraits.put(relatedTrait, individualModels);
    }

    public void addIndefiniteObservations(Set<IndividualModel> individualModels, Trait relatedTrait) {
        if (individualModels == null || relatedTrait == null)
            throw new NullPointerException("One of parameters is null.");
        indefiniteByTraits.put(relatedTrait, individualModels);
    }

    public void addDescribedObservation(IndividualModel individualModel, Trait relatedTrait/*, int timestamp*/) {
        if (individualModel == null || relatedTrait == null)
            throw new NullPointerException("One of parameters is null.");
        if (!describedByTraits.containsKey(relatedTrait))
            describedByTraits.put(relatedTrait, new HashSet<>());
        describedByTraits.get(relatedTrait).add(individualModel);
    }

    public void addNotDescribedObservation(IndividualModel individualModel, Trait relatedTrait) {
        if (individualModel == null || relatedTrait == null)
            throw new NullPointerException("One of parameters is null.");
        if (!notDescribedByTraits.containsKey(relatedTrait))
            notDescribedByTraits.put(relatedTrait, new HashSet<>());
        notDescribedByTraits.get(relatedTrait).add(individualModel);
    }

    public void addIndefiniteObservation(IndividualModel individualModel, Trait relatedTrait) {
        if (individualModel == null || relatedTrait == null)
            throw new NullPointerException("One of parameters is null.");
        if (!indefiniteByTraits.containsKey(relatedTrait))
            indefiniteByTraits.put(relatedTrait, new HashSet<>());
        indefiniteByTraits.get(relatedTrait).add(individualModel);
    }

    /**
     * Adds given observation to base profile. Destination group is selected according to given boolean value.
     * @param individualModel
     * @param relatedTrait
     * @param value
     */
    public void addObservationByValue(IndividualModel individualModel, Trait relatedTrait, Boolean value) {
        if (value == null)
            addIndefiniteObservation(individualModel, relatedTrait);
        else if (value)
            addDescribedObservation(individualModel, relatedTrait);
        else if (!value)
            addNotDescribedObservation(individualModel, relatedTrait);
    }

    public void copy(BaseProfile other) {
        setDescribedByTraits(new HashMap<>(other.getDescribedByTraits()));
        setNotDescribedByTraits(new HashMap<>(other.getNotDescribedByTraits()));
        setIndefiniteByTraits(new HashMap<>(getIndefiniteByTraits()));
        setTimestamp(other.getTimestamp());
        //setObservations(other.getAffectedIMs());
    }

    /**
     * Joins observations from all given profiles into one base profile (it's the first one - will be modified ).
     * @param baseProfiles
     */
    public static void joinBaseProfiles(BaseProfile ... baseProfiles) {
        if (baseProfiles == null || baseProfiles.length ==0)
            throw new IllegalStateException("Array not specified or empty.");
        if (baseProfiles.length>1) {
            BaseProfile toModify = baseProfiles[0];
            throw new NotImplementedException(); //todo implement if required
        }
    }

    public boolean DetermineIfSetHasTrait(Trait P, int time) {
        return describedByTraits.containsKey(P);
    }

    /**
     * //todo co to robi
     * @param P
     * @param time
     * @return
     */
    public boolean DetermineIfSetHasNotTrait(@SuppressWarnings("rawtypes") Trait P, int time) {
        return notDescribedByTraits.containsKey(P);
    }

    /**
     * This methods determines if given IM is described in this base profile (according to given trait) and if this
     * description is unambiguous - namely, state for such trait is IS or IS_NOT.
     * @param model
     * @param selectedTrait
     * @return
     */
    public boolean isContainingClearDescriptionFor(IndividualModel model, Trait selectedTrait) {
        Set<IndividualModel> objects = getIMsByTraitState(selectedTrait, State.IS);
        boolean res = objects != null && new ArrayList(objects).contains(model);
        if (!res) {
            objects = getIMsByTraitState(selectedTrait, State.IS_NOT);
            res = objects != null && new ArrayList(objects).contains(model);
        }
        return res;
    }

    /**
     * @return set of all traits described in this base profile.
     */
    public Set<Trait> getAllTraits() {
        Set<Trait> res = new HashSet<>();
        res.addAll(describedByTraits.keySet());
        res.addAll(notDescribedByTraits.keySet());
        res.addAll(indefiniteByTraits.keySet());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseProfile that = (BaseProfile) o;

        if (getTimestamp() != that.getTimestamp()) return false;
        if (!getDescribedByTraits().equals(that.getDescribedByTraits())) return false;
        if (!getNotDescribedByTraits().equals(that.getNotDescribedByTraits())) return false;
        return getIndefiniteByTraits().equals(that.getIndefiniteByTraits());
    }

    @Override
    public int hashCode() {
        int result = getDescribedByTraits().hashCode();
        result = 31 * result + getNotDescribedByTraits().hashCode();
        result = 31 * result + getIndefiniteByTraits().hashCode();
        result = 31 * result + getTimestamp();
        return result;
    }
}
