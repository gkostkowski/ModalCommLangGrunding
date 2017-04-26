package com.pwr.zpi;

/**
 * Example of identifier.
 */
public class QRCode extends Identifier{

    String id;

    public QRCode(String id) {
        this.id = id;
    }

    public String getIdNumber() {
        return id;
    }

}
