package com.pwr.zpi.language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.Object;
import com.pwr.zpi.Trait;
import com.pwr.zpi.language.Operators.Type;

/**
 * Class represents non binary holon involving information about states that certain formula has been observed in.
 *
 *
 */

public class NonBinaryHolon extends Holon{
	
	protected List<Pair<Integer,com.pwr.zpi.language.Operators.Type>> Tao;
	protected Formula formula;
	protected IndividualModel im;
	protected Map<com.pwr.zpi.language.Operators.Type,Double> Ratio;
	
	public NonBinaryHolon (Formula formula,Set<BaseProfile> baseProfile,int time,IndividualModel im){
		//Obiekt i Trait z Formuły
		// Individual model koniec końców powinno się wyciągnąć z formuły.
		//Życzenia Weroniki : Holony dla prostych i złożonych. Nadać holonom sens jako przechowującym cechę i
		//zaprzeczenie . zmodyfikowac groundera <-- Odłożone w czasie
		// Części  jest/nie jest
		//
		this.im = im;
		this.formula = formula;
		update(formula,baseProfile,time);
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
	public void update(Formula formula,Set<BaseProfile> baseProfile,int time,DistributedKnowledge dk){
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

    public com.pwr.zpi.language.Operators.Type getTaoForOperatorType(Formula formula){
    	//Zwracamy wartość dla  Traita
		//Formuła.
		//todo
	}


    /**
     *  Returns Operator which has appeared most of times
     * @return
     */
	public com.pwr.zpi.language.Operators.Type getDominant(){
		com.pwr.zpi.language.Operators.Type Dominant = null;
		Double max = 0.0;
		for (com.pwr.zpi.language.Operators.Type p:Ratio.keySet()){
			if (Ratio.get(p) > max){max = Ratio.get(p);Dominant = p;}
		}
		return Dominant;
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