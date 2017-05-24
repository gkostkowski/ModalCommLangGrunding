package com.pwr.zpi.conversation

import com.pwr.zpi.Agent
import com.pwr.zpi.semantic.IndividualModel
import com.pwr.zpi.semantic.ObjectType
import com.pwr.zpi.semantic.QRCode
import com.pwr.zpi.language.Trait
import com.pwr.zpi.language.SimpleFormula

/**
 * Created by Weronika on 13.05.2017.
 */
class SimpleStatementTest extends GroovyTestCase {


    def agent;
    def objectType;
    def model1, model2
    def trait1, trait2, trait3;

    void build()
    {
        trait1 = new Trait("Red")
        trait2 = new Trait("Black")
        trait3 = new Trait("Soft")
        objectType = new ObjectType("ID", [trait1, trait2, trait3])
        def id = new QRCode("id1");
        def id2 = new QRCode("id2")
        model1 = new IndividualModel(id, objectType)
        model2 = new IndividualModel(id2, objectType)
        agent = new Agent()
        agent.getModels().add(model1)
        agent.getModels().add(model2)
        agent.getModels().addNameToModel(id, "Zenek")
        agent.getModels().addNameToModel(id2, "Pepe pan dziobak")
    }

    @org.junit.Test
    void testGenerateSentence()
    {
        build();
        Question question = new Question("Is Zenek red", agent);
        SimpleFormula sf = question.getFormula()
        SimpleStatement ss = new SimpleStatement(sf, question.name, 0, 1);
        print(ss.generateStatement());
    }


}
