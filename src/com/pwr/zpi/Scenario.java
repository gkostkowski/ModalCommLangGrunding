/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi;

import com.pwr.zpi.conversation.ConversationSimulator;
import com.pwr.zpi.core.Agent;
import com.pwr.zpi.core.memory.episodic.Observation;
import com.pwr.zpi.exceptions.InvalidScenarioException;
import com.pwr.zpi.core.memory.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.core.memory.semantic.QRCode;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class implements realisation of scenario of conversation with agent.
 * todo
 *
 */
public class Scenario {

    private static final int FIRST_TRAIT_POS = 2;
    private static final int NAME_POS = 0;
    private static final char COMMENT_SIGN = '#';
    private static final String SCENARIO_MARKER = "SCENARIO";
    private static final String DEF_SCENARIOS_DIR = "config/scenarios/";
    private Agent agent;
    private Contextualisation contextualisation;
    private List<String> IndModIDs;
    private List<Trait> traits;
    private ConversationSimulator conversation;

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
    private final int ID_POS = 1;
    private final String DEFINITIONS_MARKER = "DEF";


    public Scenario(Agent agent, String filename) {
        this.IMsDefinitions = new HashMap<>();
        this.IMPositions = new HashMap<>();
        this.notLoadedObservations = new HashMap<>();
        notUsedQuestionsAndAnswers = new ArrayList<>();
        this.filename = filename;
        this.agent = agent;
        IndModIDs = new LinkedList<>();
        traits = new ArrayList<>();
        this.conversation = new ConversationSimulator(agent);
    }

    /**
     * Processes entire file and produces appropriate data structure.
     */
    public void execute() {
        conversation.start();
        List<String> readContent;
        try {
            readContent = readContent();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "File can't be read.", e);
            return;
        }

