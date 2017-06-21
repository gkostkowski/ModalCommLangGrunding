import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.language.Formula
import com.pwr.zpi.language.LogicOperator
import com.pwr.zpi.language.ModalOperator
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait
import com.pwr.zpi.linguistic.ComplexStatement
import com.pwr.zpi.core.memory.semantic.IndividualModel
import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.core.memory.semantic.identifiers.QRCode
import org.junit.Test

/**
 * Created by Weronika on 13.06.2017.
 */
class ComplexStatementTest extends GroovyTestCase {

    def objectType
    def model1
    def trait1, trait2, trait3;
    def cf1, cf2, cf3, cf4

    void setUp()
    {
        trait1 = new Trait("Red")
        trait2 = new Trait("Black")
        objectType = new ObjectType("ID", [trait1, trait2, trait3])
        def id = new QRCode("id1")
        model1 = new IndividualModel(id, objectType)
        cf1 = new ComplexFormula(model1, [trait1, trait2], [State.IS, State.IS], LogicOperator.AND)
        cf2 = new ComplexFormula(model1, [trait1, trait2], [State.IS_NOT, State.IS], LogicOperator.AND)
        cf3 = new ComplexFormula(model1, [trait1, trait2], [State.IS, State.IS_NOT], LogicOperator.AND)
        cf4 = new ComplexFormula(model1, [trait1, trait2], [State.IS_NOT, State.IS_NOT], LogicOperator.AND)

    }


