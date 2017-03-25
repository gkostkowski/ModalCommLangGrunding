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

    public Collection<Member> getMember(Name val) {

    }

    /*
    public List<Member> getList(){
        return collection;
    }
    public Member getMember(Member desired){
        for(Member m:collection){
            if(desired.equals(m))
            {return m;}
        }
        return null;
    }
     */

}
