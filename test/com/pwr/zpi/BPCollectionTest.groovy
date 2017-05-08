import com.pwr.zpi.BPCollection
import com.pwr.zpi.BaseProfile
import org.junit.Test

/**
 * Created by Grzesiek on 2017-05-08.
 */
class BPCollectionTest extends GroovyTestCase {

    BaseProfile bp1, bp2, bp3, bp4, bp5;
    int t1, t2,t3,t4,t5
    BPCollection testBpc

    /**
     * Builds dependencies.
     */
    void build() {}

    void buildTestObject() {
        testBpc = new BPCollection([(t1): bp1, (t2): bp2]as Map, [(t3): bp3, (t4):bp4]as Map, 1)

    }

    @Test
    void testConstructor() {
        def bpc = new BPCollection()
        assertEquals(bpc.getTimestamp(),0)
    }

    @Test
    void testComplexConstructor() {
        build()
        shouldFail{
            new BPCollection(null, null, 0)
            new BPCollection(null, null, -1)
            new BPCollection([0: bp1, 1: bp2]as Map, [2: bp3, 3:bp4]as Map, -1)
            new BPCollection(null, [2: bp3, 3:bp4]as Map)
            new BPCollection([2: bp3, 3:bp4]as Map, null)
        }
        def bpc1 = new BPCollection([0: bp1, 1: bp2]as Map, [2: bp3, 3:bp4]as Map, 1)
        def bpc2 = new BPCollection([0: bp1, 1: bp2]as Map, [2: bp3, 3:bp4]as Map)
        def bpc3 = new BPCollection([]as Map, []as Map)
        assertNotNull(bpc1)
        assertNotNull(bpc2)
        assertNotNull(bpc3)
        assertEquals(bpc1.getTimestamp(), 1)
        assertEquals(bpc2.getTimestamp(), 0)
        assertEquals(bpc1.getLongTermMemory(), [2: bp3, 3:bp4]as Map)
        assertEquals(bpc1.getWorkingMemory(), [0: bp1, 1: bp2]as Map)

    }

    @Test
    void testAddToMemory() {

    }
    @Test
    void testAddToMemory1() {

    }
    @Test
    void testGetTimedBaseProfile() {

    }
    @Test
    void testGetBaseProfile() {  //public BaseProfile getBaseProfile(int timestamp, MemoryType memType)
        buildTestObject()
        assertEquals(bp1, testBpc.getBaseProfile(t1, BPCollection.MemoryType.WM))
        assertNull(testBpc.getBaseProfile(t1, BPCollection.MemoryType.LM))
        assertEquals(bp4, testBpc.getBaseProfile(t4, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t4, BPCollection.MemoryType.WM))
        assertNull(testBpc.getBaseProfile(t3, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t5, BPCollection.MemoryType.LM))
        assertNull(testBpc.getBaseProfile(t5, BPCollection.MemoryType.WM))
        shouldFail{
            testBpc.getBaseProfile(-3, BPCollection.MemoryType.WM)
        }
        shouldFail{
            testBpc.getBaseProfile(t3, null)
        }

    }
    @Test
    void testGetTimedBaseProfiles() {

    }
    @Test
    void testGetTimedBaseProfiles1() {

    }
}
