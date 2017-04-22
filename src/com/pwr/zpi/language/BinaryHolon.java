package com.pwr.zpi.language;
import java.util.List;
import java.util.Set;

import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.Observation;
import com.pwr.zpi.Trait;

/**
 * Class represents binary holon involving information whether trait has or has been not observed.
 */
public class BinaryHolon extends Holon{

    protected List<Pair<Double,Double>> Tao;
    protected Pair<Double,Double> Ratio;
    protected Trait trait;
    protected Object;
    public BinaryHolon (Trait t,Set<BaseProfile> baseProfile,int time,Object o){
        trait = t;
        update(baseProfile,time,o);
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
    public void update(Formula formula,Set<BaseProfile> baseProfile,int time,Object o,Trait trait,DistributedKnowledge dk){
        com.pwr.zpi.language.Operators.Type update = Grounder.determineFulfillment(null, dk, formula);
        Tao.add(new Pair<Integer,com.pwr.zpi.language.Operators.Type>(time,update));
        updateRatio()
    }

    /**
     * Holon mercilessly eliminates oldest record in purpose of preserving resources
     */

    public void forgetOldest(){
        Tao.remove(Tao.get(Tao.size()));
        updateRatio();
    }

    /**
     * Updates ratio of all operators ,which provides  content of Holon
     */
    public void updateRatio(){
        Double allP = 0.0,allNot_P = 0.0;
        for (Pair<Double,Double> p:Tao){
            allP += p.P;
            allNot_P += p.not_P;
        }
        Ratio = new Pair<Double,Double>(allP/Tao.size(),allNot_P/Tao.size());
    }

    /**
     *  Returns value of IS state of Trait
     * @return
     */
    public double getP(){
        return Ratio.getP();
    }
    /**
     *  Returns value of IS_Not state of Trait
     * @return
     */
    public double getnot_P(){
        return Ratio.getnot_P();
    }
    /**
     *  Returns Operator which has appeared most of times
     * @return
     */
    public com.pwr.zpi.language.Operators.Type getDominant(){
        return Ratio.getP() > Ratio.getnot_P() ? com.pwr.zpi.language.Operators.Type.KNOW :com.pwr.zpi.language.Operators.Type.NOT ;
    }

    static class Pair<K, V> {

        private final K P;
        private final V not_P;

        public static <K, V> Pair<K, V> createPair(K element0, V element1) {
            return new Pair<K, V>(element0, element1);
        }

        public Pair(K element0, V element1) {
            this.P = element0;
            this.not_P = element1;
        }

        public K getP() {
            return P;
        }

        public V getnot_P() {
            return not_P;
        }

    }
}