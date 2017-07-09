package com.pwr.zpi.io;

import com.pwr.zpi.core.memory.episodic.Observation;
import com.pwr.zpi.core.memory.semantic.ObjectType;
import com.pwr.zpi.core.memory.semantic.identifiers.Identifier;
import com.pwr.zpi.exceptions.IdentifierClassNotFoundException;
import com.pwr.zpi.language.Trait;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class acts as data access layer for SQLite database that is used to gather observations
 * and return new observations to agent in order to update its memory.
 *
 * @author Mateusz Gawlowski
 */
public class DatabaseAO {

    private static final String DATABASE_FILENAME = Configuration.DATABASE_FILENAME;
    private static final String IDENTIFIERS_PATH = Configuration.IDENTIFIERS_PATH;

    private static Collection<ObjectType> objectTypeCollection;
    private Connection dbConnection;

    /**
     * This map contains names of tables in database and index of last fetched observation from given table.
     */
    private Map<String, Integer> nameIndexMap;

    public DatabaseAO() {
        init();
    }

    public DatabaseAO(Collection<ObjectType> objectTypes) {
        objectTypeCollection = objectTypes;
        init();
    }

    /**
     * Performs initials actions for database, such as: ensuring that 'db' directory is present,
     * establishing connection to database, building instances of class' fields.
     * It is also creating database schema appropriate to config file.
     */
    private void init(){
        nameIndexMap = new HashMap<>();
        String dbFilePath = Paths.get("db/" + DATABASE_FILENAME).toString();
        try {
            if(Files.notExists(Paths.get("db")))
                Files.createDirectories(Paths.get("db"));
            deleteDatabase();
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            addTablesForAllObjectTypes();
            setInsertFlag(true);
        } catch (IOException | SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Failed to connect to database.", e);
        }
    }

    /**
     * Deletes default database file, erasing all collected data.
     * This method is only for simplifying tests and should not be used in real agent.
     */
    @Deprecated
    public void deleteDatabase(){
        try {
            Path path = Paths.get("db/" + DATABASE_FILENAME);
            if(new File(path.toString()).exists() && Files.isReadable(path) && Files.isExecutable(path))
                Files.delete(Paths.get("db/" + DATABASE_FILENAME));
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Failed to delete database file.", e);
        }
    }

    /**
     * Creates database schema by adding (if not already present) tables for each ObjectType
     * derived from config file and a table (InsertFlag) for monitoring whether any record has been added
     * since last data fetch via triggers defined for each ObjectType table (also in this method).
     * Additionally all tables are created with adequate constraints.
     * This method also fills nameIndexMap with names of tables and index of last fetched record in each table.
     */
    private void addTablesForAllObjectTypes() {
        StringBuilder SQLCommandText = new StringBuilder();
        String tableName;
        try {
            Statement SQLStatement = dbConnection.createStatement();

            SQLCommandText.append("CREATE TABLE IF NOT EXISTS InsertFlag (flag integer NOT NULL, CHECK (flag IN (0,1)));");
            SQLStatement.addBatch(SQLCommandText.toString());
            SQLCommandText.setLength(0);

            SQLCommandText.append("INSERT INTO InsertFlag (flag) SELECT 0 WHERE NOT EXISTS (SELECT * FROM InsertFlag);");
            SQLStatement.addBatch(SQLCommandText.toString());
            SQLCommandText.setLength(0);

            if (objectTypeCollection == null)
                objectTypeCollection = ObjectType.getObjectTypes();
            for(ObjectType objectType : objectTypeCollection) {
                tableName = objectType.getTypeId();
                SQLCommandText.append("CREATE TABLE IF NOT EXISTS\"").append(tableName).
                        append("\"(timestamp integer NOT NULL,id text NOT NULL,");

                nameIndexMap.put(tableName, 0);

                for (Trait trait: objectType.getTraits()) {
                    SQLCommandText.append(trait.getName()).append(" integer CHECK (").append(trait.getName()).append(" IN (0,1)),");
                }
                SQLCommandText.deleteCharAt(SQLCommandText.length()-1); // removes last comma
                SQLCommandText.append(");");

                SQLStatement.addBatch(SQLCommandText.toString());
                SQLCommandText.setLength(0);

                SQLCommandText.append("CREATE TRIGGER IF NOT EXISTS InsertFlagTrigger").append(tableName)
                        .append(" AFTER INSERT ON\"").append(tableName).append("\"BEGIN UPDATE InsertFlag SET flag = 1; END;");

                SQLStatement.addBatch(SQLCommandText.toString());
                SQLCommandText.setLength(0);
            }
            SQLStatement.executeBatch();
            SQLStatement.clearBatch();
            SQLStatement.close();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Failed to create database's schema.", e);
        }
    }

    /**
     * Adds observation to proper table on database. By doing so it also launches a SQL trigger that changes
     * value of flag in InsertFlag table so now it implies that there are new observations.
     *
     * @param observation Observation that is being added to database.
     */
    public void addNewObservation(Observation observation) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        String tableName = observation.getType().getTypeId();
        String idNumber = observation.getIdentifier().getIdNumber();
        String timestamp = String.valueOf(observation.getTimestamp());
        Map<Trait, Boolean> observationTraits = observation.getValuedTraits();

        columns.append("id,timestamp");
        values.append("\"").append(idNumber).append("\",").append(timestamp);

