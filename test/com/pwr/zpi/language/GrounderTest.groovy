import com.pwr.zpi.core.Agent
import com.pwr.zpi.Configuration
import com.pwr.zpi.core.episodic.BPCollection
import com.pwr.zpi.core.episodic.BaseProfile
import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.core.episodic.DistributedKnowledge
import com.pwr.zpi.language.Formula
import com.pwr.zpi.language.Grounder
import com.pwr.zpi.language.LogicOperator
import com.pwr.zpi.language.ModalOperator
import com.pwr.zpi.language.SimpleFormula
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import org.testng.annotations.Test

/**
 * Created by Grzesiek on 2017-06-11.
 */
class GrounderTest extends GroovyTestCase {


    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9, bp2_2;
    BaseProfile bp1_a, bp2_a, bp3_a, bp4_a, bp5_a, bp6_a, bp7_a, bp8_a, bp9_a
    int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9
    BPCollection bpCollection1
    Agent agent
    SimpleFormula sformula1, sformula2, sformula3
    ComplexFormula cformula1, cformula2, cformula3, cformula4, cformula5, cformula6, cformula7, cformula8
    DistributedKnowledge testDk, testDk1, testDk2, testDk3, testDk4, testDk5, testDk6,
                         testCDk1, testCDk2

    def im1, model2, model3, model4, model5, model6, model7
    def tr1, tr2, tr3, tr4, tr5


