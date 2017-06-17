package holons.context

import com.pwr.zpi.episodic.BaseProfile
import com.pwr.zpi.holons.ContextJar.DistanceFunction
import com.pwr.zpi.holons.ContextJar.DistanceFunctions.DistanceFunction1
import com.pwr.zpi.util.Pair
import com.pwr.zpi.core.episodic.BaseProfile
import com.pwr.zpi.core.holons.ContextJar.DistanceFunction
import com.pwr.zpi.core.holons.ContextJar.DistanceFunctions.DistanceFunction1
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import com.pwr.zpi.language.Trait
import org.junit.Test

/**
 * Created by Jarema on 5/30/2017.
 */
class DisnatceFuncionGroovyTest extends  GroovyTestCase{

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6
    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    def tr1, tr2, tr3
    def im1, model2
    int t0, t1, t2, t3, t4, t5

    Set<Trait> positive;
    Set<Trait> negative;
    void build() {
        def tr1, tr2, tr3, tr4, tr5
        def model1, model2, model3, model4, model5, model6, model7
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
        bp6 = new BaseProfile([describedByTraits, [(tr3): [model1, model6] as Set,
                                                   (tr4): [model1, model6] as Set],
                               indefiniteByTraits] as List, t4)

        positive = new HashSet<Trait>()
        positive.add(tr1)
        positive.add(tr2)
        negative = new HashSet<Trait>()
        negative.add(tr3)
        negative.add(tr4)
    }
    @Test
    void testImplementation() throws Exception {
        build()
        DistanceFunction f1 = new DistanceFunction1()

        assertEquals(1.0, f1.implementation(bp1,bp2))
        assertEquals(6.0, f1.implementation(bp1,bp3))
        assertEquals(6.0, f1.implementation(bp1,bp4))
        assertEquals(0.0, f1.implementation(bp1,bp5))
        assertEquals(6.0, f1.implementation(bp1,bp6))
    }

    @Test
    void testComposite() throws Exception {
        build()

        DistanceFunction f1 = new DistanceFunction1()

        assertEquals(3.0, f1.composite(bp1,new Pair<Set<Trait>, Set<Trait>>(positive,negative)))
        assertEquals(0, f1.composite(bp2,new Pair<Set<Trait>, Set<Trait>>(positive,negative)))
        assertEquals(3.0, f1.composite(bp3,new Pair<Set<Trait>, Set<Trait>>(positive,negative)))
        assertEquals(3.0, f1.composite(bp4,new Pair<Set<Trait>, Set<Trait>>(positive,negative)))
        assertEquals(3.0, f1.composite(bp4,new Pair<Set<Trait>, Set<Trait>>(positive,negative)))
    }
}
