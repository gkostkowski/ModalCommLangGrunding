package com.pwr.zpi.linguistic

import com.pwr.zpi.language.ModalOperator
import com.pwr.zpi.language.SimpleFormula
import com.pwr.zpi.language.Trait
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode
import org.junit.Test

/**
 * Created by Weronika on 24.05.2017.
 */
class SimpleStatementTest extends GroovyTestCase {

    def objectType
    def model1
    def trait1, trait2, trait3;
    def sf1, sf2

    void build()
    {
        trait1 = new Trait("Red")
        objectType = new ObjectType("ID", [trait1, trait2, trait3])
        def id = new QRCode("id1")
        model1 = new IndividualModel(id, objectType)
        sf1 = new SimpleFormula(model1, trait1, true)
        sf2 = new SimpleFormula(model1, trait1, false)
    }

    @Test
    void testGenerateSentence()
    {
        build()
        def map = fillMap(ModalOperator.KNOW, null)
        def ss = new SimpleStatement(sf2, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("yes, i know that hyzio is red")
        map = fillMap(null, ModalOperator.KNOW)
        ss = new SimpleStatement(sf2, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("No, i know hyzio is not red")
    }







}
