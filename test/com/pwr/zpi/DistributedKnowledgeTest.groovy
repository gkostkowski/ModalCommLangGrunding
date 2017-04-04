import com.pwr.zpi.Agent
import com.pwr.zpi.BPCollection
import com.pwr.zpi.BaseProfile
import com.pwr.zpi.Object
import com.pwr.zpi.State
import com.pwr.zpi.Trait
import com.pwr.zpi.language.DistributedKnowledge
import com.pwr.zpi.language.SimpleFormula
import org.junit.Test

class DistributedKnowledgeTest extends GroovyTestCase {

    @Test
    void testGetDkClassByDesc() {
        def n = new Trait("Naladowany", false)
        def p = new Trait("Pracujacy", false)
        def l = new Trait("Ladowanie", false)

        def nminus = new Trait("Naladowany", State.IS_NOT)
        def nplus = new Trait("Naladowany", State.IS)
        def nminpl = new Trait("Naladowany", State.MAYHAPS)
        def pminus = new Trait("Pracujacy", State.IS_NOT)
        def pplus = new Trait("Pracujacy", State.IS)
        def pminpl = new Trait("Pracujacy", State.MAYHAPS)
        def lminus = new Trait("Ladowanie", State.IS_NOT)
        def lplus = new Trait("Ladowanie", State.IS)
        def lminpl = new Trait("Ladowanie", State.MAYHAPS)

        def o1 = new Object(1, "o1", [nminpl, pminus, lminpl])
        def o2 = new Object(2, "o2", [nminpl, pminus, lminpl])
        def o3 = new Object(3, "o3", [nminpl, pminus, lminpl])
        def o4 = new Object(4, "o4", [nminpl, pminus, lminpl])
        def o5 = new Object(5, "o5", [nminpl, pminus, lminpl])

        def pb0 = new BaseProfile([nminus: [], nplus: [], nminpl: [o1, o2, o3, o4, o5],
                                   pminus: [o1, o2, o3, o4, o5], pplus: [], pminpl: [],
                                   lplus : [], lminus: [], lminpl: [o1, o2, o3, o4, o5]], 0) //t=0
        def pb1 = new BaseProfile([nminus: [], nplus: [o1,o3,o5], nminpl: [o2,o4],
                                   pminus: [], pplus: [o1,o5], pminpl: [o2,o3,o4],
                                   lplus : [], lminus: [o1,o3,o5], lminpl: [o2,o4]], 1) //t=2
        def pb2 = new BaseProfile([nminus: [o2], nplus: [o1,o5], nminpl: [o3,o4],
                                   pminus: [o5], pplus: [o1], pminpl: [o2,o3,o4],
                                   lplus : [], lminus: [o1,o2,o5], lminpl: [o3,o4]], 2) //t=5
        def pb3 = new BaseProfile([nminus: [], nplus: [o1,o3], nminpl: [o2, o4, o5],
                                   pminus: [], pplus: [o1,o3], pminpl: [o2, o4, o5],
                                   lplus : [], lminus: [o1,o3], lminpl: [o2, o4, o5]], 3) //t=7

        def bpc3 = new BPCollection([3:pb3],[0:pb0, 1:pb1, 2:pb2],3)
        def agent = new Agent(99, "regular", [n, p, l], bpc3)


        def formula = new SimpleFormula(o1, n)
        def dk = new DistributedKnowledge(agent, formula, 3)

        assert dk
    }

    @Test
    void testGetGroundingSet() {

    }
}
