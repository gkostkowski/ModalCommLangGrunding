package language

import com.pwr.zpi.IndividualModel
import com.pwr.zpi.ObjectType
import com.pwr.zpi.QRCode
import com.pwr.zpi.State
import com.pwr.zpi.Trait
import com.pwr.zpi.language.SimpleFormula
import org.junit.Test

/**
 * Created by Weronika on 28.04.2017.
 */


class SimpleFormulaTest extends GroovyTestCase
{
    def id1, type1, model;

    void build()
    {
        id1 = new QRCode("id1");
        type1 = new ObjectType("Type1", [new Trait("Red"), new Trait("White"), new Trait("Big")]);
        model = new IndividualModel(id1, type1);
    }

    /**
     * Tests of proper simple constructors, where one's giving single trait and a boolean instead of State
     */
    @Test
    void testContructor1and2()
    {
        build()
        def trait1 = new Trait("Red");
        def sf1 = new SimpleFormula(model, trait1, true);
        assertEquals(true, sf1.isNegated())
        assertEquals(trait1, sf1.getTrait())
        assertEquals(model, sf1.getModel());

        def sf2 = new SimpleFormula(model, trait1, false);
        assertEquals(false, sf2.isNegated())
        assertEquals(trait1, sf2.getTrait())
        assertEquals(model, sf2.getModel())

        def sf3 = new SimpleFormula(model, trait1)
        assertEquals(false, sf3.isNegated())
        assertEquals(trait1, sf3.getTrait())
        assertEquals(model, sf3.getModel())
    }

    /**
     *  Test konstruktorów, w których podajemy listy cech i stanów
     *  oraz metod getStates(), getValuedTraits(), getModel()
     */
    @Test
    void testConstructor3and4()
    {
        build();
        def trait1 = new Trait("Red");
        def sf1 = new SimpleFormula(model, [trait1], [State.IS]);
        assertEquals(false, sf1.isNegated())
        assertEquals([State.IS], sf1.getStates())
        assertEquals([trait1], sf1.getTraits())
        assertEquals(model, sf1.getModel());

        def sf2 = new SimpleFormula(model, [trait1], [State.IS_NOT]);
        assertEquals(true, sf2.isNegated())
        assertEquals([State.IS_NOT], sf2.getStates())
        assertEquals([trait1], sf2.getTraits())
        assertEquals(model, sf2.getModel());

        def sf3 = new SimpleFormula(model, [trait1])
        assertEquals(false, sf3.isNegated())
        assertEquals([State.IS], sf3.getStates())
        assertEquals([trait1], sf3.getTraits())
        assertEquals(model, sf3.getModel());
    }

    /**
     * Test wyjątków w konstruktorze i przy okazji checkTraits()
     */
    @Test
    void testExceptionsInConstructors()
    {
        build();
        def trait1 = new Trait("Red")
        def trait2 = new Trait("Soft")
        def a1 = "Number of traits or states is not equal to 1"
        def a2 = "Trait doesn't describe type of the model1"
        def a3 = "One of the parameters is null"

        def msg = shouldFail {
            new SimpleFormula(model, [trait1, trait2])
        }
        assert msg == a1

        msg = shouldFail {
            new SimpleFormula(model, [])
        }
        assert msg == a1

        msg = shouldFail {
            new SimpleFormula(model, [trait1], [])
        }
        assert msg == a1

        msg = shouldFail {
            new SimpleFormula(model, trait2)
        }
        assert msg == a2

        msg = shouldFail {
            new SimpleFormula(model, [trait2], [])
        }
        assert msg == a1;

        msg = shouldFail {
            new SimpleFormula(model, [trait2], [State.IS_NOT])
        }
        assert  msg == a2

        msg = shouldFail {
            new SimpleFormula(null, trait1);
        }
        assert msg == a3

        msg = shouldFail {
            new SimpleFormula(model, null,);
        }
        assert msg == a3

        msg = shouldFail {
            new SimpleFormula(model, [trait1], null);
        }
        assert msg == a3
    }

    /**
     * Test method that compares two complementaryFormulas
     */
    @Test
    void testEqual()
    {
        build()
        def t1 = new Trait("Red")
        def t2 = new Trait("White")
        def f1 = new SimpleFormula(model, t1, true)

        def type = new ObjectType("ID", [t1]);
        def model2 = new IndividualModel(new QRCode("JAk"), type);
        def f2 = new SimpleFormula(model2, t1, true);
        assert false == f1.equals(f2)

        f2 = new SimpleFormula(model, new Trait("Red"), true)
        assertEquals(true, f1.equals(f2))

        f2 = new SimpleFormula(model, new Trait("Red"), false)
        assert false == f2.equals(f1);

        f2 = new SimpleFormula(model, t2, true)
        assert false == f1.equals(f2)

    }




}