package com.pwr.zpi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.pwr.zpi.language.Grounder;

import junit.framework.Assert;

public class GrounderTests {

    Grounder tester = new Grounder();
    @SuppressWarnings("rawtypes")
    Class testprivate = tester.getClass();


    //Dane Testowe.
    Trait<String,Integer> t = new Trait<String,Integer>("Czerwonosc",100);
    Trait<String,Integer> t1 = new Trait<String,Integer>("Niebiebieskosc",78);
    Trait<String,Integer> t2 = new Trait<String,Integer>("Seksapil",32);
    Trait<String,Integer> t3 = new Trait<String,Integer>("Brzmienie",44);

    @SuppressWarnings("rawtypes")
    Set<Trait> traitsy = new HashSet<Trait>(Arrays.asList(t,t1,t2,t3));

    Observation o = new Observation(1, "TestowyTyp", traitsy);

    Trait<String,Integer> tr1 = new Trait<String,Integer>("Budyn",50);
    Trait<String,Integer> tr2 = new Trait<String,Integer>("Smacznosc",78);
    Trait<String,Integer> tr3 = new Trait<String,Integer>("Kalorycznosc",32);
    Trait<String,Integer> tr4 = new Trait<String,Integer>("Niebieskosc",44);

    @SuppressWarnings("rawtypes")
    Set<Trait> traitsy2 = new HashSet<Trait>(Arrays.asList(t,t1,t2,t3));

    Observation o2 = new Observation(1, "TestowyTyp", traitsy);

    Map<Trait, Set<Observation>> SetOTraits = new HashMap<Trait,Set<Observation>>();

    BaseProfile bp ,bp2;
    Set<BaseProfile> SetBP = new HashSet<BaseProfile>();

    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    @Test
    public void getGroundingSetsPositiveTraitTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Grounder tester = new Grounder();
        Class testprivate = tester.getClass();
        Class[] cArg = new Class[4];
        cArg[0] = Observation.class;
        cArg[1] = Trait.class;
        cArg[2] = int.class;
        cArg[3] = Set.class;
        SetOTraits.put(t1, new HashSet<Observation>(Arrays.asList(o,o2)));
        bp = new BaseProfile(SetOTraits,10);
        bp.setDescribedByTraits(SetOTraits);
        SetBP.add(bp);

        try {
            Method getGroundingSetsPositiveTrait = testprivate.getDeclaredMethod("getGroundingSetsPositiveTrait", cArg);
            getGroundingSetsPositiveTrait.setAccessible(true);
            System.out.println(getGroundingSetsPositiveTrait.toString());

            Assert.assertEquals(new HashSet<BaseProfile>(), getGroundingSetsPositiveTrait.invoke(testprivate, o,tr4,14,SetBP) );


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    @Test
    public void getCardPositiveTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[2];
        cArg[0] = Set.class;
        cArg[1] = int.class;
        SetOTraits.put(t1, new HashSet<Observation>(Arrays.asList(o,o2)));
        bp = new BaseProfile(SetOTraits,10);
        bp.setDescribedByTraits(SetOTraits);
        bp2 = new BaseProfile(SetOTraits,12);
        bp2.setDescribedByTraits(SetOTraits);
        SetBP.add(bp);
        try {
            Method GetCard = testprivate.getDeclaredMethod("getCardPositive", cArg);
            GetCard.setAccessible(true);
            System.out.println(GetCard.toString());

            Assert.assertEquals(1.0, GetCard.invoke(testprivate, SetBP,10));
            Assert.assertEquals(0.0, GetCard.invoke(testprivate, new HashSet<BaseProfile>(),10));
            SetBP.add(bp2);
            Assert.assertEquals(2.0, GetCard.invoke(testprivate, SetBP,10));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    @Test
    public void getCardNegativeTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[2];
        cArg[0] = Set.class;
        cArg[1] = int.class;
        SetOTraits.put(t1, new HashSet<Observation>(Arrays.asList(o,o2)));
        bp = new BaseProfile(SetOTraits,10);
        bp.setDescribedByTraits(SetOTraits);
        bp2 = new BaseProfile(SetOTraits,12);
        bp2.setDescribedByTraits(SetOTraits);
        SetBP.add(bp);
        try {
            Method GetCard = testprivate.getDeclaredMethod("getCardNegative", cArg);
            GetCard.setAccessible(true);
            System.out.println(GetCard.toString());

            Assert.assertEquals(1.0, GetCard.invoke(testprivate, SetBP,10));
            Assert.assertEquals(0.0, GetCard.invoke(testprivate, new HashSet<BaseProfile>(),10));
            SetBP.add(bp2);
            Assert.assertEquals(2.0, GetCard.invoke(testprivate, SetBP,10));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    @Test
    public void relativePositiveCardTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[3];
        cArg[0] = Set.class;
        cArg[1] = Set.class;
        cArg[2] = int.class;
        SetOTraits.put(t1, new HashSet<Observation>(Arrays.asList(o,o2)));
        bp = new BaseProfile(SetOTraits,10);
        bp.setDescribedByTraits(SetOTraits);
        bp2 = new BaseProfile(SetOTraits,12);
        bp2.setDescribedByTraits(SetOTraits);
        SetBP.add(bp);
        try {
            Method GetCard = testprivate.getDeclaredMethod("relativePositiveCard", cArg);
            GetCard.setAccessible(true);
            System.out.println(GetCard.toString());

            Assert.assertEquals(0.5, GetCard.invoke(testprivate, SetBP,SetBP,10));
            Assert.assertEquals(0.0, GetCard.invoke(testprivate, new HashSet<BaseProfile>(),new HashSet<BaseProfile>(),10));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}