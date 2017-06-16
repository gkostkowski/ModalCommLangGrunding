package holons.context.measures

import com.pwr.zpi.core.episodic.BaseProfile
import com.pwr.zpi.core.holons.context.builders.ConcreteContextBuilder
import com.pwr.zpi.core.holons.context.measures.Distance
import com.pwr.zpi.core.holons.context.measures.NormalisedDistance
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import com.pwr.zpi.language.Trait
import org.junit.Test

import java.text.DecimalFormat

/**
 * Created by Grzesiek on 2017-06-03.
 */
class NormalisedDistanceTest extends GroovyTestCase {

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9;
    int t0, t1, t2, t3, t4, t5,t6,t7,t8,t9
    def model1, model2

    Distance distance;

    void setUp() {

        def tr1, tr2, tr3, tr4, tr5, tr6, tr7

        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        tr4 = new Trait("Green")
        tr5 = new Trait("Striped")
        tr6 = new Trait("patterned")
        tr7 = new Trait("Blue")
        def oType1 = new ObjectType("Type1", [tr1, tr2, tr3, tr4])
        model1 = new IndividualModel(new QRCode("ID1"), oType1)
        model1 = new IndividualModel(new QRCode("ID2"), oType1)

        def t=0

        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5;t6=6;t7=7;t8=8;t9=9

        bp1 = new BaseProfile([[(tr6): [model1] as Set<IndividualModel>, (tr7): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t++)
        bp2 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>(),
                               new HashMap<Trait, Set<IndividualModel>>()
        ] as List, t++)
        bp3 = new BaseProfile([new HashMap<Trait, Set<IndividualModel>>(),
                               new HashMap<Trait, Set<IndividualModel>>(),
                               new HashMap<Trait, Set<IndividualModel>>()

        ] as List, t++)
        bp4 = new BaseProfile([[(tr6): [model1] as Set<IndividualModel>, (tr7): [model1] as Set<IndividualModel>,
                                (tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               new HashMap<Trait, Set<IndividualModel>>()
        ] as List, t++)

        bp5 = new BaseProfile([[(tr7): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr1): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)
        bp6 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr4): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>
        ] as List, t++)

        bp7 = new BaseProfile([[(tr2): [model1] as Set<IndividualModel>, (tr3): [model1] as Set<IndividualModel>,
                                (tr1): [model1] as Set<IndividualModel>, (tr6): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
                               [(tr5): [model1] as Set<IndividualModel>, (tr7): [model1] as Set<IndividualModel>] as Map<Trait, Set<IndividualModel>>,
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


    }

    @Test
    void testCount() {
        DecimalFormat formatter = new DecimalFormat("##.00")

        distance = new NormalisedDistance()
        def context = new ConcreteContextBuilder().build(model1, [bp6]) // ([tr2, tr3, tr1, tr6],[tr4])
        assertEquals (0.0, distance.count(bp6, context))
        //todo
        /*assertEquals (formatter.format(1/5), formatter.format(distance.count(bp2, context))) //([tr2, tr3, tr1, tr6], [])
        assertEquals (formatter.format(3/7), formatter.format(distance.count(bp7, context))) //([tr2, tr3, tr1, tr6], [tr5, tr7])
        assertEquals (formatter.format(4/7), formatter.format(distance.count(bp1, context))) //([tr6, tr7, tr1, tr5], [tr4])
        assertEquals (formatter.format(5/7), formatter.format(distance.count(bp4, context))) //([tr2, tr1, tr4, tr6], [tr5, tr7])
        assertEquals (formatter.format(7/7), formatter.format(distance.count(bp5, context))) //([tr7], [tr5])
        assertEquals (formatter.format(5/5), formatter.format(distance.count(bp3, context))) //([], [])*/

        shouldFail {
            new NormalisedDistance(-1)
        }
    }
}
