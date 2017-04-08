package com.pwr.zpi;

import com.pwr.zpi.util.TypeMapper;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Grzesiek on 2017-04-08.
 */
public class TraitSignature<K, V> {
    private String name;
    //private K name;
    private String valueType;



    public TraitSignature(String name, String valueType) {
        this.name = name;
        try {
            this.valueType = valueType;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public Type getValueType() {
        return TypeMapper.toType(valueType);
    }
    public Class<?> getValueClass() {
        return TypeMapper.toClass(valueType);
    }

    public String getName() {
        return name;
    }
/*
    public TraitSignature(K name, Class<?> valueType) {
        this.name = name;
        try {
            this.valueType = (Class<V>) valueType;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public Class<V> getValueType() {
        return valueType;
    }

    public K getName() {
        return name;
    }
*/
}
