package com.pwr.zpi.language;

/**
 * Describes language symbols ,which provide logica operations.
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
    
    //Zmienić nazwę na Operators
    //
}
