package com.pwr.zpi.io;

import com.pwr.zpi.*;
import com.pwr.zpi.episodic.Observation;
import com.pwr.zpi.language.Trait;
import com.pwr.zpi.semantic.Identifier;
import com.pwr.zpi.semantic.ObjectType;
import com.pwr.zpi.semantic.QRCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static com.pwr.zpi.Agent.objectTypeCollection;

/**
 * This class acts as data access layer for SQLite database that is used to gather observations
 * and return new observations to agent in order to update its memory.
 */
public class DatabaseAO {

    public static final String DEF_DATABASE_FILENAME = "baza1.db";
    private Connection dbConnection;
    private Map<String, Integer> nameIndexMap;
    private Agent agent;

    public DatabaseAO(Agent agent) {
        this.agent = agent;
        init();
    }

    /**
     * Performs initials actions for database, such as: ensuring that 'db' directory is present,
     * establishing connection to database, building instances of class' fields.
     * It is also creating database schema appropriate to config file and performs agent's first memory update
     * in case that existing database is not empty.
     */
    private void init(){
        nameIndexMap = new HashMap<>();
        String dbFilePath = Paths.get("db/" + DEF_DATABASE_FILENAME).toString();
        try {
            if(Files.notExists(Paths.get("db")))
                Files.createDirectories(Paths.get("db"));
            deleteDatabase();
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        addTablesForAllObjectTypes();
        updateAgentMemory();
    }

    /**
     * Deletes default database file, erasing all collected data.
     * This method is only for simplifying tests and is set to be removed.
     */
    @Deprecated
    private void deleteDatabase(){
        try {
            if(Files.exists(Paths.get("db/" + DEF_DATABASE_FILENAME)))
                Files.delete(Paths.get("db/" + DEF_DATABASE_FILENAME));
        } catch (IOException e) {
            System.out.println("Probable solution: disconnect from database in your IDE.");
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    /**
     * Adds observation to proper table on database. It also launches a SQL trigger that changes value of flag
     * in InsertFlag table so now it implies that there are new observations.
     * @param observation Observation that is being added to database.
     */
    public void addNewObservation(Observation observation) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        String tableName = observation.getType().getTypeId();
        String idNumber = observation.getIdentifier().getIdNumber();
        String timestamp = String.valueOf(observation.getTimestamp());
        Map<Trait, Boolean> traits = observation.getValuedTraits();

        columns.append("id,timestamp");
        values.append("\"").append(idNumber).append("\",").append(timestamp);

        for (Map.Entry<Trait, Boolean> traitBooleanEntry : traits.entrySet())
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
            e.printStackTrace();
        }
    }

    /**
     * This method is used to obtain observations that haven't been fetched yet.
     * It also resets the InsertFlag (to imply that there are no new observations now)
     * and updates indexes of last fetched records in nameIndexMap.
     * @return Collection of new observations from database.
     */
    private Collection<Observation> fetchNewObservations() {
        Collection<Observation> newObservations = new HashSet<>();
        String SQLCommandText, tableName, traitValue; int lastIndex, obsTimestamp;
        Statement SQLStatement; ResultSet queryResultSet; Identifier obsIdentifier;
        List<Trait> typeTraits; Map<Trait, Boolean> obsTraits;
        resetFlag();
        try {
            for(Map.Entry<String, Integer> nameIndexEntry: nameIndexMap.entrySet()){
                tableName = nameIndexEntry.getKey();
                lastIndex = nameIndexEntry.getValue();
                SQLCommandText = "SELECT * FROM \"" + tableName + "\" WHERE rowid > " + lastIndex;

                nameIndexEntry.setValue(getMaxIndex(tableName));
                SQLStatement = dbConnection.createStatement();
                queryResultSet = SQLStatement.executeQuery(SQLCommandText);

                while(queryResultSet.next()) {
                    obsIdentifier = new QRCode(queryResultSet.getString("id"));
                    obsTimestamp = queryResultSet.getInt("timestamp");

                    typeTraits = obsIdentifier.getType().getTraits();
                    obsTraits = new HashMap<>();

                    for(Trait trait: typeTraits){
                        traitValue = queryResultSet.getString(trait.getName());
                        if (traitValue == null)
                            obsTraits.put(trait, null);
                        else {
                            switch(traitValue){
                                case "1":
                                    obsTraits.put(trait, true);
                                    break;
                                case "0":
                                    obsTraits.put(trait, false);
                            }
                        }
                    }
                    newObservations.add(new Observation(obsIdentifier, obsTraits, obsTimestamp));
                }
                SQLStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newObservations;
    }

    /**
     * Used to get index of last record from given table.
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
            e.printStackTrace();
        }
        return index;
    }

    /**
     * Used to determine whether any observations has been added to database since last fetch
     * via checking value of flag in InsertFlag table.
     * @return true when there are new records, false when aren't
     */
    private boolean isInsertFlagPositive(){
        int flagValue = 0;
        try {
            String SQLCommandText = "SELECT * FROM InsertFlag;";
            Statement SQLStatement = dbConnection.createStatement();
            ResultSet queryResultSet = SQLStatement.executeQuery(SQLCommandText);
            flagValue = queryResultSet.getInt(1);
            SQLStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flagValue == 1;
    }

    /**
     * Resets value of flag in InsertFlag so it implies that there are no new observations at the moment.
     */
    private void resetFlag(){
        try {
            String SQLCommandText = "UPDATE InsertFlag SET flag = 0;";
            Statement SQLStatement = dbConnection.createStatement();
            SQLStatement.execute(SQLCommandText);
            SQLStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update agent's memory with new observations.
     */
    public void updateAgentMemory() {
        if(isInsertFlagPositive())
            agent.discoverObservations(fetchNewObservations());
    }
}
