package com.pwr.zpi;

import com.pwr.zpi.conversation.Conversation;
import com.pwr.zpi.conversation.VoiceConversation;
import com.pwr.zpi.episodic.Observation;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.holons.context.Contextualisation;
import com.pwr.zpi.holons.context.LatestFilteringContextualisation;
import com.pwr.zpi.holons.context.measures.Distance;
import com.pwr.zpi.language.*;
import com.pwr.zpi.linguistic.Question;
import com.pwr.zpi.semantic.QRCode;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class Main {

    /**
     * Realizacja przykładowych przebiegow
     * @param args
     */
    static public void main(String... args) throws InterruptedException {

        //dbLoopTest();

        Agent agent = new Agent();
        QRCode[] qrCodes = new QRCode[]{new QRCode("0124"), new QRCode("02442"), new QRCode("01442")};
        Trait[] tr = new Trait[]{
                new Trait("Red"),
                new Trait("White"),
                new Trait("Blinking"),
                new Trait("Blue"),
                new Trait("Soft")};

         //simplyModalitiesScenario(agent, qrCodes, tr);
        //or
       // simplyAndConjunctionModalitiesScenario(agent, qrCodes, tr);

        // testVoice(agent, qrCodes, tr);
        //note: simplyModalitiesScenario and simplyAndConjunctionModalitiesScenario use same episodic knowledge, which
        // is present in db after launching one of them, so they can't be used together.

        modalConjunctionsScenario(agent, qrCodes, tr);
    }

    private static void testVoice(Agent agent, QRCode[] qrCodes, Trait[] tr){

        agent.getModels().addNameToModel(qrCodes[0], "Bobby");
        int t = 0;

        Observation[] obsTill3  = new Observation[]{ //inclusively
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], false);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], null);
                    put(tr[2], null);
                }}, t++)
        };
        agent.addAndUpdate(obsTill3);

        ///to powinno być w kontrolerze
        VoiceConversation voiceConversation = new VoiceConversation();
        voiceConversation.start();
        while(voiceConversation.getCurentQuestion()==null)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        Question question = new Question(voiceConversation.getCurentQuestion(), agent);
        try {
            Formula formula = question.getFormula();
            System.out.print(formula.getModel().getIdentifier().getIdNumber() + " " + formula.getTraits().get(0).getName());
            agent.processQuestion(question, voiceConversation);
        } catch (InvalidFormulaException | InvalidQuestionException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Question or formula is malformed.", e);
        }

        /////
    }

    /**
     * Conversation using simply modalities about Hyzio
     */
    private static void simplyModalitiesScenario(Agent agent, QRCode[] qrCodes, Trait[] tr) throws InterruptedException {
        if (agent.getModels().getRepresentationByName("Hyzio") != null)
            throw new IllegalStateException("You already asked about Hyzio");
        int t = 0;

        Contextualisation contextualisation = new LatestFilteringContextualisation(new Distance(2));

        agent.getModels().addNameToModel(qrCodes[0], "Hyzio");
        Conversation c1 = new Conversation(agent, "SimpleModalConv", t, contextualisation);

        Observation[] obsTill3  = new Observation[]{ //inclusively
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
        c1.setTimestamp(t);
        c1.start();

        System.out.println("asking...");
        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I know)");

        Observation[] obsTill4 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], null);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++)};
        c1.setTimestamp(t);
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
        c1.setTimestamp(t);
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
        c1.setTimestamp(t);
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
        c1.setTimestamp(t);
        c1.addQuestion("Is Hyzio white");
        System.out.println("(EXPECTED: bel not pos is)");
    }

    /**
     * Conversation using simply modalities and modal conjunctions about Hyzio
     */
    private static void simplyAndConjunctionModalitiesScenario(Agent agent, QRCode[] qrCodes, Trait[] tr) throws InterruptedException {

        if (agent.getModels().getRepresentationByName("Hyzio") != null)
            throw new IllegalStateException("You already asked about Hyzio");
        int t = 0;

        Contextualisation contextualisation = new LatestFilteringContextualisation(new Distance(2));

        agent.getModels().addNameToModel(qrCodes[0], "Hyzio");
        Conversation c1 = new Conversation(agent, "SimpleAndConjModalConv", t, contextualisation);

        Observation[] obsTill1  = new Observation[]{ //inclusively
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

        System.out.println("asking...");
        c1.addQuestion("Is Hyzio blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: No, but I know that he is not)");

        Observation[] obsTill2  = new Observation[]{ //inclusively
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

        Observation[] obsTill3  = new Observation[]{ //inclusively
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
     * Conversation using modal conjunctions about Rysio
     */
    private static void modalConjunctionsScenario(Agent agent, QRCode[] qrCodes, Trait[] tr) throws InterruptedException {

        int t = 9;

        agent.getModels().addNameToModel(qrCodes[1], "Rysio");
        Contextualisation contextualisation = new LatestFilteringContextualisation(new Distance(2));

        Conversation conversation = new Conversation(agent, "ModalConjConv", t, contextualisation);

        Observation[] obsTill12  = new Observation[]{ //inclusively
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

        System.out.println("asking...");
        conversation.addQuestion("Is Rysio blue and soft");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: No, he is not blue and soft)");

        Observation[] obsTill15  = new Observation[]{ //inclusively
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


        Observation[] obsTill16  = new Observation[]{ //inclusively
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


    static public void dbLoopTest(){
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

        Agent agent3 = new Agent();
        agent3.addObservationToDatabase(obs2);

        try {
            //noinspection InfiniteLoopStatement
            while(true) {
                System.out.println("<agent> zasypiam");
                Thread.sleep(10000); // 10 seconds
                System.out.println("<agent> wstaje");
                agent3.updateMemory();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
