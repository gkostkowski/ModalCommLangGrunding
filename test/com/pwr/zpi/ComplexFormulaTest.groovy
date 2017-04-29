package com.pwr.zpi

import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.language.Operators
import com.pwr.zpi.language.SimpleFormula
import org.junit.Test

/**
 * Created by Weronika on 29.04.2017.
 */

class ComplexFormulaTest extends GroovyTestCase
{
    def oType, model, tr1, tr2, tr3, tr4

    void build()
    {
        tr1 = new TraitSignature("Red")
        tr2 = new TraitSignature("Black")
        tr3 = new TraitSignature("White")
        tr4 = new TraitSignature("Soft")
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
        def cf = new ComplexFormula(model, [tr1, tr2], [State.IS_NOT, State.IS], Operators.Type.AND)
        assertEquals(model, cf.getModel())
        assertEquals([tr1, tr2], cf.getTraits())
        assertEquals([State.IS_NOT, State.IS], cf.getStates())
        assertEquals(Operators.Type.AND, cf.getOperator())

        cf = new ComplexFormula(model, [tr1, tr2], Operators.Type.AND)
        assertEquals(model, cf.getModel())
        assertEquals([tr1, tr2], cf.getTraits())
        assertEquals([State.IS, State.IS], cf.getStates())
        assertEquals(Operators.Type.AND, cf.getOperator())
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
            new ComplexFormula(null, [tr1, tr3], Operators.Type.AND)
        }
        assert msg==a1
        msg = shouldFail {
            new ComplexFormula(model, null, Operators.Type.AND)
        }
        assert msg == a1
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr2], null)
        }
        assert msg == a1
        msg = shouldFail {
            new ComplexFormula(model, [tr1], Operators.Type.AND)
        }
        assert msg == a2
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr2], [State.IS], Operators.Type.AND)
        }
        assert msg == a2
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr2], Operators.Type.BEL)
        }
        assert msg == a2
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr4], Operators.Type.AND)
        }
        assert msg == a3
        msg = shouldFail {
            new ComplexFormula(model, [tr1, tr1], Operators.Type.AND)
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
        def f1 = new ComplexFormula(model, [tr1, tr2], [State.IS, State.IS_NOT], Operators.Type.AND)
        def f2 = new ComplexFormula(model, [new TraitSignature("Red"),
                                            new TraitSignature("Black")], [State.IS, State.IS_NOT], Operators.Type.AND)
        assert f1.equals(f2)
        assert !f1.equals(new ComplexFormula(model, [new TraitSignature("Red"),
                                  new TraitSignature("White")], [State.IS, State.IS_NOT], Operators.Type.AND));

        assert !f1.equals(new ComplexFormula(model, [new TraitSignature("Red"),
                                  new TraitSignature("Black")], [State.IS_NOT, State.IS_NOT], Operators.Type.AND));

        assert !f1.equals(new ComplexFormula(model, [new TraitSignature("Red"),
                                  new TraitSignature("White")], [State.IS_NOT, State.IS_NOT], Operators.Type.OR));

        def model2 = new IndividualModel(new QRCode("POL"), oType)
        assert !f1.equals(new ComplexFormula(model2, [new TraitSignature("Red"),
                                  new TraitSignature("White")], [State.IS_NOT, State.IS_NOT], Operators.Type.OR));

        assert !f1.equals(new SimpleFormula(model, tr1, false));
    }

    /**
     * Testuje tworzenie i zwracanie stron złożonej formuły
     */
    @Test
    void testParts()
    {
        build()
        def cf = new ComplexFormula(model, [tr1, tr2], [State.IS_NOT, State.IS], Operators.Type.AND);
        def lp = new SimpleFormula(model, tr1, true)
        def rp = new SimpleFormula(model, tr2, false)

        assert lp.equals(cf.getLeftPart());
        assert rp.equals(cf.getRightPart());
    }




}