package com.pwr.zpi;

/**
 * Created by Grzesiek on 2017-03-19.
 * Describes State of object ,determines whether object Is,Is not ,or might be.
 *
 */
public enum State {

    Is {
        public double apply() {
            return 1;
        }

        public State and(State second) {
            switch (second) {
                case Is:
                    return Is;
                case Is_Not:
                    return Mayhaps;
                case Mayhaps:
                    return Mayhaps;
            }
            return null;
        }

        public State or(State second) {
            switch (second) {
                case Is:
                    return Is;
                case Is_Not:
                    return Mayhaps;
                case Mayhaps:
                    return Is;
            }
            return null;
        }

        public State not(){
            return Is_Not;
        }

    },
    Is_Not {
        public double apply() {
            return 0;
        }

        public State and(State second) {
            switch (second) {
                case Is:
                    return Mayhaps;
                case Is_Not:
                    return Is_Not;
                case Mayhaps:
                    return Mayhaps;
            }
            return null;
        }

        public State or(State second) {
            switch (second) {
                case Is:
                    return Mayhaps;
                case Is_Not:
                    return Is_Not;
                case Mayhaps:
                    return Is_Not;
            }
            return null;
        }
        public State not(){
            return Is;
        }
    },
    Mayhaps {
        public double apply() {
            return 0.5;
        }

        public State and(State second) {
            return Mayhaps;
        }

        public State or(State second) {
            switch (second) {
                case Is:
                    return Is;
                case Is_Not:
                    return Is_Not;
                case Mayhaps:
                    return Mayhaps;
            }
            return null;
        }
        public State not(){
            return Mayhaps;
        }
    },;

    /**
     *
     * @return double with double description of state Is - 1,Is_Not - 0,Mayhaps - 0.5;
     */
    public abstract double apply();

    @Override
    public String toString() {
        switch (this) {
            case Is:
                System.out.println(" Jest ");
                break;
            case Is_Not:
                System.out.println(" Nie jest ");
                break;
            case Mayhaps:
                System.out.println(" Byc moze ");
                break;
        }
        return "";
    }

    /**
     *
     * @param second State which will be used as operator in AND operator
     * @return  State : Is if both operands(States) ARE,Is_Not if both operands(States) ARE NOT,Mayhaps in any other
     * situation.
     */
    public abstract State and(State second);

    /**
     *
     * @param second State which will be used as operator in OR operator
     * @return  State : Is if both or ARE or one IS while the other Mayhaps.
     * Is_Not if both ARE NOT or one IS NOT while the other Mayhaps.
     * Mayhaps if both MIGHT BE or if one IS while the other IS NOT.
     */

    public abstract State or(State second);

    /**
     *
     * @return State : Is if Is_Not.
     * Is_Not if Is.
     * Mayhaps if Mayhaps.
     *
     */
    public abstract State not();

}