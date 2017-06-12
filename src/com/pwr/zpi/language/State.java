package com.pwr.zpi.language;

/**
 * Describes state of trait in object - determines whether trait is, is not or might be in object.
 * @author Jarema Radom
 */
public enum State {

    IS {
        public double apply() {
            return 1;
        }

        public State and(State second) {
            switch (second) {
                case IS:
                    return IS;
                case IS_NOT:
                    return MAYHAPS;
                case MAYHAPS:
                    return MAYHAPS;
            }
            return null;
        }

        public State or(State second) {
            switch (second) {
                case IS:
                    return IS;
                case IS_NOT:
                    return MAYHAPS;
                case MAYHAPS:
                    return IS;
            }
            return null;
        }

        public State not(){
            return IS_NOT;
        }

    },
    IS_NOT {
        public double apply() {
            return 0;
        }

        public State and(State second) {
            switch (second) {
                case IS:
                    return MAYHAPS;
                case IS_NOT:
                    return IS_NOT;
                case MAYHAPS:
                    return MAYHAPS;
            }
            return null;
        }

        public State or(State second) {
            switch (second) {
                case IS:
                    return MAYHAPS;
                case IS_NOT:
                    return IS_NOT;
                case MAYHAPS:
                    return IS_NOT;
            }
            return null;
        }
        public State not(){
            return IS;
        }
    },
    MAYHAPS {
        public double apply() {
            return 0.5;
        }

        public State and(State second) {
            return MAYHAPS;
        }

        public State or(State second) {
            switch (second) {
                case IS:
                    return IS;
                case IS_NOT:
                    return IS_NOT;
                case MAYHAPS:
                    return MAYHAPS;
            }
            return null;
        }
        public State not(){
            return MAYHAPS;
        }
    },;

    /**
     *
     * @return double with double description of state IS - 1,IS_NOT - 0,MAYHAPS - 0.5;
     */
    public abstract double apply();

    @Override
    public String toString() {
        switch (this) {
            case IS:
                System.out.println(" Jest ");
                break;
            case IS_NOT:
                System.out.println(" Nie jest ");
                break;
            case MAYHAPS:
                System.out.println(" Byc moze ");
                break;
        }
        return "";
    }

    /**
     *
     * @param second State which will be used as operator in AND operator
     * @return  State : IS if both operands(States) ARE,IS_NOT if both operands(States) ARE NOT,MAYHAPS in any other
     * situation.
     */
    public abstract State and(State second);

    /**
     *
     * @param second State which will be used as operator in OR operator
     * @return  State : IS if both or ARE or one IS while the other MAYHAPS.
     * IS_NOT if both ARE NOT or one IS NOT while the other MAYHAPS.
     * MAYHAPS if both MIGHT BE or if one IS while the other IS NOT.
     */

    public abstract State or(State second);

    /**
     *
     * @return State : IS if IS_NOT.
     * IS_NOT if IS.
     * MAYHAPS if MAYHAPS.
     *
     */
    public abstract State not();

}