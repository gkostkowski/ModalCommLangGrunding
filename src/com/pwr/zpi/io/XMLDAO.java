package com.pwr.zpi.io;


import com.pwr.zpi.ObjectType;
import com.pwr.zpi.TraitSignature;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by Grzegorz Kostkowski on 2017-03-11.
 */
public class XMLDAO<Type extends ObjectType> {
    protected XStream xstream = new XStream(new DomDriver());
    protected String xml;
    protected static final String DEF_FILEPATH = System.getProperty("user.dir") + "\\config\\types_def.xml";


    private Collection<Type> fromFile(final String filepath) {
        setAliases();
        try {
            File file = new File(filepath);
            if (!file.exists())
                file.createNewFile();
            else {
                xml = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
                return (Collection<Type>)((DataWrapper<ObjectType>) xstream.fromXML(xml)).getData();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private Collection<Type> fromFile() {
        return fromFile(DEF_FILEPATH);
    }


    private void setAliases() {

        xstream.alias("type", ObjectType.class);
        xstream.alias("types", DataWrapper.class);
        xstream.alias("trait", TraitSignature.class);
    }

    /***
     *
     * @param filename
     * @return
     */
    public Collection<Type> loadTypesDefinitions(String filename) {
        File f = new File(filename);
        Collection<Type> res = null;
        if (f.exists() && f.canRead())
            res = fromFile(filename);
        else
            res = fromFile();
        return res;
    }

    public Collection<Type> loadTypesDefinitions() {
        return fromFile(DEF_FILEPATH);
    }

}
