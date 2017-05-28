package com.pwr.zpi.language

import com.pwr.zpi.*
import com.pwr.zpi.episodic.BPCollection
import com.pwr.zpi.episodic.BaseProfile
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode
import org.testng.annotations.Test

class GrounderTest extends GroovyTestCase {


    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9,  bp2_2;
    int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9
    BPCollection bpCollection1
    Agent agent
    SimpleFormula sformula1, sformula2, sformula3
    ComplexFormula cformula1, cformula2,cformula3,cformula4
    DistributedKnowledge testDk, testDk1, testDk2, testDk3, testDk4, testDk5, testDk6,
                         testCDk1, testCDk2

    def im1, model2, model3, model4, model5, model6, model7
    def tr1, tr2, tr3, tr4, tr5



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


        agent = new Agent(bpCollection1)

        def formulaIM = im1


        cformula1 = new ComplexFormula(formulaIM, [tr3, tr2],
                [State.IS, State.IS_NOT], LogicOperator.AND)

        cformula2 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS, State.IS], LogicOperator.AND)
        cformula3 = new ComplexFormula(formulaIM, [tr1, tr3],
                [State.IS, State.IS], LogicOperator.AND)
        cformula4 = new ComplexFormula(formulaIM, [tr2, tr3],
                [State.IS_NOT, State.IS_NOT], LogicOperator.AND)

    }


    @Test
    void testGetGroundingSets() {
        build()
        def groundingSets = Grounder.getGroundingSets(sformula1, agent.knowledgeBase.getBaseProfiles())
        def sfGrSetsNumber = 2;
        assertEquals(sfGrSetsNumber, groundingSets.size())
        groundingSets = Grounder.getGroundingSets(cformula1, agent.knowledgeBase.getBaseProfiles())
        def cfGrSetsNumber = 4;
        assertEquals(cfGrSetsNumber, groundingSets.size())
    }


    @Test
    void testDetermineFulfillment() {

    }

    @Test
    void testCheckEpistemicConditions() {

        buildRelatedScenario(0)
        def res = Grounder.performFormulaGrounding(agent, cformula1)
        assertEquals([(new ComplexFormula(im1, [tr3, tr2], [State.IS, State.IS_NOT], LogicOperator.AND)):ModalOperator.POS,
                      (new ComplexFormula(im1, [tr3, tr2], [State.IS_NOT, State.IS_NOT], LogicOperator.AND)):ModalOperator.BEL]as Map<Formula, ModalOperator>,
                res)
        buildRelatedScenario(1)
        res = Grounder.performFormulaGrounding(agent, cformula2)
        assertEquals([(new ComplexFormula(im1, [tr1, tr2], [State.IS, State.IS_NOT], LogicOperator.AND)):ModalOperator.KNOW]as Map<Formula, ModalOperator>,
                res)
        buildRelatedScenario(2)
        res = Grounder.performFormulaGrounding(agent, cformula3)
        assertEquals([(new ComplexFormula(im1, [tr1, tr3], [State.IS, State.IS], LogicOperator.AND)):ModalOperator.POS,
                      (new ComplexFormula(im1, [tr1, tr3], [State.IS, State.IS_NOT], LogicOperator.AND)):ModalOperator.POS]as Map<Formula, ModalOperator>,
                res)
        res = Grounder.performFormulaGrounding(agent, cformula4)
        assertEquals([(new ComplexFormula(im1, [tr2, tr3], [State.IS_NOT, State.IS_NOT], LogicOperator.AND)):ModalOperator.POS,
                      (new ComplexFormula(im1, [tr2, tr3], [State.IS, State.IS], LogicOperator.AND)):ModalOperator.POS,
                      (new ComplexFormula(im1, [tr2, tr3], [State.IS_NOT, State.IS], LogicOperator.AND)):ModalOperator.POS]as Map<Formula, ModalOperator>,
                res)

    }

    @Test
    void testCheckEpistemicCondition() {
        final double[] simpleThresholds = Grounder.simpleThresholds;
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

}
