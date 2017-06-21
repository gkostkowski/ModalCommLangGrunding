package com.pwr.zpi.io

import com.pwr.zpi.core.Agent
import com.pwr.zpi.core.memory.episodic.Observation
import com.pwr.zpi.core.memory.semantic.identifiers.QRCode
import com.pwr.zpi.language.Trait
import org.junit.Test

/**
 * Created by Mateo on 17.05.2017.
 */
class DatabaseAOTest extends GroovyTestCase {

    def qrCodes, traits, observations
    def obs1, obs2, obs3, obs4
    Agent agent
    DatabaseAO database

    void build(){
        qrCodes = [new QRCode("0124"), new QRCode("02442"), new QRCode("01442")]
        traits = [new Trait("Red"), new Trait("White"), new Trait("Blinking")]

        def map1 = [:]; map1.put(traits[0], true); map1.put(traits[1], true)
        obs1 = new Observation(qrCodes[0], map1, 3)

        def map2 = [:]; map2.put(traits[2], true)
        obs2 = new Observation(qrCodes[1], map2, 3)

        def map3 = [:]; map2.put(traits[2], true)
        obs3 = new Observation(qrCodes[2], map3, 3)

        def map4 = [:]; map2.put(traits[0], false)
        obs4 = new Observation(qrCodes[2], map4, 3)

        agent = new Agent()
        database = agent.getDatabase()
    }

    @Test
    void test_continuous(){
        build()
        assertNotNull(agent)
        assertNotNull(database)


        database.addNewObservation(obs1)

    }



}
