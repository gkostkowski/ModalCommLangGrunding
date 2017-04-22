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
public class XMLDAO<T extends ObjectType> {
    protected XStream xstream = new XStream(new DomDriver());
    protected String xml;
    protected static final String DEF_FILEPATH = System.getProperty("user.dir") + "\\config\\types_def.xml";


    private Collection<T> fromFile(final String filepath) {
        setAliases();
        try {
            File file = new File(filepath);
            if (!file.exists())
                file.createNewFile();
            else {
                xml = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
                return (Collection<T>)((DataWrapper<ObjectType>) xstream.fromXML(xml)).getData();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private Collection<T> fromFile() {
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
    public Collection<T> loadTypesDefinitions(String filename) {
        File f = new File(filename);
        Collection<T> res = null;
        if (f.exists() && f.canRead())
            res = fromFile(filename);
        else
            res = fromFile();
        return res;
    }

    public Collection<T> loadTypesDefinitions() {
        return fromFile(DEF_FILEPATH);
    }

}
