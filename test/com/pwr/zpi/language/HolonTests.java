package com.pwr.zpi.language;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.pwr.zpi.Agent;
import com.pwr.zpi.BPCollection;
import com.pwr.zpi.BarCode;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.Identifier;
import com.pwr.zpi.IndividualModel;
import com.pwr.zpi.ObjectType;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.exceptions.NotApplicableException;
import com.pwr.zpi.exceptions.NotConsistentDKException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
public class HolonTests 	{


	Trait t = new Trait("Czerwonosc");
	Trait t1 = new Trait("Niebiebieskosc");
	Trait t2 = new Trait("Seksapil");
	Trait t3 = new Trait("Brzmienie");
	Trait t4 = new Trait("Czerwonosc");

	@Test
	public void TraitTest() {
		Assertions.assertEquals(t.getName(),"Czerwonosc" );
		Assertions.assertEquals(t.equals("Czerwonosc"),true );
	}

	Identifier id1 = new BarCode("1");
	Identifier id2 = new BarCode("2");
	Identifier id3 = new BarCode("3");

	@Test
	public void IdentifierTest() {
		Assertions.assertEquals(id1.getIdNumber(),"1");
		Assertions.assertEquals(id1.equals(id1),true);
		Assertions.assertEquals(id1.equals(id2),false);
	}

	ObjectType ot1 = new ObjectType("ot1",Arrays.asList(t,t2,t3));
	ObjectType ot2 = new ObjectType("ot2",Arrays.asList(t,t3,t1));
	ObjectType ot3 = new ObjectType("ot3",Arrays.asList(t4,t,t3));

	@Test
	public void ObjectTypeTest() {
		Assertions.assertEquals(ot1.getTypeId(),"ot1");
		Assertions	}

	IndividualModel im1 = new IndividualModel(id1,ot1);
	IndividualModel im2 = new IndividualModel(id2,ot2);
	IndividualModel im3 = new IndividualModel(id3,ot3);

	@Test
	public void IndividualModelTest() {
		Assertions.assertEquals(im1.checkIfContainsTrait(t),true);
		Assertions.assertEquals(im1.checkIfContainsTrait(t1),false);
		Assertions.assertEquals(im1.checkIfContainsTraits(Arrays.asList(t,t3,t3)),true);
		Assertions.assertEquals(im2.getIdentifier(),id2);
	}



	@Test
	public void SimpleFormulaModelTest() throws InvalidFormulaException {
		Formula sf1 = new SimpleFormula(im1,t,false);
		//Formula sf3 = new SimpleFormula(im3,t3,false);

		Assertions.assertEquals(sf1.equals(sf1),true);
		Assertions.assertEquals(sf1.getTraits().get(0).getName(),t.getName());
		Assertions.assertEquals(sf1.getTraits(),Arrays.asList(t));
		Assertions.assertEquals(sf1.getType(), Formula.Type.SIMPLE_MODALITY);
		Assertions.assertEquals(sf1.getModel(), im1);
	}

	@Test
	public void ComplexFormulaTest() throws InvalidFormulaException {
		Formula cf1 = new ComplexFormula(im1,Arrays.asList(t,t2),LogicOperator.AND);
		Formula cf2 = new ComplexFormula(im2,Arrays.asList(t1,t3),LogicOperator.AND);
		Formula cf3 = new ComplexFormula(im3,Arrays.asList(t4,t3),LogicOperator.AND);
		//Formula sf3 = new SimpleFormula(im3,t3,false);

		Assertions.assertEquals(cf1.equals(cf1),true);
		Assertions.assertEquals(cf1.getModel(),im1);
		Assertions.assertEquals(cf2.getTraits(),Arrays.asList(t1,t3));
		Assertions.assertEquals(cf3.getType(),Formula.Type.MODAL_CONJUNCTION);
	}

	@Test
	public void GrounderTest() throws InvalidFormulaException, NotConsistentDKException {
		Formula sf1 = new SimpleFormula(im1,t,false);


		Agent a = new Agent();
		DistributedKnowledge dk = new DistributedKnowledge(a,sf1);

		//Formula formula, DistributedKnowledge dk,int timestamp

		//Grounder.determineFulfillmentDouble(dk,sf1);
	}


	@Test
	public void BinaryHolonTest() throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
		Formula sf1 = new SimpleFormula(im1,t,false);
		//Formula sf3 = new SimpleFormula(im3,t3,false);

		Agent a = new Agent();

		DistributedKnowledge dk = new DistributedKnowledge(a,sf1);
		Holon h1 = new BinaryHolon(dk);

		Assertions.assertEquals(h1.getKind(), Holon.HolonKind.Binary);
		Assertions.assertEquals(h1.getFormula(), sf1);
		Assertions.assertEquals(h1.getStrongest().getK(), true);
		Assertions.assertEquals(h1.getStrongest().getV(), 0.0);
		Assertions.assertEquals(h1.getWeakest().getK(), false);
		Assertions.assertEquals(h1.getWeakest().getV(), 0.0);

		BaseProfile bp = new BaseProfile(2);
		bp.addDescribedObservation(im1, t);
		Set<BaseProfile> setBP = new HashSet<BaseProfile>();
		setBP.add(bp);
		dk.setLM(setBP,sf1);
		Holon h2 = new BinaryHolon(dk);

		BPCollection bpc = new BPCollection();
		bpc.setTimestamp(2);
		bpc.addToMemory(bp);
		dk.setrelatedObservationsBase(bpc);

		Assertions.assertEquals(java.util.Optional.of(Grounder.checkEpistemicConditionsDouble(sf1, dk, 0)), 0.0);
	}

	@Test
	public void NonBinaryHolonTest() throws InvalidFormulaException, NotConsistentDKException, NotApplicableException {
		Formula cf1 = new ComplexFormula(im1,Arrays.asList(t,t2),LogicOperator.AND);
		Formula cf2 = new ComplexFormula(im2,Arrays.asList(t1,t3),LogicOperator.AND);
		Formula cf3 = new ComplexFormula(im3,Arrays.asList(t4,t3),LogicOperator.AND);

		Agent a = new Agent();

		BPCollection bpc = new BPCollection();
		bpc.setTimestamp(2);
		BaseProfile bp = new BaseProfile(2);
		bp.addDescribedObservation(im1, t);
		bpc.addToMemory(bp);
		a.setKnowledgeBase(bpc);

		DistributedKnowledge dk = new DistributedKnowledge(a,cf1);
		DistributedKnowledge dk2 = new DistributedKnowledge(a,cf2);
		DistributedKnowledge dk3 = new DistributedKnowledge(a,cf3);

		Holon h1 = new NonBinaryHolon(a,dk);
		Holon h2= new NonBinaryHolon(a,dk2);
		Holon h3 = new NonBinaryHolon(a,dk3);


	}
}