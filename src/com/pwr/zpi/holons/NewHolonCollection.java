package com.pwr.zpi.holons;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.language.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Class stores collection of all holons in agent's memory.
 */
public class NewHolonCollection {

    private Set<Holon> holonCollection;
    Agent owner;

    public NewHolonCollection() {
        holonCollection = new HashSet<>();
    }

    /**
     * Complex constructor.
     * @param holonCollection
     * @param owner Agent which contains this HolonCollection.
     */
    public NewHolonCollection(Set<Holon> holonCollection, Agent owner) {
        if (owner == null)
            throw new NullPointerException("Agent not specified.");
        this.holonCollection = holonCollection != null ? holonCollection : new HashSet<>();
        this.owner = owner;
    }

    public NewHolonCollection(Agent owner) {
        this(null, owner);
    }


    /**
     * Method looks for specific holon in holons and returns it. If none is found returns newly created one.
     *
     * @param formula
     * @param timeStamp
     * @return desired holon
     */
    public Holon getHolon(Formula formula, int timeStamp) {
        for (Holon h : holonCollection) {
            if (h.getFormula().get(0).isFormulaSimilar(formula)) {
                return h;
            }
        }
        return addHolon(formula, timeStamp);
    }

    /**
     * Method adds a new holon to holons based on givn formula, agent and timestamp
     *
     * @param formula
     * @param timestamp
     * @return created holon
     */
    public Holon addHolon(Formula formula, int timestamp) {
        Holon holon = null;
        try {
            if (formula instanceof SimpleFormula)
                holon = new BinaryHolon(new DistributedKnowledge(owner, formula, timestamp, true), context);
            else holon = new NonBinaryHolon(owner.distributeKnowledge(formula, timestamp, true));

            holonCollection.add(holon);
        } catch (InvalidFormulaException e) {
            e.printStackTrace();
        } catch (NotApplicableException e) {
            e.printStackTrace();
        } catch (NotConsistentDKException e) {
            e.printStackTrace();
        }
        return holon;
    }

    public void updateBeliefs(int timestamp) throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
        for (Holon h : holonCollection) {
            System.out.println(h);
            h.update(owner.distributeKnowledge(h.getFormula().get(0), timestamp, true));
        }
    }


    public Holon findHolon(Formula formula, Agent agent, int timestamp) {
        for (Holon holon : holonCollection)
            if (holon.getFormula().equals(formula))
                return holon;
        return addHolon(formula, timestamp);
    }

}
