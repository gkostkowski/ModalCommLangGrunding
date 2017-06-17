package holons

import com.pwr.zpi.core.Agent
import com.pwr.zpi.core.memory.episodic.BPCollection
import com.pwr.zpi.core.memory.episodic.BaseProfile
import com.pwr.zpi.core.memory.episodic.DistributedKnowledge
import com.pwr.zpi.core.memory.holons.BinaryHolon
import com.pwr.zpi.core.memory.holons.Holon
import com.pwr.zpi.core.memory.semantic.IndividualModel
import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.core.memory.semantic.QRCode
import com.pwr.zpi.exceptions.InvalidFormulaException
import com.pwr.zpi.exceptions.NotApplicableException
import com.pwr.zpi.exceptions.NotConsistentDKException
import com.pwr.zpi.language.Formula
import com.pwr.zpi.language.SimpleFormula
import com.pwr.zpi.language.Trait
import org.testng.annotations.Test

class BinaryHolonTest extends GroovyTestCase{

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6
    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    def tr1, tr2, tr3, tr4, tr5
    def model1, model2, model3, model4, model5, model6, model7
    int t0, t1, t2, t3, t4, t5
    Map<Formula, Set<BaseProfile>> bpmap1;
    Map<Formula, Set<BaseProfile>> bpmap2;
    Map<Formula, Set<BaseProfile>> bpmap3;
    SimpleFormula sformula1, sformula2, sformula3, sformula1neg , sformula2neg

    Set<BaseProfile> bpset1 ,bpset2 ,bpset3, bpset4

    Agent agent;

    void build() {

        tr1 = new Trait("Red")
        tr2 = new Trait("Black")
        tr3 = new Trait("White")
        tr4 = new Trait("Soft")

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
        bp2.addDescribedObservation(model5, tr4)
        bp2.addDescribedObservation(model7, tr4)

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

        BPCollection bpc = new BPCollection([bp1,bp3,bp4,bp5,bp6] as Set, [] as Set)
        Agent.AgentBuilder ab = new Agent.AgentBuilder()
        ab.knowledgeBase(bpc)
        agent = new Agent(ab)
    }


    @Test
    void testmainTest() throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
        build()
        Formula f1 = new SimpleFormula(model1,tr1,false)
        Formula f2 = new SimpleFormula(model1,tr1,true)
        Formula f3 = new SimpleFormula(model2,tr4,true)
        DistributedKnowledge dk = new DistributedKnowledge(agent,f1)
        Holon h1 = new BinaryHolon(dk,null)

        assertEquals(h1.getFormula().get(0),f1)

        assertEquals(h1.getFormula(),[f1,f2] as List)

        assertEquals(true,h1.isApplicable(f1))
        assertEquals(true,h1.isApplicable(f2))



        assertEquals(false,h1.isApplicable(f3))

        assertEquals(0.3,h1.getnot_P().round(1))

        assertEquals(0,h1.getP())

        assertEquals(false,h1.getStrongest().getK())
        assertEquals(0.3,h1.getStrongest().getV().round(1))

        assertEquals(true,h1.getWeakest().getK())
        assertEquals(0,h1.getWeakest().getV().round(1))

        assertEquals(Holon.HolonKind.Binary,h1.getKind())

        assertEquals(2,h1.getSummaries().size())

        assertEquals(0.0,h1.getSummary(f1))
        assertEquals(0.33,h1.getSummary(f2),2)

        assertEquals(2,h1.getSummaries([f1,f2] as List).size())

        assertEquals(0.0,h1.getSummaries([f1,f2] as List).get(f1))

        assertEquals(0.33,h1.getSummaries([f1,f2] as List).get(f2),2)
    }

    void secondbuild (){

        tr1 = new Trait("Red")
        tr2 = new Trait("Black")
        tr3 = new Trait("White")
        tr4 = new Trait("Soft")

        def oType1 = new ObjectType("Type1", [tr1, tr2, tr3, tr4])
        model1 = new IndividualModel(new QRCode("ID1"), oType1)

        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5

        bp1 = new BaseProfile(t2)

        bp1.addDescribedObservation(model1, tr4)
        bp1.addDescribedObservation(model1, tr1)

        bp2 = new BaseProfile(t3)

        bp2.addNotDescribedObservation(model1,tr4)
        bp2.addNotDescribedObservation(model1,tr1)

        BPCollection bpc = new BPCollection([bp1,bp2] as Set, [] as Set)
        Agent.AgentBuilder ab = new Agent.AgentBuilder()
        ab.knowledgeBase(bpc)
        agent = new Agent(ab)
    }

    @Test
    void testAnotherTest(){
        secondbuild()
        Formula f1 = new SimpleFormula(model1,tr1,false)
        Formula f2 = new SimpleFormula(model1,tr1,true)
        DistributedKnowledge dk = new DistributedKnowledge(agent,f1)
        Holon h1 = new BinaryHolon(dk,null)

        assertEquals(h1.getFormula().get(0),f1)

        assertEquals(h1.getFormula(),[f1,f2] as List)

        assertEquals(true,h1.isApplicable(f1))
        assertEquals(true,h1.isApplicable(f2))


        assertEquals(0.5,h1.getnot_P())
        assertEquals(0.5,h1.getP())


        assertEquals(0.5,h1.getSummary(f1))

        assertEquals(0.5,h1.getSummary(f2))

        assertEquals(0.5,h1.getSummaries([f1,f2] as List).get(f1))

        assertEquals(0.5,h1.getSummaries([f1,f2] as List).get(f2))
        /*def map = [:]
        map.put(tr1,true)
        Observation o1 = new Observation(model1.getIdentifier(),map)

        def array = new Observation[1]
        array[0] = o1
        agent.addAndUpdate(array)*/
    }
}
