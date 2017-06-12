package com.pwr.zpi.language;


/**
 * //todo
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
