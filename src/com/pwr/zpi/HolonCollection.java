package com.pwr.zpi;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import com.pwr.zpi.language.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Collection stores all of the holons in agents' memory
 */
public class HolonCollection {

    private Set<Holon> holonCollection;

    public HolonCollection()
    {
        holonCollection = new HashSet<>();
    }

    /**
     * Constructor which sets a set of Holons in Collection
     * @param holonCollection
     */
    public HolonCollection(Set<Holon> holonCollection)
    {
        this.holonCollection = holonCollection;
    }


    public Holon getHolon(Formula formula, Agent agent, int timeStamp){
        for(Holon h:holonCollection){
            if(h.getFormula().isFormulaSimilar(formula)){
                return h;
            }
        }
        return addHolon(formula, agent, timeStamp);
    }

   /**
     * Method adds holon based on Formula
     * @param formula
     * @return
     */
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

    public void updateBeliefs(Formula formula, Agent agent, int timestamp) throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
        for(Holon h : holonCollection){
            h.update(new DistributedKnowledge(agent, h.getFormula(), timestamp, true));
        }
    }

    /*
    public Holon findHolon(Formula formula, Agent agent, int timestamp)
    {
        for(Holon holon : holonCollection)
            if(holon.getFormula().equals(formula))
                return holon;
        return addHolon(formula, agent, timestamp);
    }*/







}
