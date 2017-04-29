package com.pwr.zpi.language;

/**
 * Describes language symbols ,which provide logica operations.
 */
public class Operators {

    private static Type type;

    public enum Type {
        AND, OR, XOR, NOT,
        POS, BEL, KNOW;
        public Type getType() {
            return type;
        }
    }


}
