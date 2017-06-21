package com.pwr.zpi.io;

import com.pwr.zpi.exceptions.ConfigValueNotFoundException;
import com.pwr.zpi.exceptions.InvalidConfigurationException;
import com.pwr.zpi.language.Formula;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for reading configuration from XML config file and to provide
 * configuration values to the application's environment.
 *
 * @author Mateusz Gawlowski
 * @author Grzegorz Kostkowski
 */
public class Configuration {

    private static final String DEF_CONFIGURATION_FILENAME = "configuration.xml";

    /**
     * Distributed knowledge static values.
     */
    public static final boolean DK_IS_COMPLEX, DEF_DK_IS_COMPLEX = true;

    /**
     * Scenario static values.
     */
    public static final int FIRST_TRAIT_POS, DEF_FIRST_TRAIT_POS = 2;
    public static final int NAME_POS, DEF_NAME_POS = 0;
    public static final char COMMENT_SIGN, DEF_COMMENT_SIGN = '#';
    public static final String SCENARIO_MARKER, DEF_SCENARIO_MARKER = "SCENARIO";
    public static final String SCENARIOS_DIR, DEF_SCENARIOS_DIR = "config/scenarios/";

    /**
     * XMLDAO static values.
     */
    public static final String MAIN_MODULE_NAME, DEF_MAIN_MODULE_NAME = "ModalCommLangGrunding";
    public static final String RELATIVE_FILEPATH, DEF_RELATIVE_FILEPATH = "\\config\\types_def.xml";

    /**
     * DatabaseAO static value.
     */
    public static final String DATABASE_FILENAME, DEF_DATABASE_FILENAME = "database.db";
    public static final String IDENTIFIERS_PATH, DEF_IDENTIFIERS_PATH = "com.pwr.zpi.core.memory.semantic.identifiers";

    /**
     * Context static values.
     */
    public static final double MAX_THRESHOLD, DEF_MAX_THRESHOLD = 3;
    public static final double NORMALISED_MAX_THRESHOLD, DEF_NORMALISED_MAX_THRESHOLD = 0.4;
    public static final int LATEST_GROUP_SIZE, DEF_LATEST_GROUP_SIZE = 5;

    /**
     * BPCollection static values.
     */
    public static final int INIT_TIMESTAMP, DEF_INIT_TIMESTAMP = 0;
    public static final int MAX_WM_CAPACITY, DEF_MAX_WM_CAPACITY = 100;
    public static final boolean OVERRIDE_IF_EXISTS, DEF_OVERRIDE_IF_EXISTS = true;

    /**
     * Voice server ports values.
     */
    public static final int LISTENING_SERVER_PORT, DEF_LISTENING_SERVER_PORT = 6666;
    public static final int TALKING_SERVER_PORT, DEF_TALKING_SERVER_PORT = 6667;

    /**
     * Thresholds for simple modalities.
     */
    private static final double MIN_POS, DEF_MIN_POS = 0.2;
    private static final double MAX_POS, DEF_MAX_POS = 0.6;
    private static final double MIN_BEL, DEF_MIN_BEL = 0.7;
    private static final double MAX_BEL, DEF_MAX_BEL = 0.9;
    private static final double KNOW, DEF_KNOW = 1;

    /**
     * Thresholds for modal conjunctions.
     */
    private static final double CONJ_MIN_POS, DEF_CONJ_MIN_POS = 0.1;
    private static final double CONJ_MAX_POS, DEF_CONJ_MAX_POS = 0.6;
    private static final double CONJ_MIN_BEL, DEF_CONJ_MIN_BEL = 0.65;
    private static final double CONJ_MAX_BEL, DEF_CONJ_MAX_BEL = 0.9;
    private static final double CONJ_KNOW, DEF_CONJ_KNOW = 1;

    /**
     * Thresholds for modal disjunctions. //TODO rozwazyc inne wartosci
     */
    private static final double DISJ_MIN_POS, DEF_DISJ_MIN_POS = 0.4;
    private static final double DISJ_MAX_POS, DEF_DISJ_MAX_POS = 0.55;
    private static final double DISJ_MIN_BEL, DEF_DISJ_MIN_BEL = DEF_DISJ_MAX_POS;
    private static final double DISJ_MAX_BEL, DEF_DISJ_MAX_BEL = 1;
    private static final double DISJ_KNOW, DEF_DISJ_KNOW = 1;

    /**
     * Thresholds for modal exclusive disjunctions.
     */
    private static final double EX_DISJ_MIN_POS, DEF_EX_DISJ_MIN_POS = 0.21;
    private static final double EX_DISJ_MAX_POS, DEF_EX_DISJ_MAX_POS = 0.67;
    private static final double EX_DISJ_MIN_BEL, DEF_EX_DISJ_MIN_BEL = DEF_EX_DISJ_MAX_POS;
    private static final double EX_DISJ_MAX_BEL, DEF_EX_DISJ_MAX_BEL = 1;
    private static final double EX_DISJ_KNOW, DEF_EX_DISJ_KNOW = 1;

