package measures

import com.pwr.zpi.episodic.BaseProfile
import com.pwr.zpi.holons.context.measures.Distance
import com.pwr.zpi.language.Trait
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode
import org.junit.Test

/**
 * Created by Grzesiek on 2017-05-27.
 */
class DistanceTest extends GroovyTestCase {

    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9;
    int t0, t1, t2, t3, t4, t5,t6,t7,t8,t9

    Distance distance;

    void setUp() {

        def tr1, tr2, tr3, tr4

        tr1 = new Trait("Red")
        tr2 = new Trait("White")
        tr3 = new Trait("Blinking")
        tr4 = new Trait("Soft")
        def oType1 = new ObjectType("Type1", [tr1, tr2, tr3, tr4])
        def im1 = new IndividualModel(new QRCode("ID1"), oType1)



        t0 = 0; t1 = 1; t2 = 2; t3 = 3; t4 = 4; t5 = 5;t6=6;t7=7;t8=8;t9=9

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


    }

    @Test
    void testCount() {
        distance = new Distance(10);
        assertEquals (0.0, distance.count(bp1, bp1))
        assertEquals (0.0, distance.count(bp1, bp2))
        assertEquals (1.0, distance.count(bp1, bp3))
        assertEquals (2.0, distance.count(bp1, bp6))
        assertEquals (3.0, distance.count(bp1, bp9))
    }
}
