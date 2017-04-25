package com.pwr.zpi;

public class Main {

    public static void main(String[] args) {
        Agent agent1 = null;
        //ccztytanie pliku kon
        // ladowanie obiektow typow
        //obserwacje + zaladowanie do profili bazowych <-- agent

        Trait<String,String> o1t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o1t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o1t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1o1 = new HashSet<Trait>(Arrays.asList(o1t,o1t1,o1t2));
        Observation o1o1 = new Observation(null,traitsyo1o1);

        Trait<String,String> o2t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o2t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o2t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1o2 = new HashSet<Trait>(Arrays.asList(o2t,o2t1,o2t2));
        Observation o1o2 = new Observation(null,traitsyo1o2);

        Trait<String,String> o3t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o3t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o3t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1o3 = new HashSet<Trait>(Arrays.asList(o3t,o3t1,o3t2));
        Observation o1o3 = new Observation(null,traitsyo1o3);

        Trait<String,String> o4t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o4t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o4t2 = new Trait<String,Boolean>("Blinking",null);
        Set<Trait> traitsyo1o4 = new HashSet<Trait>(Arrays.asList(o4t,o4t1,o4t2));
        Observation o1o4 = new Observation(null,traitsyo1o4);

        Trait<String,String> o5t = new Trait<String,String>("Colour","Black");
        Trait<String,String> o5t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o5t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1o5 = new HashSet<Trait>(Arrays.asList(o5t,o5t1,o5t2));
        Observation o1o5 = new Observation(null,traitsyo1o5);

        //UPDATE 1

        Trait<String,String> o6t = new Trait<String,String>("Colour","Black");
        Trait<String,String> o6t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o6t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1o6 = new HashSet<Trait>(Arrays.asList(o6t,o6t1,o6t2));
        Observation o1o6 = new Observation(null,traitsyo1o6);

        Trait<String,String> o7t = new Trait<String,String>("Colour","Black");
        Trait<String,String> o7t1 = new Trait<String,String>("Shape",null);
        Trait<String,Boolean> o7t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1o7 = new HashSet<Trait>(Arrays.asList(o7t,o7t1,o7t2));
        Observation o1o7 = new Observation(null,traitsyo1o7);

        Trait<String,String> o8t = new Trait<String,String>("Colour","White");
        Trait<String,String> o8t1 = new Trait<String,String>("Shape","Square");
        Trait<String,Boolean> o8t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1o8 = new HashSet<Trait>(Arrays.asList(o8t,o8t1,o8t2));
        Observation o1o8 = new Observation(null,traitsyo1o8);

        Trait<String,String> o9t = new Trait<String,String>("Colour","White");
        Trait<String,String> o9t1 = new Trait<String,String>("Shape","Round");
        Trait<String,Boolean> o9t2 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo1o9 = new HashSet<Trait>(Arrays.asList(o9t,o9t1,o9t2));
        Observation o1o9 = new Observation(null,traitsyo1o9);

        Trait<String,String> o10t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o10t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o10t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo1o10 = new HashSet<Trait>(Arrays.asList(o10t,o10t1,o10t2));
        Observation o1o10 = new Observation(null,traitsyo1o10);

        //UPDATE 2

        Trait<String,String> o11t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o11t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o11t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo2o11 = new HashSet<Trait>(Arrays.asList(o11t,o11t1,o11t2));
        Observation o2o11 = new Observation(null,traitsyo2o11);

        Trait<String,String> o12t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o12t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o12t2 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo2o12 = new HashSet<Trait>(Arrays.asList(o12t,o12t1,o12t2));
        Observation o2o12 = new Observation(null,traitsyo2o12);

        Trait<String,String> o13t = new Trait<String,String>("Colour","White");
        Trait<String,String> o13t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o13t2 = new Trait<String,Boolean>("Blinking",null);
        Set<Trait> traitsyo2o13 = new HashSet<Trait>(Arrays.asList(o13t,o13t1,o13t2));
        Observation o2o13 = new Observation(null,traitsyo2o13);

        Trait<String,String> o14t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o14t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o14t2 = new Trait<String,Boolean>("Blinking",true);
        Trait<String,String> o14t3 = new Trait<String,String>("Colour",null);
        Trait<String,String> o14t4 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o14t5 = new Trait<String,Boolean>("Blinking",false);
        Trait<String,String> o14t6 = new Trait<String,String>("Colour","Red");
        Trait<String,Integer> o14t7 = new Trait<String,Integer>("Shape",3);
        Trait<String,Boolean> o14t8 = new Trait<String,Boolean>("Blinking",false);

        Set<Trait> traitsyo2o14 = new HashSet<Trait>(Arrays.asList(o14t,o14t1,o14t2,o14t4,o14t5,o14t6,o14t7,o14t8));
        Observation o2o14 = new Observation(null,traitsyo2o14);

        // UPDATE 3

        Trait<String,String> o15t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o15t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o15t2 = new Trait<String,Boolean>("Blinking",null);
        Set<Trait> traitsyo2o15 = new HashSet<Trait>(Arrays.asList(o15t,o15t1,o15t2));
        Observation o2o15 = new Observation(null,traitsyo2o15);

        Trait<String,String> o16t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o16t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o16t2 = new Trait<String,Boolean>("Blinking",false);
        Trait<String,String> o16t3 = new Trait<String,String>("Colour","Red");
        Trait<String,Integer> o16t4 = new Trait<String,Integer>("Shape",3);
        Trait<String,Boolean> o16t5 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo2o16 = new HashSet<Trait>(Arrays.asList(o16t,o16t1,o16t2,o16t3,o16t4,o16t5));
        Observation o2o16 = new Observation(null,traitsyo2o16);

        Trait<String,String> o17t = new Trait<String,String>("Colour","White");
        Trait<String,String> o17t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o17t2 = new Trait<String,Boolean>("Blinking",true);
        Trait<String,String> o17t3 = new Trait<String,String>("Colour","White");
        Trait<String,Integer> o17t4 = new Trait<String,Integer>("Shape",3);
        Trait<String,Boolean> o17t5 = new Trait<String,Boolean>("Blinking",false);
        Set<Trait> traitsyo2o17 = new HashSet<Trait>(Arrays.asList(o17t,o17t1,o17t2,o17t3,o17t4,o17t5));
        Observation o2o17 = new Observation(null,traitsyo2o17);

        Trait<String,String> o18t = new Trait<String,String>("Colour","Red");
        Trait<String,String> o18t1 = new Trait<String,String>("Shape","Sqaure");
        Trait<String,Boolean> o18t2 = new Trait<String,Boolean>("Blinking",true);
        Trait<String,String> o18t3 = new Trait<String,String>("Colour","Red");
        Trait<String,Integer> o18t4 = new Trait<String,Integer>("Shape",2);
        Trait<String,Boolean> o18t5 = new Trait<String,Boolean>("Blinking",true);
        Set<Trait> traitsyo2o18 = new HashSet<Trait>(Arrays.asList(o18t,o18t1,o18t2,o18t3,o18t4,o18t5));
        Observation o2o18 = new Observation(null,traitsyo2o18);

        Set<Trait> traitsy1 = new HashSet<Trait>(Arrays.asList(t,t1,t2,t3));
        Set<Trait> traitsy2 = new HashSet<Trait>(Arrays.asList(t4,t5,t6,t7));
        //Potrzebny Identifier
        Observation o1 = new Observation(null,traitsy1);
        Observation o2 = new Observation(null,traitsy2);

        Formula f1 = new SimpleFormula(o1,t1,true);
        Formula f2 = new SimpleFormula(o1,t2,false);
        Formula f3 = new SimpleFormula(o1,t3,false);

        Formula f4 = new SimpleFormula(o2,t4,true);
        Formula f5 = new SimpleFormula(o2,t5,false);
        Formula f6 = new SimpleFormula(o2,t6,false);

        List<Trait> complexTrait1 = new ArrayList<Trait>(Arrays.asList(t,t4));
        List<Trait> complexTrait2 = new ArrayList<Trait>(Arrays.asList(t2,t7));
        List<Trait> complexTrait3 = new ArrayList<Trait>(Arrays.asList(t7,t2));

        BaseProfile bp1,bp2;
        Set<BaseProfile> SetBP = new HashSet<BaseProfile>();

        //stworzenie holonów
        //Brakuje ModelowIndywiduowych,BaseProfili i Distributed Knowledge
        Holon h1 = new Holon(f1,SetBP,(int)System.currentTimeMillis(),null,null);
        //uruchomienie metod z groundera (w holonach)
        //zadanie pytan
        //uzyskanie odpowiedzi
    }
}