    @Test
    void testGenerateStatementKNOW()
    {
        Map<Formula, ModalOperator> map = new HashMap<>();
        map.put(cf1, ModalOperator.KNOW);
        def cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I know that Hyzio is Red and Black")
        map = new HashMap<>();
        map.put(cf2, ModalOperator.KNOW);
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("No, but I know that Hyzio is not Red and Black")
        map = new HashMap<>();
        map.put(cf3, ModalOperator.KNOW);
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("No, but I know that Hyzio is Red and not Black")
        map = new HashMap<>();
        map.put(cf4, ModalOperator.KNOW);
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("No, but I know that Hyzio is not Red and not Black")
        map = new HashMap<>();
        map.put(cf3, ModalOperator.KNOW);
        cs = new ComplexStatement(cf2, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("No, but I know that Hyzio is Red and not Black")
    }


    @Test
    void testOR()
    {
        cf1 = new ComplexFormula(model1, [trait1, trait2], [State.IS, State.IS], LogicOperator.OR)
        cf2 = new ComplexFormula(model1, [trait1, trait2], [State.IS_NOT, State.IS], LogicOperator.OR)
        cf3 = new ComplexFormula(model1, [trait1, trait2], [State.IS, State.IS_NOT], LogicOperator.OR)
        cf4 = new ComplexFormula(model1, [trait1, trait2], [State.IS_NOT, State.IS_NOT], LogicOperator.OR)
        Map<Formula, ModalOperator> map = new HashMap<>();
        map.put(cf1, ModalOperator.KNOW)
        def cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I know that Hyzio is Red or Black")
        map = new HashMap<>();
        map.put(cf2, ModalOperator.KNOW)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("No, but I know that Hyzio is not Red or Black")
        map = new HashMap<>();
        map.put(cf1, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red or Black")
        map = new HashMap<>();
        map.put(cf2, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, " +
                "but it is possible that it is not Red or Black")
        map = new HashMap<>()
        map.put(cf2, ModalOperator.BEL)
        map.put(cf3, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, " +
                "however I believe that it is not Red or Black, but it is also possible that it is Red or not Black")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.BEL)
        map.put(cf3, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I believe that Hyzio is Red or Black, " +
                "but it is possible that it is Red or not Black")

    }
    @Test
    void testForXOR()
    {
        cf1 = new ComplexFormula(model1, [trait1, trait2], [State.IS, State.IS], LogicOperator.XOR)
        cf2 = new ComplexFormula(model1, [trait1, trait2], [State.IS_NOT, State.IS], LogicOperator.XOR)
        cf3 = new ComplexFormula(model1, [trait1, trait2], [State.IS, State.IS_NOT], LogicOperator.XOR)
        cf4 = new ComplexFormula(model1, [trait1, trait2], [State.IS_NOT, State.IS_NOT], LogicOperator.XOR)
        Map<Formula, ModalOperator> map = new HashMap<>();
        map.put(cf2, ModalOperator.KNOW)
        map.put(cf1, ModalOperator.KNOW)
        def cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I know that Hyzio is either Red or Black")
        map = new HashMap<>();
        map.put(cf2, ModalOperator.KNOW)
        map.put(cf3, ModalOperator.KNOW)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("No, but I know that Hyzio is either Red or not Black")
        map = new HashMap<>();
        map.put(cf1, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        map.put(cf2, ModalOperator.BEL)
        map.put(cf3, ModalOperator.BEL)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is either Red or Black, " +
                "however I believe that it is either Red or not Black")
    }

    @Test
    void testGenerateStatementBEL()
    {
        Map<Formula, ModalOperator> map = new HashMap<>();
        map.put(cf1, ModalOperator.BEL);
        def cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I believe that Hyzio is Red and Black")
        map = new HashMap<>()
        map.put(cf2, ModalOperator.BEL)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, however I believe that it is not Red and Black")
        map = new HashMap<>()
        map.put(cf3, ModalOperator.BEL)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, however I believe that it is Red and not Black")
        map = new HashMap<>()
        map.put(cf4, ModalOperator.BEL)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, however I believe that it is quite the opposite")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.BEL)
        map.put(cf2, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I believe that Hyzio is Red and Black, but it is possible that it is not Red and Black")
        map = new HashMap<>()
        map.put(cf3, ModalOperator.BEL)
        map.put(cf2, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, however I believe that it is Red and not Black, but it is also possible that it is not Red and Black")
        map = new HashMap<>()
        map.put(cf3, ModalOperator.BEL)
        map.put(cf1, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, however I believe that it is Red and not Black")
        map = new HashMap<>()
        map.put(cf3, ModalOperator.BEL)
        map.put(cf2, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, however I believe that it is Red and not Black, but it is also possible that it is not Red and Black or it is quite the opposite")
        map = new HashMap<>()
        map.put(cf3, ModalOperator.BEL)
        map.put(cf1, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, however I believe that it is Red and not Black, but it is also possible that it is quite the opposite")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.BEL)
        map.put(cf2, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I believe that Hyzio is Red and Black, but it is possible that it is not Red and Black or it is quite the opposite")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.BEL)
        map.put(cf2, ModalOperator.POS)
        map.put(cf3, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I believe that Hyzio is Red and Black, but it is possible that it is Red and not Black or it is not Red and Black")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.BEL)
        map.put(cf2, ModalOperator.POS)
        map.put(cf3, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, I believe that Hyzio is Red and Black, but all other options are also possible")
        map = new HashMap<>()
        map.put(cf2, ModalOperator.BEL)
        map.put(cf1, ModalOperator.POS)
        map.put(cf3, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, however I believe that it is not Red and Black, but it is also possible that it is Red and not Black or it is quite the opposite");
    }

    @Test
    void testGenerateStatementPOS()
    {
        Map<Formula, ModalOperator> map = new HashMap<>();
        map.put(cf1, ModalOperator.POS);
        def cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black")
        map = new HashMap<>()
        map.put(cf2, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, but it is possible that it is not Red and Black")
        map = new HashMap<>()
        map.put(cf3, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, but it is possible that it is Red and not Black")
        map = new HashMap<>()
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I don't know about those states of Hyzio, but it is possible that it is quite the opposite")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, but it is possible that it is quite the opposite")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.POS)
        map.put(cf2, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, but it is possible that it is not Red and Black")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.POS)
        map.put(cf2, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, but it is possible that it is not Red and Black or it is quite the opposite")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.POS)
        map.put(cf2, ModalOperator.POS)
        map.put(cf3, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, but it is possible that it is Red and not Black or it is not Red and Black")
        map = new HashMap<>()
        map.put(cf1, ModalOperator.POS)
        map.put(cf2, ModalOperator.POS)
        map.put(cf3, ModalOperator.POS)
        map.put(cf4, ModalOperator.POS)
        cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("Yes, it is possible that Hyzio is Red and Black, but all other options are also possible")
    }

    @Test
    void testGenerateStatementVoid()
    {
        Map<Formula, ModalOperator> map = new HashMap<>()
        def cs = new ComplexStatement(cf1, map, "Hyzio")
        assert cs.generateStatement().equalsIgnoreCase("I do not know what to say about it")
    }






}
