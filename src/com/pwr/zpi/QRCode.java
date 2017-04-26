package com.pwr.zpi;

/**
 * Example of identifier.
 */
public class QRCode extends Identifier{

    int id;

    public QRCode(int id) {
        this.id = id;
    }

    public String getIdNumber() {
        return String.valueOf(id);
    }

}
