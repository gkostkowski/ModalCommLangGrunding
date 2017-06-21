package holons

import com.pwr.zpi.core.Agent
import com.pwr.zpi.core.memory.episodic.BPCollection
import com.pwr.zpi.core.memory.episodic.BaseProfile
import com.pwr.zpi.core.memory.episodic.DistributedKnowledge
import com.pwr.zpi.core.memory.holons.NewNonBinaryHolon
import com.pwr.zpi.core.memory.holons.context.contextualisation.Contextualisation
import com.pwr.zpi.core.memory.holons.context.measures.Distance
import com.pwr.zpi.core.memory.holons.context.measures.Measure
import com.pwr.zpi.core.memory.semantic.IndividualModel
import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.core.memory.semantic.identifiers.QRCode
import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.language.LogicOperator
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait

import java.text.DecimalFormat

/**
 * Created by Grzesiek on 2017-05-29.
 */
class NewNonBinaryHolonTest extends GroovyTestCase {

    NewNonBinaryHolon testObj
    Contextualisation contextualisation
    Measure measure

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9, bp2_2;
    int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9
    BPCollection bpCollection1
    Agent agent
    ComplexFormula cformula1, cformula2, cformula3, cformula4, cformula5, cformula6
    DistributedKnowledge testDk1, testDk2, testDk3, testDk4, testDk5, testDk6,
                         testCDk1, testCDk2

    def im1, model1, model2, model3, model4, model5, model6, model7
    def tr1, tr2, tr3, tr4

    void setUp() {


        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        def oType1 = new ObjectType("01", [tr1, tr2, tr3])

        model1 = new IndividualModel(new QRCode("ID1"), oType1)


        im1 = new IndividualModel(new QRCode("0124"), oType1)


        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5; t6 = 6; t7 = 7;


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
        3   1   0   1
        -------------
        4   1   0   N
        -------------
        5   N   0   0
        6   N   0   N
        7   N   1   1
        8   0   1   N
        9   N   N   1
         */


    }

    void buildRelatedScenario(int phaseNbr) {
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
        }

        agent = new Agent.AgentBuilder().knowledgeBase(bpCollection1).build()

        def formulaIM = im1


        cformula1 = new ComplexFormula(formulaIM, [tr3, tr2],
                [State.IS, State.IS_NOT], LogicOperator.AND) //Is hyzio blinking and not white

        cformula2 = new ComplexFormula(formulaIM, [tr1, tr3],
                [State.IS, State.IS], LogicOperator.AND) //Is hyzio red and blinking
        cformula3 = new ComplexFormula(formulaIM, [tr2, tr3],
                [State.IS, State.IS], LogicOperator.AND) //is hyzio white and blinking
        cformula4 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS_NOT], LogicOperator.AND) //is hyzio not red and not white
        cformula5 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS_NOT], LogicOperator.OR) //is hyzio not red OR not white
        cformula6 = new ComplexFormula(formulaIM, [tr2, tr3],
                [State.IS, State.IS_NOT], LogicOperator.OR) //is hyzio white OR not blinking

        def currTime = agent.knowledgeBase.getTimestamp()
        testDk1 = new DistributedKnowledge(agent, cformula1)
        testDk2 = new DistributedKnowledge(agent, cformula2)
        testDk3 = new DistributedKnowledge(agent, cformula3)
        testDk4 = new DistributedKnowledge(agent, cformula4)
        testDk5 = new DistributedKnowledge(agent, cformula5)
        testDk6 = new DistributedKnowledge(agent, cformula6)
        testCDk1 = new DistributedKnowledge(agent, cformula1, true) //with complex formula - complex distribution

        measure = new Distance(2)

