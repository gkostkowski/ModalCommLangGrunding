package com.pwr.zpi;

import com.pwr.zpi.conversation.Conversation;
import com.pwr.zpi.episodic.Observation;
import com.pwr.zpi.exceptions.InvalidScenarioException;
import com.pwr.zpi.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.semantic.QRCode;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implements realisation of scenario of conversation with agent.
 */
public class Scenario {

    private static final int FIRST_TRAIT_POS = 2;
    private static final int NAME_POS = 0;
    private Agent agent;
    private Contextualisation contextualisation;
    private List<String> IndModIDs;
    private List<Trait> traits;
    private Conversation conversation;

    /**
     * natural_name, Pair(ID, traits)
     */
    private Map<String, Pair<String, List<Trait>>> IMsDefinitions;

    /**
     * ID for IM and related range of columns. key in pair is inclusive and value is exclusive.
     */
    private Map<String, Pair<Integer, Integer>> IMPositions;

    /**
     * Note: Observations from notLoadedObservations are removed after loading into agent
     */
    private Map<Integer, Observation> notLoadedObservations;
    /**
     * Note: Questions from notUsedQuestionsAndAnswers are removed after asking agent
     */
    private List<Pair<String, String>> notUsedQuestionsAndAnswers;

    private static final List<Character> IGNORABLE_SIGN = new ArrayList<Character>() {{
        add('#');
    }};
    private String filename;
    private final int ID_POS=1;


    public Scenario(Agent agent, Contextualisation contextualisation, String filename, String
            cnversationName) {
        this.filename = filename;
        this.agent = agent;
        IndModIDs = new LinkedList<>();
        traits = new ArrayList<>();
        this.conversation = new Conversation(agent, cnversationName, 0, contextualisation);
        conversation.start();
        execute();
    }

    /**
     * Processes entire file and produces appropriate data structure.
     */
    private void execute() {
        List<String> readContent;
        try {
            readContent = readContent();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "File can't be read.", e);
            return;
        }

