package holons.context.selectors

import com.pwr.zpi.core.memory.episodic.BaseProfile
import com.pwr.zpi.core.memory.holons.context.selectors.LatestGroupSelector
import com.pwr.zpi.core.memory.semantic.IndividualModel
import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.core.memory.semantic.QRCode
import com.pwr.zpi.language.*

/**
 * Created by Grzesiek on 2017-06-03.
 */
class LatestGroupSelectorTest extends GroovyTestCase {

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9, bp6_2, bp4_2, bp9_2
    int t

    def testSelector

    def model1, model2
    def tr1, tr2, tr3, tr4, tr5, tr6, tr7

    def formula
    def namedGroundingSets

    void setUp() {

        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        tr4 = new Trait("Green")
        tr5 = new Trait("Striped")
        tr6 = new Trait("patterned")
        tr7 = new Trait("Blue")
        def oType1 = new ObjectType("04", [tr1, tr2, tr3, tr4, tr5, tr6])

        model1 = new IndividualModel(new QRCode("0123"), oType1)
        model2 = new IndividualModel(new QRCode("0124"), oType1)



        t = 0;
        bp1 = new BaseProfile([[(tr6): [model1] as Set<IndividualModel>, (tr7): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t++)
        bp2 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()
        ] as List, t++)
        bp3 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr1): [model1] as Set<IndividualModel>,
                                (tr4): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t++)
        bp4 = new BaseProfile([[(tr1): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr3): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)
        bp4_2 = new BaseProfile([[(tr2): [model2] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr3): [model2] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr1): [model2] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t)
        bp5 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t)
        bp6 = new BaseProfile([[(tr3): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr2): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)
        bp6_2 = new BaseProfile([[(tr2): [model2] as Set<IndividualModel>, (tr3): [model2] as Set<IndividualModel>,
                                  (tr1): [model2] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr1): [model2] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 [(tr3): [model2] as Set<IndividualModel>, (tr2): [model2] as Set<IndividualModel>]
        ] as List, t)
        bp7 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)
        bp8 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr3): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)
        bp9 = new BaseProfile([[(tr3): [model1] as Set<IndividualModel>, (tr5): [model1] as Set<IndividualModel>,
                                (tr4): [model2] as Set<IndividualModel>, (tr1): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>(),
                               [(tr1): [model1] as Set<IndividualModel>, (tr2): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)
        bp9_2 = new BaseProfile([[(tr2): [model2] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                                 new HashMap<Trait, Set<IndividualModel>>(),
                                 [(tr1): [model2] as Set<IndividualModel>, (tr3): [model2] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)

        formula = new ComplexFormula(model1, [tr1, tr2],
                [State.IS_NOT, State.IS], LogicOperator.AND)

        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4, bp5] as Set<BaseProfile>)

    }

    void testSelect() {
        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4, bp5] as Set<BaseProfile>)
        def size = 4
        testSelector = new LatestGroupSelector(size)
        def res = testSelector.select(namedGroundingSets)
        assertTrue(res.size()==size)
        assertEquals([bp2, bp3, bp4, bp5] as Set<BaseProfile>, res)

        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4, bp5, bp6,bp7,bp8,bp9] as Set<BaseProfile>) // bp9 sie nie ugruntuje
        res = testSelector.select(namedGroundingSets)
        assertTrue(res.size()==size)
        assertEquals([bp5, bp6,bp7,bp8] as Set<BaseProfile>,res)

        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4, bp5, bp6] as Set<BaseProfile>)
        assertTrue(testSelector.select(namedGroundingSets).containsAll([bp6] as Set<BaseProfile>)
                || testSelector.select(namedGroundingSets).containsAll([bp5] as Set<BaseProfile>)) //non-deterministic for BPs with same timestamp

        size = 8
        testSelector = new LatestGroupSelector(size)
        res = testSelector.select(namedGroundingSets)
        assertTrue(res.size()==5)  //bp1 sie nie ugruntuje
        assertEquals([bp2, bp3, bp4, bp5, bp6] as Set<BaseProfile>,res)

        size = 1
        testSelector = new LatestGroupSelector(size)
        res = testSelector.select(namedGroundingSets)
        assertTrue(res.size()==size)
        assertTrue(testSelector.select(namedGroundingSets).containsAll([bp6] as Set<BaseProfile>)
                || testSelector.select(namedGroundingSets).containsAll([bp5] as Set<BaseProfile>)) //non-deterministic for BPs with same timestamp



        shouldFail {
            testSelector.select([] as Map<Formula, Set<BaseProfile>>)
        }

        shouldFail {
            new LatestGroupSelector(0)
        }
    }

}
