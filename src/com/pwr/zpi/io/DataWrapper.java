/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.io;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class DataWrapper <T> implements Serializable {
    private LinkedList<T> collection;

    public DataWrapper() {
        collection = new LinkedList<T>();
    }
    public DataWrapper(LinkedList<T> elems) {
        setData(elems);
    }

    public Collection<T> getData() {
        return collection;
    }

    public void setData(LinkedList<T> elems) {
        this.collection = elems;
    }

}
