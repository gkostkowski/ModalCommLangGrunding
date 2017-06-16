package holons.context.contextualisation

import com.pwr.zpi.core.episodic.BaseProfile
import com.pwr.zpi.core.holons.context.Context
import com.pwr.zpi.core.holons.context.builders.ConcreteContextBuilder
import com.pwr.zpi.core.holons.context.contextualisation.FilteringContextualisation
import com.pwr.zpi.core.holons.context.measures.Distance
import com.pwr.zpi.core.holons.context.measures.Measure
import com.pwr.zpi.core.holons.context.selectors.LatestGroupSelector
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import com.pwr.zpi.language.*

/**
 * Created by Grzesiek on 2017-06-03.
 */
class FilteringContextualisationTest extends GroovyTestCase {
    FilteringContextualisation testObj;
    Measure distance;
    Map<Formula, Set<BaseProfile>> namedGroundingSets;

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9, bp2_2;
    int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9


    def im1, model2
    def tr1, tr2, tr3
    Formula formula

    Context context

    void setUp() {
        distance = new Distance(2);

        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        def im1 = new IndividualModel(new QRCode("0124"),
                new ObjectType("01", [tr1, tr2, tr3]))
        formula = new ComplexFormula(im1, [tr1, tr2],
                [State.IS_NOT, State.IS], LogicOperator.AND)


        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5; t6 = 6; t7 = 7;


        bp1 = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr2): [im1] as Set<IndividualModel>, (tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t1)
        bp2 = new BaseProfile([[(tr1): [im1] as Set<IndividualModel>, (tr2): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr3): [im1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
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

        /* obsevation matrix:
        RED  WHI  BLI
        1	 0	  0
        1	 1    0
        1	 0	  1
        1	 0	  null
        null 0	  0 - nie ugruntuje sie

         */

        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4, bp5] as Set<BaseProfile>)
        context = new ConcreteContextBuilder().build(im1, new LatestGroupSelector(4).select(namedGroundingSets))

        testObj = new FilteringContextualisation(context, distance)

    }

/*
    void testSelectRepresentativeBPs() {
        shouldFail { testObj.selectRepresentativeBPs(new HashSet<BaseProfile>()) }
        assertEquals([bp4] as Set<BaseProfile>, testObj.selectRepresentativeBPs(namedGroundingSets))
        //bo bp5 sie nie gruntuje
        assertEquals([bp3] as Set<BaseProfile>, testObj.selectRepresentativeBPs(Grounder.getGroundingSetsMap(formula, [bp1, bp3] as Set<BaseProfile>)))

    }
*/

    /*void testIsMeetingCondition() {
        namedGroundingSets = Grounder.getGroundingSetsMap(formula, [bp1, bp2, bp3, bp4] as Set<BaseProfile>)
        testObj.selectRepresentativeBPs(namedGroundingSets)

        //last representative is bp4 [1 0 null]
        *//* obsevation matrix:
        1	 0	  0
        1	 1    0
        1	 0	  1
        1	 0	  null
        null 0	  0 - nie ugruntuje sie

         *//*
        testObj.setMaxThreshold(1)
        assertEquals(true, testObj.isMeetingCondition(bp3))
        assertEquals(false, testObj.isMeetingCondition(bp2))
        testObj.selectRepresentativeBPs(Grounder.getGroundingSetsMap(formula, [bp1, bp2, bp3] as Set<BaseProfile>))
        assertEquals(false, testObj.isMeetingCondition(bp2))
        assertEquals(true, testObj.isMeetingCondition(bp1))

        testObj.selectRepresentativeBPs(Grounder.getGroundingSetsMap(formula, [bp1, bp2] as Set<BaseProfile>))
        testObj.setMaxThreshold(0)
        assertEquals(false, testObj.isMeetingCondition(bp1))
        assertEquals(true, testObj.isMeetingCondition(bp2))

        testObj.selectRepresentativeBPs(Grounder.getGroundingSetsMap(formula, [bp1, bp2, bp3, bp4] as Set<BaseProfile>))
        testObj.setMaxThreshold(2)
        assertEquals(true, testObj.isMeetingCondition(bp2))
    }*/

