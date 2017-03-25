package com.pwr.zpi;

import java.util.Collection;

/**
 * Created by Grzesiek on 2017-03-16.
 */
public class NamedCollection<Name, Member> {
    private Name identifier;
    private List<Member> collection; //moze byc konkretniej - List zamiast Collection
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
}
