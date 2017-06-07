package com.pwr.zpi.util;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for java non-primitive types used for loading types definitions from config file.
 *
 * @author Grzegorz Kostkowski
 */
public class TypeMapper {

    private static Map<String, Type> typesMap;

    static {
        typesMap = new HashMap<>();
        typesMap.put(String.class.getSimpleName(), String.class);
        typesMap.put(Integer.class.getSimpleName(), Integer.class);
        typesMap.put(Float.class.getSimpleName(), Float.class);
        typesMap.put(Double.class.getSimpleName(), Double.class);
        typesMap.put(Byte.class.getSimpleName(), Byte.class);
        typesMap.put(Boolean.class.getSimpleName(), Boolean.class);
        typesMap.put(Long.class.getSimpleName(), Long.class);
        typesMap.put(Short.class.getSimpleName(), Short.class);
        typesMap.put(Character.class.getSimpleName(), Character.class);
    }

    private TypeMapper() {
    }

    /**
     * Returns Java type for given type name.
     * @param typeName Name of Java type given as string.
     * @return Java type for given type name.
     */
    @Nullable
    public static Type toType(String typeName) {
        return typesMap.get(typeName);
    }

    /**
     * Returns Java class for given type name.
     * @param typeName Name of Java type given as string.
     * @return  Java class for given type name.
     */
    @Nullable
    public static Class<?> toClass(String typeName) {
        return typesMap.get(typeName).getClass();
    }
}
