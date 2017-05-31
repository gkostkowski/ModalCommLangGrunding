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
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.holons.context.LatestFilteringContext;
import com.pwr.zpi.holons.context.measures.Distance;
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
 */
public class Agent {
    private BPCollection knowledgeBase;
    private IMCollection models;
    /**
     * Contains map of collections of holons built according to different contexts
     */
    private Map<String, HolonCollection> contextualisedHolons;
    private DatabaseAO database;
    public static Collection<ObjectType> objectTypeCollection;


    public Agent() {
        init();
        knowledgeBase = new BPCollection();
        Context context = new LatestFilteringContext(new Distance(2));
        contextualisedHolons = new HashMap<>();
        database = new DatabaseAO(this);
    }

    /*public Agent(String databaseFilename) {
        init();
        knowledgeBase = new BPCollection();
        contextualisedHolons = new HolonCollection();
        database = new DatabaseAO(this, databaseFilename);
    }*/

    public Agent(BPCollection knowledgeBase) {
        init();
        this.knowledgeBase = knowledgeBase;
//        Context context = null; //todo podawanie odpowiedniego typu kontekstu
        Context context = new LatestFilteringContext(new Distance(2));
        contextualisedHolons = new HashMap<>();

        //contextualisedHolons = new HolonCollection(this, context);
    }

    public Agent(BPCollection knowledgeBase, IMCollection models) {
        init();
        this.models = models;
        this.knowledgeBase = knowledgeBase;
//        Context context = null; //todo podawanie odpowiedniego typu kontekstu
        Context context = new LatestFilteringContext(new Distance(2));
        contextualisedHolons = new HashMap<>();
    }

    public Agent(BPCollection knowledgeBase, IMCollection models, HolonCollection holons) {
        this.knowledgeBase = knowledgeBase;
        this.models = models;
        contextualisedHolons = new HashMap<>();
        this.contextualisedHolons.put(holons.getHolonsContext().toString(), holons);
    }

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

    public HolonCollection getContextualisedHolons(Context context) {
        if (contextualisedHolons.get(context) == null)
            contextualisedHolons.put(context.toString(), new HolonCollection(this, context));
        return contextualisedHolons.get(context.toString());
    }

    @Deprecated //uzywamy updateMemory()
    public DatabaseAO getDatabase() {
        return database;
    }

    public void addContextualisedHolons(HolonCollection contextualisedHolons) {
        this.contextualisedHolons.put(contextualisedHolons.getHolonsContext().toString(), contextualisedHolons);
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
        System.out.println("Discovering new observations ...");

        if (newObservations != null && !newObservations.isEmpty()) {
            System.out.println("Processing " + newObservations.size() + " new observation(s):");
            for (Observation obs : newObservations) {
                System.out.println("\t" + obs);
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
        database.updateAgentMemory();
    }

    public void updateBeliefs(Context context) {
        try {
            System.out.print("Updating beliefs for t=" + knowledgeBase.getTimestamp() + "...");
            contextualisedHolons.get(context.toString()).updateBeliefs(knowledgeBase.getTimestamp());
            System.out.println("Done.");
        } catch (InvalidFormulaException | NotConsistentDKException | NotApplicableException e) {
            System.out.println("Agent was not able to update contextualisedHolons.");
        }
    }


    public void addObservationToDatabase(Observation... observations) {
        for (Observation obs : observations)
            database.addNewObservation(obs);
    }

    /**
     * Methods for testing purposes: adds given observation to database,
     * then updates episodic memory (fetches new observations) and updates contextualisedHolons.
     * Semantic memory is also updated (if required) during updating episodic memory.
     */
    public void addAndUpdate(Observation[] observations) {
        addObservationToDatabase(observations);
        updateMemory();
        for (HolonCollection hCOllection : contextualisedHolons.values())
            updateBeliefs(hCOllection.getHolonsContext());
    }

    /**
     * Methods performs question processing and the expected result is the answer.
     * This method should be run by controller.
     *
     * @param question
     * @param voiceConversation
     */
    public void processQuestion(Question question, VoiceConversation voiceConversation) {
        ComplexStatement ss = null;
        Context context = question.getContext();
        if (contextualisedHolons.containsKey(context)) {
            contextualisedHolons.put(context.toString(), new HolonCollection(new HashSet<>(), this));
        }
        try {
            Formula formula = question.getFormula();
            ss = new ComplexStatement((ComplexFormula) formula,
                    Grounder.performFormulaGrounding(this, formula, context), question.getName());
        } catch (InvalidFormulaException | NotApplicableException | NotConsistentDKException | InvalidQuestionException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Question can't be processed.", e);
            voiceConversation.setCurrentAnswer(Question.DEFAULT_FAILURE_ANSWER);
        }
        voiceConversation.setCurrentAnswer(ss.generateStatement());
    }
}
