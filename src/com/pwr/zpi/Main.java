package com.pwr.zpi;

import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;
import com.pwr.zpi.language.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.pwr.zpi.exceptions.InvalidFormulaException;
import com.pwr.zpi.io.XMLDAO;
import com.pwr.zpi.language.*;

import java.util.*;

import static com.pwr.zpi.Agent.ObjectTypeCollection;

public class Main {

    private static final int INIT_TIME = 1;
    private static final int CURR_TIME= 5;

    public static void main(String[] args) throws InvalidFormulaException {
        Agent agent1 = new Agent();
        //ccztytanie pliku kon
        //grzkos {
        XMLDAO xmldao = new XMLDAO();
        Collection<ObjectType> objectTypes = xmldao.loadTypesDefinitions(Utils.makePath());

        // }grzkos
        // ladowanie obiektow typow
        //obserwacje + zaladowanie do profili bazowych <-- agent

        List<String> dColour = new ArrayList<String>(Arrays.asList("Black", "White", "Red"));
        TraitSignature<String> tColour = new TraitSignature<String>("Colour", "String", dColour);
        List<String> dShape = new ArrayList<String>(Arrays.asList("Square", "Round"));
        TraitSignature<String> tShape = new TraitSignature<String>("Shape", "String", dShape);
        List<String> dBlinking = new ArrayList<String>(Arrays.asList("true", "false"));
        TraitSignature<Boolean> tBlinking = new TraitSignature<Boolean>("Blinking", "Boolean", dBlinking);
        List<String> dNoOfWalls = new ArrayList<String>(Arrays.asList("2", "3", "4"));
        TraitSignature<Integer> tNoOfWalls = new TraitSignature<Integer>("NoOfWalls", "Integer", dNoOfWalls);
        ObjectTypeCollection.add(new ObjectType("01", new ArrayList<TraitSignature>(Arrays.asList(tColour, tShape, tBlinking))));
        ObjectTypeCollection.add(new ObjectType("02", new ArrayList<TraitSignature>(Arrays.asList(tColour, tNoOfWalls, tBlinking))));

        Identifier id1 = new QRCode("01555");
        Identifier id2 = new BarCode("01555");
        Identifier id3 = new BarCode("02555");

        Trait<String,String> o1t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o1t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o1t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1obs1 = new HashSet<Trait>(Arrays.asList(o1t,o1t1,o1t2));
        Observation o1obs1 = new Observation(id1,traitsyo1obs1,1);

        Trait<String,String> o2t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o2t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o2t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1obs2 = new HashSet<Trait>(Arrays.asList(o2t,o2t1,o2t2));
        Observation o1obs2 = new Observation(id1,traitsyo1obs2,2);

        Trait<String,String> o3t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o3t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o3t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1obs3 = new HashSet<Trait>(Arrays.asList(o3t,o3t1,o3t2));
        Observation o1obs3 = new Observation(id1,traitsyo1obs3,3);

        Trait<String,String> o4t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o4t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o4t2 = new Trait<String,Boolean>("Blinking",null);
        Set<Trait> traitsyo1obs4 = new HashSet<Trait>(Arrays.asList(o4t,o4t1,o4t2));
        Observation o1obs4 = new Observation(id1,traitsyo1obs4,4);

        Trait<String,String> o5t = new Trait<String,String>("Colour","Black");
        Trait<String,String> o5t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o5t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1obs5 = new HashSet<Trait>(Arrays.asList(o5t,o5t1,o5t2));
        Observation o1obs5 = new Observation(id1,traitsyo1obs5,5);

        //UPDATE 1

        Trait<String,String> o6t = new Trait<String,String>("Colour","Black");
        Trait<String,String> o6t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o6t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1obs6 = new HashSet<Trait>(Arrays.asList(o6t,o6t1,o6t2));
        Observation o1obs6 = new Observation(id1,traitsyo1obs6,6);

        Trait<String,String> o7t = new Trait<String,String>("Colour","Black");
        Trait<String,String> o7t1 = new Trait<String,String>("Shape",null);
        Trait<String,Boolean> o7t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1obs7 = new HashSet<Trait>(Arrays.asList(o7t,o7t1,o7t2));
        Observation o1obs7 = new Observation(id1,traitsyo1obs7,7);

        Trait<String,String> o8t = new Trait<String,String>("Colour","White");
        Trait<String,String> o8t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o8t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1obs8 = new HashSet<Trait>(Arrays.asList(o8t,o8t1,o8t2));
        Observation o1obs8 = new Observation(id1,traitsyo1obs8,8);

        Trait<String,String> o9t = new Trait<String,String>("Colour","White");
        Trait<String,String> o9t1 = new Trait<String,String>("Shape","Round");
        Trait<String,Boolean> o9t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1obs9 = new HashSet<Trait>(Arrays.asList(o9t,o9t1,o9t2));
        Observation o1obs9 = new Observation(id1,traitsyo1obs9,9);

        Trait<String,String> o10t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o10t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o10t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1obs10 = new HashSet<Trait>(Arrays.asList(o10t,o10t1,o10t2));
        Observation o1obs10 = new Observation(id1,traitsyo1obs10,10);

        //UPDATE 2

        Trait<String,String> o11t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o11t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o11t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo2obs11 = new HashSet<Trait>(Arrays.asList(o11t,o11t1,o11t2));
        Observation o2obs11 = new Observation(id2,traitsyo2obs11,11);

        Trait<String,String> o12t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o12t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o12t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo2obs12 = new HashSet<Trait>(Arrays.asList(o12t,o12t1,o12t2));
        Observation o2obs12 = new Observation(id2,traitsyo2obs12,12);

        Trait<String,String> o13t = new Trait<String,String>("Colour","White");
        Trait<String,String> o13t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o13t2 = new Trait<String,Boolean>("Blinking",null);
        Set<Trait> traitsyo2obs13 = new HashSet<Trait>(Arrays.asList(o13t,o13t1,o13t2));
        Observation o2obs13 = new Observation(id2,traitsyo2obs13,13);

        Trait<String,String> o14t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o14t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o14t2 = new Trait<String,Boolean>("Blinking",true);
        Trait<String,String> o14t3 = new Trait<String,String>("Colour",null);
        Trait<String,String> o14t4 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o14t5 = new Trait<String,Boolean>("Blinking",false);
        Trait<String,String> o14t6 = new Trait<String,String>("Colour","Red");
        Trait<String,Integer> o14t7 = new Trait<String,Integer>("Shape",3);
        Trait<String,Boolean> o14t8 = new Trait<String,Boolean>("Blinking",false);

        Set<Trait> traitsyo1obs14 = new HashSet<Trait>(Arrays.asList(o14t,o14t1,o14t2));
        Observation o1obs14 = new Observation(id1,traitsyo1obs14,14);

        Set<Trait> traitsyo2obs14 = new HashSet<Trait>(Arrays.asList(o14t3,o14t4,o14t5));
        Observation o2obs14 = new Observation(id2,traitsyo2obs14,14);

        Set<Trait> traitsyo3obs14 = new HashSet<Trait>(Arrays.asList(o14t6,o14t7,o14t8));
        Observation o3obs14 = new Observation(id3,traitsyo3obs14,14);

        // UPDATE 3

        Trait<String,String> o15t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o15t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o15t2 = new Trait<String,Boolean>("Blinking",null);
        Set<Trait> traitsyo2obs15 = new HashSet<Trait>(Arrays.asList(o15t,o15t1,o15t2));
        Observation o2obs15 = new Observation(id2,traitsyo2obs15,15);

        Trait<String,String> o16t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o16t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o16t2 = new Trait<String,Boolean>("Blinking",false);
        Trait<String,String> o16t3 = new Trait<String,String>("Colour","Red");
        Trait<String,Integer> o16t4 = new Trait<String,Integer>("NoOfWalls",3);
        Trait<String,Boolean> o16t5 = new Trait<String,Boolean>("Blinking",false);

        Set<Trait> traitsyo2obs16 = new HashSet<Trait>(Arrays.asList(o16t,o16t1,o16t2));
        Observation o2obs16 = new Observation(id2,traitsyo2obs16,16);

        Set<Trait> traitsyo3obs16 = new HashSet<Trait>(Arrays.asList(o16t3,o16t4,o16t5));
        Observation o3obs16 = new Observation(id3,traitsyo3obs16,16);

        Trait<String,String> o17t = new Trait<String,String>("Colour","White");
        Trait<String,String> o17t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o17t2 = new Trait<String,Boolean>("Blinking",true);
        Trait<String,String> o17t3 = new Trait<String,String>("Colour","White");
        Trait<String,Integer> o17t4 = new Trait<String,Integer>("NoOfWalls",3);
        Trait<String,Boolean> o17t5 = new Trait<String,Boolean>("Blinking",false);

        Set<Trait> traitsyo2obs17 = new HashSet<Trait>(Arrays.asList(o17t,o17t1,o17t2));
        Observation o2obs17 = new Observation(id2,traitsyo2obs17,17);

        Set<Trait> traitsyo3obs17 = new HashSet<Trait>(Arrays.asList(o17t3,o17t4,o17t5));
        Observation o3obs17 = new Observation(id3,traitsyo3obs17,17);

        Trait<String,String> o18t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o18t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o18t2 = new Trait<String,Boolean>("Blinking",true);
        Trait<String,String> o18t3 = new Trait<String,String>("Colour","Red");
        Trait<String,Integer> o18t4 = new Trait<String,Integer>("Shape",2);
        Trait<String,Boolean> o18t5 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo2o18 = new HashSet<Trait>(Arrays.asList(o18t,o18t1,o18t2,o18t3,o18t4,o18t5));
        Observation o2o18 = new Observation(null,traitsyo2o18);

        BPCollection bpCollection = new BPCollection();
        BaseProfile[] baseProfilesArr = new BaseProfile[CURR_TIME];
        int iterations = CURR_TIME - INIT_TIME;
        for (int i= 0; i <= iterations; i++) {
            baseProfilesArr[i] = new BaseProfile(i);
        }
        Set<Trait> traitsyo2obs18 = new HashSet<Trait>(Arrays.asList(o18t,o18t1,o18t2,o18t3,o18t4,o18t5));
        Observation o2obs18 = new Observation(id2,traitsyo2obs18,18);

        Set<Trait> traitsyo3obs18 = new HashSet<Trait>(Arrays.asList(o18t,o18t1,o18t2,o18t3,o18t4,o18t5));
        Observation o3obs18 = new Observation(id3,traitsyo3obs18,18);

        Set<Observation> observations = new HashSet<>(Arrays.asList(o1obs1, o1obs2, o1obs3, o1obs4, o1obs5, o1obs6,
                o1obs7, o1obs8, o1obs9, o1obs10, o1obs14, o2obs11, o2obs12, o2obs13, o2obs14, o2obs15, o2obs16,
                o2obs17, o2obs18, o3obs14, o3obs16, o3obs17, o3obs18));
        agent1.addObservations(observations); //dodaje przy okazji odpowiednie modele indywiduowe

        Trait colour = o1t, shape = o1t1, blinking = o1t2;
        //Update 1
        Formula form1 = new SimpleFormula(o1obs1,new Trait<String,String>("Colour","Red"),true);
        Formula form2 = new SimpleFormula(o1obs2,new Trait<String,String>("Colour","Black"),true);
        Formula form3 = new SimpleFormula(o1obs3,new Trait<String,String>("Shape","Round"),true);
        Formula form4 = new SimpleFormula(o1obs3,new Trait<String,String>("Blinking","Square"),true);

        baseProfilesArr[0].addDescribedObservation(o1obs1,colour);
        baseProfilesArr[0].addDescribedObservation(o1obs1,shape);
        baseProfilesArr[0].addNotDescribedObservation(o1obs1,blinking);
        bpCollection.addToMemory(baseProfilesArr[0], BPCollection.MemoryType.LM, 1);

        baseProfilesArr[1].addDescribedObservation(o1obs2,colour);
        baseProfilesArr[1].addDescribedObservation(o1obs2,shape);
        baseProfilesArr[1].addNotDescribedObservation(o1obs2,blinking);
        bpCollection.addToMemory(baseProfilesArr[1], BPCollection.MemoryType.LM, 2);

        baseProfilesArr[2].addDescribedObservation(o1obs2,colour);
        baseProfilesArr[2].addDescribedObservation(o1obs2,shape);
        baseProfilesArr[2].addDescribedObservation(o1obs2,blinking);
        bpCollection.addToMemory(baseProfilesArr[2], BPCollection.MemoryType.LM, 3);

        baseProfilesArr[3].addDescribedObservation(o1obs2,colour);
        baseProfilesArr[3].addDescribedObservation(o1obs2,shape);
        bpCollection.addToMemory(baseProfilesArr[3], BPCollection.MemoryType.WM, 4);

        baseProfilesArr[4].addDescribedObservation(o1obs1,colour);
        baseProfilesArr[4].addDescribedObservation(o1obs1,shape);
        baseProfilesArr[4].addNotDescribedObservation(o1obs1,blinking);
        bpCollection.addToMemory(baseProfilesArr[4], BPCollection.MemoryType.WM, 5);

        agent1 = new Agent(bpCollection);

        Trait redColour =o1t;

        /*Set<Trait> traitsy1 = new HashSet<Trait>(Arrays.asList(t,t1,t2,t3));
        Set<Trait> traitsy2 = new HashSet<Trait>(Arrays.asList(t4,t5,t6,t7));
        //Potrzebny Identifier
        Observation o1 = new Observation(null,traitsy1);
        Observation o2 = new Observation(null,traitsy2);*/

        //grzkos
        Formula f1 = new SimpleFormula(o1obs2,redColour,false);

        DistributedKnowledge dk = new DistributedKnowledge(agent1, f1, 2);

        //grzkos
/*
        //Update 2
        Formula form5 = new SimpleFormula(o1obs6,new Trait<String,String>("Colour","Red"),true);
        Formula form6 = new SimpleFormula(o1obs7,new Trait<String,String>("Colour","Black"),true);
        Formula form7 = new SimpleFormula(o1obs8,new Trait<String,String>("Shape","Round"),true);
        Formula form8 = new SimpleFormula(o1obs8,new Trait<String,String>("Shape","Square"),true);
        Formula form9 = new SimpleFormula(o1obs10,new Trait<String,Boolean>("Blinking",true),true);
        //Update 3
        Formula form10 = new SimpleFormula(o2obs11,new Trait<String,String>("Colour","Red"),true);
        Formula form11 = new SimpleFormula(o2obs12,new Trait<String,String>("Shape","Round"),true);
        //Update 4
        Formula form12 = new SimpleFormula(o2obs17,new Trait<String,String>("Colour","Red"),true);

        Formula complexform1 = new ComplexFormula(o2obs17,Arrays.asList(new Trait<String,Integer>("NoOfWalls",3),new Trait<String,Boolean>("Blinking",false)), Operators.Type.AND);
        Formula complexform2= new ComplexFormula(o2obs17,Arrays.asList(new Trait<String,String>("Colour","Red"),new Trait<String,Boolean>("Blinking",false)), Operators.Type.AND);

        Formula f4 = new SimpleFormula(o2,t4,true);
        Formula f5 = new SimpleFormula(o2,t5,false);
        Formula f6 = new SimpleFormula(o2,t6,false);

        List<Trait> complexTrait1 = new ArrayList<Trait>(Arrays.asList(t,t4));
        List<Trait> complexTrait2 = new ArrayList<Trait>(Arrays.asList(t2,t7));
        List<Trait> complexTrait3 = new ArrayList<Trait>(Arrays.asList(t7,t2));
*/

        BaseProfile bp1,bp2;
        Set<BaseProfile> SetBP = new HashSet<BaseProfile>();

        //stworzenie holonów
        //Brakuje ModelowIndywiduowych,BaseProfili i Distributed Knowledge
        //Holon h1 = new Holon(f1,SetBP,(int)System.currentTimeMillis(),null,null);
        //uruchomienie metod z groundera (w holonach)
        //zadanie pytan
        //uzyskanie odpowiedzi
    }
}
