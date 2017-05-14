package com.pwr.zpi;

import com.pwr.zpi.exceptions.InvalidFormulaException;

import java.util.Collection;

/**
 * Agent is crucial class for project - it has members of collections
 * that hold all of data that we are processing.
 */
public class Agent {
    private BPCollection knowledgeBase;
    private IMCollection models;
    private HolonCollection holons;
    private DatabaseAO database;
    public static Collection<ObjectType> objectTypeCollection;
    private boolean listeningMode;
    private long updatefrequencyInMs = 1000;


    public Agent() {
        init();
        knowledgeBase = new BPCollection();
        holons = new HolonCollection();
        database = new DatabaseAO(this);
    }

    public Agent(String databaseFilename) {
        init();
        knowledgeBase = new BPCollection();
        holons = new HolonCollection();
        database = new DatabaseAO(this, databaseFilename);
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

/*    public Agent(BPCollection knowledgeBase, IMCollection models, HolonCollection holons) {
        this.knowledgeBase = knowledgeBase;
        this.models = models;
        this.holons = holons;
    }*/

    /**
     * Performs initials actions related to loading semantic memory: builds instances of ObjectTypes and IndividualModels.
     * Note: builds IMs according to config file and objects occurrences in DB entries.
     */
    private void init() {
        objectTypeCollection = ObjectType.getObjectTypes();
        models = new IMCollection();
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

    public HolonCollection getHolons()
    {
        return holons;
    }

    public DatabaseAO getDatabase() {
        return database;
    }
    /*    public HolonCollection getHolons() {
        return holons;
    }

    public void setHolons(HolonCollection holons) {
        this.holons = holons;
    }*/

   /* *//**
     * Builds distributed knowledge, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to certain moment in time.
     *
     * @param formula Formula
     * @param time    Certain moment in time.
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     *//*
    @Nullable
    public DistributedKnowledge distributeKnowledge(Formula formula, int time) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula, time);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    *//**
     * Builds distributed knowledge, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to timestamp of last registered by this agent base profile.
     *
     * @param formula Formula
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     *//*
    @Nullable
    public DistributedKnowledge distributeKnowledge(Formula formula) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }
*/

    /**
     * This is method for realising one of fundamental task: registering task. Precisely, this method includes
     * observation saved in agent database in program for processing.
     */
    public void discoverObservations(Collection<Observation> newObservations) {
        System.out.println("Discovering new observations ...");

        if (newObservations != null && !newObservations.isEmpty()) {
            System.out.println("Processing " + newObservations.size() + " new observation(s).");
            for (Observation obs : newObservations)
                registerObservation(obs);
        }

    }

    public void registerObservation(Observation newObservation) {
        IndividualModel relatedIM = models.captureNewIM(newObservation);

        knowledgeBase.includeNewObservation(newObservation, relatedIM);
    }

    public void registerBaseProfile(BaseProfile newBp) {
        models.captureNewIM(newBp.getAffectedIMs());
        knowledgeBase.addToMemory(newBp);

    }

    public void addObservationToDatabase(Observation observation) {
        database.addNewObservation(observation);
    }
}
