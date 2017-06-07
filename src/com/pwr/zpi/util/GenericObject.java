package com.pwr.zpi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * todo
 *
 * @param <T> Generic type
 *
 * @author Grzegorz Kostkowski
 */
public class GenericObject<T> {
    private T object;

    private static final Map<Class<?>, Class<?>> typesMap = new HashMap<>();

    public GenericObject(Class<T> c, Object arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (arg == null)
            object = c.newInstance();
        else {
            setTypesMap();
            Class<?> type = typesMap.containsKey(arg.getClass()) ? typesMap.get(arg.getClass()) : arg.getClass();
            arg.getClass().getConstructor(type).newInstance(arg);
        }
    }


    private void setTypesMap() {
        typesMap.put(Integer.class, int.class);
        typesMap.put(Short.class, short.class);
        typesMap.put(Float.class, float.class);
        typesMap.put(Byte.class, byte.class);
        typesMap.put(Long.class, long.class);
        typesMap.put(Double.class, double.class);
        typesMap.put(Boolean.class, boolean.class);
        typesMap.put(Character.class, char.class);
    }

    public void setValue(T val) {
        object = val;
    }

    public T getObj() {
        return object;//(objClass) object;
    }
}
