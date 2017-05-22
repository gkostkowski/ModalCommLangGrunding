package com.pwr.zpi.language

import com.pwr.zpi.Agent
import com.pwr.zpi.episodic.BPCollection
import com.pwr.zpi.episodic.BaseProfile
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode
import javafx.util.Pair
import org.junit.Test

class DistributedKnowledgeTest extends GroovyTestCase {


    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp2_2;
    int t0, t1, t2, t3, t4, t5
    BPCollection bpCollection1
    Agent agent
    SimpleFormula sformula1, sformula2, sformula3
    ComplexFormula cformula1, cformula2
    DistributedKnowledge testDk, testDk1, testDk2, testDk3, testDk4, testDk5, testDk6,
        testCDk1, testCDk2

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
        def oType1 = new ObjectType("Type1", [tr1, tr2, tr3, tr4])
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

        describedByTraits = [(tr1): [model2] as Set<IndividualModel>,
                             (tr2): [model1, model3, model4] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>;
        notDescribedByTraits = [(tr1): [model1, model6] as Set,
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
        bp6 = new BaseProfile([describedByTraits, [(tr1): [model1, model6] as Set,
                                                   (tr4): [model1, model6] as Set],
                               indefiniteByTraits] as List, t4)


        bpCollection1 = new BPCollection([bp1, bp2] as Set, [bp3, bp4, bp6] as Set)

        agent = new Agent(bpCollection1)

        def formulaIM = model1

        sformula1 = new SimpleFormula(formulaIM, tr2, false); // black(individualModel:id1)
        sformula2 = new SimpleFormula(formulaIM, tr1, true); // !red(individualModel:id1)
        sformula3 = new SimpleFormula(formulaIM, tr4, true); // !warm(individualModel:id1)
        cformula1 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS], LogicOperator.AND) // !red(individualModel:id1) && black(individualModel:id1)
        cformula2 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS, State.IS_NOT], LogicOperator.AND) // red(individualModel:id1) && !black(individualModel:id1)

    }

    void buildTestObject() {
        build()
        def currTime = agent.knowledgeBase.getTimestamp()
        testDk1 = new DistributedKnowledge(agent, sformula1) //with simple formula - at least one matching base profile
        testDk2 = new DistributedKnowledge(agent, cformula1) //with complex formula - at least one matching base profile
        testDk3 = new DistributedKnowledge(agent, sformula2) //with other simple formula - at least one matching base profile
        testDk4 = new DistributedKnowledge(agent, sformula1, t3) // till given timestamp
        testDk5 = new DistributedKnowledge(agent, sformula3) //only one matching base profile
        testDk6 = new DistributedKnowledge(agent, cformula2) // empty results
        testCDk1 = new DistributedKnowledge(agent, sformula1, true) //with simple formula - complex distribution
        testCDk2 = new DistributedKnowledge(agent, cformula1, true) //with complex formula - complex distribution
    }

    @Test
    void testConstructor() {
        build()
        testDk1 = new DistributedKnowledge(agent, sformula1)
        testDk2 = new DistributedKnowledge(agent, cformula1)

        shouldFail { testDk = new DistributedKnowledge(agent, null) }
        shouldFail { testDk = new DistributedKnowledge(null, sformula1) }
    }

    @Test
    void testDK() {  //general checking
        buildTestObject()
        def simpleClassesNumber  = 2;

        def dkClasses = testDk1.getDistributionClasses()
        assertEquals (simpleClassesNumber, dkClasses.size())
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp1)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp2)
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp3)
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp4)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp5)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp5)

        dkClasses = testDk2.getDistributionClasses()
        assertEquals (simpleClassesNumber, dkClasses.size())
        assert dkClasses.get(new Pair(cformula1, BPCollection.MemoryType.WM)).contains(bp1)
        assert !dkClasses.get(new Pair(cformula1, BPCollection.MemoryType.WM)).contains(bp2)
        assert dkClasses.get(new Pair(cformula1, BPCollection.MemoryType.LM)).contains(bp3)
        assert dkClasses.get(new Pair(cformula1, BPCollection.MemoryType.LM)).contains(bp4)
        assert !dkClasses.get(new Pair(cformula1, BPCollection.MemoryType.LM)).contains(bp5)
        assert !dkClasses.get(new Pair(cformula1, BPCollection.MemoryType.WM)).contains(bp5)
        assertNull dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM))


        dkClasses = testDk3.getDistributionClasses()
        assertEquals (simpleClassesNumber, dkClasses.size())
        assert dkClasses.get(new Pair(sformula2, BPCollection.MemoryType.WM)).contains(bp1)
        assertNull dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM))
        assertNull dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM))
        assert dkClasses.get(new Pair(sformula2, BPCollection.MemoryType.LM)).contains(bp3)
        assert dkClasses.get(new Pair(sformula2, BPCollection.MemoryType.LM)).contains(bp4)
        assert !dkClasses.get(new Pair(sformula2, BPCollection.MemoryType.LM)).contains(bp5)
        assert !dkClasses.get(new Pair(sformula2, BPCollection.MemoryType.WM)).contains(bp5)


        dkClasses = testDk4.getDistributionClasses()
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp1)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp2)
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp3)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp4)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp5)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp5)

        dkClasses = testDk5.getDistributionClasses()
        assertEquals (simpleClassesNumber, dkClasses.size())
        assert !dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.WM)).contains(bp1)
        assert !dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.WM)).contains(bp2)
        assert !dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.LM)).contains(bp3)
        assert !dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.LM)).contains(bp4)
        assert !dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.LM)).contains(bp5)
        assert !dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.WM)).contains(bp6)
        assert dkClasses.get(new Pair(sformula3, BPCollection.MemoryType.LM)).contains(bp6)

        dkClasses = testDk6.getDistributionClasses()
        assertEquals (simpleClassesNumber, dkClasses.size())
        assert !dkClasses.isEmpty()
        for (def entry : dkClasses)
            assert entry.value.isEmpty()


    }

    @Test
    void testComplexDK(){
        buildTestObject()
        def complexClassesNumberSM  = 4;
        def complexClassesNumberMC  = 8;

        def dkClasses = testCDk1.getDistributionClasses()
        assertEquals (complexClassesNumberSM, dkClasses.size())
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp1)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp2)
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp3)
        assert dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp4)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.LM)).contains(bp5)
        assert !dkClasses.get(new Pair(sformula1, BPCollection.MemoryType.WM)).contains(bp5)

        dkClasses = testCDk2.getDistributionClasses()
        assertEquals (complexClassesNumberMC, dkClasses.size())
        def complFormulas = testCDk2.getComplementaryFormulas()
        for (Formula f :complFormulas) {
            assertNotNull dkClasses.get(new Pair(f, BPCollection.MemoryType.WM))
            assertNotNull dkClasses.get(new Pair(f, BPCollection.MemoryType.LM))
        }
    }
}
