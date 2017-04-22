package com.pwr.zpi;

/**
 * Created by Mateo on 22.04.2017.
 */
public class BarCode extends Identifier {

    String id;

    public BarCode(String id) {
        this.id = id;
    }

    public ObjectType getType() {
        return null; //todo
    }

    public String getId() {
        return id;
    }
}
