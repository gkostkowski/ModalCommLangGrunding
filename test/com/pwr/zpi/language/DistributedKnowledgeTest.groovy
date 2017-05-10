package language

import com.pwr.zpi.Agent
import com.pwr.zpi.BPCollection
import com.pwr.zpi.BaseProfile
import com.pwr.zpi.IndividualModel
import com.pwr.zpi.ObjectType
import com.pwr.zpi.QRCode
import com.pwr.zpi.State
import com.pwr.zpi.Trait
import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.language.DistributedKnowledge
import com.pwr.zpi.language.Operators
import com.pwr.zpi.language.SimpleFormula
import org.junit.Test

class DistributedKnowledgeTest extends GroovyTestCase {

    /*@Test
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
    }*/

    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp2_2;
    int t0, t1, t2, t3, t4, t5
    BPCollection bpCollection1
    Agent agent
    SimpleFormula sformula1,sformula2
    ComplexFormula cformula1
    DistributedKnowledge testDk,testDk1,testDk2

    /**
     * Builds dependencies.
     */
    /**
     * Builds all required dependencies.
     */
    void build() {

        def tr1, tr2, tr3, tr4, tr5
        def model1, model2, model3, model4, model5, model6, model7
        def usedIMs
        int defTime = 1

        tr1 = new Trait("Red")
        tr2 = new Trait("Black")
        tr3 = new Trait("White")
        tr4 = new Trait("Soft")
        tr5 = new Trait("Warm")
        def oType1 = new ObjectType("Type1", [tr1, tr2, tr3])
        def oType2 = new ObjectType("Type2", [tr1, tr4])
        def oType3 = new ObjectType("Type3", [tr2, tr3])
        def oType4 = new ObjectType("Type4", [tr2, tr4])
        def oType5 = new ObjectType("Type5", [tr3, tr4])
        def oType6 = new ObjectType("Type6", [tr2, tr4, tr1])
        def oType7 = new ObjectType("Type7", [tr2, tr4, tr1, tr3])
        model1 = new IndividualModel(new QRCode("ID1"), oType1)
        model2 = new IndividualModel(new QRCode("ID2"), oType2)
        model3 = new IndividualModel(new QRCode("ID3"), oType3)
        model4 = new IndividualModel(new QRCode("ID4"), oType4)
        model5 = new IndividualModel(new QRCode("ID5"), oType5)
        model6 = new IndividualModel(new QRCode("ID6"), oType6)
        model7 = new IndividualModel(new QRCode("ID7"), oType7)

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
        bp4 = new BaseProfile([describedByTraits, notDescribedByTraits,
                               indefiniteByTraits] as List, t4)
        bp5 = new BaseProfile(t5)


        bpCollection1 = new BPCollection([bp1, bp2] as Set, [bp3, bp4] as Set)

        agent = new Agent(bpCollection1)

        def formulaIM = new IndividualModel(new QRCode("ID1"), oType1);

        sformula1 = new SimpleFormula(formulaIM, tr1, false);
        sformula2 = new SimpleFormula(formulaIM, tr1, true);
        cformula1 = new ComplexFormula(formulaIM, [tr1, tr2], [State.IS_NOT, State.IS], Operators.Type.AND)

    }

    void buildTestObject() {
        build()
        def currTime = agent.knowledgeBase.getTimestamp()
        testDk1 = new DistributedKnowledge(agent, sformula1)
        testDk2 = new DistributedKnowledge(agent, cformula1)
    }

    @Test
    void testConstructor() {
        build()
        testDk1 = new DistributedKnowledge(agent, sformula1)
        testDk2 = new DistributedKnowledge(agent, cformula1)
        shouldFail { testDk = new DistributedKnowledge(agent, null)}
        shouldFail { testDk = new DistributedKnowledge(null, sformula1)}
    }

    @Test
    void testDK() {  //general checking
        buildTestObject()
        def dkClasses = testDk1.getDistributionClasses()
        println(dkClasses)
    }
}