    /**
     * This block is used to initialize static values via reading xml file.
     */
    static {
        String xml;
        ConfigurationReader reader = new ConfigurationReader();

        try {
            xml = new String(Files.readAllBytes(Paths.get("config/" + DEF_CONFIGURATION_FILENAME)), StandardCharsets.UTF_8);

            XStream xStream = new XStream(new DomDriver());
            xStream.registerConverter(new ConfigurationConverter());
            xStream.alias("statics", ConfigurationReader.class);

            reader = (ConfigurationReader) xStream.fromXML(xml);

        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Not able to load configuration file", e);
        }

        //Distributed knowledge
        DK_IS_COMPLEX = selectValue(reader.getValue("DK_IS_COMPLEX"), DEF_DK_IS_COMPLEX);

        //Scenario
        FIRST_TRAIT_POS = selectValue(reader.getValue("FIRST_TRAIT_POS"), DEF_FIRST_TRAIT_POS);
        NAME_POS = selectValue(reader.getValue("NAME_POS"), DEF_NAME_POS);
        COMMENT_SIGN = selectValue(reader.getValue("COMMENT_SIGN"), DEF_COMMENT_SIGN);
        SCENARIO_MARKER = selectValue(reader.getValue("SCENARIO_MARKER"), DEF_SCENARIO_MARKER);
        SCENARIOS_DIR = selectValue(reader.getValue("SCENARIOS_DIR"), DEF_SCENARIOS_DIR);

        //XMLDAO
        MAIN_MODULE_NAME = selectValue(reader.getValue("MAIN_MODULE_NAME"), DEF_MAIN_MODULE_NAME);
        RELATIVE_FILEPATH = selectValue(reader.getValue("RELATIVE_FILEPATH"), DEF_RELATIVE_FILEPATH);

        //DatabaseAO
        DATABASE_FILENAME = selectValue(reader.getValue("DATABASE_FILENAME"), DEF_DATABASE_FILENAME);
        IDENTIFIERS_PATH = selectValue(reader.getValue("IDENTIFIERS_PATH"), DEF_IDENTIFIERS_PATH);

        //Context
        MAX_THRESHOLD = selectValue(reader.getValue("MAX_THRESHOLD"), DEF_MAX_THRESHOLD);
        NORMALISED_MAX_THRESHOLD = selectValue(reader.getValue("NORMALISED_MAX_THRESHOLD"), DEF_NORMALISED_MAX_THRESHOLD);
        LATEST_GROUP_SIZE = selectValue(reader.getValue("LATEST_GROUP_SIZE"), DEF_LATEST_GROUP_SIZE);

        //BPCollection
        INIT_TIMESTAMP = selectValue(reader.getValue("INIT_TIMESTAMP"), DEF_INIT_TIMESTAMP);
        MAX_WM_CAPACITY = selectValue(reader.getValue("MAX_WM_CAPACITY"), DEF_MAX_WM_CAPACITY);
        OVERRIDE_IF_EXISTS = selectValue(reader.getValue("OVERRIDE_IF_EXISTS"), DEF_OVERRIDE_IF_EXISTS);

        //Voice servers ports
        LISTENING_SERVER_PORT = selectValue(reader.getValue("LISTENING_SERVER_PORT"), DEF_LISTENING_SERVER_PORT);
        TALKING_SERVER_PORT = selectValue(reader.getValue("TALKING_SERVER_PORT"), DEF_TALKING_SERVER_PORT);

        //Thresholds for simple modalities
        MIN_POS = selectValue(reader.getValue("MIN_POS"), DEF_MIN_POS);
        MAX_POS = selectValue(reader.getValue("MAX_POS"), DEF_MAX_POS);
        MIN_BEL = selectValue(reader.getValue("MIN_BEL"), DEF_MIN_BEL);
        MAX_BEL = selectValue(reader.getValue("MAX_BEL"), DEF_MAX_BEL);
        KNOW = selectValue(reader.getValue("KNOW"), DEF_KNOW);

        //Thresholds for modal conjunctions
        CONJ_MIN_POS = selectValue(reader.getValue("CONJ_MIN_POS"), DEF_CONJ_MIN_POS);
        CONJ_MAX_POS = selectValue(reader.getValue("CONJ_MAX_POS"), DEF_CONJ_MAX_POS);
        CONJ_MIN_BEL = selectValue(reader.getValue("CONJ_MIN_BEL"), DEF_CONJ_MIN_BEL);
        CONJ_MAX_BEL = selectValue(reader.getValue("CONJ_MAX_BEL"), DEF_CONJ_MAX_BEL);
        CONJ_KNOW = selectValue(reader.getValue("CONJ_KNOW"), DEF_CONJ_KNOW);

        //Thresholds for modal disjunctions
        DISJ_MIN_POS = selectValue(reader.getValue("DISJ_MIN_POS"), DEF_DISJ_MIN_POS);
        DISJ_MAX_POS = selectValue(reader.getValue("DISJ_MAX_POS"), DEF_DISJ_MAX_POS);
        //DISJ_MIN_BEL = selectValue(reader.getValue("DISJ_MIN_BEL"), DEF_DISJ_MIN_BEL);
        DISJ_MIN_BEL = DISJ_MAX_POS;
        DISJ_MAX_BEL = selectValue(reader.getValue("DISJ_MAX_BEL"), DEF_DISJ_MAX_BEL);
        DISJ_KNOW = selectValue(reader.getValue("DISJ_KNOW"), DEF_DISJ_KNOW);

        //Thresholds for modal exclusive disjunctions
        EX_DISJ_MIN_POS = selectValue(reader.getValue("EX_DISJ_MIN_POS"), DEF_EX_DISJ_MIN_POS);
        EX_DISJ_MAX_POS = selectValue(reader.getValue("EX_DISJ_MAX_POS"), DEF_EX_DISJ_MAX_POS);
        //EX_DISJ_MIN_BEL = selectValue(reader.getValue("EX_DISJ_MIN_BEL"), DEF_EX_DISJ_MIN_BEL);
        EX_DISJ_MIN_BEL = EX_DISJ_MAX_POS;
        EX_DISJ_MAX_BEL = selectValue(reader.getValue("EX_DISJ_MAX_BEL"), DEF_EX_DISJ_MAX_BEL);
        EX_DISJ_KNOW = selectValue(reader.getValue("EX_DISJ_KNOW"), DEF_EX_DISJ_KNOW);
    }

