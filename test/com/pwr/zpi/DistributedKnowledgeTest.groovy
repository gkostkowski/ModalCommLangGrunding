import com.pwr.zpi.Agent
import com.pwr.zpi.BPCollection
import com.pwr.zpi.BaseProfile
import com.pwr.zpi.Observation
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

        def o1 = new Observation(1, "o1", [nminpl, pminus, lminpl] as Set)
        def o2 = new Observation(2, "o2", [nminpl, pminus, lminpl] as Set)
        def o3 = new Observation(3, "o3", [nminpl, pminus, lminpl] as Set)
        def o4 = new Observation(4, "o4", [nminpl, pminus, lminpl] as Set)
        def o5 = new Observation(5, "o5", [nminpl, pminus, lminpl] as Set)

        def pb0 = new BaseProfile([nminus: [] as Set, nplus: [] as Set, nminpl: [o1, o2, o3, o4, o5] as Set,
                                   pminus: [o1, o2, o3, o4, o5] as Set, pplus: [] as Set, pminpl: [] as Set,
                                   lplus : [] as Set, lminus: [] as Set, lminpl: [o1, o2, o3, o4, o5] as Set], 0) //t=0
        def pb1 = new BaseProfile([nminus: [] as Set, nplus: [o1,o3,o5] as Set, nminpl: [o2,o4] as Set,
                                   pminus: [] as Set, pplus: [o1,o5] as Set, pminpl: [o2,o3,o4] as Set,
                                   lplus : [] as Set, lminus: [o1,o3,o5] as Set, lminpl: [o2,o4] as Set], 1) //t=2
        def pb2 = new BaseProfile([nminus: [o2] as Set, nplus: [o1,o5] as Set, nminpl: [o3,o4] as Set,
                                   pminus: [o5] as Set, pplus: [o1] as Set, pminpl: [o2,o3,o4] as Set,
                                   lplus : [] as Set, lminus: [o1,o2,o5] as Set, lminpl: [o3,o4]as Set] , 2) //t=5
        def pb3 = new BaseProfile([nminus: [] as Set, nplus: [o1,o3] as Set, nminpl: [o2, o4, o5] as Set,
                                   pminus: [] as Set, pplus: [o1,o3] as Set, pminpl: [o2, o4, o5] as Set,
                                   lplus : [] as Set, lminus: [o1,o3] as Set, lminpl: [o2, o4, o5] as Set], 3) //t=7

        def bpc3 = new BPCollection([3:pb3],[0:pb0, 1:pb1, 2:pb2],3)
        def agent = new Agent(99, "regular", [n, p, l] as Set, bpc3)


        def formula = new SimpleFormula(o1, n)
        def dk = new DistributedKnowledge(agent, formula, 3)

        assert dk
    }

    @Test
    void testGetGroundingSet() {

    }
}
