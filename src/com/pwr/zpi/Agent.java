package com.pwr.zpi;

import com.pwr.zpi.conversation.VoiceConversation;
import com.pwr.zpi.episodic.BPCollection;
import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.episodic.Observation;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.holons.HolonCollection;
import com.pwr.zpi.holons.HolonsIntercessor;
import com.pwr.zpi.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.io.DatabaseAO;
import com.pwr.zpi.language.ComplexFormula;
import com.pwr.zpi.language.DistributedKnowledge;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Grounder;
import com.pwr.zpi.linguistic.ComplexStatement;
import com.pwr.zpi.linguistic.Question;
import com.pwr.zpi.semantic.IMCollection;
import com.pwr.zpi.semantic.IndividualModel;
import com.pwr.zpi.semantic.ObjectType;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Agent is crucial class for project - it has members of collections
 * that hold all of data that we are processing.
 *
 * @author Grzegorz Kostkowski
 * @author Mateusz Gawlowski
 * @author Jarema Radom
 */
public class Agent {
    private BPCollection knowledgeBase;
    private IMCollection models;
    /**
     * Agent interacts with knowledge summarization (holons) through HolonsIntercessor object.
     */
    private HolonsIntercessor holonsIntercessor;
    private static DatabaseAO database;
    public static Collection<ObjectType> objectTypeCollection;
    private String label;


