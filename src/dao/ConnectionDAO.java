package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.DBConnection;
import model.DisplayScreen;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author brightoibe
 */
public class ConnectionDAO {

    private DisplayScreen screen;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public boolean loadDriver() {
        boolean ans;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            ans = true;
            screen.updateStatus("Mysql jdbc driver loaded......");
        } catch (ClassNotFoundException e) {
            ans = false;
            screen.updateStatus(e.getMessage());
        } catch (InstantiationException ie) {
            System.err.println(ie.getMessage());
            ans = false;
            screen.updateStatus(ie.getMessage());
        } catch (IllegalAccessException iae) {
            System.err.println(iae.getMessage());
            screen.updateStatus(iae.getMessage());
            ans = false;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            screen.updateStatus(ex.getMessage());
            ans = false;
        }
        return ans;
    }

    public boolean connect(DBConnection con) {
        boolean ans;
        try {
            String conString = "jdbc:mysql://" + con.getHostName() + ":" + con.getMysqlPort() + "/" + con.getDatabase() + "?user=" + con.getUsername() + "&password=" + con.getPassword();
            connection = DriverManager.getConnection(conString);
            ans = true;
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            displayErrors(ex);
            ans = false;
        }
        return ans;
    }

    public void closeConnections() {
        try {
            if (connection != null) {
                connection.commit();
                connection.close();
                
            }
        } catch (SQLException ex) {
            screen.showError(ex.getMessage());
        }
    }

    public void displayErrors(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        ex.printStackTrace();
    }

    public void setDisplay(DisplayScreen screen) {
        this.screen = screen;
    }

    public void removeLock() {
        if(connection==null){
            screen.updateStatus("Click Connect button first");
            return;
        }
        String sql_text1, sql_text2;
        sql_text1 = "delete from reporting_report_processor";
        sql_text2 = "delete from reporting_report_request";
        try {
            statement = connection.createStatement();
            statement.addBatch(sql_text1);
            statement.addBatch(sql_text2);
            statement.executeBatch();
            screen.showSuccess("queue has been cleared");
        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            try {
                statement.clearBatch();
                statement.close();
                connection.commit();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

    }

    public void handleException(Exception e) {
        screen.updateStatus(e.getMessage());
        e.printStackTrace();
    }

}
