package com.pwr.zpi.language;

/**
 * Created by Grzesiek on 2017-03-19.
 */
public class Operations {

    enum Type {
        AND, OR, XOR,
        POS, BEL, KNOW
    }

    <T> boolean XorY(T op1, T op2){
        return false; //todo
    }
    <T> boolean XandY(T op1, T op2){
        return false; //todo
    }
    <T> boolean XxorY(T op1, T op2){
        return false; //todo
    }

    <T> double posX(T operand) {}
    <T> double knowX(T operand) {}
    <T> double belX(T operand) {}
}
