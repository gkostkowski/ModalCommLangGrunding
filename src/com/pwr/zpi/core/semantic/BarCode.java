package com.pwr.zpi.core.semantic;

/**
 * Example of identifier.
 */
public class BarCode extends Identifier {

    String id;

    public BarCode(String id) {
        this.id = id;
    }

    public String getIdNumber() {
        return id;
    }
}
