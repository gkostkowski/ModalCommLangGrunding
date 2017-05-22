/*
package com.pwr.zpi;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.language.*;

import java.util.HashSet;
import java.util.Set;

*/
/**
 * Collection stores all of the holons in agents' memory
 *//*

public class HolonCollection {

    private Set<Holon> holonCollection;

    public HolonCollection()
    {
        holonCollection = new HashSet<>();
    }

    */
/**
     * Constructor which sets a set of Holons in Collection
     * @param holonCollection
     *//*

    public HolonCollection(Set<Holon> holonCollection)
    {
        this.holonCollection = holonCollection;
    }


    */
/**
     * Method looks for specific holon in holons and if none is found creates a new one
     * @param formula
     * @param agent
     * @param timeStamp
     * @return desired holon
     *//*

    public Holon getHolon(Formula formula, Agent agent, int timeStamp){
        for(Holon h:holonCollection){
            if(h.getFormula().get(0).isFormulaSimilar(formula)){
                return h;
            }
        }
        return addHolon(formula, agent, timeStamp);
    }

    */
/**
     * Method adds a new holon to holons based on givn formula, agent and timestamp
     * @param formula
     * @param agent
     * @param timestamp
     * @return created holon
     *//*

    public Holon addHolon(Formula formula, Agent agent, int timestamp)
    {
        Holon holon = null;
        try {
            if(formula instanceof SimpleFormula)
                holon = new BinaryHolon(new DistributedKnowledge(agent, formula, timestamp, true));
            else holon = new NonBinaryHolon(new DistributedKnowledge(agent, formula, timestamp, true));
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

    public void updateBeliefs(Agent agent, int timestamp) throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
        for(Holon h : holonCollection){
            System.out.println(h);
            h.update(new DistributedKnowledge(agent, h.getFormula().get(0), timestamp, true));
        }
    }

    */
/*
    public Holon findHolon(Formula formula, Agent agent, int timestamp)
    {
        for(Holon holon : holonCollection)
            if(holon.getFormula().equals(formula))
                return holon;
        return addHolon(formula, agent, timestamp);
    }*//*


}
*/


/**
 * Wprowadzone zmiany względem starego HolonCollection:
 * - głowna zmiana - dodanie referencji agenta jako atrybutu - kolekcja holonow jest tworzona dla konkretnego agenta
 * dlatego lepiej zapamietac go i wywolywac pozniejsze metody przekazujac te referencje niz kazac przy wywolaniu podawac
 * agenta ktory i tak nie moze byc inny niz wlasciciel tej holonCollection
 * - jakieś kosmetyczne poprawki
 */
package com.pwr.zpi.holons;

import com.pwr.zpi.Agent;
import com.pwr.zpi.exceptions.InvalidContextException;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.holons.context.Context;
import com.pwr.zpi.language.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class stores collection of all holons in agent's memory.
 */
public class HolonCollection {

    private Set<Holon> holonCollection;
    Agent owner;
    /**
     * Object of Context concrete class providing "contextualisation service".
     */
    Context holonsContext;


    /**
     * Complex constructor.
     * @param holonCollection
     * @param owner Agent which contains this HolonCollection.
     */
    public HolonCollection(Set<Holon> holonCollection, Agent owner) {
        if (owner == null)
            throw new NullPointerException("Some parameter was not specified.");

        this.holonCollection = holonCollection != null ? holonCollection : new TreeSet<>();
        try {
            checkHolonsConsistency(holonCollection);
        } catch (InvalidContextException e) {
            this.holonCollection = new TreeSet<>();
        }
        this.owner = owner;
    }

    private void checkHolonsConsistency(Set<Holon> holonCollection) throws InvalidContextException {
        Context examined = new ArrayList<>(holonCollection).get(0).getContext();
        for (Holon holon : holonCollection)
            if (!holon.getContext().equals(examined))
                throw new InvalidContextException("Set contains holons with different context.");
    }

    public HolonCollection(Agent owner, Context context) {
            if (owner == null || context == null)
                throw new NullPointerException("Some parameter was not specified.");

            this.holonCollection = new TreeSet<>();
            this.holonsContext = context;
            this.owner = owner;
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
            if (h.getAffectedFormulas().get(0).isFormulaSimilar(formula)) {
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
                holon = new BinaryHolon(new DistributedKnowledge(owner, formula, timestamp, true), holonsContext);
            else holon = new NewNonBinaryHolon(owner.distributeKnowledge(formula, timestamp, true), holonsContext);

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