    /**
     * Arrays of thresholds for certain formula types. Order of elements in arrays is strictly defined and can't be other:
     * [MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW].
     */
    public final static double[] simpleThresholds = new double[]{MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW};
    public final static double[] conjThresholds = new double[]{CONJ_MIN_POS, CONJ_MAX_POS, CONJ_MIN_BEL, CONJ_MAX_BEL, CONJ_KNOW};
    public final static double[] disjThresholds = new double[]{DISJ_MIN_POS, DISJ_MAX_POS, DISJ_MIN_BEL, DISJ_MAX_BEL, DISJ_KNOW};
    public final static double[] exDisjThresholds = new double[]{EX_DISJ_MIN_POS, EX_DISJ_MAX_POS, EX_DISJ_MIN_BEL, EX_DISJ_MAX_BEL, EX_DISJ_KNOW};

    private static <T> T selectValue(Object configValue, T defValue){
        if (configValue != null)
            return (T) configValue;
        else{
            Logger.getAnonymousLogger().log(Level.INFO, "Default value used (" + defValue + ")", new ConfigValueNotFoundException());
            return defValue;
        }
    }

    /**
     * Returns array of appropriate thresholds for specified type. This thresholds are used in grounding process.
     *
     * @param type Type of formula.
     * @return Array of threshold values for given formula type.
     */
    public static double[] getThresholds(Formula.Type type) throws InvalidConfigurationException {
        double[] res = null;
        switch (type) {
            case SIMPLE_MODALITY:
                res= simpleThresholds;
                break;
            case MODAL_CONJUNCTION:
                res= conjThresholds;
                break;
            case MODAL_DISJUNCTION:
                res = disjThresholds;
                break;
            case MODAL_EXCLUSIVE_DISJUNCTION:
                res = exDisjThresholds;
                break;
            default:
                res= null;
        }
        if (res == null || res.length != 5)
            throw new InvalidConfigurationException("Invalid or not specified thresholds.");
        return res;
    }

    /**
     * This class holds dictionary of values from config file.
     * Instance of this class is created and dictionary populated while reading the XML file.
     *
     * @author Mateusz Gawlowski
     */
    public static class ConfigurationReader {

        private Map<String, Object> configValuesMap;

        public ConfigurationReader() {
            configValuesMap = new HashMap<>();
        }

        public void addValue(String key, Object value){
            configValuesMap.put(key, value);
        }

        public Object getValue(String key){
            return configValuesMap.get(key);
        }
    }

    /**
     * Custom converter of XStream for reading config file.
     *
     * @author Mateusz Gawlowski
     */
    public static class ConfigurationConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return clazz.equals(ConfigurationReader.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            throw new NotImplementedException();
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            ConfigurationReader configurationReader = new ConfigurationReader();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                String value = reader.getValue().replaceAll("\\s+",""); // removes all non-visible characters
                String name = reader.getNodeName();
                String type = reader.getAttribute("type");
                    switch (type != null ? type.toLowerCase() : "null") {
                        case "int": case "integer":
                            configurationReader.addValue(name, Integer.parseInt(value));
                            break;

                        case "double":
                            configurationReader.addValue(name, Double.parseDouble(value));
                            break;

                        case "bool": case "boolean":
                            configurationReader.addValue(name, Boolean.parseBoolean(value));
                            break;

                        case "char": case "character":
                            configurationReader.addValue(name, value.charAt(0));
                            break;

                        default:
                            configurationReader.addValue(name, value);
                    }

                reader.moveUp();
            }
            return configurationReader;
        }
    }
}
