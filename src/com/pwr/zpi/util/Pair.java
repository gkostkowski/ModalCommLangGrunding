package com.pwr.zpi.util;

/**
 * Custom class representing Pair of Objects, addition to standard javafx.util Class is addition of setters.
 * @param <K>
 * @param <V>
 */

public class Pair<K, V> {

    K Case;
    V Value;

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

    public void setK(K k) {
        this.Case = k;
    }

    public void setV(V v) {
        this.Value = v;
    }
}