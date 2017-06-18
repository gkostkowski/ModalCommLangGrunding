/*
 * Created by Grzegorz Kostkowski
 */
package com.pwr.zpi.core.memory.holons.context;

import com.pwr.zpi.language.Trait;
import com.pwr.zpi.core.memory.semantic.IndividualModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents context, which is established basing on some base profiles known as representative - in
 * standard version this representatives could contain last observed base profile.
 * @author Grzegorz Kostkowski
 */
final public class Context {
    List<Trait> observedTraits;
    List<Trait> notObservedTraits;
    IndividualModel relatedObject;


    public Context(List<Trait> observedTraits, List<Trait> notObservedTraits, IndividualModel relatedObject) {
        if (relatedObject == null)
            throw new NullPointerException();
        this.observedTraits = observedTraits;
        this.notObservedTraits = notObservedTraits;
        this.relatedObject =relatedObject;
    }

    public Context(IndividualModel relatedObject) {
        this(new ArrayList<>(), new ArrayList<>(), relatedObject);
    }

    public List<Trait> getObservedTraits() {
        return observedTraits;
    }

    public List<Trait> getNotObservedTraits() {
        return notObservedTraits;
    }

    public IndividualModel getRelatedObject() {
        return relatedObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Context context = (Context) o;

        if (!observedTraits.containsAll(context.observedTraits)) return false;
        if (!notObservedTraits.containsAll(context.notObservedTraits)) return false;
        return relatedObject.equals(context.relatedObject);
    }

    @Override
    public int hashCode() {
        int result = observedTraits.hashCode();
        result = 31 * result + notObservedTraits.hashCode();
        result = 31 * result + relatedObject.hashCode();
        return result;
    }

    /**
     * Method determines if built context contain any traits. It is possible that context context will be empty and
     * it could mean that selected set of representatives is too diverse.
     * It depends on builder implementation.
     * @return
     */
    public boolean isEmpty() {
        return getObservedTraits().isEmpty() && getNotObservedTraits().isEmpty();
    }
}
