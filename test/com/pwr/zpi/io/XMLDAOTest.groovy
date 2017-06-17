package com.pwr.zpi.io

import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.util.GenericObject

//package io

import org.junit.Test

/**
 * Created by Grzesiek on 2017-04-08.
 */
class XMLDAOTest extends GroovyTestCase {
/*
    @Test
    void readTypeDefFileTest() {
        XMLDAO xmldao = new XMLDAO();
        def res = xmldao.loadTypesDefinitions();
        assertNotNull(res)
        println res
    }
*/
    @Test
    void test() {
        XMLDAO xmldao = new XMLDAO()

        def filepath = makePath()

        def res = xmldao.loadTypesDefinitions(filepath);
        assertNotNull res


        def cl = ((List<ObjectType>)res).get(1).traits.get(1).getValueType()
        def initVal = 0
        GenericObject obj2 = new GenericObject<>(cl, initVal);
        obj2.setValue(4);
        println obj2.getObj().getClass().getCanonicalName()

        cl = ((List<ObjectType>)res).get(0).traits.get(0).getValueType()
        initVal = 0
        GenericObject obj3 = new GenericObject<>(cl, initVal);
        obj3.setValue("")
        println obj3.getObj().getClass().getCanonicalName()

        cl = ((List<ObjectType>)res).get(1).traits.get(2).getValueType()
        initVal = 0
        GenericObject obj4 = new GenericObject<>(cl, initVal);
        obj4.setValue(false)
        println obj4.getObj().getClass().getCanonicalName()

        assert obj2.getObj() instanceof Integer
        assert obj3.getObj() instanceof String
        assert obj4.getObj() instanceof Boolean

    }

    String makePath() {
        def currDir = System.getProperty("user.dir")
        String [] parts = currDir.split("\\\\");
        parts = parts.dropRight(1)
        def path = parts.join("\\")+ '\\config\\types_def.xml'
    }
}
