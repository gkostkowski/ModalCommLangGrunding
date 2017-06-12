package com.pwr.zpi.language;


/**
 * Enum which collects all the logic operators which can be used in grounded complex formula
 */
public enum LogicOperator {

        AND, OR, XOR;

        @Override
        public String toString()
        {
                switch (this)
                {
                        case AND: return "and ";
                        case OR: return  "or ";
                        case XOR: return "xor";
                        default: return null;
                }
        }

}
