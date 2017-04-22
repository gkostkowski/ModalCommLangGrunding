package com.pwr.zpi;

/**
 * Created by Mateo on 22.04.2017.
 */
public class QRCode extends Identifier{

    int id;

    public QRCode(int id) {
        this.id = id;
    }

    public ObjectType getType() {
        return null; //todo
    }

    public String getId() {
        return String.valueOf(id);
    }
}
