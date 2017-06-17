package semantic

import com.pwr.zpi.core.memory.semantic.IndividualModel
import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.core.memory.semantic.QRCode
import com.pwr.zpi.language.Trait
import org.junit.Test

/**
 * Created by Weronika on 29.04.2017.
 */

class IndividualModelTest extends GroovyTestCase
{
    def oType, model

    void build()
    {
        oType = new ObjectType("type1", [new Trait("Red"), new Trait("White"), new Trait("Black")])
        model = new IndividualModel(new QRCode("id1"), oType)
    }

    /**
     * Sprawdzenie metody checkIfContainsTrait(Trait trait)
     */
    @Test
    void testCheckIfContainTrait()
    {
        build();
        def t1 = new Trait("Red");
        assert true == model.checkIfContainsTrait(t1);

        def t2 = new Trait("Soft");
        assert false == model.checkIfContainsTrait(t2);
    }

    /**
     * Sprawdzenie metody checkIfContainsTraits(List<Trait> traits)
     */
    @Test
    void testCheckIfContainsTraits()
    {
        build();
        def t1 = new Trait("Red");
        def t2 = new Trait("Soft");
        def t3 = new Trait("White");
        def t4 = new Trait("Black")

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
