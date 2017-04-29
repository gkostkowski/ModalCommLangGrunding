package com.pwr.zpi

import org.junit.Test

/**
 * Created by Weronika on 29.04.2017.
 */

class IndividualModelTest extends GroovyTestCase
{
    def oType, model

    void build()
    {
        oType = new ObjectType("type1", [new TraitSignature("Red"), new TraitSignature("White"), new TraitSignature("Black")])
        model = new IndividualModel(new QRCode("id1"), oType)
    }

    /**
     * Sprawdzenie metody checkIfContainsTrait(TraitSignature trait)
     */
    @Test
    void testCheckIfContainTrait()
    {
        build();
        def t1 = new TraitSignature("Red");
        assert true == model.checkIfContainsTrait(t1);

        def t2 = new TraitSignature("Soft");
        assert false == model.checkIfContainsTrait(t2);
    }

    /**
     * Sprawdzenie metody checkIfContainsTraits(List<TraitSignature> traits)
     */
    @Test
    void testCheckIfContainsTraits()
    {
        build();
        def t1 = new TraitSignature("Red");
        def t2 = new TraitSignature("Soft");
        def t3 = new TraitSignature("White");
        def t4 = new TraitSignature("Black")

        assert true == model.checkIfContainsTraits([t1, t3]);
        assert true == model.checkIfContainsTraits([t3, t1]);
        assert true == model.checkIfContainsTraits([t1]);
        assert true == model.checkIfContainsTraits([t1, t4, t3])

        assert false == model.checkIfContainsTraits([t2]);
        assert false == model.checkIfContainsTraits([t1, t2])
        assert false == model.checkIfContainsTraits([t1, t2, t3, t4])
        assert false == model.checkIfContainsTraits([t1, t3, t4, t2])

    }

}
