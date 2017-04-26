package com.pwr.zpi.Conversation;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.exceptions.InvalidSentenceFormulaException;
import com.pwr.zpi.language.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weronika on 24.04.2017.
 */
public class Question<V> {

    private Formula formula;
    private List<String> info;


    private String[] parts;
    private int index = 0;

    private IndividualModel model;
    private List<TraitSignature> traitSignatures;
    private List<Trait> traits;
    private List<State> states;

    public Question(String question) throws InvalidQuestionException
    {
        parts = question.split(" ");
        states = new ArrayList<>();
        traitSignatures = new ArrayList<>();
        traits = new ArrayList<>();
        info = new ArrayList<>();
    }

    public List<String> getInfo()
    {
        return info;
    }

    public Formula getFormula() throws InvalidQuestionException
    {
        String name = parts[1]+parts[2];
        info.add(parts[1]+ " " + parts[2]);
        model = IMCollection.getModelFromName(name);
        if(model == null)
            throw new InvalidQuestionException(InvalidQuestionException.NO_OBJECT);
        TraitSignature trait1 = getTraitSinature();
        if(trait1==null)
            throw new InvalidQuestionException(InvalidQuestionException.NO_FIRST_TRAIT);
        traitSignatures.add(trait1);
        String value1 = getValue(trait1);
        if(value1==null)
            throw new InvalidQuestionException(InvalidQuestionException.NO_FIRST_VALUE);
        Trait trait = putValue(value1, trait1);
        traits.add(trait);
        Observation ob = new Observation(model.getIdentifier(), null);
        if(parts[index].equalsIgnoreCase("and"))
        {
            TraitSignature traitSinature2 = getTraitSinature();
            if(traitSinature2==null)
                throw new InvalidQuestionException(InvalidQuestionException.NO_SECOND_TRAIT);
            traitSignatures.add(traitSinature2);
            String value2 = getValue(traitSinature2);
            if(value2==null)
                throw new InvalidQuestionException(InvalidQuestionException.NO_SECOND_VALUE);
            Trait trait2 = putValue(value2, traitSinature2);
            traits.add(trait2);
            try
            {
                formula = new ComplexFormula(ob, traits, states, Operators.Type.AND);
            } catch (InvalidSentenceFormulaException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                formula = new SimpleFormula(ob, traits, states);
            } catch (InvalidSentenceFormulaException e) {
                e.printStackTrace();
            }
        }
        return formula;
    }

    private TraitSignature getTraitSinature()
    {
        String name = parts[index];
        info.add(name);
        while(index<parts.length-1 && !parts[index].equalsIgnoreCase("and")) // todo inne zdania złożone
        {
            for(TraitSignature trait: model.getType().getTraits())
                if(trait.getName().equalsIgnoreCase(name))
                    return trait;
            index++;
            name += parts[index];
            info.get(info.size()-1).concat(" " + parts[index]);
        }
        return null;
    }

    private Trait putValue(String value, TraitSignature trait)
    {
        String s = trait.getValueTypeString();
        switch (s)
        {
            case "Boolean" : return new Trait(trait.getName(), Boolean.valueOf(value));
            case "Integer" : return new Trait(trait.getName(), Integer.valueOf(value));
            case "String" : return new Trait(trait.getName(), value);
            case "Byte" : return new Trait(trait.getName(), Byte.valueOf(value));
            case "Double" : return new Trait(trait.getName(), Double.valueOf(value));
            case "Float" : return new Trait(trait.getName(), Float.valueOf(value));
            case "Long" : return new Trait(trait.getName(), Long.valueOf(value));
            case "Short" : return new Trait(trait.getName(), Short.valueOf(value));
            case "Character" : return new Trait(trait.getName(), value.charAt(0));
            default: return null;

        }
    }

    private String getValue(TraitSignature trait)
    {
        String name = "";
        if(parts[index].equalsIgnoreCase("not"))
        {
            states.add(State.IS_NOT);
            index++;
        } else states.add(State.IS);
        info.add(parts[index]);
        do
        {
            name += parts[index];
            index++;
            if(trait.isInDomain(name))
                return name;
            info.get(info.size()-1).concat(" " + parts[index]);
        } while(index<parts.length && !parts[index].equalsIgnoreCase("and")); // todo inne zdania złożone
        return null;
    }

}