        List<List<String>> parsedContent = parseLines(readContent);
        for (List<String> parsedLine : parsedContent) {
            try {
                processLine(parsedLine, parsedContent);
            } catch (InvalidScenarioException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, "File contains malformed scenario.", e);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "Thread was interrupted.", e);
            }
            //System.out.println(parseLine(parsedLine));
        }

    }

    private void processLine(List<String> line, List<List<String>> allLines) throws InvalidScenarioException, InterruptedException {
        if (!shouldBeIgnored(line.get(0))) {
            LINE_TYPE type = determineLineType(line, allLines);
            switch (type) {
                case IM_DEF:
                    addIMDefinition(line);
                    break;
                case AFFECTED_IMS_DEF: //feature for "multiobservation"
                    //makeTraitPositionAssoc(line);
                    break;
                case SCENARIO_HEADER: //feature for "multiobservation"
                    break;
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
            conversation.addQuestion(notUsedQuestionsAndAnswers.get(i).getKey());
            Thread.sleep(1000);
            System.out.println("(EXPECTED: " + notUsedQuestionsAndAnswers.get(i).getValue() + ")");
        }
        notUsedQuestionsAndAnswers.clear();
    }

    private void addQuestions(List<String> line) throws InvalidScenarioException {
        int observationSize = IMsDefinitions.get(extractObjectName(line)).getValue().size();
        int firstQuestionIndex = FIRST_TRAIT_POS + observationSize;
        List<String> questionsAndAnswers = line.subList(firstQuestionIndex, line.size());
        if (questionsAndAnswers.size() % 2 != 0)
            throw new InvalidScenarioException("There is a lack of question or answer.");
        for (int i = firstQuestionIndex; i < line.size(); i += 2) {
            notUsedQuestionsAndAnswers.add(new Pair<>(line.get(i), line.get(i + 1)));
        }
    }

    private void addNewSingleIMObservation(int timestamp, String id, Map<Trait, Boolean> traitBooleanMap) {
        notLoadedObservations.put(timestamp, new Observation(new QRCode(id), traitBooleanMap, timestamp));
    }

    private Map<Trait, Boolean> extractStatedTraits(List<String> line) throws InvalidScenarioException {
        Map<Trait, Boolean> res = new HashMap<>();
        List<Trait> relatedTraits = IMsDefinitions.get(extractObjectName(line)).getValue();
        List<String> traitValues = line.subList(FIRST_TRAIT_POS, FIRST_TRAIT_POS + relatedTraits.size
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
        return IMsDefinitions.get(extractObjectName(line)).getKey();
    }

    private String extractObjectName(List<String> line) {
        return line.get(1).toLowerCase();
    }


    private void makeTraitPositionAssoc(List<String> line) {
        /*int nextAvailableIndex = 2;
        for (String id : line) {
            int noOfTraits = IMsDefinitions.get(id).size();
            IMPositions.put(id, new Pair<>(nextAvailableIndex, nextAvailableIndex + noOfTraits));
        }*/

    }

    private void addIMDefinition(List<String> line) {
        String id = line.get(ID_POS),
                name = line.get(NAME_POS).toLowerCase();
        IMsDefinitions.put(name,
                new Pair(id, extractTraits(line.subList(ID_POS + 1, line.size()))));
        if (agent.getModels().getRepresentationByName(name) == null)
            agent.getModels().addNameToModel(new QRCode(id), name);

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

    private LINE_TYPE determineLineType(List<String> line, List<List<String>> allLines) throws InvalidScenarioException {
        String first = line.get(0);
        if (line == null || line.isEmpty() || first.isEmpty())
            return LINE_TYPE.IGNORABLE;
        if (first.charAt(0) == COMMENT_SIGN)
            return LINE_TYPE.COMMENT;
        if (first.equals(DEFINITIONS_MARKER))
            return LINE_TYPE.DEF_MARKER;
        if (first.equals(SCENARIO_MARKER))
            return LINE_TYPE.SCENARIO_MARKER;

        int currentIndex = spotCurrentIndex(line, allLines);
        if (currentIndex < 1) throw new InvalidScenarioException("Scenario file should be started with DEF marker.");
        int scenarioMarkerIndex = findSCENARIOLineNbr(allLines);
        if (currentIndex > findDEFLineNbr(allLines) && currentIndex < scenarioMarkerIndex)
            return LINE_TYPE.IM_DEF;
        if (currentIndex == (scenarioMarkerIndex + 1))
            return LINE_TYPE.AFFECTED_IMS_DEF;
        if (currentIndex == (scenarioMarkerIndex + 2)) {
            if(getNoOfTraitsInHeader(line) !=getDeclatedTraits().size() )
                throw new InvalidScenarioException("Not all declared traits are inclueded in header.");
            return LINE_TYPE.SCENARIO_HEADER;
        }
        if (currentIndex > (scenarioMarkerIndex + 2)) {
            return containsQuestion(line) ? LINE_TYPE.SC_QUESTION_ENTRY : LINE_TYPE.SCENARIO_ENTRY;
        }
        return LINE_TYPE.IGNORABLE;
    }

    private int getNoOfTraitsInHeader(List<String> header) {
        List<String> declaredTraits = getDeclatedTraits().stream().map(Trait::getName).collect(Collectors.toList());
        return (int)header.stream()
                .filter(declaredTraits::contains)
                .count();
    }

    private List<Trait> getDeclatedTraits() {
        return IMsDefinitions.entrySet().stream()
                .map(e -> e.getValue().getValue())
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private boolean containsQuestion(List<String> line) {
        return !line.get(line.size() - 1).matches("^\\d*$");
    }

    private int findDEFLineNbr(List<List<String>> allLines) throws InvalidScenarioException {
        for (int i = 0; i < allLines.size(); i++)
            if (allLines.get(i).get(0).equals(DEFINITIONS_MARKER))
                return i;
        throw new InvalidScenarioException("Scenario doesn't contain DEF marker.");
    }

    private int findSCENARIOLineNbr(List<List<String>> allLines) throws InvalidScenarioException {
        for (int i = 0; i < allLines.size(); i++)
            if (allLines.get(i).get(0).equals(SCENARIO_MARKER))
                return i;
        throw new InvalidScenarioException("Scenario doesn't contain SCENARIO marker.");
    }

    private boolean shouldBeIgnored(String line) {
        return line == null || line.matches("^;*$") || IGNORABLE_SIGN.contains(line.charAt(0));
    }

    private List<String> readContent() throws IOException {
        try {
            return Files.readAllLines(Paths.get(DEF_SCENARIOS_DIR + filename), Charset.forName("UTF-8"));
        } catch (IOException e) {
            return Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8")); //if absolute
        }
    }

    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>(Arrays.asList(line.split(";")));
        for (Iterator<String> it = fields.iterator(); it.hasNext(); )
            if (it.next().isEmpty())
                it.remove();
        return fields;
    }

    private List<List<String>> parseLines(List<String> line) {
        List<List<String>> res = new ArrayList<>();
        for (String s : line) {
            if(!shouldBeIgnored(s))
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


    private int spotCurrentIndex(List<String> line, List<List<String>> allLines) throws InvalidScenarioException {
        int i = 0;
        for (List<String> list : allLines) {
            if (list.containsAll(line) && line.containsAll(list))
                return i;
            i++;
        }
        throw new InvalidScenarioException("Can't ");
    }

    enum LINE_TYPE {
        IM_DEF,
        COMMENT,
        SCENARIO_HEADER,
        SCENARIO_ENTRY,
        SC_QUESTION_ENTRY,
        DEF_MARKER,
        SCENARIO_MARKER,
        AFFECTED_IMS_DEF,
        IGNORABLE;
    }
}
