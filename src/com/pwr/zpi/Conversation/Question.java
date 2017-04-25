package com.pwr.zpi.Conversation;

import com.pwr.zpi.*;
import com.pwr.zpi.exceptions.InvalidQuestionException;
import com.pwr.zpi.language.Formula;

/**
 * Created by Weronika on 24.04.2017.
 */
public class Question {

    private Formula question;

    private String[] parts;
    int index = 0;

    private IndividualModel model;
    private Trait trait1, trait2;
    private State state1, state2;

    public Question(String question) throws InvalidQuestionException
    {
        parts = question.split(" ");
        String name = parts[1]+parts[2];
        IndividualModel model = IMCollection.getModelFromName(name);
        if(model == null)
            throw new InvalidQuestionException(InvalidQuestionException.NO_OBJECT);
        TraitSignature traitS = getTraitSinature();
        if(traitS==null)
            throw new InvalidQuestionException(InvalidQuestionException.NO_FIRST_TRAIT);
        trait1 = new Trait(traitS.getName(), )


    }

    private TraitSignature getTraitSinature()
    {
        String name = parts[index];
        while(index<parts.length-1 && !parts[index].equalsIgnoreCase("and")) // todo inne zdania złożone
        {
            for(TraitSignature trait: model.getType().getTraits())
                if(trait.getName().equalsIgnoreCase(name))
                    return trait;
            index++;
            name += parts[index];
        }
        return null;
    }

    private String getValue(TraitSignature trait)
    {
        String name = parts[index];
        while(index<parts.length-1 && !parts[index].equalsIgnoreCase("and")) // todo inne zdania złożone
        {
            if(trait.isInDomain(name))
                return name;
            index++;
            name += parts[index];
        }
        return null;
    }




}
