package context

import com.pwr.zpi.episodic.BaseProfile
import com.pwr.zpi.holons.context.FilteringContext
import com.pwr.zpi.holons.context.LatestFilteringContext
import com.pwr.zpi.holons.context.measures.Distance
import com.pwr.zpi.holons.context.measures.Measure
import com.pwr.zpi.language.*
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode

/**
 * Created by Grzesiek on 2017-05-27.
 */
class LatestFilteringContextTest extends GroovyTestCase {
    FilteringContext testObj;
    Measure distance;
    Map<Formula, Set<BaseProfile>> namedGroundingSets;

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9,  bp2_2;
    int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9


    def im1, model2
    def tr1, tr2, tr3
    def formula

    void setUp() {
        distance = new Distance(2);
        testObj = new LatestFilteringContext(distance)

        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        def im1 = new IndividualModel(new QRCode("0124"),
                new ObjectType("01", [tr1,tr2,tr3]))
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
        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4, bp5]as Set<BaseProfile>)
    }

    void tearDown() {
    }

    void testSelectRepresentativeBPs() {
        shouldFail {testObj.selectRepresentativeBPs(new HashSet<BaseProfile>())}
        assertEquals([bp4] as Set<BaseProfile>, testObj.selectRepresentativeBPs(namedGroundingSets))  //bo bp5 sie nie gruntuje
        assertEquals([bp3] as Set<BaseProfile>, testObj.selectRepresentativeBPs(Grounder.getGroundingSets( formula, [bp1, bp3]as Set<BaseProfile>)))

    }


    void testIsMeetingCondition() {
        namedGroundingSets = Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4]as Set<BaseProfile>)
        testObj.selectRepresentativeBPs(namedGroundingSets)


        //last representative is bp4 [1 0 null]
        /* obsevation matrix:
        1	 0	  0
        1	 1    0
        1	 0	  1
        1	 0	  null
        null 0	  0 - nie ugruntuje sie

         */
        testObj.setMaxThreshold(1)
        assertEquals(true, testObj.isMeetingCondition(bp3))
        assertEquals(false, testObj.isMeetingCondition(bp2))
        testObj.selectRepresentativeBPs(Grounder.getGroundingSets(formula, [bp1, bp2, bp3] as Set<BaseProfile>))
        assertEquals(false, testObj.isMeetingCondition(bp2))
        assertEquals(true, testObj.isMeetingCondition(bp1))

        testObj.selectRepresentativeBPs(Grounder.getGroundingSets(formula, [bp1, bp2]as Set<BaseProfile>))
        testObj.setMaxThreshold(0)
        assertEquals(false, testObj.isMeetingCondition(bp1))
        assertEquals(true, testObj.isMeetingCondition(bp2))

        testObj.selectRepresentativeBPs(Grounder.getGroundingSets(formula, [bp1, bp2, bp3, bp4]as Set<BaseProfile>))
        testObj.setMaxThreshold(2)
        assertEquals(true, testObj.isMeetingCondition(bp2))
    }

    void testPerformContextualisation() {

    }
}