        List<List<String>> parsedContent = parseLines(readContent);
        for (List<String> parsedLine : parsedContent) {
            processLine(parsedLine, parsedContent);
            System.out.println(parseLine(parsedLine));
        }

    }

    void processLine(List<String> line, List<List<String>> allLines) throws InvalidScenarioException, InterruptedException {
        if (!shouldBeIgnored(line.get(0))) {
            LINE_TYPE type = determineLineType(line, allLines);
            switch (type) {
                case IM_DEF:
                    addIMDefinition(line);

                    break;
                case AFFECTED_IMS_DEF:
                    //makeTraitPositionAssoc(line);
                case SCENARIO_ENTRY:
                    addNewSingleIMObservation(extractTimestamp(line), extractRelatedIndModelID(line),
                            extractStatedTraits(line));
                    break;
                case SC_QUESTION_ENTRY:
                    addNewSingleIMObservation(extractTimestamp(line), extractRelatedIndModelID(line),
                            extractStatedTraits(line));
                    addQuestions(line);
                    updateAgent();
                    askQuestions();
                    break;

            }
        }
    }

    private void updateAgent() {
        agent.addAndUpdate(notLoadedObservations.values().toArray(new Observation[]{}));
        notLoadedObservations.clear();
    }

    private void askQuestions() throws InterruptedException {
        for (int i = 0; i < notUsedQuestionsAndAnswers.size(); i++) {
            System.out.println("asking...");
            conversation.addQuestion(notUsedQuestionsAndAnswers.get(0).getKey());
            Thread.sleep(1000);
            System.out.println("(EXPECTED: "+notUsedQuestionsAndAnswers.get(0).getValue()+")");
        }
        notUsedQuestionsAndAnswers.clear();
    }

    private void addQuestions(List<String> line) throws InvalidScenarioException {
        int observationSize=IMsDefinitions.get(extractRelatedIndModelID
                (line)).getValue().size();
        int firstQuestionIndex = FIRST_TRAIT_POS + observationSize+1;
        List<String> questionsAndAnswers = line.subList(firstQuestionIndex, line.size());
        if (questionsAndAnswers.size() % 2 != 0)
            throw new InvalidScenarioException("There is a lack of question or answer.");
        for (int i = firstQuestionIndex; i < line.size(); i+=2) {
            notUsedQuestionsAndAnswers.add(new Pair<>(line.get(i), line.get(i + 1)));
        }
    }

    private void addNewSingleIMObservation(int timestamp, String id, Map<Trait, Boolean> traitBooleanMap) {
        notLoadedObservations.put(timestamp, new Observation(new QRCode(id), traitBooleanMap, timestamp));
    }

    private Map<Trait, Boolean> extractStatedTraits(List<String> line) throws InvalidScenarioException {
        Map<Trait, Boolean> res = new HashMap<>();
        List<Trait> relatedTraits = IMsDefinitions.get(line.get(1)).getValue();
        List<String> traitValues=line.subList(FIRST_TRAIT_POS, FIRST_TRAIT_POS + relatedTraits.size
                ());
        if (relatedTraits.size() != traitValues.size())
            throw new InvalidScenarioException("Value for some trait was not provided");
        for (int i = 0; i < relatedTraits.size(); i++) {
            Boolean boolVal = Integer.parseInt(traitValues.get(i)) == 0 ? null :
                    (Integer.parseInt(traitValues.get(i)) == 1);
            res.put(relatedTraits.get(i), boolVal);
        }
        return res;
    }

    private String extractRelatedIndModelID(List<String> line) {
        return IMsDefinitions.get(line.get(1)).getKey();
    }



    private void makeTraitPositionAssoc(List<String> line) {
        /*int nextAvailableIndex = 2;
        for (String id : line) {
            int noOfTraits = IMsDefinitions.get(id).size();
            IMPositions.put(id, new Pair<>(nextAvailableIndex, nextAvailableIndex + noOfTraits));
        }*/

    }

    private void addIMDefinition(List<String> line) {
        IMsDefinitions.put(line.get(NAME_POS),
                new Pair(line.get(ID_POS), extractTraits(line.subList(ID_POS + 1, line.size()))));
    }

    private List<Trait> extractTraits(List<String> traits) {
        List<Trait> res = new ArrayList<>();
        for (String trait : traits)
            res.add(new Trait(trait));
        return res;
    }

    private int extractTimestamp(List<String> line) {
        return Integer.parseInt(line.get(0));
    }

    private LINE_TYPE determineLineType(List<String> line, List<List<String>> allLines) {

    }

    private boolean shouldBeIgnored(String line) {
        return IGNORABLE_SIGN.contains(line.charAt(0));
    }

    private List<String> readContent() throws IOException {
        try {
            return Files.readAllLines(Paths.get("config/" + filename), Charset.forName("UTF-8"));
        } catch (IOException e) {
            return Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8")); //if absolute
        }
    }

    List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>(Arrays.asList(line.split(";")));
        for (Iterator<String> it = fields.iterator(); it.hasNext(); )
            if (it.next().isEmpty())
                it.remove();
        return fields;
    }

    List<List<String>> parseLines(List<String> line) {
        List<List<String>> res = new ArrayList<>();
        for (String s : line) {
            res.add(parseLine(s));
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder("");
        try {
            for (String s : readContent())
                content.append(s + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Scenario{" +
                "filename='" + filename + '\'' + "}:\n" + content;


    }

    public String toString(List<List<String>> splitLines) {
        StringBuilder content = new StringBuilder("");
        for (List<String> l : splitLines)
            for (String field : l)
                content.append(field);
        content.append(System.getProperty("line.separator"));

        return "Scenario{" +
                "filename='" + filename + '\'' + "}:\n" + content;


    }

    enum LINE_TYPE {
        IM_DEF,
        COMMENT,
        SCENARIO_HEADER,
        SCENARIO_ENTRY,
        SC_QUESTION_ENTRY,
        DEF_MARKER,
        SCENARIO_MARKER,
        AFFECTED_IMS_DEF
    }
}
