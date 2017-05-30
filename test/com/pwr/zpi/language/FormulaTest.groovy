import com.pwr.zpi.episodic.BaseProfile
import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.language.LogicOperator
import com.pwr.zpi.language.SimpleFormula
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode

/**
 * Created by Grzesiek on 2017-05-30.
 */
class FormulaTest extends GroovyTestCase {

    BaseProfile bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8, bp9
    int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9

    def im1, model1, model2
    def tr1, tr2, tr3

    SimpleFormula sformula1, sformula2
    ComplexFormula cformulaConj1, cformulaConj2, cformulaConj3, cformulaDisj1, cformulaDisj2, cformulaDisj3

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


        def formulaIM = im1

        sformula1 = new SimpleFormula(formulaIM, tr2, false); // is white
        sformula2 = new SimpleFormula(formulaIM, tr1, true); // is not red

        cformulaConj1 = new ComplexFormula(formulaIM, [tr3, tr2],
                [State.IS, State.IS_NOT], LogicOperator.AND) //Is hyzio blinking and not white

        cformulaConj2 = new ComplexFormula(formulaIM, [tr1, tr3],
                [State.IS, State.IS], LogicOperator.AND) //Is hyzio red and blinking
        cformulaConj3 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS_NOT], LogicOperator.AND) //is hyzio not red and not white
        cformulaDisj1 = new ComplexFormula(formulaIM, [tr3, tr2],
                [State.IS, State.IS_NOT], LogicOperator.OR) //Is hyzio blinking OR not white

        cformulaDisj2 = new ComplexFormula(formulaIM, [tr1, tr3],
                [State.IS, State.IS], LogicOperator.OR) //Is hyzio red OR blinking
        cformulaDisj3 = new ComplexFormula(formulaIM, [tr1, tr2],
                [State.IS_NOT, State.IS_NOT], LogicOperator.OR) //is hyzio not red OR not white
    }

    void testIsFormulaFulfilled() {

        //SIMPLE
        assertEquals(false, sformula1.isFormulaFulfilled(bp1))
        assertEquals(true, sformula1.isFormulaFulfilled(bp8))
        assertEquals(false, sformula1.isFormulaFulfilled(bp9))

        assertEquals(false, sformula2.isFormulaFulfilled(bp1))
        assertEquals(true, sformula2.isFormulaFulfilled(bp8))
        //assertEquals(true, sformula2.isFormulaFulfilled(bp9))  //todo true czy false

        //CONJUNCTIONS
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp1))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp2))
        assertEquals(true, cformulaConj1.isFormulaFulfilled(bp3))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp4))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp5))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp6))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp7))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp8))
        assertEquals(false, cformulaConj1.isFormulaFulfilled(bp9))

        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp1))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp2))
        assertEquals(true, cformulaConj2.isFormulaFulfilled(bp3))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp4))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp5))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp6))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp7))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp8))
        assertEquals(false, cformulaConj2.isFormulaFulfilled(bp9))

        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp1))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp2))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp3))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp4))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp5))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp6))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp7))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp8))
        assertEquals(false, cformulaConj3.isFormulaFulfilled(bp9))

        //DISJUNCTIONS
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp1))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp2))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp3))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp4))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp5))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp6))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp7))
        assertEquals(false, cformulaDisj1.isFormulaFulfilled(bp8))
        assertEquals(true, cformulaDisj1.isFormulaFulfilled(bp9))

        assertEquals(true, cformulaDisj2.isFormulaFulfilled(bp1))
        assertEquals(true, cformulaDisj2.isFormulaFulfilled(bp2))
        assertEquals(true, cformulaDisj2.isFormulaFulfilled(bp3))
        assertEquals(true, cformulaDisj2.isFormulaFulfilled(bp4))
        assertEquals(false, cformulaDisj2.isFormulaFulfilled(bp5))
        assertEquals(false, cformulaDisj2.isFormulaFulfilled(bp6))
        assertEquals(true, cformulaDisj2.isFormulaFulfilled(bp7))
        assertEquals(false, cformulaDisj2.isFormulaFulfilled(bp8))
        assertEquals(true, cformulaDisj2.isFormulaFulfilled(bp9))

        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp1))
        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp2))
        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp3))
        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp4))
        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp5))
        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp6))
        assertEquals(false, cformulaDisj3.isFormulaFulfilled(bp7))
        assertEquals(true, cformulaDisj3.isFormulaFulfilled(bp8))
        assertEquals(false, cformulaDisj3.isFormulaFulfilled(bp9))
    }
}
