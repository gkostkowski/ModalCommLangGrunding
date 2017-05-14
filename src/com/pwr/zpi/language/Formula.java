package com.pwr.zpi.language;

import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.IndividualModel;
import com.pwr.zpi.State;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidFormulaException;

import java.util.List;

/**
 *
 */
public abstract class Formula {

    public abstract List<Trait> getTraits();

    public abstract IndividualModel getModel();

    public abstract Type getType();

    public abstract List<State> getStates();

    /**
     * Produces set of formulas which are complementary to this one. Amount and form of this formulas depends on
     * formula type. For convenience, given formula is also included in resulted collection.
     * @return
     */
    public abstract List<Formula> getComplementaryFormulas() throws InvalidFormulaException;

    /**
     * Gives list of successive states. Classic case will contains states which describe whether parts of formula[traits]
     * (in case of complex formula - simple formula is special case and contains one part) occur with or without negation.
     * The order of returned states is respective to order of traits returned by getValuedTraits().
     * @return List of states.
     */

    public enum Type {
        SIMPLE_MODALITY,
        MODAL_CONJUNCTION
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
        LogicOperator op = getType().equals(Formula.Type.SIMPLE_MODALITY) ? null
                :(getType().equals(Formula.Type.MODAL_CONJUNCTION) ? LogicOperator.AND :LogicOperator.OR);

        boolean res = op != null && op.equals(LogicOperator.AND) ? true : false;
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
            }
        }
        return res;
    }

    abstract public boolean isFormulaSimilar(Formula formula);

}
