package com.pwr.zpi.language;

import com.pwr.zpi.episodic.BaseProfile;
import com.pwr.zpi.semantic.IndividualModel;
import com.pwr.zpi.exceptions.InvalidFormulaException;

import java.util.*;

/**
 * //todo
 *
 * @author Weronika Wolska
 * @author Grzegorz Kostkowski
 */
public abstract class Formula {

    public abstract List<Trait> getTraits();

    public abstract IndividualModel getModel();

    public abstract Type getType();

    public abstract List<State> getStates();

    /**
     * Produces set of formulas which are complementary to this one. Amount and form of this formulas depends on
     * formula type. For convenience, given formula is also included in resulted collection.
     *
     * @return
     */
    public abstract List<Formula> getComplementaryFormulas() throws InvalidFormulaException;

    /**
     * Method returns standard version of this formula. Standard formula is known as formula without any negations.
     *
     * @return new formula which is standard formula.
     */
    public abstract Formula getStandardFormula() throws InvalidFormulaException;

    /**
     * Method is used to point out exact formulas which should be used in grounding process for formulas processed
     * in an <b>indirect way</b>. This situation takes place for disjunctions which use conjunctions.
     * Such formulas can be used for building grounding set or retrieving summarization.
     * In case of disjunction grounding sets, these are composed of more than one conjunctive grounding set.
     * In case of simply modalities and conjunctions method should return formula which is provided as parameter.
     *
     * @return Array of bind formulas used in exact grounding.
     */
    public abstract List<Formula> getDependentFormulas();

    /**
     * Method creates new formula, based on this one, where the only one difference is logic operator given as parameter.
     *
     * @param newOperator
     * @return
     */
    public ComplexFormula transformTo(LogicOperator newOperator) throws InvalidFormulaException {
        return new ComplexFormula(getModel(), getTraits(), getStates(), newOperator);
    }

    /**
     * Determines if checking for epistemic fulfillment relationship should also perform checking related with
     * epsilon-concentration.
     *
     * @return
     */
    public boolean needEpsilonConcentrationChecking() {
        return getType().equals(Type.MODAL_DISJUNCTION) || getType().equals(Type.MODAL_EXCLUSIVE_DISJUNCTION);
    }

    public Comparator<Formula> comparator() {
        return ((f1, f2) -> f1.hashCode() < f2.hashCode() ? -1 : (f1.hashCode() > f2.hashCode() ? 1 : 0));
    }

    /**
     * Method returns standard formula for one of formulas from provided collection. This method can be used
     * when there is certainty that entire collection contains similar formulas - namely, with same operator
     * and same traits.
     * @param familyCandidate
     * @return Formula with same concrete type as this stored in provided collection.
     * @throws InvalidFormulaException
     */

    public static Formula getStandardFormula(Collection<Formula> familyCandidate) throws InvalidFormulaException {
        if (!familyCandidate.isEmpty())
            return familyCandidate.iterator().next().getStandardFormula();
        return null;
    }

    /**
     * This static method is used to filter given family of sets of formulas - retains only this which are supersets
     * for given formula set.
     * @param formulasSet
     * @param relevantFamily
     * @return
     */
    public static List<Set<Formula>> getFormulaSupersets(Collection<Formula> formulasSet,
                                                                      List<Set<Formula>> relevantFamily) {
        List<Set<Formula>> res = new ArrayList<>();
        for (Set<Formula> member : relevantFamily) {
            if (member.size() > formulasSet.size() && new ArrayList<>(member).containsAll(formulasSet))
                res.add(member);
        }
        return res;
    }


    /**
     * Gives list of successive states. Classic case will contains states which describe whether parts of formula[traits]
     * (in case of complex formula - simple formula is special case and contains one part) occur with or without negation.
     * The order of returned states is respective to order of traits returned by getValuedTraits().
     * Returns List of states.
     */

    public enum Type {
        SIMPLE_MODALITY,
        MODAL_CONJUNCTION,
        MODAL_DISJUNCTION, MODAL_EXCLUSIVE_DISJUNCTION;
    }

    abstract boolean equals(Formula other);

/*    abstract com.pwr.zpi.Object getAffectedObject();

    *//**
     * Returns traits affected in formula - only one trait in case of SimpleFormula; two or more in case of ComplexFormula.
     * @return Collections of affected traits.
     *//*
    abstract Collection<Trait> getAffectedTraits();*/

    /**
     * Checks if given base profile is in accordance with mental model implied through this formula.
     * In other words, it checks if this formula is fulfilled by this base profile.
     * Such situation takes place when:
     * a. given base profile contains information about object mentioned in formula and
     * b. this object was classified as having or not having trait(s) mentioned in formula and
     * c. state of this trait's observation is agreeable with state of traits asked in formula
     * <p>
     * While building condition, takes into account order of traits and states in given lists.
     *
     * @param bp
     * @return
     */
    public boolean isFormulaFulfilled(BaseProfile bp) {
        IndividualModel object = getModel();
        List<Trait> traits = getTraits();
        List<State> states = getStates();
        LogicOperator op = null;
        if (!getType().equals(Type.SIMPLE_MODALITY))
            op = ((ComplexFormula) this).getOperator();

        boolean res = op != null && op.equals(LogicOperator.AND);
        for (int i = 0; i < traits.size(); i++) {
            boolean partialRes = bp.checkIfObserved(object, traits.get(i), states.get(i));
            if (op == null)
                res = partialRes;
            else switch (op) {
                case AND:
                    res = res && partialRes;
                    break;
                case OR:
                    res = res || partialRes;
                    break;
                case XOR:
                    if (res && partialRes)
                        return false;
                    res = res || partialRes;
            }
        }
        return res;
    }

    abstract public boolean isFormulaSimilar(Formula formula);

    /**
     * Method determines if holons for such this formula should be build <b>in a direct way</b> (from grounding sets)
     * or with company of others holons. It's determined based on this formula type. Only holons for simple modalities
     * and modal conjunctions are built in direct way.
     *
     * @return
     */
    public boolean usesDirectHolonConstruction() {
        return getType().equals(Type.SIMPLE_MODALITY) || getType().equals(Type.MODAL_CONJUNCTION);
    }
}
