package com.pwr.zpi;

import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.language.DistributedKnowledge;
import com.pwr.zpi.language.Formula;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Agent is crucial class for project - it has members of collections
 * that hold all of data that we are processing.
 */
public class Agent {
    private BPCollection knowledgeBase;
    private IMCollection models;
    private HolonCollection holons;
    public static Collection<ObjectType> objectTypeCollection;

    public Agent() {
        init();
        knowledgeBase = new BPCollection();
        holons = new HolonCollection();
    }

    public Agent(BPCollection knowledgeBase) {
        init();
        this.knowledgeBase = knowledgeBase;
        holons = new HolonCollection();
    }

    public Agent(BPCollection knowledgeBase, IMCollection models) {
        init();
        this.models = models;
        this.knowledgeBase = knowledgeBase;
        holons = new HolonCollection();
    }

    /**
     * Performs initials actions related to loading semantic memory: builds instances of ObjectTypes and IndividualModels.
     * Note: builds IMs according to config file and objects occurences in DB entries.
     */
    private void init() {
        objectTypeCollection = ObjectType.getObjectTypes();
        models = new IMCollection();
    }

    public Agent(BPCollection knowledgeBase, IMCollection models, HolonCollection holons) {
        this.knowledgeBase = knowledgeBase;
        this.models = models;
        this.holons = holons;
    }

    public BPCollection getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(BPCollection knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public IMCollection getModels() {
        return models;
    }

    public void setModels(IMCollection models) {
        this.models = models;
    }

    /**
     * Builds distributed knowledge, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to certain moment in time.
     *
     * @param formula Formula
     * @param time    Certain moment in time.
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     */
    @Nullable
    public DistributedKnowledge distributeKnowledge(Formula formula, int time) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula, time);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds distributed knowledge, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to timestamp of last registered by this agent base profile.
     *
     * @param formula Formula
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     */
    @Nullable
    public DistributedKnowledge distributeKnowledge(Formula formula) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T>void registerObservation(Observation newObservation, int timestamp) {
        models.captureNewIM(newObservation);
        knowledgeBase.addToMemory(newObservation.toBaseProfile());
        //lub
        knowledgeBase.includeNewObservation(newObservation); // domyslnie bedzie

    }

    public void registerBaseProfile(BaseProfile newBp) {
        models.captureNewIM(newBp.getAffectedIMs());
        knowledgeBase.addToMemory(newBp);

    }

    public HolonCollection getHolons() {
        return holons;
    }

    public void setHolons(HolonCollection holons) {
        this.holons = holons;
    }
}