        for (Map.Entry<Trait, Boolean> traitBooleanEntry : observationTraits.entrySet())
        {
            if(traitBooleanEntry.getValue() != null) {
                columns.append(",").append(traitBooleanEntry.getKey().getName());
                values.append(",").append(traitBooleanEntry.getValue() ? 1 : 0);
            }
        }
        String SQLCommandText = "INSERT INTO \"" + tableName + "\" (" + columns + ") VALUES (" + values + ");";
        try {
            Statement SQLStatement = dbConnection.createStatement();
            SQLStatement.execute(SQLCommandText);
            SQLStatement.close();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Failed to add new observation.", e);
        }
    }

    /**
     * This method is used to obtain observations that haven't been fetched yet.
     * It also resets the InsertFlag (to imply that there are no new observations now)
     * and updates indexes of last fetched records in nameIndexMap.
     *
     * @return Collection of new observations from database.
     */
    public Collection<Observation> fetchNewObservations() {
        Collection<Observation> newObservations = new HashSet<>();
        String SQLCommandText, tableName; int lastIndex;
        Statement SQLStatement; ResultSet queryResultSet;

        List<Trait> obsTypeTraits; String observationTraitValue;
        Identifier observationIdentifier; int observationTimestamp; Map<Trait, Boolean> observationTraits;
        setInsertFlag(false);
        try {
            for(Map.Entry<String, Integer> nameIndexEntry: nameIndexMap.entrySet()){
                tableName = nameIndexEntry.getKey();
                lastIndex = nameIndexEntry.getValue();
                SQLCommandText = "SELECT * FROM \"" + tableName + "\" WHERE rowid > " + lastIndex;

                nameIndexEntry.setValue(getMaxIndex(tableName));
                SQLStatement = dbConnection.createStatement();
                queryResultSet = SQLStatement.executeQuery(SQLCommandText);

                while(queryResultSet.next()) {
                    observationIdentifier = createIdentifier(queryResultSet.getString("id"));
                    observationTimestamp = queryResultSet.getInt("timestamp");

                    obsTypeTraits = observationIdentifier.getType().getTraits();
                    observationTraits = new HashMap<>();

                    for(Trait trait: obsTypeTraits){
                        observationTraitValue = queryResultSet.getString(trait.getName());
                        if (observationTraitValue == null)
                            observationTraits.put(trait, null);
                        else {
                            switch(observationTraitValue){
                                case "1":
                                    observationTraits.put(trait, true);
                                    break;
                                case "0":
                                    observationTraits.put(trait, false);
                            }
                        }
                    }
                    newObservations.add(new Observation(observationIdentifier, observationTraits, observationTimestamp));
                }
                SQLStatement.close();
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Failed to fetch new observations.", e);
        }
        return newObservations;
    }

    /**
     * Creates proper identifier for given id number by looking among all implemented identifiers
     * and checking whether it matches identifier's scheme.
     *
     * @param idNumber Number of identifier.
     * @return Proper identifier or null if not found.
     */
    private Identifier createIdentifier(String idNumber) {
        Reflections reflections = new Reflections(IDENTIFIERS_PATH);
        Set<Class<? extends Identifier>> classes = reflections.getSubTypesOf(Identifier.class);

        for (Class clazz: classes) {
            try {
                Identifier identifier = (Identifier) clazz.newInstance();
                if(identifier.isIdMemberOf(idNumber)) {
                    identifier.setId(idNumber);
                    return identifier;
                }
            } catch (IllegalAccessException | InstantiationException e) {
                Logger.getAnonymousLogger().log(Level.WARNING, "Error when trying to create identifier for idNumber: " + idNumber + ".", e);
            }
        }
        Logger.getAnonymousLogger().log(Level.WARNING, "Failed to find identifier class for idNumber: " + idNumber + ".", new IdentifierClassNotFoundException());
        return null;
    }

    /**
     * Used to get index of last record from given table.
     *
     * @param tableName Name of table in database.
     * @return Index if last record.
     */
    private int getMaxIndex(String tableName){
        int index = 0;
        String SQLCommandText = "SELECT MAX(rowid) FROM \"" + tableName + "\"";
        try {
            Statement SQLStatement = dbConnection.createStatement();
            ResultSet queryResultSet = SQLStatement.executeQuery(SQLCommandText);
            index = queryResultSet.getInt(1);
            SQLStatement.close();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Failed to get max index from table " + tableName + ".", e);
        }
        return index;
    }

    /**
     * Used to determine whether any observations has been added to database since last fetch
     * via checking value of flag in InsertFlag table.
     *
     * @return True when there are new records, false when aren't.
     */
    public boolean isInsertFlagPositive(){
        int flagValue = 0;
        try {
            String SQLCommandText = "SELECT * FROM InsertFlag;";
            Statement SQLStatement = dbConnection.createStatement();
            ResultSet queryResultSet = SQLStatement.executeQuery(SQLCommandText);
            flagValue = queryResultSet.getInt(1);
            SQLStatement.close();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Failed to check flag's status.", e);
        }
        return flagValue == 1;
    }

    /**
     * Resets value of flag in InsertFlag so it implies that there are no new observations at the moment.
     */
    private void setInsertFlag(boolean isAnyNew){
        try {
            String SQLCommandText = "UPDATE InsertFlag SET flag = " + (isAnyNew ? 1 : 0) + ";";
            Statement SQLStatement = dbConnection.createStatement();
            SQLStatement.execute(SQLCommandText);
            SQLStatement.close();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Failed to set flag to " + isAnyNew + ".", e);
        }
    }
}
