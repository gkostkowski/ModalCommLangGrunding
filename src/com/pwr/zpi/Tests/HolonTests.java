import java.util.Arrays;

import org.junit.Test;

import com.pwr.zpi.Agent;
import com.pwr.zpi.BPCollection;
import com.pwr.zpi.BarCode;
import com.pwr.zpi.BaseProfile;
import com.pwr.zpi.Identifier;
import com.pwr.zpi.IndividualModel;
import com.pwr.zpi.ObjectType;
import com.pwr.zpi.Trait;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.language.BinaryHolon;
import com.pwr.zpi.language.Formula;
import com.pwr.zpi.language.Holon;
import com.pwr.zpi.language.SimpleFormula;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class HolonTests 	{
	
	
	Trait t = new Trait("Czerwonosc");
    Trait t1 = new Trait("Niebiebieskosc");
    Trait t2 = new Trait("Seksapil");
    Trait t3 = new Trait("Brzmienie");
    Trait t4 = new Trait("Czerwonosc");
    
    
    @Test
    public void TraitTest() {
    	Assert.assertEquals(t.getName(),"Czerwonosc" );
    	Assert.assertEquals(t.equals(t4),true );
    	Assert.assertEquals(t.equals("Czerwonosc"),true );
    }
    
    Identifier id1 = new BarCode("1");
    Identifier id2 = new BarCode("2");
    Identifier id3 = new BarCode("3");
    
    @Test
    public void IdentifierTest() {
    	Assert.assertEquals(id1.getIdNumber(),"1");
    	Assert.assertEquals(id1.equals(id1),true);
    	Assert.assertEquals(id1.equals(id2),false);
    }
    
    ObjectType ot1 = new ObjectType("ot1",Arrays.asList(t,t2,t3));
    ObjectType ot2 = new ObjectType("ot2",Arrays.asList(t,t3,t1));
    ObjectType ot3 = new ObjectType("ot3",Arrays.asList(t4,t,t3));
    
    @Test
    public void ObjectTypeTest() {
    	Assert.assertEquals(ot1.getTypeId(),"ot1");
    	Assert.assertEquals(ot2.getTraits(),Arrays.asList(t,t3,t1));
    }
    
    IndividualModel im1 = new IndividualModel(id1,ot1);
    IndividualModel im2 = new IndividualModel(id2,ot2);
    IndividualModel im3 = new IndividualModel(id3,ot3);
    
    @Test
    public void IndividualModelTest() {
    	Assert.assertEquals(im1.checkIfContainsTrait(t),true);
    	Assert.assertEquals(im1.checkIfContainsTrait(t1),false);
    	Assert.assertEquals(im1.checkIfContainsTraits(Arrays.asList(t,t3,t3)),true);
    	Assert.assertEquals(im2.getIdentifier(),id2);
    }
    
    
    	
    @Test
    public void SimpleFormulaModelTest() throws InvalidFormulaException {
    	Formula sf1 = new SimpleFormula(im1,t,false);
    	//Formula sf3 = new SimpleFormula(im3,t3,false);
    	
    	Assert.assertEquals(sf1.equals(sf1),true);
    	Assert.assertEquals(sf1.getTraits().get(0).getName(),t.getName());
    	Assert.assertEquals(sf1.getTraits(),Arrays.asList(t));
    	Assert.assertEquals(sf1.getType(), Formula.Type.SIMPLE_MODALITY);
    	Assert.assertEquals(sf1.getModel(), im1);
    }
    @Test
    public void BinaryHolonTest() throws InvalidFormulaException {
    	Formula sf1 = new SimpleFormula(im1,t,false);
    	//Formula sf3 = new SimpleFormula(im3,t3,false);
    	
    	Agent a = new Agent();
    	
    	BPCollection bpc = new BPCollection();
    	bpc.setTimestamp(2);
    	BaseProfile bp = new BaseProfile(2);
    	bp.addDescribedObservation(im1, t);
    	bpc.addToMemory(bp, BPCollection.MemoryType.LM, 2);
    	a.setKnowledgeBase(bpc);
    	
    	Holon h1 = new BinaryHolon(sf1,a,2);
    	
    	Assert.assertEquals(h1.getKind(), Holon.HolonKind.Binary);
    	Assert.assertEquals(h1.getFormula(), sf1);
    	Assert.assertEquals(h1.getStrongest().getK(), true);
    	Assert.assertEquals(h1.getStrongest().getV(), 0.5);
    }
}
