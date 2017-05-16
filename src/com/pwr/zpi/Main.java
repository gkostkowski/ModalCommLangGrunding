package com.pwr.zpi;

import com.pwr.zpi.conversation.Conversation;

import java.util.HashMap;

class Main {

    /**
     * Realizacja przykładowych przebiegow - opis rozpatrywanych przypadkow:
     * case 1:
     * budowanie agenta od zera - bez bazy wiedzy czy kolekcji modeli indywiduowych - tak jak W RZECZYWISTOŚCI
     * będzie to miało miejsce.
     * case 2:
     * budowanie agenta z określoną bazą wiedzy oraz związanymi modelami indywiduowymi.
     * Uwaga: żeby zadziałalo trzeba dostarczyć identifiers z bazy danych, wyciągniętych z rekordów w bd
     *
     * @param args
     */
    static public void main(String... args) throws InterruptedException {

        //testyM();
/*
        *//*CASE 1*/
        QRCode[] qrCodes = new QRCode[]{new QRCode("0124"), new QRCode("02442"), new QRCode("01442")};
        Trait[] tr = new Trait[]{
                new Trait("Red"),
                new Trait("White"),
                new Trait("Blinking"),
                new Trait("Blue"), //[4]
                new Trait("Soft")};
        int t = 0;
        Observation[] obsTill3  = new Observation[]{ //inclusively
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], true);
                    put(tr[1], true);
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
                }}, t++)
        };

        Agent agent1 = new Agent();
        agent1.getModels().addNameToModel(qrCodes[0], "Hyzio");
        agent1.addObservationToDatabase(obsTill3);
        agent1.updateMemory();
       // agent1.updateBeliefs();
//        agent1.getDatabase().updateAgentMemory();
//        agent1.discoverObservations();
//        for (Observation observation: observations)
//            agent1.registerObservation(observation);


        int tt1 = 3;
        Conversation c1 = new Conversation(agent1, "conv1", tt1);
        c1.start();

        System.out.println("asking...");
        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: I know)");

        Observation[] obsTill4 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[1], false);
                    put(tr[2], false);
                }}, t++)};
        agent1.addObservationToDatabase(obsTill4);
        agent1.updateMemory();
        agent1.updateBeliefs();

//        agent1.getDatabase().updateAgentMemory();

        c1.addQuestion("Is Hyzio red");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: bel p pos ~p)");


        Observation[] obsTill5 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[1], false);
                    put(tr[2], null);
                }}, t++)};
        agent1.addObservationToDatabase(obsTill5);
        agent1.updateMemory();
        agent1.updateBeliefs();

//        agent1.getDatabase().updateAgentMemory();
        c1.addQuestion("Is Hyzio blinking");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: i dont know what to say)");
        //
        c1.addQuestion("Is Hyzio white");
        Thread.sleep(1000);
        System.out.println("(EXPECTED: i know it is not)");

        Observation[] obsTill7 = new Observation[]{
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[1], true);
                    put(tr[2], true);
                }}, t++),
                new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                    put(tr[0], false);
                    put(tr[1], true);
                }}, t++)};
        agent1.addObservationToDatabase(obsTill7);
        agent1.updateMemory();
        agent1.updateBeliefs();

        //agent1.getDatabase().updateAgentMemory();
            c1.addQuestion("Is Hyzio red");
            System.out.println("(EXPECTED: pos is pos not is)");

            c1.addQuestion("Is Hyzio white");
            Thread.sleep(1000);
            System.out.println("(EXPECTED: pos is bel not)");
    }

    static public void testyM(){
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