    private Agent(AgentBuilder builder) {
        knowledgeBase = builder.getKnowledgeBase();
        holonsIntercessor = new HolonsIntercessor(this, builder.getContextualisation());
        this.objectTypeCollection = builder.getObjectTypeCollection();
        this.models = builder.getModels();
        database = builder.getDatabase();
        this.label=builder.getLabel();
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

    public HolonCollection getHolon() {
        return holonsIntercessor.getHolonsCollection();
    }

    public Contextualisation getContextualisationMethod() {
        return holonsIntercessor.getHolonsContextualisation();
    }

    public void setContextualisationMethod(Contextualisation newContextualisation) {
        holonsIntercessor.setHolonsContextualisation(newContextualisation);
    }

    @Deprecated //uzywamy updateMemory()
    public DatabaseAO getDatabase() {
        return database;
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
    public DistributedKnowledge distributeKnowledge(Formula formula, int time) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula, time);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Produces complete map of grounding sets for specified formula. Used for convenient creation of contextualisation.
     * @param formula
     * @return
     */
    public Map<Formula, Set<BaseProfile>> makeGroundingSets(Formula formula) {
        try {
            return distributeKnowledge(formula).getGroundingSetsMap();
        } catch (InvalidFormulaException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Not able to produce grounding sets", e);
            return new HashMap<>();
        }
    }


/*    *//**
     * Builds complete (with all classes) distributed knowledge, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to timestamp of last registered by this agent base profile.
     *
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     *//*
    public DistributedKnowledge distributeCompleteKnowledge() throws InvalidFormulaException {
        return distributeKnowledge();
    }*/

    /**
     * Builds distributed knowledge with default complexity, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to timestamp of last registered by this agent base profile.
     *
     * @param formula Formula
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     */
    public DistributedKnowledge distributeKnowledge(Formula formula) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds distributed knowledge with specified complexity, which will be used to make respective mental models associated
     * with formulas. It is used to build distribution of different mental models.
     * Built distributed knowledge is related to given timestamp.
     *
     * @param formula        Formula
     * @param timestamp      timestamp
     * @param buildComplexDK According to additional operations overhead, parameter can determine if complete distribution
     *                       (with all classes) should be build (if true) or simple (only classes related to exact formula) - false.
     * @return Distribution of knowledge.
     * @throws InvalidFormulaException
     */
    public DistributedKnowledge distributeKnowledge(Formula formula, int timestamp, boolean buildComplexDK) throws InvalidFormulaException {
        try {
            return new DistributedKnowledge(this, formula, buildComplexDK);
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DistributedKnowledge distributeKnowledge(Formula formula, boolean buildComplexDK) throws InvalidFormulaException, NotConsistentDKException {
        return new DistributedKnowledge(this, formula, true);
    }


    /**
     * This is method for realising one of fundamental task: registering task. Precisely, this method includes
     * observation saved in agent database in program for processing.
     */
    public void discoverObservations(Collection<Observation> newObservations) {
        Logger.getAnonymousLogger().log(Level.INFO, "Discovering new observations ...");

        if (newObservations != null && !newObservations.isEmpty()) {
            Logger.getAnonymousLogger().log(Level.INFO, "Processing " + newObservations.size() + " new observation(s):");
            for (Observation obs : newObservations) {
                Logger.getAnonymousLogger().log(Level.FINE, "\t" + obs);
                registerObservation(obs);
            }
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

    /**
     * Updates agent's episodic memory by fetching new observations from database.
     */
    public void updateMemory() {
        discoverObservations(database.fetchNewObservations());
    }

    /**
     * Checks whether there are new observations in database.
     * @return true/false
     */
    public boolean isNewObservationInDatabase() {
        return database.isInsertFlagPositive();
    }

    public void updateBeliefs(){
        try {
            Logger.getAnonymousLogger().log(Level.INFO, "Updating beliefs for t="+knowledgeBase.getTimestamp()+"...");
            holonsIntercessor.updateBeliefs(knowledgeBase.getTimestamp());
            Logger.getAnonymousLogger().log(Level.INFO, "Update is done.");
        } catch (InvalidFormulaException | NotApplicableException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Agent was not able to update holons.");
        }
    }


    public void addObservationToDatabase(Observation... observations) {
        for (Observation obs : observations)
            database.addNewObservation(obs);
    }

    /**
     * Methods for testing purposes: adds given observation to database,
     * then updates episodic memory (fetches new observations) and updates holonsIntercessor.
     * Semantic memory is also updated (if required) during updating episodic memory.
     */
    public void addAndUpdate(Observation[] observations) {
        addObservationToDatabase(observations);
        updateMemory();
        updateBeliefs();
    }

    public Map<Formula, Double> getSummarization(Formula currFormula, int timestamp) {
        return holonsIntercessor.getSummaries(currFormula, timestamp);
    }

    @Override
    public String toString() {
        return label != null ? "Agent "+label : super.toString();
    }

    /**
     * Class is introduced due to diversity of ways of agent creation. it simplifies construction of agent.
     * Class implements builder pattern.
     *
     * @author Grzegorz Kostkowski
     */
    public static class AgentBuilder {
        private BPCollection knowledgeBase;
        private IMCollection models;
        /**
         * Agent interacts with knowledge summarization (holons) through HolonsIntercessor object.
         */
        private HolonsIntercessor holonsIntercessor;
        private DatabaseAO database;
        private Contextualisation contextualisation;
        private String label=null;


        public AgentBuilder() {
            /*if (Agent.database != null)
                Agent.database.deleteDatabase();
            this.database = Agent.database = new DatabaseAO(ObjectType.getObjectTypes());*/ //todo

            if (Agent.database == null)
                Agent.database = new DatabaseAO(ObjectType.getObjectTypes());
            this.database = Agent.database;
            this.models = new IMCollection();
            this.knowledgeBase = new BPCollection();
            this.contextualisation =null;
        }

        public AgentBuilder knowledgeBase(BPCollection bpCollection) {
            this.knowledgeBase = bpCollection;
            return this;
        }

        public AgentBuilder models(IMCollection ims) {
            this.models =ims;
            return this;
        }

        public AgentBuilder label(String label) {
            this.label =label;
            return this;
        }

        public AgentBuilder database(DatabaseAO dao) {
            this.database = dao;
            return this;
        }

        public AgentBuilder contextualisation(Contextualisation contextualisation) {
            this.contextualisation=contextualisation;
            return this;
        }

        /**
         * Method which creates
         * @return
         */
        public Agent build() {
            return new Agent(this);
        }

        public BPCollection getKnowledgeBase() {
            return knowledgeBase;
        }

        public IMCollection getModels() {
            return models;
        }

        public DatabaseAO getDatabase() {
            return database;
        }

        public Collection<ObjectType> getObjectTypeCollection() {
            return objectTypeCollection;
        }

        public Contextualisation getContextualisation() {
            return contextualisation;
        }

        public String getLabel() {
            return label;
        }
    }
}
