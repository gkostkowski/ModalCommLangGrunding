package com.pwr.zpi;

/**
 * Example of identifier.
 */
public class BarCode extends Identifier{

    String id;

    public BarCode(String id) {
        this.id = id;
    }

    public String getIdNumber() {
        return id;
    }
}
