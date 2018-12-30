package com.gluma.timesheet.conectivity;
import com.github.vldrus.sql.rowset.CachedRowSetWrapper;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());
    private static final String url = "jdbc:mysql://127.0.0.1:3306/databasetests?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw";
    private static final String username = "root";
    private static final String password = "Susanoo12345@";
    private static Connection con=null;

    public static Connection dbConnect(){
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Connection Failed! Check output console",e);
        }
        return con;
    }

    public static Connection dbDisconnect() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Something went wrong while disconnecting",e);
        }
        return con;
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException {
        CachedRowSetWrapper crs;
        dbConnect();
        try (PreparedStatement stmt = con.prepareStatement(queryStmt);
            ResultSet resultSet = stmt.executeQuery(queryStmt)){
            System.out.println(queryStmt + "\n");
            crs = new CachedRowSetWrapper();
            crs.populate(resultSet);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Problem occurred at executeQuery operation",e);
            throw e;
        }finally{dbDisconnect();}
        return crs;
    }

    public static void dbExecuteUpdate(String sqlStmt) throws SQLException{
        Statement stmt = null;
        try {
            dbConnect();
            stmt = con.createStatement();

            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Problem occurred at executeUpdate operation",e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }

    public static int getId(String idEmployee) throws SQLException{
        PreparedStatement pstmt = null;
        int id = 0;
        try{
            dbConnect();
            pstmt = con.prepareStatement(idEmployee, Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()){
                id = resultSet.getInt(1);
            }
        }
        catch (SQLException e){
            logger.log(Level.SEVERE,"Problem occurred agetting id: ",e);
        }
        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dbDisconnect();
        }
        return id;
    }
}