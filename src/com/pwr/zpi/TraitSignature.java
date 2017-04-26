package com.pwr.zpi;

import com.pwr.zpi.util.TypeMapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Grzesiek on 2017-04-08.
 */
public class TraitSignature<V> {
    private String name;
    private String valueType;

    private List<String> domain; //todo

    public TraitSignature(String name, String valueType, List<String> domain)
    {
        this.name = name;
        try {
            this.valueType = valueType;
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.domain = domain;
    }

    public TraitSignature(String name, String valueType) {
        this(name, valueType, null);
    }

    public Type getValueType() {
        return TypeMapper.toType(valueType);
    }

    public String getValueTypeString()
    {
        return valueType;
    }

    public Class<?> getValueClass() {
        return TypeMapper.toClass(valueType);
    }

    public String getName() {
        return name;
    }

    public boolean isInDomain(String value)
    {
        for(String v : domain)
            if(v.equalsIgnoreCase(value))
                return true;
        return false;
    }

    public List<String> getDomain() {
        return domain;
    }

    public void setDomain(List<String> domain) {
        this.domain = domain;
    }

    /*    public boolean isInDomain(V value)
    {
        for(String v : domain)
            if(v.equals(value))
                return true;
        return false;
    }*/

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
