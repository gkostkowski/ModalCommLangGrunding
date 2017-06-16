/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.holons;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.holons.context.contextualisation.Contextualisation;
import com.pwr.zpi.language.DistributedKnowledge;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Grounder;
import com.pwr.zpi.util.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * This class implements non-binary holon which is related to complex formulas and provides summarization them.
 * Foundations for non-binary holon are related with conjunctive grounding sets and respective relative cardinality.
 * In case of disjunctions (simple and exclusive) entire summarization can be calculated when basing on respective
 * conjunctive summarizations. Thus, holons for disjunctions are not generated directly, but with usage of already created
 * conjunctive holons.
 * Note: Important contract is that due to above conditions, values stored in holon are not accessed directly, but through
 * HolonsIntercessor.
 *
 * @author Grzegorz Kostkowski
 */
public class NewNonBinaryHolon implements Holon{
    /**
     * Note: formula is given in standard form, namely: without any negations.
     */
    Formula relatedFormula;
    Map<Formula, Double> summaries;
    Map<Formula, Set<BaseProfile>> contextualisedGroundedSets;
    DistributedKnowledge dk;
    private Contextualisation contextualisation; // contextualisation used to prepare grounding sets for holon - todo sprawdz czy potrzeba przechowywac?

    /**
     * Standard constructor. If Contextualisation wont't be provided, then holon will be built without any contextualisation.
     * @param dk
     * @param contextualisation
     * @throws InvalidFormulaException
     * @throws NotApplicableException
     */
    NewNonBinaryHolon(DistributedKnowledge dk, Contextualisation contextualisation) throws InvalidFormulaException, NotApplicableException {
        relatedFormula = dk.getFormula();
        try {
            this.dk = dk.clone();
        } catch (CloneNotSupportedException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Not able to clone knowledge distribution.", e);
        }
        this.contextualisation = contextualisation;
        summaries = new HashMap<>();
        update();
    }


    /**
     * Builds holon for specified formula. If holon has defined particular contextualisation, then it will be used
     * to retrieve needed base profiles from grounding sets.
     */
    @Override
    public void update() throws InvalidFormulaException, NotApplicableException {
        //List<Formula> complFormulas = relatedFormula.getComplementaryFormulas();
        applyContextualisationIfProvided();
        summaries = Grounder.relativeCard_(contextualisedGroundedSets);
        /*
        for (Formula formula : contextualisedGroundedSets.keySet())
            //for (Formula f : complFormulas) {
            summaries.put(formula,
                    Grounder.relativeCard(contextualisedGroundedSets, formula));
*/
    }

    public Formula getRelatedFormula() {
        return relatedFormula;
    }

    @Override
    public Map<Formula, Double> getSummaries() {
        return summaries;
    }

    @Override
    public Map<Formula, Double> getSummaries(List<Formula> selectedFormulas) {
        return summaries.entrySet().stream()
                .filter(e -> selectedFormulas.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Double getSummary(Formula formula) {
        return summaries.get(formula);
    }

    @Override
    public HolonKind getKind() {
        return HolonKind.Non_Binary;
    }

    /**
     * Returns list of complementary formulas which were used when building this holon.
     */
    @Override
    public List<Formula> getAffectedFormulas() {
        return new ArrayList<>(summaries.keySet());
    }

    /**
     * Checks if this holon is related to given formula.
     *
     * @param formula
     * @return
     * @throws InvalidFormulaException
     */
    @Override
    public boolean isApplicable(Formula formula) throws InvalidFormulaException {
        return getAffectedFormulas().contains(formula);
    }

    /**
     * Method launches contextualistion mechanism, if such was provided
     */
    private void applyContextualisationIfProvided() {
        if (contextualisation != null)
            this.contextualisedGroundedSets = contextualisation.performContextualisation(dk.mapOfGroundingSets());
        else this.contextualisedGroundedSets = dk.mapOfGroundingSets();
    }

    @Override
    public String toString() {
        return "NewNonBinaryHolon{" +
                "summaries=" + summaries +
                '}';
    }

    @Override
    public boolean update(DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        if(shouldUpdateDistributedKnowledge(dk)) {
            update();
            return true;
        }
        return false;
    }

    private boolean shouldUpdateDistributedKnowledge(DistributedKnowledge dk) {
        if(dk.isNewerThan(this.dk)) {
            try {
                this.dk = dk.clone();
            } catch (CloneNotSupportedException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, "Not able to clone knowledge distribution.", e);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Pair getStrongest() {
        throw new NotImplementedException();
    }

    @Override
    public Pair getWeakest() {
        throw new NotImplementedException();
    }

    @Override
    public List<Formula> getFormula() {
        throw new NotImplementedException();
    }

    /**
     * Returns contextualisation which was used to build grounding sets for this holon.
     */
    @Override
    public Contextualisation getContextualisation() {
        return contextualisation;
    }

}
