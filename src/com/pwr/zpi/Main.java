package com.pwr.zpi;

import com.pwr.zpi.conversation.ConversationSimulator;
import com.pwr.zpi.core.Agent;
import com.pwr.zpi.core.memory.episodic.Observation;
import com.pwr.zpi.core.memory.holons.context.measures.NormalisedSoftDistance;
import com.pwr.zpi.core.memory.holons.context.selectors.LatestFocusedGroupSelector;
import com.pwr.zpi.core.memory.holons.context.selectors.LatestGroupSelector;
import com.pwr.zpi.core.memory.semantic.IndividualModel;
import com.pwr.zpi.core.memory.semantic.ObjectType;
import com.pwr.zpi.exceptions.InvalidContextualisationException;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidGroupSelectorException;
import com.pwr.zpi.exceptions.InvalidMeasureException;
import com.pwr.zpi.core.memory.holons.context.builders.ConcreteContextBuilder;
import com.pwr.zpi.core.memory.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.core.memory.holons.context.contextualisation.FilteringContextualisation;
import com.pwr.zpi.core.memory.holons.context.measures.NormalisedDistance;
import com.pwr.zpi.core.memory.holons.context.selectors.LatestSelector;
import com.pwr.zpi.language.*;
import com.pwr.zpi.core.memory.semantic.QRCode;
import com.pwr.zpi.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class Main {

    /**
     * Realizacja przykładowych przebiegow
     *
     * @param args
     */
    static public void main(String... args) throws InterruptedException, InvalidMeasureException,
            InvalidContextualisationException, InvalidGroupSelectorException, InvalidFormulaException {

        Level logsVisibilityLevel = Level.INFO;
        Util.setLogVisibilityLevel(logsVisibilityLevel);
        //dbLoopTest();

        Contextualisation latestContext = new FilteringContextualisation(new ConcreteContextBuilder(), new LatestSelector(),
                new NormalisedDistance(0.5));
        Contextualisation latestGroupContext = new FilteringContextualisation(new ConcreteContextBuilder(),
                new LatestGroupSelector(3),
                new NormalisedDistance(0.3));



//                new Distance(2));
        Agent agentNoCtxt = new Agent.AgentBuilder()
                //.contextualisation(null)
                .label("agentNoCtxt")
                .build();
        Agent agentLtstCntxt = new Agent.AgentBuilder()
                .contextualisation(latestContext)
                .label("agentLtstCntxt")
                .build();
        Agent agentLtstGrpCntxt = new Agent.AgentBuilder()
                .contextualisation(latestGroupContext)
                .label("agentLtstGrpCntxt")
                .build();


        /*Description of below lines:*/
        /*Launching scenarios for simple modalities: */
//        new Scenario(agentNoCtxt, "scenario01.csv").execute();
//        new Scenario(agentNoCtxt, "conj_disj_scenario01a_main.csv").execute();
//        new Scenario(agentNoCtxt, "scenario01_main.csv").execute();



        /*      * * * Sample scenarios execution * * *          */

//        new Scenario(agentNoCtxt, "conj_disj_scenario01a_main.csv").execute();  //scenario01a
//        new Scenario(agentNoCtxt, "conj_disj_scenario01c_main.csv").execute();  //scenario01c
//        new Scenario(agentNoCtxt, "sm_conj_disj_scenario01b_main.csv").execute();  //scenario01b //todo
//
//        new Scenario(agentNoCtxt, "conj_no_context_scenario001.csv").execute(); //scenario001
//          new Scenario(agentNoCtxt, "sm_scenario01d.csv").execute(); //scenarioBinHolon
//        new Scenario(agentNoCtxt, "conj_no_context_scenario02a.csv").execute(); //scenario02a
//        new Scenario(agentLtstCntxt, "conj_context_scenario02b.csv").execute();  //scenario02b
//        new Scenario(agentNoCtxt, "sm_conj_no_context_scenario03.csv").execute(); //scenario03 //todo
//        new Scenario(agentLtstCntxt, "conj_latest_context_scenario04.csv").execute(); //scenario04
//        new Scenario(agentNoCtxt, "ex_disj_no_context_scenario05.csv").execute(); //scenario05
//        new Scenario(agentNoCtxt, "disj_no_context_scenario06.csv").execute(); //scenario06
//        executeConjLatestGroupSoftDistContextScenario(); //scenario07
//        executeConjFocusedGroupContextScenario(); //scenario08
//
//        showMalformedScenarioExceprions();

        /*   *** old stuff *** */
        // simplyModalitiesScenario(agent, qrCodes, tr);
        //or
        //simplyAndConjunctionModalitiesScenario(agent, qrCodes, tr);

        QRCode[] qrCodes = new QRCode[]{new QRCode("0124"), new QRCode("02442"), new QRCode("01442")};
        Trait[] tr = new Trait[]{
                new Trait("Red"),
                new Trait("White"),
                new Trait("Blinking"),
                new Trait("Blue"),
                new Trait("Soft")};

        startLifeCycle(agentNoCtxt, qrCodes, tr);

        //testVoice(agent, qrCodes, tr);
        //note: simplyModalitiesScenario and simplyAndConjunctionModalitiesScenario use same episodic knowledge, which
        // is present in db after launching one of them, so they can't be used together.

        //modalConjunctionsScenario(agent, qrCodes, tr);
    }

    private static void executeConjLatestGroupSoftDistContextScenario() throws InvalidGroupSelectorException, InvalidMeasureException, InvalidContextualisationException {
        Contextualisation latestGroupContextSoftDis = new FilteringContextualisation(new ConcreteContextBuilder(),
                new LatestGroupSelector(3),
                new NormalisedSoftDistance(0.5));
        Agent agentLtstGrpCntxtSoftDist = new Agent.AgentBuilder()
                .contextualisation(latestGroupContextSoftDis)
                .label("agentLtstGrpCntxtSoftDist")
                .build();
        new Scenario(agentLtstGrpCntxtSoftDist, "conj_latest_group_context_scenario07.csv").execute();
    }

    /**
     * Aim of this method is to execute prepared malformed scenarios to show related exceptions and handling them.
     */
    private static void showMalformedScenarioExceprions() {
        Agent agentNoCtxt = new Agent.AgentBuilder()
                .label("agentNoCtxt")
                .build();
        String [] malformedScenariosNames = {
                "malformed_no_question_answer_scenario101.csv",
                "malformed_not_defined_object_scenario102.csv",
                "malformed_missing_trait_in_header_scenario103.csv",
                "malformed_no_scenario_marker_scenario104.csv",
                "malformed_no_def_marker_scenario105.csv"
        };
        for (String scenarioName : malformedScenariosNames) {
            System.out.println("\t\t\tScenario "+scenarioName);
            new Scenario(agentNoCtxt, scenarioName).execute();
        }
    }

    private static void executeConjFocusedGroupContextScenario() throws InvalidFormulaException, InvalidMeasureException, InvalidContextualisationException {
        Formula relevantTraitedFormula = prepareSampleFormula();
        Contextualisation latestFocusedGroupContextSoftDis = new FilteringContextualisation(new ConcreteContextBuilder(),
                new LatestFocusedGroupSelector(relevantTraitedFormula),
                new NormalisedSoftDistance(0.5));
        Agent agentLtstFcsdGrpCntxtSoftDist = new Agent.AgentBuilder()
                .contextualisation(latestFocusedGroupContextSoftDis)
                .label("agentLtstFcsdGrpCntxtSoftDist")
                .build();

        new Scenario(agentLtstFcsdGrpCntxtSoftDist, "conj_latest_focused_group_context_scenario08.csv").execute();
    }

    /**
     * Returns <i>Is 04512 blue and blinking.</i>
     */
    private static Formula prepareSampleFormula() throws InvalidFormulaException {

        List<Trait> traits = new ArrayList<Trait>() {{
            add(new Trait("Blue"));
            add(new Trait("Soft"));
            add(new Trait("Blinking"));
            add(new Trait("Green"));
            add(new Trait("Square"));
            add(new Trait("Solid"));
            add(new Trait("Striped"));
        }};
        return new ComplexFormula(new IndividualModel(new QRCode("04512"),
                new ObjectType("04", traits)),
                Arrays.asList(new Trait[]{traits.get(0), traits.get(2)}),
                Arrays.asList(new State[]{State.IS, State.IS}),
                LogicOperator.AND);
    }

    private static void startLifeCycle(Agent agent, QRCode[] qrCodes, Trait[] tr) {
        int t = 0;
        agent.getDatabase().addNewObservation(new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
            put(tr[0], true);
            put(tr[1], false);
            put(tr[2], false);
        }}, t++));
        agent.getDatabase().addNewObservation(new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
            put(tr[0], true);
            put(tr[1], false);
            put(tr[2], false);
        }}, t++));
        agent.getDatabase().addNewObservation(new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
            put(tr[0], true);
            put(tr[1], true);
            put(tr[2], true);
        }}, t++));
        agent.getDatabase().addNewObservation(new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
            put(tr[0], true);
            put(tr[1], null);
            put(tr[2], null);
        }}, t++));
        agent.getModels().addNameToModel(qrCodes[0], "Bobby");

        agent.startLifeCycle();

    }

    /**
     * Conversation using simply modalities about Hyzio
     */
    private static void simplyModalitiesScenario(Agent agent, QRCode[] qrCodes, Trait[] tr) throws InterruptedException {
        if (agent.getModels().getRepresentationByName("Hyzio") != null)
            throw new IllegalStateException("You already asked about Hyzio");
        int t = 0;

        Contextualisation contextualisation = null;//new LatestFilteringContextualisation(new Distance(2));

        agent.getModels().addNameToModel(qrCodes[0], "Hyzio");
        ConversationSimulator c1 = new ConversationSimulator(agent);

        Observation[] obsTill3 = new Observation[]{ //inclusively
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], null);
                }}, t++)
        };

        agent.addAndUpdate(obsTill3);
        c1.start();

        Logger.getAnonymousLogger().log(Level.INFO, "asking...");
        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I know)");

        Observation[] obsTill4 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++)};
        agent.addAndUpdate(obsTill4);

        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I know)");


        Observation[] obsTill5 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], false);
                    put(tr[2], null);
                }}, t++)};

        agent.addAndUpdate(obsTill5);
        c1.addQuestion("Is Hyzio blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: It is possible it is blinking, but I believe it is not)");


        c1.addQuestion("Is Hyzio white");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: i know it is not)");

        Observation[] obsTill7 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], false);
                    put(tr[1], true);
                    put(tr[2], null);
                }}, t++)};

        agent.addAndUpdate(obsTill7);
        c1.addQuestion("Is Hyzio red");
        System.out.println("(EXPECTED: Know not)");

        c1.addQuestion("Is Hyzio white");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: know is)");

        Observation[] obsTill9 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], null);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], null);
                    put(tr[2], true);
                }}, t++)};

        agent.addAndUpdate(obsTill9);
        c1.addQuestion("Is Hyzio white");
        System.out.println("(EXPECTED: bel not pos is)");
    }

    /**
     * ConversationSimulator using simply modalities and modal conjunctions about Hyzio
     */
    private static void simplyAndConjunctionModalitiesScenario(Agent agent, QRCode[] qrCodes, Trait[] tr) throws InterruptedException {

        if (agent.getModels().getRepresentationByName("Hyzio") != null)
            throw new IllegalStateException("You already asked about Hyzio");
        int t = 0;

        Contextualisation contextualisation = null;//new LatestFilteringContextualisation(new Distance(2));

        agent.getModels().addNameToModel(qrCodes[0], "Hyzio");
        ConversationSimulator c1 = new ConversationSimulator(agent);

        Observation[] obsTill1 = new Observation[]{ //inclusively
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++)
        };

        agent.addAndUpdate(obsTill1);

        c1.start();

        Logger.getAnonymousLogger().log(Level.INFO, "asking...");
        c1.addQuestion("Is Hyzio blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: No, but I know that he is not)");

        Observation[] obsTill2 = new Observation[]{ //inclusively
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], true);
                }}, t++)
        };

        agent.addAndUpdate(obsTill2);

        c1.addQuestion("Is hyzio blinking and not white");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: Yes, I am sure it is blinking and not white )");

        Observation[] obsTill3 = new Observation[]{ //inclusively
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], null);
                }}, t++)
        };

        agent.addAndUpdate(obsTill3);

        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I know)");
        c1.addQuestion("Is hyzio red and blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: It is possible, that it is red and blinking, but I believe it is red and not blinking)");

        Observation[] obsTill4 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++)};

        agent.addAndUpdate(obsTill4);

        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: know p)");


        Observation[] obsTill5 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], false);
                    put(tr[2], null);
                }}, t++)};

        agent.addAndUpdate(obsTill5);

        c1.addQuestion("Is Hyzio blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: It is possible it is blinking, but I believe it is not)");

        c1.addQuestion("Is Hyzio white");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: i know it is not)");

        Observation[] obsTill7 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], false);
                    put(tr[1], true);
                    put(tr[2], null);
                }}, t++)};

        agent.addAndUpdate(obsTill7);

        c1.addQuestion("Is Hyzio red");
        System.out.println("(EXPECTED: know not)");

        c1.addQuestion("Is Hyzio white");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: know is)");

        Observation[] obsTill8 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], null);
                    put(tr[2], true);
                }}, t++)
        };

        agent.addAndUpdate(obsTill8);

        c1.addQuestion("is hyzio red and blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: It is possible, that it is red and blinking, but I believe it is red and not blinking )");

        c1.addQuestion("is hyzio not white and not blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I think it is possible that it is not white and not blinking)");
    }


    /**
     * ConversationSimulator using modal conjunctions about Rysio
     */
    private static void modalConjunctionsScenario(Agent agent, QRCode[] qrCodes, Trait[] tr) throws InterruptedException {

        int t = 9;

        agent.getModels().addNameToModel(qrCodes[1], "Rysio");
        Contextualisation contextualisation = null;//new LatestFilteringContextualisation(new Distance(2));

        ConversationSimulator conversation = new ConversationSimulator(agent);

        Observation[] obsTill12 = new Observation[]{ //inclusively
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], true);
                    put(tr[4], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], false);
                    put(tr[4], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], true);
                    put(tr[4], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], false);
                    put(tr[4], false);
                    put(tr[2], false);
                }}, t++)
        };

        agent.addAndUpdate(obsTill12);

        conversation.start();

        System.out.println("\tMODAL CONJUNCTIONS QUESTIONS:");

        Logger.getAnonymousLogger().log(Level.INFO, "asking...");
        conversation.addQuestion("Is Rysio blue and soft");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: No, he is not blue and soft)");

        Observation[] obsTill15 = new Observation[]{ //inclusively
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], null);
                    put(tr[4], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], null);
                    put(tr[4], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], true);
                    put(tr[4], null);
                    put(tr[2], true);
                }}, t++)
        };

        agent.addAndUpdate(obsTill15);

        conversation.addQuestion("is rysio soft and blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I believe it is sof and blinking, but it is possible it is opposite)");


        Observation[] obsTill16 = new Observation[]{ //inclusively
                new Observation(qrCodes[1], new HashMap<Trait, Boolean>() {{
                    put(tr[3], null);
                    put(tr[4], null);
                    put(tr[2], null);
                }}, t++)
        };

        agent.addAndUpdate(obsTill16);

        conversation.addQuestion("is rysio blue and blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: Yes, it is possible that it is blue and blinking)");

    }


    static public void dbLoopTest() {
        QRCode[] qrCodes2 = new QRCode[]{new QRCode("0124"), new QRCode("02442"), new QRCode("01442")};
        Trait[] tr2 = new Trait[]{
                new Trait("Red"),
                new Trait("White"),
                new Trait("Blinking"),
                new Trait("blinking"),
                new Trait("Blue"), //[4]
                new Trait("Soft")};
        int t2 = 0;
        Observation[] obs2 = new Observation[]{
                new Observation(qrCodes2[0], new HashMap<Trait, Boolean>() {{
                    put(tr2[0], true);
                    put(tr2[1], true);
                }}, t2++),
                new Observation(qrCodes2[1], new HashMap<Trait, Boolean>() {{
                    put(tr2[2], true);
                }}, t2),
                new Observation(qrCodes2[2], new HashMap<Trait, Boolean>() {{
                    put(tr2[2], true);
                }}, t2++),
                new Observation(qrCodes2[2], new HashMap<Trait, Boolean>() {{
                    put(tr2[0], false);
                }}, t2++),
                new Observation(qrCodes2[0], new HashMap<Trait, Boolean>() {{
                    put(tr2[2], false);
                }}, t2++),
                new Observation(qrCodes2[2], new HashMap<Trait, Boolean>() {{
                    put(tr2[0], false);
                }}, t2++)};

        Agent agent3 = new Agent.AgentBuilder().build();
        agent3.addObservationToDatabase(obs2);

        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                Logger.getAnonymousLogger().log(Level.INFO, "<agent> zasypiam");
                Thread.sleep(10000); // 10 seconds
                Logger.getAnonymousLogger().log(Level.INFO, "<agent> wstaje");
                agent3.updateMemory();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
