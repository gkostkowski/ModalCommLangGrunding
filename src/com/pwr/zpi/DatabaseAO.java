package com.pwr.zpi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static com.pwr.zpi.Agent.objectTypeCollection;

/**
 *
 */
public class DatabaseAO {

    //todo testy
    //todo dokumentacja
    public static final String DEF_DATABASE = "baza1.db";
    private Connection dbConnection;
    private Map<String, Integer> nameIndexMap;
    private Agent agent;

    public DatabaseAO(Agent agent) {
        this.agent = agent;
        String dbFilePath = Paths.get("db/" + DEF_DATABASE).toString();
        init(dbFilePath);
    }

    /*public DatabaseAO(Agent agent, String databaseFilename) {
        this.agent = agent;
        String dbFilePath = Paths.get("db/" + databaseFilename).toString();
        init(dbFilePath);
    }*/

    @Deprecated
    private void deleteDatabase(){
        try {
            if(Files.exists(Paths.get("db/" + DEF_DATABASE)))
                Files.delete(Paths.get("db/" + DEF_DATABASE));
        } catch (IOException e) {
            System.out.println("Probable solution: disconnect from database.");
            e.printStackTrace();
        }
    }

    private void init(String dbFilePath){
        nameIndexMap = new HashMap<>();
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

        if(flagValue == 1)
            return true;
        else
            return false;
    }

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
     * This method will be called when new observation(s) will appear.
     */
    public void updateAgentMemory() {
        if(isInsertFlagPositive())
            agent.discoverObservations(fetchNewObservations());
    }
}
