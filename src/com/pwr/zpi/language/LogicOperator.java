package com.pwr.zpi.language;


/**
 * Enum which collects all the logic operators which can be used in grounded complex formula
 * @author Weronika Wolska
 */
public enum LogicOperator {

        AND, OR, XOR;

        @Override
        public String toString()
        {
                switch (this)
                {
                        case AND: return " and ";
                        case OR: return  " or ";
                        case XOR: return " xor ";
                        default: return null;
                }
        }

        /**
         * In a natural language sentence operator XOR is built as 'either ... or",
         * and this method returns for both OR and XOR 'or' in same place in sentence
         * @return      string modified to use in natural language
         */
        public String getString()
        {
                if(this==AND)
                        return " and ";
                if(this==OR || this==XOR)
                        return " or ";
                return null;
        }

}