//        contextualisation = new FilteringContextualisation(new ConcreteContextBuilder(), measure,
//                im1, new LatestSelector(), testDk1.getGroundingSetsMap());
    }


    void testUpdate() {
        DecimalFormat formatter = new DecimalFormat("##.00")

        buildRelatedScenario(0)
        testObj = new NewNonBinaryHolon(testDk1, contextualisation)
        testObj.update()
        def complFormulas = testDk1.getFormula().getStandardFormula().getComplementaryFormulas()

        assertTrue(testObj.getAffectedFormulas().containsAll(complFormulas))

        assertEquals(0, testObj.getSummary(complFormulas.get(0))) // IS, IS
        assertEquals(0, testObj.getSummary(complFormulas.get(1))) // IS_NOT, IS
        assertEquals(formatter.format(1/3), formatter.format(testObj.getSummary(complFormulas.get(2)))) // IS, IS_NOT
        assertEquals(formatter.format(2 / 3), formatter.format(testObj.getSummary(complFormulas.get(3)))) // IS_NOT, IS_NOT


        buildRelatedScenario(1)
        testObj = new NewNonBinaryHolon(testDk2, contextualisation)
        testObj.update()
        complFormulas = testDk2.getFormula().getStandardFormula().getComplementaryFormulas()

        assertTrue(testObj.getAffectedFormulas().containsAll(complFormulas))

        assertEquals(formatter.format(1/3), formatter.format(testObj.getSummary(complFormulas.get(0)))) // IS, IS
        assertEquals(0, testObj.getSummary(complFormulas.get(1))) // IS_NOT, IS
        assertEquals(formatter.format(2/3), formatter.format(testObj.getSummary(complFormulas.get(2)))) // IS, IS_NOT
        assertEquals(0, testObj.getSummary(complFormulas.get(3))) // IS_NOT, IS_NOT


        buildRelatedScenario(2)
        testObj = new NewNonBinaryHolon(testDk3, contextualisation)
        testObj.update()
        complFormulas = testDk3.getFormula().getStandardFormula().getComplementaryFormulas()

        assertTrue(testObj.getAffectedFormulas().containsAll(complFormulas))

        assertEquals(formatter.format(1/5), formatter.format(testObj.getSummary(complFormulas.get(0)))) // IS, IS
        assertEquals(formatter.format(1/5), formatter.format(testObj.getSummary(complFormulas.get(1)))) // IS_NOT, IS
        assertEquals(0, testObj.getSummary(complFormulas.get(2))) // IS, IS_NOT
        assertEquals(formatter.format(3/5), formatter.format(testObj.getSummary(complFormulas.get(3)))) // IS_NOT, IS_NOT

        testObj = new NewNonBinaryHolon(testDk4, contextualisation)
        testObj.update()
        complFormulas = testDk4.getFormula().getStandardFormula().getComplementaryFormulas()

        assertTrue(testObj.getAffectedFormulas().containsAll(complFormulas))

        assertEquals(0, testObj.getSummary(complFormulas.get(0))) // IS, IS
        assertEquals(formatter.format(1/5), formatter.format(testObj.getSummary(complFormulas.get(1)))) // IS_NOT, IS
        assertEquals(formatter.format(4/5), formatter.format(testObj.getSummary(complFormulas.get(2)))) // IS, IS_NOT
        assertEquals(0, testObj.getSummary(complFormulas.get(3))) // IS_NOT, IS_NOT
/*
        testObj = new NewNonBinaryHolon(testDk5, contextualisation)
        testObj.update()
        complFormulas = testDk5.getFormula().getStandardFormula().getComplementaryFormulas()

        assertTrue(testObj.getAffectedFormulas().containsAll(complFormulas))

        assertEquals(formatter.format(6/8), formatter.format(testObj.getSummary(complFormulas.get(0)))) // IS, IS
        assertEquals(formatter.format(2/8), formatter.format(testObj.getSummary(complFormulas.get(1)))) // IS_NOT, IS
        assertEquals(formatter.format(6/8), formatter.format(testObj.getSummary(complFormulas.get(2)))) // IS, IS_NOT
        assertEquals(formatter.format(7/8), formatter.format(testObj.getSummary(complFormulas.get(3)))) // IS_NOT, IS_NOT
    */
    }

}
