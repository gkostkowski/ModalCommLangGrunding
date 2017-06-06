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
import com.pwr.zpi.language.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;


/**
 * This class implements non-binary holon which is related to complex formulas and provides summarization them.
 * Foundations for non-binary holon are related with conjunctive grounding sets and respective relative cardinality.
 * In case of disjunctions (simple and exclusive) entire summarization can be calculated when basing on respective
 * conjunctive summarizations. Thus, holons for disjunctions are not generated directly, but with usage of already created
 * conjunctive holons.
 * Note: Important contract is that due to above conditions, values stored in holon are not accessed directly, but through
 * HolonsIntercessor.
 */
public class NewNonBinaryHolon implements Holon, Comparable<NewNonBinaryHolon> {
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
        this.dk = dk;
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
        return null;
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
        if(updateDistributedKnowledge(dk)) {
            update();
            return true;
        }
        return false;
    }

    private boolean updateDistributedKnowledge(DistributedKnowledge dk) {
        if(dk.isNewerThan(this.dk)) {
            this.dk = dk;
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(NewNonBinaryHolon o) {
        double res=0, res2=0;
        for (Formula f:summaries.keySet()) {
            res = f.hashCode() + summaries.get(f);
        }

        for (Formula f:o.summaries.keySet()) {
            res2 = f.hashCode() + o.summaries.get(f);
        }
        return res > res2 ? 1: (res < res2 ? -1 : 0);
    }
}
