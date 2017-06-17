package com.pwr.zpi.linguistic

import com.pwr.zpi.language.Formula
import com.pwr.zpi.language.ModalOperator
import com.pwr.zpi.language.SimpleFormula
import com.pwr.zpi.language.Trait
import com.pwr.zpi.core.semantic.IndividualModel
import com.pwr.zpi.core.semantic.ObjectType
import com.pwr.zpi.core.semantic.QRCode
import org.junit.Test

/**
 * Created by Weronika on 24.05.2017.
 */
class SimpleStatementTest extends GroovyTestCase {

    def objectType
    def model1
    def trait1, trait2, trait3;
    def sf1, sf2

    void setUp()
    {
        trait1 = new Trait("Red")
        objectType = new ObjectType("ID", [trait1, trait2, trait3])
        def id = new QRCode("id1")
        model1 = new IndividualModel(id, objectType)
        sf1 = new SimpleFormula(model1, trait1, false)
        sf2 = new SimpleFormula(model1, trait1, true)
    }

    @Test
    void testGenerateSentence()
    {
        Map<Formula, ModalOperator> map = new HashMap<>()
        map.put(sf1, ModalOperator.KNOW)
        def ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("yes, i know that hyzio is red")
        map = new HashMap<>()
        map.put(sf2, ModalOperator.KNOW)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("No, i know hyzio is not red")
        map = new HashMap<>()
        map.put(sf1, ModalOperator.BEL)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("yes, i believe hyzio is red")
        map = new HashMap<>()
        map.put(sf2, ModalOperator.BEL)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("Well, I believe Hyzio is not Red, I do not know enough about the asked state")
        map = new HashMap<>()
        map.put(sf2, ModalOperator.BEL)
        map.put(sf1, ModalOperator.POS)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("I think it is possible, that Hyzio is Red, however I rather believe Hyzio is not Red")
        map = new HashMap<>()
        map.put(sf1, ModalOperator.BEL)
        map.put(sf2, ModalOperator.POS)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("Yes, I believe Hyzio is Red, but it possible that it is not")
        map = new HashMap<>()
        map.put(sf2, ModalOperator.POS)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("I do not know enough about asked state, I only think it is possible that Hyzio is not Red")
        map = new HashMap<>()
        map.put(sf1, ModalOperator.POS)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("It is possible that Hyzio is Red")
        map = new HashMap<>()
        map.put(sf1, ModalOperator.POS)
        map.put(sf2, ModalOperator.POS)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("It is possible that Hyzio is Red, but it is also possible that it is not")
        map = new HashMap<>()
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("I really do not know what to say about it")
        map = new HashMap<>()
        map.put(sf1, ModalOperator.POS)
        map.put(sf2, ModalOperator.BEL)
        ss = new SimpleStatement(sf1, map, "Hyzio")
        assert ss.generateStatement().equalsIgnoreCase("I think it is possible, that Hyzio is Red, however I rather believe Hyzio is not Red")


    }







}
