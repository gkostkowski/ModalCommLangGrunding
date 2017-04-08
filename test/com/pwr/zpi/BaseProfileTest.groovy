package com.pwr.zpi

import org.junit.Test


/**
 * Created by Grzesiek on 2017-04-04.
 */
class BaseProfileTest extends GroovyTestCase {


/*

    BaseProfile build() {
        def t1 = new Trait<>("Kolor", "niebieski")
        def t2 = new Trait<>("Kolor", "czerwony")
        def t3 = new Trait<>("Rozmiar", 3)
        def t4 = new Trait<>("Ksztalt", "kwadrat")

        def o1 = new Observation(1, "podstawowy", [t1, t4, t3])
        def o2 = new Observation(2, "podstawowy", [t1, t4])
        def o3 = new Observation(3, "podstawowy", [t1, t2, t3])
        def o4 = new Observation(4, "podstawowy", [t1, t4])
        def o5 = new Observation(5, "podstawowy", [t1, t2, t3])
        def o6 = new Observation(6, "podstawowy", [t1, t4, t3])
        def o7 = new Observation(7, "podstawowy", [t1, t2])
        def bp = new BaseProfile([t1: [o1, o2, o3, o4, o5, o6, o7],
                                  t2: [o3, o5, o7],
                                  t3: [o1, o3, o5, o6]], 1)
        println bp

        return bp
    }

*/

    @Test
    void testGetByTraitState() {
//        build()

    }

}
