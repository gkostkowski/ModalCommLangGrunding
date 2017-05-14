package com.pwr.zpi;

import java.io.File;
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

    //todo unikatowe nazwy (filename, path)?
    //todo listener dla nowych rekordow (handler, trigger, callback)
    //todo para nazwa tabeli, index (rowid)
    //todo testy
    //todo dokumentacja
    public static final String DEF_DATABASE = "baza1.db";
    private Connection connection;
    private int lastTimestamp = -1;
    private Agent agent;

    public DatabaseAO(Agent agent) {
        this.agent = agent;
        String path = Paths.get("db/" + DEF_DATABASE).toString();
        init(path);
    }

    public DatabaseAO(Agent agent, String databaseFilename) {
        this.agent = agent;
        String path = Paths.get("db/" + databaseFilename).toString();
        init(path);
    }

    @Deprecated
    private void deleteDatabase(){
        try {
            if(Files.exists(Paths.get("db/" + DEF_DATABASE)))
                Files.delete(Paths.get("db/" + DEF_DATABASE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(String path){
        try {
            if(Files.notExists(Paths.get("db")))
                Files.createDirectories(Paths.get("db"));
            deleteDatabase();
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        addTablesForAllObjectTypes();
    }

    private void addTablesForAllObjectTypes() {
        StringBuilder sql = new StringBuilder();
        try {
            Statement statement = connection.createStatement();

            for(ObjectType ob: objectTypeCollection) {
                sql.append("CREATE TABLE IF NOT EXISTS \"").append(ob.getTypeId()).append("\" (\n");
                sql.append("timestamp integer NOT NULL,\n");
                sql.append("id text NOT NULL,\n");

                for (Trait t : ob.getTraits()) {
                    sql.append(t.getName()).append(" integer,\n");
                }
                sql.deleteCharAt(sql.length()-2); // removes last comma
                sql.append(");\n");

                statement.addBatch(sql.toString());
                sql.setLength(0);
            }
            statement.executeBatch();
            statement.clearBatch();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

        columns.append("id").append(",").append("timestamp");
        values.append("\"").append(idNumber).append("\"").append(",").append(timestamp);

        for (Map.Entry<Trait, Boolean> entry : traits.entrySet())
        {
            if(entry.getValue() != null) {
                columns.append(",").append(entry.getKey().getName());
                values.append(",").append(entry.getValue() ? 1 : 0);
            }
        }

        String sql = "INSERT INTO \"" + tableName + "\" (" + columns + ") VALUES (" + values + ");";

        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Collection<Observation> fetchNewObservations() {
        //todo lepiej
        Collection<Observation> newObservations = new HashSet<Observation>();

        int newTimestamp = lastTimestamp;
        StringBuilder sql = new StringBuilder();
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet tableNamesSet = md.getTables(null, null, "%", null);

            while (tableNamesSet.next()) {
                String tableName = tableNamesSet.getString(3);
                sql.append("SELECT * FROM \"").append(tableName).append("\" WHERE timestamp > ").append(String.valueOf(lastTimestamp));

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql.toString());

                while(resultSet.next()) {
                    Identifier identifier = new QRCode(resultSet.getString("id"));
                    int timestamp = resultSet.getInt("timestamp");

                    List<Trait> typeTraits = identifier.getType().getTraits();
                    Map<Trait, Boolean> traits = new HashMap<>();

                    for(Trait t: typeTraits){
                        String value = resultSet.getString(t.getName());
                        if (value == null)
                            traits.put(t, null);
                        else {
                            switch(value){
                                case "1":
                                    traits.put(t, true);
                                    break;
                                case "0":
                                    traits.put(t, false);
                            }
                        }

                    }
                    newObservations.add(new Observation(identifier, traits, timestamp));

                    if(timestamp > newTimestamp)
                        newTimestamp = timestamp;
                }
                statement.close();
                sql.setLength(0);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        lastTimestamp = newTimestamp;
        return newObservations;
    }

    /**
     * This method will be called when new observation(s) will appear.
     */
    public void updateAgentMemory() {
        agent.discoverObservations(fetchNewObservations());
    }
}
