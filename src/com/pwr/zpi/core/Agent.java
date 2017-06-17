package com.pwr.zpi.core;

import com.pwr.zpi.conversation.Listening;
import com.pwr.zpi.conversation.Talking;
import com.pwr.zpi.conversation.VoiceListening;
import com.pwr.zpi.conversation.VoiceTalking;
import com.pwr.zpi.core.memory.episodic.BPCollection;
import com.pwr.zpi.core.memory.episodic.BaseProfile;
import com.pwr.zpi.core.memory.episodic.DistributedKnowledge;
import com.pwr.zpi.core.memory.episodic.Observation;
import com.pwr.zpi.core.memory.holons.HolonCollection;
import com.pwr.zpi.core.memory.holons.HolonsIntercessor;
import com.pwr.zpi.core.memory.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.core.memory.semantic.IMCollection;
import com.pwr.zpi.core.memory.semantic.IndividualModel;
import com.pwr.zpi.core.memory.semantic.ObjectType;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.io.DatabaseAO;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.core.behaviours.AnswerThread;
import com.pwr.zpi.core.behaviours.UpdateThread;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Agent is crucial class for project - it has members of collections
 * that hold all of data that we are processing.
 *
 * @author Grzegorz Kostkowski
 * @author Mateusz Gawlowski
 * @author Weronika Wolska
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
    LifeCycle lifeCycle;

    private Agent(AgentBuilder builder) {
        knowledgeBase = builder.getKnowledgeBase();
        holonsIntercessor = new HolonsIntercessor(this, builder.getContextualisation());
        this.objectTypeCollection = builder.getObjectTypeCollection();
        this.models = builder.getModels();
        database = builder.getDatabase();
        this.label=builder.getLabel();
    }

    public void startLifeCycle()
    {
        lifeCycle = new LifeCycle();
        lifeCycle.start();
    }

    public void sttopLifeCycle()
    {
        if(lifeCycle !=null)
            lifeCycle.stop();
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
            Logger.getAnonymousLogger().log(Level.WARNING, "Not able to distribute knowledge.", e);
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

    /**
     * LifeCycle allows for starting new thread with life cycle of an agent, which consists of cyclic behaviours
     * that can trigger one-shot behaviours. For now it implements periodical checking for new observations and new questions.
     */
    public class LifeCycle implements Runnable {

        /**
         * The RUNNING boolean allows for constant looping through life cycle of the agent when it is true.
         */
        private boolean RUNNING = false;
        /**
         * The thread Thread is an instance of this thread
         */
        private Thread thread;
        /**
         * The listening thread is a thread that allows for receiving voice questions to agent
         */
        private Listening listeningThread;
        /**
         * The talkingThread is a thread that allows to send answer to program which will answer to question vocally
         */
        private Talking talkingThread;

        private int semaphore = 0;
        /**
         * static Object foo is used for synchronization between Threads //todo może jednak flagi
         */
        private final Object foo = new Object();
        /**
         * list of formulas that are currently being processed by agent
         */
        private List<Formula> formulasInProcess;
        /**
         * Reference to thread updating agents memory
         */
        private Thread updateThread;

        /**
         * main loop of the agent. Periodically checks for new observations and new questions
         */
        @Override
        public void run() {
            while(RUNNING)
            {
                if(checkIfNewObservations() && !updateThread.isAlive())
                {
                    acquire(true);
                    updateThread = new Thread(new UpdateThread(Agent.this));
                    updateThread.start();
                    release(true);
                }
                String question = listeningThread.getQuestion();
                if(question!=null)
                {
                    System.out.println(question);
                    new AnswerThread(talkingThread, question, this, Agent.this);
                }
            }
            System.out.println("Stopped - life cycle");
        }

        private boolean checkIfNewObservations() {
            return Agent.this.isNewObservationInDatabase();
        }

        /**
         * starts new life cycle of a completly new Agent or resumes life cycle of agent. Also starts threads
         * that allows for voice communications
         */
        public void start()
        {
            formulasInProcess = new ArrayList<>();
            listeningThread = new VoiceListening();
            listeningThread.start();
            talkingThread = new VoiceTalking(listeningThread);
            talkingThread.start();
            updateThread = new Thread(new UpdateThread(Agent.this));
            updateThread.start();
            if(thread==null)
            {
                thread = new Thread(this, "life cycle");
                RUNNING = true;
                thread.start();
            }
        }

        /**
         * stops life cycle of agent, but leaves options for resuming it in the future
         */
        public void stop()
        {
            RUNNING = false;
            if(thread!=null)
                thread = null;
            if(listeningThread!=null)
                listeningThread.stop();
            if(talkingThread!=null)
                talkingThread.stop();
        }

        /**
         * Method which checks if any similar formula is being processed at the moment, if none such was found it
         * adds given formula to list of ones in middle of processing
         * @param formula   to which currently processed formula are compared
         * @return true if none was found, false if similar was found
         */
        public boolean canFormulaBeProccessed(Formula formula)
        {
            synchronized (formulasInProcess) {
                for (Formula f : formulasInProcess) {
                    if (formula.isFormulaSimilar(f))
                        return false;
                }
                formulasInProcess.add(formula);
                return true;
            }
        }

        /**
         * method removes given formula from formulas being processed
         * @param formula   formula which was completely processed and is not used anymore
         */
        public void removeFromFormulasInProccess(Formula formula)
        {
            synchronized (formulasInProcess)
            {
                formulasInProcess.remove(formula);
                formulasInProcess.notifyAll();
            }
        }

        /**
         * method which blocks access for specific threads
         * @param isMemoryUpdateThread  true if blocking thread is the memory update thread
         */
        public void acquire(boolean isMemoryUpdateThread)
        {
            synchronized (foo) {
                if (isMemoryUpdateThread) {
                    while (semaphore > 0)
                        try {
                            foo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    semaphore--;
                } else {
                    while (semaphore < 0)
                        try {
                            foo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    semaphore++;
                }
            }
        }

        /**
         * method releases blockage and allows other threads to access resources
         * @param isMemoryUpdateThread  true if releasing thread is the memory update one
         */
        public void release(boolean isMemoryUpdateThread)
        {
            synchronized (foo) {
                if (isMemoryUpdateThread)
                    semaphore++;
                else semaphore--;
                foo.notifyAll();
            }
        }

        /**
         * Method called when there is formula to process. It waits till no similar formula is being processed
         * @param formula
         */
        public void tryProccessingFormula(Formula formula)
        {
            while(!canFormulaBeProccessed(formula))
                synchronized (formulasInProcess)
                {
                    try {
                        formulasInProcess.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }



    }
}
