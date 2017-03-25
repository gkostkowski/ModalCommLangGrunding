package com.pwr.zpi;

import java.util.Collection;

/**
 * Created by Grzesiek on 2017-03-16.
 */
public class NamedCollection<Name, Member> {
    private Name identifier;
    private Collection<Member> collection; //moze byc konkretniej - List zamiast Collection

    public Collection<Member> getCollection() {
        return collection;
    }

    public Member getMember(Member val) {
        Iterator<Member> it = collection.iterator();
        while(it.hasNext()){
            Member m = it.next();
            if(m.equals(val)){
                return m;
            }
        }
        return null;
    }
}
