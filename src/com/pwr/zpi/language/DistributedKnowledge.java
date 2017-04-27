package com.pwr.zpi.language;

import com.pwr.zpi.*;

import com.pwr.zpi.exceptions.InvalidFormulaException;
import javafx.util.Pair;

import java.util.*;

/**
 * Described distributed knowledge as set of classes established for given moment in time,
 * for certain simple or complex formula and associated individualModel. Number of classes depends on kind of grounded formula
 * (which induces mental models).
 * RA1 - Represents set of all base profiles, which are included in working memory and presents individualModel as described by
 * given trait.
 * TA1 - Represents set of all base profiles, which are included in long-term memory and presents individualModel as described by
 * given trait.
 * Etc.
 */
public class DistributedKnowledge {
    private static final int TRAITS_AMOUNT = 1;
    private static final int GROUNDING_SETS_AMOUNT = 4*TRAITS_AMOUNT;
    private static final int CLASSES_AMOUNT = 2*GROUNDING_SETS_AMOUNT;
    private static final int MEMORY_TYPES_AMOUNT = 2;

    private int timestamp;
    private final Formula formula;
    private final List<TraitSignature> traits;
    private final IndividualModel obj;
    private Map<Integer, BaseProfile> inLM;
    private Map<Integer, BaseProfile> inWM;

    private List<Formula> mentalModels = new ArrayList<>();
    /**
     * Formula represents mental model.
     */
    private Map<Formula, Set<BaseProfile>> groundingSets = new HashMap<>();
    //private List<Set<BaseProfile>> dkClasses = new ArrayList<>();
    /**
     * Map of classes for this knowledge distribution. Exact class is certain value in map
     * and associated key is pair which represent mental model (formula) and memory type used
     * to build such class.
     */
    private Map<Pair<Formula, BPCollection.MemoryType>, Set<BaseProfile>> dkClasses = new HashMap<>();




    public DistributedKnowledge(Agent agent, Formula formula, int time) throws InvalidFormulaException {
        this.timestamp = time;
        this.formula = formula;
        this.traits = formula.getTraits();
        this.obj = formula.getModel();

        inLM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryType.LM);
        inWM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryType.WM);

        //initSets();
        groundingSets = Grounder.getGroundingSets(formula, time,agent.getKnowledgeBase().getBaseProfiles(time));

        Iterator<Formula> it = groundingSets.keySet().iterator();
        for (int i=0; i < CLASSES_AMOUNT && it.hasNext(); i=i+2) {
            Formula currMentalModel = it.next();
            mentalModels.add(currMentalModel);
            setDkClass(inWM, currMentalModel, BPCollection.MemoryType.WM);
            setDkClass(inLM, currMentalModel, BPCollection.MemoryType.LM);
        }

        //important checking
        //todo
        /*Set<BaseProfile> check3_9_1 = new HashSet<BaseProfile>(RA1);
        check3_9_1.retainAll(TA1);
        Set<BaseProfile> check3_9_2 = new HashSet<BaseProfile>(RA2);
        check3_9_1.retainAll(TA2);
        assert check3_9_1.isEmpty()&& check3_9_2.isEmpty();

        Set<BaseProfile> check3_10_1 = new HashSet<BaseProfile>(RA1);
        check3_10_1.addAll(TA1);
        Set<BaseProfile> check3_10_2 = new HashSet<BaseProfile>(RA2);
        check3_10_2.addAll(TA2);
        assert check3_10_1.equals(A1) && check3_10_2.equals(A2);
        */
    }

/*
    private void initSets() {
        for (int i=0; i < GROUNDING_SETS_AMOUNT; i++)
            groundingSets.add(new HashSet<>());
       for (int i=0; i < CLASSES_AMOUNT; i++)
            dkClasses.put(null, new HashSet<>());
    }*/

    private void setDkClass(Map<Integer, BaseProfile> memory, Formula mentalModel,
                            BPCollection.MemoryType mem) {
        Set<BaseProfile> currClass;
        dkClasses.put(new Pair<>(mentalModel, mem), currClass=new HashSet<>());
        currClass.addAll(memory.values());
        currClass.retainAll(groundingSets.get(mentalModel));
    }


    public Map<Pair<Formula, BPCollection.MemoryType>, Set<BaseProfile>> getDistributionClasses() {
        return dkClasses;
    }

    public Map<Formula, Set<BaseProfile>> getGroundingSets() {
        return groundingSets;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public IndividualModel getObservation() {
        return obj;
    }

    public List<TraitSignature> getTraits() throws InvalidFormulaException {
        if (traits == null || traits.isEmpty())
            throw new InvalidFormulaException();
        return traits;
    }

    public Formula getFormula() {
        return formula;
    }

    public List<Formula> getMentalModels() {
        return mentalModels;
    }

    /**
     * Gives class according to given formula (mental model) and memory type.
     *
     * @return
     */
    public Set<BaseProfile> getDkClassByDesc(Formula formula, BPCollection.MemoryType mem) {
        return dkClasses.get(new Pair<>(formula, mem));
    }

    public Set<BaseProfile> getGroundingSet(Formula formula) {
        return groundingSets.get(formula);
    }
}
