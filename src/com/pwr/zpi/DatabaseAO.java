package com.pwr.zpi;

import java.io.File;
import java.sql.*;
import java.util.*;

import static com.pwr.zpi.Agent.objectTypeCollection;

/**
 *
 */
public class DatabaseAO {

    //todo unikatowe nazwy (filename, path) [utworzyc folder db jesli nie ma]
    //todo listener dla nowych rekordow (handler, trigger, callback)
    //todo testy
    //todo dokumentacja
    public static final String DEF_DATABASE_FILEPATH = "db/baza1.db";
    Connection connection;
    int lastTimestamp = -1;
    Agent agent;

    public DatabaseAO(Agent agent) {
        this.agent=agent;
        String path = (new File(DEF_DATABASE_FILEPATH)).getAbsolutePath();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addTablesForAllObjectTypes();
    }

    public DatabaseAO(Agent agent, String databaseFilename) {
        this.agent = agent;
        String path = (new File("db/" + databaseFilename)).getAbsolutePath();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
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
            columns.append(",").append(entry.getKey().getName());
            values.append(",").append(entry.getValue() ? 1 : 0);
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

    public Collection<Observation> fetchAllObservations() {
        //todo
        return null;
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
                        int value = resultSet.getInt(t.getName());
                        if(value == 1)
                            traits.put(t, true);
                        if(value == 0)
                            traits.put(t, false);
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
