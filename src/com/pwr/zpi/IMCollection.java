package com.pwr.zpi;

import java.util.Objects;
import java.util.Set;

/**
 * Collection of individual models.
 */
public class IMCollection {

    private static Set<IndividualModel> individualModelSet;

    public IMCollection() {

    }

    public IMCollection(Set<IndividualModel> individualModelSet) {
        this.individualModelSet = individualModelSet;
    }

    public boolean isObservationRepresented(Observation observation) {
        for(IndividualModel model : individualModelSet) {
            if (model.getIdentifier().equals(observation.getIdentifier()))
                return true;
        }
        return false;
    }

    public boolean add(IndividualModel individualModel) {
        return individualModelSet.add(individualModel);
    }

    //todo mało dokładne nazwy...
    public static IndividualModel f(Identifier id) {
        for(IndividualModel model : individualModelSet) {
            if (model.getIdentifier().equals(id))
                return model;
        }
        return null;
    }

    public IndividualModel g(Observation observation) {
        for(IndividualModel model : individualModelSet) {
            if (model.getIdentifier().equals(observation.getIdentifier()))
                return model;
        }
        return null;
    }

    /**
     * Method finds a specific individual model based on given name
     * @param name common name for the object
     * @return IndividualModel of the name
     */
    public static IndividualModel getModelFromName(String name)
    {
        for(IndividualModel model : individualModelSet)
            if(model.getName().equals(name))
                return model;
        return null;
    }
}
