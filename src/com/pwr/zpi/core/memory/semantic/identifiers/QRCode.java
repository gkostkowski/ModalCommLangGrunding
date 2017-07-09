package com.pwr.zpi.core.memory.semantic.identifiers;

/**
 * Example of identifier.
 *
 * @author Mateusz Gawlowski
 */
public class QRCode extends Identifier {

    public QRCode() {
    }

    public QRCode(String idNumber) {
        setId(idNumber);
    }

    @Override
    public boolean isIdMemberOf(String idNumber) {
        return true; //to implement
    }

    @Override
    public String toString() {
        return "QRCode{" +
                "id='" + getIdNumber() + '\'' +
                '}';
    }
}
