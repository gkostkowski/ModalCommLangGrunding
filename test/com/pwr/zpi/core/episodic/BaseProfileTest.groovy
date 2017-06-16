package episodic

import com.pwr.zpi.core.episodic.BaseProfile
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait
import org.junit.Test

/**
 * Created by Grzesiek on 2017-04-04.
 */
class BaseProfileTest extends GroovyTestCase {

    BaseProfile testBaseProfile, testBaseProfile2

    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    Set<IndividualModel> describedByTraitsIMs, notDescribedByTraitsIMs,
                         indefiniteByTraitsIMs

    def tr1, tr2, tr3, tr4, tr5
    def model1, model2, model3, model4, model5, model6, model7
    def usedIMs
    int defTime = 1

    /**
     * Builds all required dependencies.
     */
    void build() {
        print "Building dependencies..."

        tr1 = new Trait("Red")
        tr2 = new Trait("Black")
        tr3 = new Trait("White")
        tr4 = new Trait("Soft")
        tr5 = new Trait("Warm")
        def oType1 = new ObjectType("Typ1", [tr1, tr2, tr3])
        def oType2 = new ObjectType("Typ2", [tr1, tr4])
        def oType3 = new ObjectType("Typ3", [tr2, tr3])
        def oType4 = new ObjectType("Typ4", [tr2, tr4])
        def oType5 = new ObjectType("Typ5", [tr3, tr4])
        def oType6 = new ObjectType("Typ6", [tr2, tr4, tr1])
        def oType7 = new ObjectType("Typ7", [tr2, tr4, tr1, tr3])
        model1 = new IndividualModel(new QRCode("ID"), oType1)
        model2 = new IndividualModel(new QRCode("ID"), oType2)
        model3 = new IndividualModel(new QRCode("ID"), oType3)
        model4 = new IndividualModel(new QRCode("ID"), oType4)
        model5 = new IndividualModel(new QRCode("ID"), oType5)
        model6 = new IndividualModel(new QRCode("ID"), oType6)
        model7 = new IndividualModel(new QRCode("ID"), oType7)

        usedIMs = [model1, model2, model3, model4, model5, model6] as Set
        describedByTraitsIMs = [model1, model2, model3, model4] as Set<IndividualModel>;
        notDescribedByTraitsIMs = [model6] as Set<IndividualModel>;
        indefiniteByTraitsIMs = [model5] as Set<IndividualModel>;

        describedByTraits = [ (tr1) : [model1, model2] as Set<IndividualModel>,
                              (tr2): [model3, model4] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>;
        notDescribedByTraits = [(tr1): [model6] as Set,
                                (tr4): [model6] as Set]
        indefiniteByTraits = [(tr1): [model5] as Set,
                              (tr2): [model5] as Set]
        println "Done."

        testBaseProfile2 = new BaseProfile(defTime)
        testBaseProfile2.addDescribedObservation(model7, tr4)
    }

    /**
     * Builds exact test object.
     */
    void buildTestObject(int time) {
        build()
        testBaseProfile = new BaseProfile([describedByTraits, notDescribedByTraits,
                                           indefiniteByTraits] as List, time)
    }

    void buildTestObject() {
        buildTestObject(defTime)
    }

    @Test
    void testSimpleConstructor() {
        def timestamp1 = 3
        def testBP = new BaseProfile(timestamp1)
        assertNotNull(testBP)
        assertEquals(timestamp1, testBP.getTimestamp())

        def msg = shouldFail {
            new BaseProfile(-1)
        }
        def notValidTMsg = "Not valid timestamp."
        assert msg == notValidTMsg
        assertNotNull(testBP.getDescribedByTraits())
        assertNotNull(testBP.getNotDescribedByTraits())
        assertNotNull(testBP.getIndefiniteByTraits())
    }

    @Test
    void testComplexConstructor() {
        build()
        def wrongTimestamp = -1
        def msg = shouldFail {
            new BaseProfile([describedByTraits, notDescribedByTraits,
                             indefiniteByTraits] as List, wrongTimestamp)
        }
        def notValidTMsg = "Not valid timestamp."
        assert msg == notValidTMsg

        def timestamp1 = 3
        def testBP = new BaseProfile([describedByTraits, notDescribedByTraits,
                                      indefiniteByTraits] as List, timestamp1)
        assertNotNull(testBP)
        assertEquals(timestamp1, testBP.getTimestamp())
        assertEquals(describedByTraits, testBP.getDescribedByTraits())
        assertEquals(notDescribedByTraits, testBP.getNotDescribedByTraits())
        assertEquals(indefiniteByTraits, testBP.getIndefiniteByTraits())

        msg = shouldFail {
            new BaseProfile([describedByTraits, notDescribedByTraits]
                    as List, timestamp1)
        }
        def notValidMap = "Not valid map of base profiles."
        assert msg == notValidMap
        msg = shouldFail {
            new BaseProfile([describedByTraits, null, notDescribedByTraits]
                    as List, timestamp1)
        }
        assert msg == notValidMap

    }

    @Test
    void testGetByTraitState() {
//        build()

    }

    @Test
    void testGetAffectedIMs() { //public Set<IndividualModel> getAffectedIMs()
        buildTestObject()
        assertEquals(usedIMs, testBaseProfile.getAffectedIMs())
    }

    @Test
    void testGetAffectedIMs1() {
        //private Set<IndividualModel> getAffectedIMs(Map<Trait, Set<IndividualModel>> relatedMap)
        buildTestObject()
        assertEquals(describedByTraitsIMs, testBaseProfile.getAffectedIMs(describedByTraits))
        assertEquals(notDescribedByTraitsIMs, testBaseProfile.getAffectedIMs(notDescribedByTraits))
        assertEquals(indefiniteByTraitsIMs, testBaseProfile.getAffectedIMs(indefiniteByTraits))
    }

    @Test
    void testGetAffectedIMs2() { //public static Set<IndividualModel> getAffectedIMs(BaseProfile ... baseProfileArr)
        buildTestObject()
        assertEquals([model1, model2, model3, model4, model5, model6, model7] as Set<IndividualModel>,
                BaseProfile.getAffectedIMs(testBaseProfile, testBaseProfile2))
    }

    @Test
    void testGetIMsDescribedByTrait() {
        buildTestObject()
        assertEquals([model1, model2] as Set, testBaseProfile.getIMsDescribedByTrait(tr1))
        assertNull(testBaseProfile.getIMsNotDescribedByTrait(tr5))
    }

    @Test
    void testGetIMsNotDescribedByTrait() {

    }

    @Test
    void testGetIMsIndefiniteByTrait() {

    }

    @Test
    void testGetIMsByTraitState() {
        buildTestObject()
        assertEquals([model1, model2] as Set, testBaseProfile.getIMsByTraitState(tr1, State.IS))
        assertNull(testBaseProfile.getIMsByTraitState(tr5, State.IS_NOT))
    }

    @Test
    void testAddDescribedObservations() {
        shouldFail {
            testBaseProfile.addDescribedObservations(null, tr2)
        }
        shouldFail {
            testBaseProfile.addDescribedObservations(usedIMs, null)
        }
    }

    @Test
    void testAddNotDescribedObservations() {
        shouldFail {
            testBaseProfile.addNotDescribedObservations(null, tr2)
        }
        shouldFail {
            testBaseProfile.addNotDescribedObservations(usedIMs, null)
        }
    }

    @Test
    void testAddIndefiniteObservations() {
        shouldFail {
            testBaseProfile.addIndefiniteObservations(null, tr2)
        }
        shouldFail {
            testBaseProfile.addIndefiniteObservations(usedIMs, null)
        }
    }

    @Test
    void testAddDescribedObservation() {
        shouldFail {
            testBaseProfile.addDescribedObservation(null, tr2)
        }
        shouldFail {
            testBaseProfile.addDescribedObservation(model7, null)
        }
    }

    @Test
    void testAddNotDescribedObservation() {
        shouldFail {
            testBaseProfile.addDescribedObservation(null, tr2)
        }
        shouldFail {
            testBaseProfile.addNotDescribedObservation(model7, null)
        }
    }

    @Test
    void testAddIndefiniteObservation() {
        shouldFail {
            testBaseProfile.addIndefiniteObservation(null, tr2)
        }
        shouldFail {
            testBaseProfile.addIndefiniteObservation(model7, null)
        }
    }

}
