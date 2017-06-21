package com.pwr.zpi.linguistic

import com.pwr.zpi.core.Agent
import com.pwr.zpi.core.memory.holons.context.builders.ConcreteContextBuilder
import com.pwr.zpi.core.memory.holons.context.contextualisation.Contextualisation
import com.pwr.zpi.core.memory.holons.context.contextualisation.FilteringContextualisation
import com.pwr.zpi.core.memory.holons.context.measures.NormalisedDistance
import com.pwr.zpi.core.memory.holons.context.selectors.LatestSelector
import com.pwr.zpi.core.memory.semantic.IndividualModel
import com.pwr.zpi.core.memory.semantic.ObjectType
import com.pwr.zpi.core.memory.semantic.identifiers.QRCode
import com.pwr.zpi.language.State
import com.pwr.zpi.language.Trait
import com.pwr.zpi.language.ComplexFormula
import com.pwr.zpi.language.LogicOperator
import com.pwr.zpi.language.SimpleFormula
import org.junit.Test

/**
 * Created by Weronika on 07.05.2017.
 */


class QuestionTest extends GroovyTestCase {


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
        Contextualisation latestContext= new FilteringContextualisation(new ConcreteContextBuilder(), new LatestSelector(),
                new NormalisedDistance(0.5));
        agent = new Agent.AgentBuilder()
                .contextualisation(latestContext)
                .build()
        agent.getModels().add(model1)
        agent.getModels().add(model2)
        agent.getModels().addNameToModel(id, "Zenek")
        agent.getModels().addNameToModel(id2, "Pepe pan dziobak")
    }

    @Test
    void testGettingFormula()
    {
        build()
        def question1 = new Question("Is Zenek red", agent);
        assert question1.getFormula().equals(new SimpleFormula(model1, trait1, false))
        question1 = new Question("Is zenek black", agent);
        assert question1.getFormula().equals(new SimpleFormula(model1, trait2, false))
        question1 = new Question("Is pepe pan dziobak not black", agent)
        assert question1.getFormula().equals(new SimpleFormula(model2, trait2, true))
        question1 = new Question("Is pepe pan dziobak not black and soft", agent)
        assert question1.getFormula().equals(new ComplexFormula(model2, [trait2, trait3], [State.IS_NOT, State.IS], LogicOperator.AND))
        question1 = new Question("Is pepe pan dziobak black or soft", agent)
        assert  question1.getFormula().equals(new ComplexFormula(model2, [trait2, trait3], [State.IS, State.IS], LogicOperator.OR))
        question1 = new Question("Is pepe pan dziobak either black or soft", agent)
        assert  question1.getFormula().equals(new ComplexFormula(model2, [trait2, trait3], [State.IS, State.IS], LogicOperator.XOR))
        question1 = new Question("Agent Smith Is pepe pan dziobak either black or soft", agent)
        assert  question1.getFormula().equals(new ComplexFormula(model2, [trait2, trait3], [State.IS, State.IS], LogicOperator.XOR))
        question1 = new Question("Is Zenek not black or not soft", agent)
        assert question1.getFormula().equals(new ComplexFormula(model1, [trait2, trait3], [State.IS_NOT, State.IS_NOT], LogicOperator.OR))
        question1 = new Question("Is Zenek black or soft", agent)
        assert question1.getFormula().equals(new ComplexFormula(model1, [trait2, trait3], [State.IS, State.IS], LogicOperator.OR))

    }

    @Test
    void testWrongName()
    {
        build()
        def question = new Question("Is Benek red", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is pepe pan dzioba", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is pepe red", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is", agent)
        shouldFail {question.getFormula()}
    }

    @Test
    void testWrongTraits()
    {
        def question = new Question("Is Zenek white", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is Zenek blue and red", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is Zenek red and blue", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is Zenek red and not blue", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is Zenek white and", agent)
        shouldFail {question.getFormula()}
    }

    @Test
    void testWrongOperator()
    {
        def question = new Question("Is Zenek red ond black", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is pepe pan dziobak either black or not soft", agent)
        shouldFail {question.getFormula()}
        question = new Question("Is Zenek either red",agent)
        shouldFail {question.getFormula()}
    }

}
