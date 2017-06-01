package com.pwr.zpi.holons.context;

import com.pwr.zpi.language.Trait;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents context, which is established basing on some base profiles known as representative - in
 * standard version this representatives could contain last observed base profile.
 */
final public class Context {
    List<Trait> observedTraits;
    List<Trait> notObservedTraits;


    public Context(List<Trait> observedTraits, List<Trait> notObservedTraits) {
        this.observedTraits = observedTraits;
        this.notObservedTraits = notObservedTraits;
    }

    public Context() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public List<Trait> getObservedTraits() {
        return observedTraits;
    }

    public List<Trait> getNotObservedTraits() {
        return notObservedTraits;
    }
}
