package com.pwr.zpi.language;

//todo usunac i uzywac klasy ze standardu
@Deprecated
public class Pair<K, V> {

    final K Case;
    final V Value;

    public static <K, V> Pair<K, V> createPair(K element0, V element1) {
        return new Pair<K, V>(element0, element1);
    }

    public Pair(K element0, V element1) {
        this.Case = element0;
        this.Value = element1;
    }

    public K getK() {
        return Case;
    }

    public V getV() {
        return Value;
    }

}