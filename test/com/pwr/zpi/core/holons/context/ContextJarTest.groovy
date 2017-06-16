package holons.context

import com.pwr.zpi.core.episodic.BaseProfile

/**
 * Created by Jarema on 6/11/2017.
 */
import com.pwr.zpi.core.holons.ContextJar.ContextJar
import com.pwr.zpi.core.holons.ContextJar.DistanceFunction
import com.pwr.zpi.core.holons.ContextJar.DistanceFunctions.DistanceFunction1
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import com.pwr.zpi.language.Formula
import com.pwr.zpi.language.SimpleFormula
import com.pwr.zpi.language.Trait
import org.testng.annotations.Test

/**
 * Created by Jarema on 5/30/2017.
 */
class ContextJarTest extends GroovyTestCase{
    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6
    Map<Trait, Set<IndividualModel>> describedByTraits, notDescribedByTraits,
                                     indefiniteByTraits;
    def tr1, tr2, tr3
    def im1, model2
    int t0, t1, t2, t3, t4, t5
    Map<Formula, Set<BaseProfile>> bpmap1;
    Map<Formula, Set<BaseProfile>> bpmap2;
    Map<Formula, Set<BaseProfile>> bpmap3;
    SimpleFormula sformula1, sformula2, sformula3, sformula1neg ,sformula2neg

    Set<BaseProfile> bpset1 ,bpset2 ,bpset3, bpset4

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
        bp6 = new BaseProfile([describedByTraits, [(tr1): [model1, model6] as Set,
                                                   (tr4): [model1, model6] as Set],
                               indefiniteByTraits] as List, t4)
        sformula1 = new SimpleFormula(model1, tr2, false) // black(individualModel:id1)
        sformula1neg = new SimpleFormula(model1, tr2, true) // !black(individualModel:id1)
        sformula2 = new SimpleFormula(model6, tr1, false) // red(individualModel:id6)
        sformula2neg = new SimpleFormula(model6, tr1, true) // !red(individualModel:id6)
        sformula3 = new SimpleFormula(model5, tr4, true) // !warm(individualModel:id5)
        bpmap1 = new HashMap<Formula, Set<BaseProfile>>()
        bpmap2 = new HashMap<Formula, Set<BaseProfile>>()

        bpset1 = [bp1,bp2,bp3,bp4,bp5,bp6] as Set
        bpset2 = [bp3,bp6] as Set

        bpmap2.put(sformula1,bpset1)
        bpmap2.put(sformula1neg,bpset2)

        bpmap3 = new HashMap<Formula, Set<BaseProfile>>()

        bpset3 = [bp1,bp2,bp4,bp5,bp6] as Set
        bpset4 = [bp3,bp6] as Set

        bpmap3.put(sformula2,bpset3)
        bpmap3.put(sformula2neg,bpset4)

    }

    @Test
    void testPerformContextualisationFor3(){
        //Mode 2
        build()
        DistanceFunction f1 = new DistanceFunction1()
        ContextJar cj = new ContextJar(f1,2)
        cj.setMaxThreshold(12)
        assertEquals(12,cj.getMaxThreshold())

        //assertEquals(0,cj.performContextualisation(bpmap1).size())


        assertEquals(1,cj.performContextualisation(bpmap2).size())
        assertEquals(6,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(null,cj.performContextualisation(bpmap2).get(sformula1neg))
        cj.setMaxThreshold(6)
        assertEquals(6,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(null,cj.performContextualisation(bpmap2).get(sformula1neg))
        cj.setMaxThreshold(0)
        assertEquals(1,cj.performContextualisation(bpmap2).get(sformula1).size()) //Zbadac Zachowanie w tym przypadku
        assertEquals(null,cj.performContextualisation(bpmap2).get(sformula1neg))

        cj.setMaxThreshold(-1)
        assertEquals(0,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(null,cj.performContextualisation(bpmap2).get(sformula1neg))

        cj.setMaxThreshold(4)
        assertEquals(2,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(null,cj.performContextualisation(bpmap2).get(sformula1neg))

        assertEquals(null,cj.performContextualisation(bpmap3).get(sformula1))
        assertEquals(null,cj.performContextualisation(bpmap3).get(sformula1neg))

    }


    @Test
    void testPerformContextualisation(){
        build()

        DistanceFunction f1 = new DistanceFunction1()

        ContextJar cj = new ContextJar(f1,1)
        cj.setMaxThreshold(12)
        assertEquals(12,cj.getMaxThreshold())

        assertEquals(0,cj.performContextualisation(bpmap1).size())
        assertEquals(2,cj.performContextualisation(bpmap2).size())
        assertEquals(6,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(2,cj.performContextualisation(bpmap2).get(sformula1neg).size())
        cj.setMaxThreshold(0)
        assertEquals(2,cj.performContextualisation(bpmap2).size())
        assertEquals(0,cj.performContextualisation(bpmap2).get(sformula1).size())
        cj.setMaxThreshold(4)
        assertEquals(2,cj.performContextualisation(bpmap2).get(sformula1).size())
        cj.setMaxThreshold(1)
        assertEquals(1,cj.performContextualisation(bpmap2).get(sformula1).size())
        cj.setMaxThreshold(2)
        assertEquals(2,cj.performContextualisation(bpmap2).get(sformula1).size())
        cj.setMaxThreshold(6)
        assertEquals(2,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(0,cj.performContextualisation(bpmap2).get(sformula1neg).size())
        cj.setMaxThreshold(8)
        assertEquals(6,cj.performContextualisation(bpmap2).get(sformula1).size())
        assertEquals(2,cj.performContextualisation(bpmap2).get(sformula1neg).size())




        //bpmap3

        cj.setMaxThreshold(0)
        assertEquals(0,cj.performContextualisation(bpmap3).get(sformula2).size())
        assertEquals(0,cj.performContextualisation(bpmap3).get(sformula2neg).size())
        cj.setMaxThreshold(8)
        assertEquals(5,cj.performContextualisation(bpmap3).get(sformula2).size())
        assertEquals(2,cj.performContextualisation(bpmap3).get(sformula2neg).size())
        cj.setMaxThreshold(2)
        assertEquals(2,cj.performContextualisation(bpmap3).get(sformula2).size())
        assertEquals(0,cj.performContextualisation(bpmap3).get(sformula2neg).size())
        cj.setMaxThreshold(3)
        assertEquals(2,cj.performContextualisation(bpmap3).get(sformula2).size())
        assertEquals(0,cj.performContextualisation(bpmap3).get(sformula2neg).size())
        cj.setMaxThreshold(4)
        assertEquals(2,cj.performContextualisation(bpmap3).get(sformula2).size())
        assertEquals(0,cj.performContextualisation(bpmap3).get(sformula2neg).size())
        cj.setMaxThreshold(7)
        assertEquals(5,cj.performContextualisation(bpmap3).get(sformula2).size())
        assertEquals(2,cj.performContextualisation(bpmap3).get(sformula2neg).size())

    }


}
