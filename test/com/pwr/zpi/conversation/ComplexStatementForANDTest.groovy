package com.pwr.zpi.conversation

import com.pwr.zpi.Agent
import com.pwr.zpi.IndividualModel
import com.pwr.zpi.ObjectType
import com.pwr.zpi.QRCode
import com.pwr.zpi.Trait
import com.pwr.zpi.language.ComplexFormula


/**

 * Created by Weronika on 14.05.2017.
 */
class ComplexStatementForANDTest extends GroovyTestCase {

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

    @Test
    void testSentence()
    {
        build();
        Question question = new Question("Is Zenek not red and not soft", agent)
        ComplexFormula complexFormula = question.getFormula()
        ComplexStatementForAND cs = new ComplexStatementForAND(complexFormula, question.name, 0.03, 0.01, 0.19, 0.79)
        print(cs.generateStatement())
    }



}
