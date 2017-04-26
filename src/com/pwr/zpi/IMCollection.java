package com.pwr.zpi;

import java.util.HashSet;
import java.util.Set;

/**
 * Collection of individual models. Used to find or add new model.
 */
public class IMCollection {

    private static Set<IndividualModel> individualModelSet;

    public IMCollection() {
        individualModelSet = new HashSet<>();
    }

    public IMCollection(Set<IndividualModel> individualModelSet) {
        IMCollection.individualModelSet = individualModelSet;
    }

    /**
     * Checks whether object's observation has its representation amongst individual models.
     * @param observation Given observation of some object.
     * @return  Model found - true/false.
     */
    public boolean isObservationRepresented(Observation observation) {
        for(IndividualModel model : individualModelSet) {
            if (model.getIdentifier().equals(observation.getIdentifier()))
                return true;
        }
        return false;
    }

    /**
     * Simply adds new individual model to collection.
     * @param individualModel New model.
     * @return true/false (success of add operation)
     */
    public boolean add(IndividualModel individualModel) {
        return individualModelSet.add(individualModel);
    }

    /**
     * Returns the individual model representing object of given identifier.
     * @param identifier Identifier of object we are looking for..
     * @return Individual model of object.
     */
    public static IndividualModel getRepresentationByIdentifier(Identifier identifier) {
        for(IndividualModel model : individualModelSet) {
            if (model.getIdentifier().equals(identifier))
                return model;
        }
        return null;
    }

    /**
     * Returns the individual model representing object based on its observation.
     * @param observation Observation of object we are looking for.
     * @return Individual model of object.
     */
    public static IndividualModel getRepresentationByObservation(Observation observation) {
        for(IndividualModel model : individualModelSet) {
            if (model.getIdentifier().equals(observation.getIdentifier()))
                return model;
        }
        return null;
    }

    /**
     * Method finds a specific individual model based on given name.
     * @param name common name for the object
     * @return IndividualModel of the name
     */
    public static IndividualModel getModelFromName(String name)
    {
        for(IndividualModel model : individualModelSet)
            if(model.getName().equalsIgnoreCase(name))
                return model;
        return null;
    }
}