    void setUp() {

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

        agent = new Agent.AgentBuilder().knowledgeBase(bpCollection1).build()

        def formulaIM = model1

        sformula1 = new SimpleFormula(formulaIM, tr2, false); // black(individualModel:id1)
        sformula2 = new SimpleFormula(formulaIM, tr1, true); // !red(individualModel:id1)
        sformula3 = new SimpleFormula(formulaIM, tr4, true); // !warm(individualModel:id1)
        cformula1 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS], LogicOperator.AND) // !red(individualModel:id1) && black(individualModel:id1)
        cformula2 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS, State.IS_NOT], LogicOperator.AND) // red(individualModel:id1) && !black(individualModel:id1)


        def currTime = agent.knowledgeBase.getTimestamp()
        testDk1 = new DistributedKnowledge(agent, sformula1) //with simple formula - at least one matching base profile
        testDk2 = new DistributedKnowledge(agent, cformula1) //with complex formula - at least one matching base profile
        testDk3 = new DistributedKnowledge(agent, sformula2)
        //with other simple formula - at least one matching base profile
        testDk4 = new DistributedKnowledge(agent, sformula1, t3) // till given timestamp
        testDk5 = new DistributedKnowledge(agent, sformula3) //only one matching base profile
        testDk6 = new DistributedKnowledge(agent, cformula2) // empty results
        testCDk1 = new DistributedKnowledge(agent, sformula1, true) //with simple formula - complex distribution
        testCDk2 = new DistributedKnowledge(agent, cformula1, true) //with complex formula - complex distribution
    }


    void buildRelatedScenario(int phaseNbr) {

        def usedIMs
        int defTime = 1

        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        //tr4 = new Trait("Soft")
        //tr5 = new Trait("Warm")
        def oType1 = new ObjectType("01", [tr1, tr2, tr3])

        im1 = new IndividualModel(new QRCode("0124"), oType1)


        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5; t6 = 6; t7 = 7;t8=8;t9=9


        bp1 = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t1)
        bp2 = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()
        ] as List, t2)
        bp3 = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t3)
        bp4 = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t4)
        bp5 = new BaseProfile([new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [im1] as Set<IndividualModel>]
        ] as List, t5)
        bp6 = new BaseProfile([new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>]
        ] as List, t6)
        bp7 = new BaseProfile([[(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t7)
        bp8 = new BaseProfile([[(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t8)
        bp9 = new BaseProfile([[(tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr1): [im1] as Set<IndividualModel>, (tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t9)

        /*
base profiles matrix:
NR  RED WHI BLI
1   1   0   0
2   1   0   0
3   1   0   1   (blinking, white): {(is, is not):KNOW}
-------------
4   1   0   N   (blinking, white): {(is, is not):POS, (si not, is not):BEL}
                (red, white): {(is, is not): know}
                (red, blinking): {(is, is): pos, (is, is not): bel}
-------------
5   N   0   0
6   N   0   N
7   N   1   1
8   0   1   N
9   N   N   1   (red, blinking): {(is, is): pos, (is, is not): bel}
                (white, blinking): {(is, is): pos, (is not, is): pos}
 */

        bp1_a = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t1)
        bp2_a = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 new HashMap<Trait, Set<IndividualModel>>()
        ] as List, t2)
        bp3_a = new BaseProfile([[(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr1): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t3)
        bp4_a = new BaseProfile([[(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr1): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 new HashMap<Trait, Set<IndividualModel>>()
        ] as List, t4)
        bp5_a = new BaseProfile([new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [im1] as Set<IndividualModel>]
        ] as List, t5)
        bp6_a = new BaseProfile([new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>]
        ] as List, t6)
        bp7_a = new BaseProfile([[(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t7)
        bp8_a = new BaseProfile([[(tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t8)
        bp9_a = new BaseProfile([[(tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr1): [im1] as Set<IndividualModel>, (tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t9)

        /*
        alternative base profiles matrix:
NR  RED WHI BLI
1   1   0   0
2   1   0   0
3   0   1   1
4   0   1   1
5   N   0   1
-------------
6   0   0   1
7   1   1   1
8   0   1   0
9   1   1   1
         */

        switch (phaseNbr) {
            case 0:
                bpCollection1 = new BPCollection([bp1, bp2, bp3] as Set, [] as Set)
                break
            case 1:
                bpCollection1 = new BPCollection([bp1, bp2, bp3, bp4] as Set, [] as Set)
                break
            case 2:
                bpCollection1 = new BPCollection([bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9] as Set, [] as Set)
                break
            case 3:
                bpCollection1 = new BPCollection([bp1_a, bp2_a, bp3_a, bp4_a, bp5_a/*, bp6_a, bp7_a, bp8_a, bp9_a*/] as Set, [] as Set)
                break
        }


        agent = new Agent.AgentBuilder().knowledgeBase(bpCollection1).build()

        def formulaIM = im1


        cformula1 = new ComplexFormula(formulaIM, [tr3, tr2],
                [State.IS, State.IS_NOT], LogicOperator.AND)

        cformula2 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS, State.IS], LogicOperator.AND)
        cformula3 = new ComplexFormula(formulaIM, [tr1, tr3],
                [State.IS, State.IS], LogicOperator.AND)
        cformula4 = new ComplexFormula(formulaIM, [tr2, tr3],
                [State.IS_NOT, State.IS_NOT], LogicOperator.AND)
        cformula5 = new ComplexFormula(formulaIM, [tr2, tr3],
                [State.IS_NOT, State.IS_NOT], LogicOperator.OR)
        cformula6 = new ComplexFormula(formulaIM, [tr2, tr3],
                [State.IS, State.IS_NOT], LogicOperator.OR)
        cformula7 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS_NOT], LogicOperator.OR)
        cformula8 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS, State.IS], LogicOperator.XOR)

    }


    @Test
    void testGetGroundingSets() {
        def groundingSets = Grounder.getGroundingSets(sformula1, agent.knowledgeBase.getBaseProfiles())
        def sfGrSetsNumber = 2;
        assertEquals(sfGrSetsNumber, groundingSets.size())
        groundingSets = Grounder.getGroundingSets(cformula1, agent.knowledgeBase.getBaseProfiles())
        def cfGrSetsNumber = 4;
        assertEquals(cfGrSetsNumber, groundingSets.size())

        buildRelatedScenario(0)
        groundingSets = Grounder.getGroundingSets(cformula4, agent.knowledgeBase.getBaseProfiles())
        assertEquals([bp1, bp2] as Set<BaseProfile>, groundingSets.get(cformula4))

        buildRelatedScenario(1)
        groundingSets = Grounder.getGroundingSets(cformula5, agent.knowledgeBase.getBaseProfiles())
        assertEquals([bp1, bp2, bp3] as Set<BaseProfile>, groundingSets.get(cformula5))

        buildRelatedScenario(1)
        groundingSets = Grounder.getGroundingSets(cformula6, agent.knowledgeBase.getBaseProfiles())
        assertEquals([bp1, bp2] as Set<BaseProfile>, groundingSets.get(cformula6))

        buildRelatedScenario(2)
        groundingSets = Grounder.getGroundingSets(cformula5, agent.knowledgeBase.getBaseProfiles())
        assertEquals([bp1, bp2, bp3, bp5] as Set<BaseProfile>, groundingSets.get(cformula5))
    }


    @Test
    void testCheckEpistemicConditions() {

        buildRelatedScenario(0)
        def res = Grounder.performFormulaGrounding(agent, cformula1)
        assertEquals([(new ComplexFormula(im1, [tr3, tr2], [State.IS, State.IS_NOT], LogicOperator.AND))
                      : ModalOperator.KNOW] as Map<Formula, ModalOperator>,
                res)

        buildRelatedScenario(1)
        res = Grounder.performFormulaGrounding(agent, cformula1)
        assertEquals([(new ComplexFormula(im1, [tr3, tr2], [State.IS, State.IS_NOT], LogicOperator.AND)): ModalOperator.POS,
                      (new ComplexFormula(im1, [tr3, tr2], [State.IS_NOT, State.IS_NOT], LogicOperator.AND)): ModalOperator.BEL] as Map<Formula, ModalOperator>,
                res)

        buildRelatedScenario(1)
        res = Grounder.performFormulaGrounding(agent, cformula2)
        assertEquals([(new ComplexFormula(im1, [tr1, tr2], [State.IS, State.IS_NOT], LogicOperator.AND)): ModalOperator.KNOW]
                as Map<Formula, ModalOperator>,
                res)

        buildRelatedScenario(1)
        res = Grounder.performFormulaGrounding(agent, cformula3)
        assertEquals([(new ComplexFormula(im1, [tr1, tr3], [State.IS, State.IS], LogicOperator.AND)): ModalOperator.POS,
                      (new ComplexFormula(im1, [tr1, tr3], [State.IS, State.IS_NOT], LogicOperator.AND)): ModalOperator.BEL] as Map<Formula, ModalOperator>,
                res)

        buildRelatedScenario(2)
        res = Grounder.performFormulaGrounding(agent, cformula3)
        assertEquals([(new ComplexFormula(im1, [tr1, tr3], [State.IS, State.IS], LogicOperator.AND))    : ModalOperator.POS,
                      (new ComplexFormula(im1, [tr1, tr3], [State.IS, State.IS_NOT], LogicOperator.AND)): ModalOperator.BEL] as Map<Formula, ModalOperator>,
                res)
        buildRelatedScenario(2)
        res = Grounder.performFormulaGrounding(agent, cformula4)
        assertEquals([/*(new ComplexFormula(model1, [tr2, tr3], [State.IS_NOT, State.IS_NOT], LogicOperator.AND)): ModalOperator.POS,*/
                      (new ComplexFormula(im1, [tr2, tr3], [State.IS, State.IS], LogicOperator.AND))        : ModalOperator.POS,
                      (new ComplexFormula(im1, [tr2, tr3], [State.IS_NOT, State.IS], LogicOperator.AND))    : ModalOperator.POS] as Map<Formula, ModalOperator>,
                res)

        /*buildRelatedScenario(2)
        res = Grounder.performFormulaGrounding(agent, cformula5)

        //according to [0.1, 0.6, 0.65, 0.9, 1.0] thresholds
        assertEquals([(new ComplexFormula(model1, [tr2, tr3], [State.IS, State.IS], LogicOperator.AND))        : ModalOperator.POS,
                      (new ComplexFormula(model1, [tr2, tr3], [State.IS_NOT, State.IS], LogicOperator.AND))    : ModalOperator.POS
        ] as Map<Formula, ModalOperator>,
                res)*/

    }

    @Test
    void testCheckEpistemicCondition() {
        final double[] simpleThresholds = Configuration.simpleThresholds;
        //[0.2, 0.6, 0.7, 0.9, 1.0];

        assertNull(Grounder.checkEpistemicCondition(true, true,
                0.0, ModalOperator.POS, simpleThresholds))
        assertEquals(ModalOperator.POS, Grounder.checkEpistemicCondition(true, true,
                0.3, ModalOperator.POS, simpleThresholds))
        assertEquals(ModalOperator.POS, Grounder.checkEpistemicCondition(true, true,
                0.2, ModalOperator.POS, simpleThresholds))
        assertEquals(ModalOperator.POS, Grounder.checkEpistemicCondition(true, true,
                0.59, ModalOperator.POS, simpleThresholds))
        assertEquals(null, Grounder.checkEpistemicCondition(true, true,
                0.61, ModalOperator.BEL, simpleThresholds))
        assertEquals(ModalOperator.BEL, Grounder.checkEpistemicCondition(true, true,
                0.7, ModalOperator.BEL, simpleThresholds))
        assertEquals(null, Grounder.checkEpistemicCondition(true, true,
                0.9, ModalOperator.BEL, simpleThresholds))
        assertEquals(ModalOperator.BEL, Grounder.checkEpistemicCondition(true, true,
                0.89, ModalOperator.BEL, simpleThresholds))
        assertNull(Grounder.checkEpistemicCondition(true, true,
                0.95, ModalOperator.KNOW, simpleThresholds))
        assertEquals(ModalOperator.KNOW, Grounder.checkEpistemicCondition(true, true,
                1.0, ModalOperator.KNOW, simpleThresholds))
        assertEquals(null, Grounder.checkEpistemicCondition(false, true,
                1.0, ModalOperator.KNOW, simpleThresholds))
        assertNull(Grounder.checkEpistemicCondition(true, true,
                0.85, ModalOperator.KNOW, simpleThresholds))

        assertNull(Grounder.checkEpistemicCondition(false, true,
                0.3, ModalOperator.POS, simpleThresholds))
        assertNull(Grounder.checkEpistemicCondition(true, false,
                0.2, ModalOperator.POS, simpleThresholds))

        assertEquals(null, Grounder.checkEpistemicCondition(true, true,
                0.95, ModalOperator.KNOW, simpleThresholds))
    }

    @Test
    void testRelativeCard() {
        setUp()
        def groundingSets = Grounder.getGroundingSets(sformula1, agent.knowledgeBase.getBaseProfiles())
        assertEquals(8.0, Grounder.relativeCard(groundingSets,sformula1))
        assertEquals(0.0, Grounder.relativeCard(groundingSets,sformula2))
        def groundingSets2 = Grounder.getGroundingSets(sformula3, agent.knowledgeBase.getBaseProfiles())
        assertEquals(1.0, Grounder.relativeCard(groundingSets2,sformula3))

        def groundingSets3 = Grounder.getGroundingSets(cformula1, agent.knowledgeBase.getBaseProfiles())
        assertEquals(12.0, Grounder.relativeCard(groundingSets3,cformula1))

        def groundingSets4 = Grounder.getGroundingSets(cformula2, agent.knowledgeBase.getBaseProfiles())
        assertEquals(0.0, Grounder.relativeCard(groundingSets4,cformula2))

        def groundingSets5 = Grounder.getGroundingSets(cformula3, agent.knowledgeBase.getBaseProfiles())
        assertEquals(0.0, Grounder.relativeCard(groundingSets5,cformula3))
    }

    @Test
    void testIsEpsilonConcentrated() {
        buildRelatedScenario(2)
        println("first check")
        assertFalse(Grounder.checkEpsilonConcentratedCondition(cformula5, [bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9] as Set<BaseProfile>))
        println("second check")
        assertTrue(Grounder.checkEpsilonConcentratedCondition(cformula8, [bp1_a, bp2_a, bp3_a, bp4_a, bp5_a] as Set<BaseProfile>))

    }

    @Test
    void testCountSetDiameter() {
        buildRelatedScenario(3)

        //dependent test  todo should be moved to FormulaTest
        def dependent = cformula5.getDependentFormulas()
        def expected1 = new ComplexFormula(cformula5.getModel(), cformula5.getTraits(), [State.IS_NOT, State.IS_NOT]as List<State>, LogicOperator.AND)
        def expected2 = new ComplexFormula(cformula5.getModel(), cformula5.getTraits(), [State.IS_NOT, State.IS]as List<State>, LogicOperator.AND)
        def expected3 = new ComplexFormula(cformula5.getModel(), cformula5.getTraits(), [State.IS, State.IS_NOT]as List<State>, LogicOperator.AND)
        assertTrue(dependent.containsAll([expected1, expected3, expected2]as List<ComplexFormula>) && dependent.size()==3)
        //
        assertEquals(0.6, Grounder.countSetDiameter(dependent.get(0), dependent, [bp1,bp2,bp3,bp4,bp5,bp6,bp7,bp8,bp9]as Set<BaseProfile>))

        dependent = cformula8.getDependentFormulas()
//        println Grounder.relativeCard_(Grounder.getGroundingSets(dependent.get(0), [bp1,bp2,bp3,bp4,bp5,bp6,bp7,bp8,bp9]as Set<BaseProfile>))
        println Grounder.relativeCard_(Grounder.getGroundingSets(dependent.get(0), [bp1_a, bp2_a, bp3_a, bp4_a, bp5_a]as Set<BaseProfile>))
        println cformula8
        println dependent.get(0)
        println dependent.get(1)
        println( Grounder.countSetDiameter(dependent.get(0), dependent, [bp1_a, bp2_a, bp3_a, bp4_a, bp5_a]as Set<BaseProfile>))
        println( Grounder.countSetDiameter(dependent.get(1), dependent, [bp1_a, bp2_a, bp3_a, bp4_a, bp5_a]as Set<BaseProfile>))
    }
}