    void testPerformContextualisation() {
        distance = new Distance(2);
        testObj = new FilteringContextualisation(context, distance)
        /*
        INPUT:
        [ComplexFormula{QRCode{id='0124'}: IS Red and  IS_NOT White}:
            [1 0 0, 1 0 1, 1 0 null],
        ComplexFormula{QRCode{id='0124'}: IS Red and  IS White}:
            [1 1 0],
        ComplexFormula{QRCode{id='0124'}: IS_NOT Red and  IS_NOT White}:
            [],
        ComplexFormula{QRCode{id='0124'}:IS_NOT Red and  IS White}:
            []]
         */
        def res = testObj.performContextualisation(namedGroundingSets)

        //general contract checking
        for (Formula f : res.keySet()) {
            assertTrue(namedGroundingSets.get(f).containsAll(res.get(f))) //if stem from certain input set

            def minus = new HashSet<>()
            minus.addAll(res)
            minus.removeAll(namedGroundingSets)
            assertTrue(minus.isEmpty())  //nothing which is not a part of certain input set

            assertTrue(res.get(f).size() <= namedGroundingSets.get(f).size()) //size checking
        }

        //nothing was filtered because of distance
        for (Formula f : res.keySet()) {
            assertTrue(namedGroundingSets.get(f).containsAll(res.get(f))
                    && res.get(f).containsAll(namedGroundingSets.get(f))) //same

        }

        //CASE II
        distance = new Distance(1);
        testObj = new FilteringContextualisation(context, distance)
        def complFormulas = formula.standardFormula.getComplementaryFormulas()
        res = testObj.performContextualisation(namedGroundingSets)
        /*
        representative:  1	 0	  null
        expected OUTPUT:
        ComplexFormula{QRCode{id='0124'}: IS Red and  IS White}:
            [],
        ComplexFormula{QRCode{id='0124'}:IS_NOT Red and  IS White}:
            []]
        [ComplexFormula{QRCode{id='0124'}: IS Red and  IS_NOT White}:
         [1 0 0, 1 0 1, 1 0 null],
        ComplexFormula{QRCode{id='0124'}: IS_NOT Red and  IS_NOT White}:
            [],
        */

        assertTrue(res.get(complFormulas.get(0)).isEmpty())
        assertTrue(res.get(complFormulas.get(1)).isEmpty())
        assertTrue(res.get(complFormulas.get(2)).containsAll([bp1, bp3, bp4] as Set<BaseProfile>))
        assertTrue(res.get(complFormulas.get(3)).isEmpty())

        //CASE III
        /*
        representative:  1	 0	  null
        expected OUTPUT:
        ComplexFormula{QRCode{id='0124'}: IS Red and  IS White}:
            [],
        ComplexFormula{QRCode{id='0124'}:IS_NOT Red and  IS White}:
            []]
        [ComplexFormula{QRCode{id='0124'}: IS Red and  IS_NOT White}:
         [1 0 null],
        ComplexFormula{QRCode{id='0124'}: IS_NOT Red and  IS_NOT White}:
            [],
        */
        distance = new Distance(0);
        testObj = new FilteringContextualisation(context, distance)
        res = testObj.performContextualisation(namedGroundingSets)

        assertTrue(res.get(complFormulas.get(0)).isEmpty())
        assertTrue(res.get(complFormulas.get(1)).isEmpty())
        assertTrue(res.get(complFormulas.get(2)).containsAll([bp4] as Set<BaseProfile>))
        assertTrue(res.get(complFormulas.get(3)).isEmpty())

        //CASE EMPTY
        distance = new Distance(2);
        testObj = new FilteringContextualisation(context, distance)
        res = testObj.performContextualisation([] as Map<Formula, Set<BaseProfile>>) //with warning
        assertTrue(res.isEmpty())
    }


    void testDetermineFulfillment() {
    }
}
