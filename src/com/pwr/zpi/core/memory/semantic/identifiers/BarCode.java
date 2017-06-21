package com.pwr.zpi.core.memory.semantic.identifiers;

/**
 * Example of identifier.
 *
 * @author Mateusz Gawlowski
 */
public class BarCode extends Identifier {

    public BarCode() {
    }

    public BarCode(String idNumber) {
        setId(idNumber);
    }

    @Override
    public boolean isIdMemberOf(String idNumber) {
        return false;
    }
}
