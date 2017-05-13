package com.pwr.zpi;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;

import static com.pwr.zpi.Agent.objectTypeCollection;

/**
 *
 */
public class DatabaseAO {

    //todo unikatowe nazwy
    //todo listener dla nowych rekordow
    public static final String DEF_DATABASE_FILEPATH = "db/baza1.db";
    Connection connection;

    public DatabaseAO() {
        String path = (new File(DEF_DATABASE_FILEPATH)).getAbsolutePath();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addTablesForAllObjectTypes();
    }

    public DatabaseAO(String databaseFilename) {
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
        //todo
        return null;
    }
}
