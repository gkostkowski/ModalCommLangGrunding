package episodic

import com.pwr.zpi.core.episodic.BPCollection
import com.pwr.zpi.core.episodic.BaseProfile
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait
import org.junit.Test

/**
 * Created by Grzesiek on 2017-05-08.
 */
class BPCollectionTest extends GroovyTestCase {

    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp2_2;
    int t0, t1, t2, t3, t4, t5
    BPCollection testBpc
    def tr1, tr2, tr3, tr4, tr5
    def is= State.IS, isNot = State.IS_NOT, mayhaps = State.MAYHAPS
    def model1, model2, model3, model4, model5, model6, model7

    /**
     * Builds dependencies.
     */
    /**
     * Builds all required dependencies.
     */
    void build() {


        def usedIMs
        int defTime = 1

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

        describedByTraits = [(tr1): [model1, model2] as Set<IndividualModel>,
                             (tr2): [model3, model4] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>;
        notDescribedByTraits = [(tr1): [model6] as Set,
                                (tr4): [model6] as Set]
        indefiniteByTraits = [(tr1): [model5] as Set,
                              (tr2): [model5] as Set]

        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5

        bp2 = new BaseProfile(t2)
        bp2_2 = new BaseProfile(t2)
        bp2.addDescribedObservation(model7, tr4)
        bp2.addDescribedObservation(model6, tr4)

        bp1 = new BaseProfile([describedByTraits, notDescribedByTraits,
                               indefiniteByTraits] as List, t1)
        bp3 = new BaseProfile([describedByTraits, notDescribedByTraits,
                               indefiniteByTraits] as List, t3)
        bp4 = new BaseProfile([[(tr1): [model1, model2] as Set<IndividualModel>,
                                (tr2): [model3, model1, model4] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>, notDescribedByTraits,
                               indefiniteByTraits] as List, t4)
        bp5 = new BaseProfile(t5)
    }

    void buildTestObject() {
        build()
        testBpc = new BPCollection([bp1, bp2] as Set, [bp3, bp4] as Set)

    }

    @Test
    void testConstructor() {
        def bpc = new BPCollection()
        assertEquals(bpc.getTimestamp(), 0)
    }

    @Test
    void testComplexConstructor() {
        build()
        shouldFail {
            new BPCollection(null, null)
            new BPCollection([bp1, bp2] as Set, null)
            new BPCollection(null, [bp3, bp4] as Set)
        }
        def bpc1 = new BPCollection([bp1, bp2] as Set, [bp3, bp4] as Set)
        def bpc2 = new BPCollection([bp1] as Set, [bp3, bp5] as Set)
        def bpc3 = new BPCollection([] as Set, [] as Set)
        assertNotNull(bpc1)
        assertNotNull(bpc2)
        assertNotNull(bpc3)
        assertEquals(bpc1.getTimestamp(), t4)
        assertEquals(bpc2.getTimestamp(), t5)
        assertEquals(bpc1.getLongTermMemory(), [bp3, bp4] as Set)
        assertEquals(bpc1.getWorkingMemory(), [bp1, bp2] as Set)

    }

    @Test
    void testAddToMemory() {
        buildTestObject()
        def msg = shouldFail {
            testBpc.addToMemory(BPCollection.MemoryType.WM, null)
        }
        assertEquals(testBpc.getTimestamp(), t4)
        testBpc.addToMemory(BPCollection.MemoryType.WM, bp5)
        assertEquals(bp5, testBpc.getBaseProfile(t5, BPCollection.MemoryType.WM))
        assertEquals(t5, testBpc.getTimestamp())   //checking if updateTimestamp works


        def oldBP2 = testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM)
        testBpc.addToMemory(BPCollection.MemoryType.WM, bp2_2)
        assert (!testBpc.getWorkingMemory().contains(oldBP2))
        assertEquals(bp2_2, testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM))

    }

