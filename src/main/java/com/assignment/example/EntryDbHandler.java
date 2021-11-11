package com.assignment.example;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Created by Joel on 28/07/2018.
 */
public class EntryDbHandler {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public static void init() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("ERROR: Failed to load HSQLDB JDBC driver.");
        }
    }

    private static Connection setConnection() throws SQLException {
       return DriverManager.getConnection("jdbc:hsqldb:file:src/res/ENTRIES", "sa", "");
    }

    void createDb() throws SQLException {
        init();
        try(Connection connection = setConnection(); Statement statement = connection.createStatement();){
            statement.execute("CREATE TABLE ENTRIES(id VARCHAR(20) NOT NULL,state VARCHAR(10) NOT NULL,type VARCHAR(20), host VARCHAR(10), timestamp VARCHAR(50) NOT NULL, alert BIT DEFAULT 0)");
            connection.commit();
        }
    }

    void insertRecord(Entry entry) throws SQLException {
        try (Connection connection = setConnection(); Statement statement = connection.createStatement();){
            String update = "INSERT INTO ENTRIES VALUES ('" + entry.getId().toUpperCase() + "','" + entry.getState()+ "','" +  entry.getType()+ "','" +  entry.getHost()+ "','" + entry.getTimestamp() + "',0)";
            statement.executeUpdate(update);
            connection.commit();
        }
    }

    void alertEntry(String id) throws SQLException {
        try (Connection connection = setConnection(); Statement statement = connection.createStatement();){
            statement.executeUpdate("UPDATE ENTRIES SET alert = 1 WHERE id = '"+id.toUpperCase()+"'");
            connection.commit();
        }
    }

    void lookupAlert() throws SQLException {
        try (Connection connection = setConnection(); Statement statement = connection.createStatement();){
            ResultSet result = statement.executeQuery("SELECT * FROM ENTRIES WHERE alert = 1");
            while (result.next()){
                logger.info("Longest Event Is "+result.getString("id"));
                System.out.println("Longest Event Is "+result.getString("id"));
            }
        }
    }
    
    void getAllEntries() throws SQLException {
        try (Connection connection = setConnection(); Statement statement = connection.createStatement();){
            ResultSet result = statement.executeQuery("SELECT * FROM ENTRIES");
            while (result.next()){
                logger.info("ID "+result.getString("id")+" Host "+result.getString("host") + " alerted on "+result.getString("timestamp"));
            }
        }
    }

    void dropDb() throws SQLException {
        try (Connection connection = setConnection(); Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE ENTRIES");
            connection.commit();
        }
    }

    void destroy() throws SQLException {
        try (Connection connection = setConnection(); Statement statement = connection.createStatement();) {
            dropDb();
            statement.executeUpdate("SHUTDOWN");
            connection.commit();
        }
    }
}
