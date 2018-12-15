package com.gluma.timesheet.conectivity;
import com.github.vldrus.sql.rowset.CachedRowSetWrapper;

import java.sql.*;

public class ConnectionManager {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/databasetests?useSSL=false";
    private static final String username = "root";
    private static final String password = "Susanoo12345@";
    private static Connection con=null;

    public static Connection dbConnect() throws SQLException, ClassNotFoundException {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
        return con;
    }

    public static Connection dbDisconnect() throws SQLException {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            throw e;
        }
        return con;
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        CachedRowSetWrapper crs;
        dbConnect();
        try (PreparedStatement stmt = con.prepareStatement(queryStmt);
            ResultSet resultSet = stmt.executeQuery(queryStmt)){
            System.out.println(queryStmt + "\n");
            crs = new CachedRowSetWrapper();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        }finally{dbDisconnect();}
        return crs;
    }

    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = con.createStatement();

            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }

    public static int getId(String idEmployee) throws SQLException, ClassNotFoundException {
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
            System.out.println("Problem occured at getting id: " + e);
            throw e;
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