package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.Object;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;

import java.util.*;

/**
 * Described distributed knowledge as set of classes established for given moment in time,
 * for certain simple or complex formula and associated object. Number of classes depends on kind of grounded formula
 * (which induces mental models).
 * RA1 - Represents set of all base profiles, which are included in working memory and presents object as described by
 * given trait.
 * TA1 - Represents set of all base profiles, which are included in long-term memory and presents object as described by
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
    private final Set<Trait> traits;
    private final Object obj;
    private Map<Integer, BaseProfile> inLM;
    private Map<Integer, BaseProfile> inWM;
    private List<Set<BaseProfile>> groundingSets = new ArrayList<>();
    private List<Set<BaseProfile>> dkClasses = new ArrayList<>();




    public DistributedKnowledge(Agent agent, Formula formula, int time) {
        this.timestamp = time;
        this.formula = formula;
        this.traits = formula.getTraits();
        this.obj = formula.getObject();

        inLM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.LM);
        inWM = agent.getKnowledgeBase().getTimedBaseProfiles(time, BPCollection.MemoryTypes.WM);

        initSets();
        groundingSets = Grounder.getGroundingSets(formula, time,agent.getKnowledgeBase().getBaseProfiles(time));
        for (int i=0; i < CLASSES_AMOUNT; i=i+2) {
            setDkClass(i, i/2, inWM);
            setDkClass(i+1, i/2, inLM);
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

    private void initSets() {
        for (int i=0; i < GROUNDING_SETS_AMOUNT; i++)
            groundingSets.add(new HashSet<>());
        for (int i=0; i < CLASSES_AMOUNT; i++)
            dkClasses.add(new HashSet<>());
    }

    private void setDkClass(int classNbr, int grntSetNbr, Map<Integer, BaseProfile> memory) {
        Set<BaseProfile> currFromWM = dkClasses.get(classNbr);
        currFromWM.addAll(memory.values());
        currFromWM.retainAll(groundingSets.get(grntSetNbr));
    }


    public List<Set<BaseProfile>> getDistributionClasses() {
        return dkClasses;
    }

    public List<Set<BaseProfile>> getGroundingSets() {
        return groundingSets;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Object getObject() {
        return obj;
    }

    public Set<Trait> getTraits() throws InvalidFormulaException {
        if (traits == null || traits.isEmpty())
            throw new InvalidFormulaException();
        return traits;
    }

    public Formula getFormula() {
        return formula;
    }
}