    @Test
    void testGetBaseProfile() {  //public BaseProfile getBaseProfile(int timestamp, MemoryType memType)
        buildTestObject()
        assertEquals(bp1, testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM))
        assertNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.LM))
        assertEquals(bp2, testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM))
        assertNull(testBpc.getBaseProfile(t2, BPCollection.MemoryType.LM))
        assertEquals(bp4, testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.WM))
        assertEquals(bp3, testBpc.getBaseProfile(t3, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t3, BPCollection.MemoryType.WM))
        assertNull(testBpc.getBaseProfile(t5, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t5, BPCollection.MemoryType.WM))
        shouldFail {
            testBpc.getBaseProfile(-3, BPCollection.MemoryType.WM)
        }
        shouldFail {
            testBpc.getBaseProfile(t3, null)
        }

    }

    @Test
    void testDeleteFromMemory() {  //    public boolean deleteFromMemory(MemoryType type, BaseProfile... oldBPs)
        buildTestObject()
        assertNotNull testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM)
        assertNotNull testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM)
        assertNotNull testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM)
        assertNull testBpc.getBaseProfile(t3, BPCollection.MemoryType.WM)

        testBpc.deleteFromMemory(BPCollection.MemoryType.WM, bp1, bp2)
        testBpc.deleteFromMemory(BPCollection.MemoryType.LM, bp4)
        testBpc.deleteFromMemory(BPCollection.MemoryType.WM, bp4)
        assertNull testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM)
        assertNull testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM)
        assertNull testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM)
        assertNull testBpc.getBaseProfile(t3, BPCollection.MemoryType.WM)
        assertNotNull testBpc.getBaseProfile(t3, BPCollection.MemoryType.LM)
    }

    @Test
    void testDeleteFromMemory2() {   //    public boolean deleteFromMemory(MemoryType type, int... oldBPTimestamps)
        buildTestObject()
        assertNotNull testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM)
        assertNotNull testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM)
        assertNotNull testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM)
        assertNull testBpc.getBaseProfile(t3, BPCollection.MemoryType.WM)

        testBpc.deleteFromMemory(BPCollection.MemoryType.WM, t1, t2)
        testBpc.deleteFromMemory(BPCollection.MemoryType.LM, t4)
        testBpc.deleteFromMemory(BPCollection.MemoryType.WM, t4)
        assertNull testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM)
        assertNull testBpc.getBaseProfile(t2, BPCollection.MemoryType.WM)
        assertNull testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM)
        assertNull testBpc.getBaseProfile(t3, BPCollection.MemoryType.WM)
        assertNotNull testBpc.getBaseProfile(t3, BPCollection.MemoryType.LM)
    }

    @Test
    void testGetBaseProfiles() {
        buildTestObject()
        def WMtillt1 = [bp1] as Set
        def LMtillt3 = [bp3] as Set
        assertEquals(WMtillt1, testBpc.getBaseProfiles(t1, BPCollection.MemoryType.WM))
        assertEquals(LMtillt3, testBpc.getBaseProfiles(t3, BPCollection.MemoryType.LM))
        assertEquals(testBpc.getLongTermMemory(), testBpc.getBaseProfiles(t5, BPCollection.MemoryType.LM))
        def allBPs = [bp1, bp2, bp3, bp4] as Set
        assertEquals(allBPs, testBpc.getBaseProfiles(t5))
        testBpc.addToMemory(BPCollection.MemoryType.WM, bp5)
        assertEquals([bp1, bp2, bp3, bp4, bp5] as Set, testBpc.getBaseProfiles(t5))

    }

    @Test
    void testDetermineIncludingMemory() {  //    public Set<BaseProfile> determineIncludingMemory(BaseProfile bp)
        buildTestObject()
        assertEquals(testBpc.getWorkingMemory(), testBpc.determineIncludingMemory(bp1))
        assertEquals(testBpc.getLongTermMemory(), testBpc.determineIncludingMemory(bp3))
        assertEquals(null, testBpc.determineIncludingMemory(bp5))
        shouldFail { assertEquals(BPCollection.MemoryType.LM, testBpc.determineIncludingMemory(null)) }
    }

    @Test
    void testDuplicateBaseProfile() {
        //        void duplicateBaseProfile(MemoryType src, MemoryType dest, BaseProfile toDuplicate)
        buildTestObject()
        assertNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.LM))
        assertNotNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM))
        testBpc.duplicateBaseProfile(BPCollection.MemoryType.WM, BPCollection.MemoryType.LM, bp1)
        assertNotNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.LM))
        assertNotNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM))

        assertNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.WM))
        assertNotNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM))
        testBpc.duplicateBaseProfile(BPCollection.MemoryType.LM, BPCollection.MemoryType.WM, bp4)
        assertNotNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM))
        assertNotNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.WM))

        def msg = shouldFail {
            testBpc.duplicateBaseProfile(BPCollection.MemoryType.LM, BPCollection.MemoryType.LM, bp5)
        }
        assertEquals("Memory specified as source doesn't contain given base profile.", msg)

        shouldFail { testBpc.duplicateBaseProfile(BPCollection.MemoryType.LM, BPCollection.MemoryType.WM, null) }
    }

    @Test
    void testShiftBaseProfile() {
        buildTestObject()
        assertNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.LM))
        assertNotNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM))
        testBpc.shiftBaseProfile(BPCollection.MemoryType.WM, BPCollection.MemoryType.LM, bp1)
        assertNotNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM))

        assertNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.WM))
        assertNotNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM))
        testBpc.shiftBaseProfile(BPCollection.MemoryType.LM, BPCollection.MemoryType.WM, bp4)
        assertNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM))
        assertNotNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.WM))

        testBpc.shiftBaseProfile(BPCollection.MemoryType.LM, BPCollection.MemoryType.LM, bp5)

        shouldFail {         testBpc.shiftBaseProfile(BPCollection.MemoryType.LM, BPCollection.MemoryType.WM, null)
        }
    }

    @Test
    void testSpotLastTimestamp() {

    }

    @Test
    void testGetIMsByTraitState() {
        buildTestObject()
        def resIMs
        resIMs= testBpc.getIMsByTraitState(tr1, is, t5)
        assertEquals([model1, model3, model4] as Set, resIMs = testBpc.getIMsByTraitState(tr2, is, t4))
        assertEquals([model3, model4] as Set, resIMs = testBpc.getIMsByTraitState(tr2, is, t3))
        assertEquals([] as Set, resIMs = testBpc.getIMsByTraitState(tr5, is, t5))

    }



}
