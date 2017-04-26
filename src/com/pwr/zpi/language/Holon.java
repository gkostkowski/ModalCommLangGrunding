package com.pwr.zpi.language;


/**
 * Holon is representation of agent's reflections on gathered observations. Agent is suppoused to form Holons based
 *  on Traits or Formulas. Holon returns Operator(BEL,POS,KNOW,NOT) ,which has been most frequent in most observations.
 *
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.IndividualModel;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.language.Operators.Type;

/**
 * Class represents holon.
 *
 *
 */

public class Holon{

    protected List<Pair<Integer,com.pwr.zpi.language.Operators.Type>> Tao;
    protected Formula formula;
    protected IndividualModel im;
    protected Map<com.pwr.zpi.language.Operators.Type,Double> Ratio;


    public Holon (Formula formula,Set<BaseProfile> baseProfile,int time,IndividualModel im,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        // Individual model koniec końców powinno się wyciągnąć z formuły.
        //Życzenia Weroniki : Holony dla prostych i złożonych. Nadać holonom sens jako przechowującym cechę i
        //zaprzeczenie . zmodyfikowac groundera <-- Odłożone w czasie
        // Części  jest/nie jest
        //
        this.im = im;
        this.formula = formula;
        update(formula,baseProfile,time,dk);
    }
    /**
     * Allows holon to update it's mental model on certain Trait
     *@param baseProfile
     *@param dk
     *@param formula
     *@param o
     *@param time
     *@param trait
     *
     */
    public void update(Formula formula,Set<BaseProfile> baseProfile,int time,DistributedKnowledge dk) throws InvalidFormulaException, NotApplicableException {
        com.pwr.zpi.language.Operators.Type update = Grounder.determineFulfillment(null, dk, formula);
        Tao.add(new 	Pair<Integer,com.pwr.zpi.language.Operators.Type>(time,update));
        updateRatio();
    }

    /**
     * Holon mercilessly eliminates oldest record in purpose of preserving resources
     */

    public void forgetOldest(){
        int max = 0;
        Pair<Integer,com.pwr.zpi.language.Operators.Type> elder = null;
        for (Pair<Integer,com.pwr.zpi.language.Operators.Type> p:Tao){
            if(p.timestamp > max){max = p.timestamp;elder = p;}
        }
        Tao.remove(elder);
        updateRatio();
    }

    /**
     * Updates ratio of all operators ,which provides  content of Holon
     */
    public void updateRatio(){
        Ratio = new HashMap<com.pwr.zpi.language.Operators.Type,Double>();
        for(Pair<Integer,com.pwr.zpi.language.Operators.Type> p:Tao){
            if(Ratio.containsKey(p.getType())){Ratio.put(p.type, Ratio.get(p.type) + 1.0);}
            else{Ratio.put(p.type, 1.0);}
        }
    }

    /**
     *  Returns Operator which has appeared most of times
     * @return
     */
    public com.pwr.zpi.language.Operators.Type getTaoForFormula(Formula formula){
        com.pwr.zpi.language.Operators.Type Dominant = null;
        Double max = 0.0;
        for (com.pwr.zpi.language.Operators.Type p:Ratio.keySet()){
            if (Ratio.get(p) > max){max = Ratio.get(p);Dominant = p;}
        }
        return Dominant;
    }

    public Formula getFormula(){
        return formula;
    }


    static class Pair<K, V> {

        private final K timestamp;
        private final V type;

        public static <K, V> Pair<K, V> createPair(K element0, V element1) {
            return new Pair<K, V>(element0, element1);
        }

        public Pair(K element0, V element1) {
            this.timestamp = element0;
            this.type = element1;
        }

        public K getTimestamp() {
            return timestamp;
        }

        public V getType() {
            return type;
        }

    }

}