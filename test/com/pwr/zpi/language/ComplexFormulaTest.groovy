package com.pwr.zpi.language

import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode
import org.junit.Test

/**
 * Created by Weronika on 29.04.2017.
 */

class ComplexFormulaTest extends GroovyTestCase
{
    def oType, model, tr1, tr2, tr3, tr4

    void build()
    {
        tr1 = new Trait("Red")
        tr2 = new Trait("Black")
        tr3 = new Trait("White")
        tr4 = new Trait("Soft")
        oType = new ObjectType("Typ1", [tr1, tr2, tr3])
        model = new IndividualModel(new QRCode("ID"), oType)
    }

    /**
     * Testy konstruktorów
     */
    @Test
    void testConstructors()
    {
        build()
        def cf = new ComplexFormula(model, [tr1, tr2], [State.IS_NOT, State.IS], LogicOperator.AND)
        assertEquals(model, cf.getModel())
        assertEquals([tr1, tr2], cf.getTraits())
        assertEquals([State.IS_NOT, State.IS], cf.getStates())
        assertEquals(LogicOperator.AND, cf.getOperator())

        cf = new ComplexFormula(model, [tr1, tr2], LogicOperator.AND)
        assertEquals(model, cf.getModel())
        assertEquals([tr1, tr2], cf.getTraits())
        assertEquals([State.IS, State.IS], cf.getStates())
        assertEquals(LogicOperator.AND, cf.getOperator())
    }

    /**
     * Test wyłapaywania wyjątków w konstruktorach + test checkTraits()
     */
    @Test
    void testExceptionsInConstructors()
    {
        build();
        def a1 = "One or more parameteres are null"
        def a2 = "Either size of traits or states is not 2 or operator is not valid"
        def a3 = "Given traits don't describe type of the object"

        def msg = shouldFail {
            new ComplexFormula(null, [tr1, tr3], LogicOperator.And)
        }
        assert msg==a1
        msg = shouldFail {
            new ComplexFormula(model, null, LogicOperator.AND)
        }
        assert msg == a1
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr2], null)
        }
        assert msg == a1
        msg = shouldFail {
            new ComplexFormula(model, [tr1], LogicOperator.AND)
        }
        assert msg == a2
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr2], [State.IS], LogicOperator.AND)
        }
        assert msg == a2
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr2], ModalOperator.BEL)
        }
        assert msg == a2
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr4], LogicOperator.AND)
        }
        assert msg == a3
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr1], LogicOperator.AND)
        }
        assert msg == a3

    }

    /**
     * Testuje metodę porównującą dwie złożone formuły, equal() + compareTraits()
     */
    @Test
    void testEqual()
    {
        build()
        def f1 = new ComplexFormula(model, [tr1, tr2], [State.IS, State.IS_NOT], LogicOperator.AND)
        def f2 = new ComplexFormula(model, [new Trait("Red"),
                                            new Trait("Black")], [State.IS, State.IS_NOT], LogicOperator.AND)
        assert f1.equals(f2)
        assert !f1.equals(new ComplexFormula(model, [new Trait("Red"),
                                                     new Trait("White")], [State.IS, State.IS_NOT], LogicOperator.AND));

        assert !f1.equals(new ComplexFormula(model, [new Trait("Red"),
                                                     new Trait("Black")], [State.IS_NOT, State.IS_NOT], LogicOperator.AND));

        assert !f1.equals(new ComplexFormula(model, [new Trait("Red"),
                                                     new Trait("White")], [State.IS_NOT, State.IS_NOT], LogicOperator.Or));

        def model2 = new IndividualModel(new QRCode("POL"), oType)
        assert !f1.equals(new ComplexFormula(model2, [new Trait("Red"),
                                                      new Trait("White")], [State.IS_NOT, State.IS_NOT], LogicOperator.Or));

        assert !f1.equals(new SimpleFormula(model, tr1, false));
    }

    /**
     * Testuje tworzenie i zwracanie stron złożonej formuły
     */
    @Test
    void testParts()
    {
        build()
        def cf = new ComplexFormula(model, [tr1, tr2], [State.IS_NOT, State.IS], LogicOperator.AND);
        def lp = new SimpleFormula(model, tr1, true)
        def rp = new SimpleFormula(model, tr2, false)

        assert lp.equals(cf.getLeftPart());
        assert rp.equals(cf.getRightPart());
    }




}