package com.pwr.zpi;

/**
 * Created by Grzesiek on 2017-03-16.
 */
public class Trait <K, V> {
    private K name;

    public K getName() {
        return name;
    }

    public void setName(K name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    private V value;

}
