package language;/*
package com.pwr.zpi.language;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.management.resources.agent;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

*/
/**
 * Created by Jarema on 5/20/2017.
 *//*

class BinaryHolonTest {


    Agent agent = new Agent();
    QRCode[] qrCodes = new QRCode[]{new QRCode("0124"), new QRCode("02442"), new QRCode("01442")};
    Trait[] tr = new Trait[]{
            new Trait("Red"),
            new Trait("White"),
            new Trait("Blinking"),
            new Trait("Blue"),
            new Trait("Soft")};
    int t = 0;


    Observation[] obsTill3  = new Observation[]{ //inclusively
            new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                put(tr[0], true);
                put(tr[1], false);
                put(tr[2], false);
            }}, t++),
            new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                put(tr[0], true);
                put(tr[1], false);
                put(tr[2], false);
            }}, t++),
            new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                put(tr[0], true);
                put(tr[1], false);
                put(tr[2], true);
            }}, t++),
            new Observation(qrCodes[0], new HashMap<Trait, Boolean>() {{
                put(tr[0], true);
                put(tr[1], false);
                put(tr[2], false);
            }}, t++)
    };
    ObjectType type1 = new ObjectType("1", Arrays.asList(tr[0],tr[1]));
    IndividualModel im1 = new IndividualModel(qrCodes[0],type1);
    @Test
    void mainTest() throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
        agent.getModels().addNameToModel(qrCodes[0], "Hyzio");
        agent.addAndUpdate(obsTill3);
        Formula f1 = new SimpleFormula(im1,tr[0],false);
        DistributedKnowledge dk = new DistributedKnowledge(agent,f1);
        Holon h1 = new BinaryHolon(dk);
        //Assertions.assertEquals(h1.getFormula(),f1);
    }

    @Test
    void getFormula() {

    }

    @Test
    void isApplicable() {

    }

    @Test
    void getP() {

    }

    @Test
    void getnot_P() {

    }

    @Test
    void getStrongest() {

    }

    @Test
    void getWeakest() {

    }

    @Test
    void getKind() {

    }

    @Test
    void update1() {

    }

    @Test
    void getStrongest1() {

    }

    @Test
    void getWeakest1() {

    }

    @Test
    void getKind1() {

    }

    @Test
    void getFormula1() {

    }

    @Test
    void isApplicable1() {

    }

}*/
