package com.gluma.timesheet.services.dao;

import com.gluma.timesheet.conectivity.ConnectionManager;
import com.gluma.timesheet.datamdodel.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class EmployeeDAO{
private static ResultSet rsEmp;

private static StringBuilder stringBuilder;

    public static Employee searchEmployee (int empId) throws SQLException {

        String selectStmt = "SELECT * FROM employee WHERE idEmployee="+empId;
        try {

            rsEmp = ConnectionManager.dbExecuteQuery(selectStmt);

            Employee employee = getEmployeeFromResultSet(rsEmp);

            return employee;
        } catch (SQLException e) {
            System.out.println("While searching an employee with " + empId + " id, an error occurred: " + e);

            throw e;
        }
    }

    private static Employee getEmployeeFromResultSet(ResultSet rs) throws SQLException
    {
        Employee emp = null;
        if (rs.next()) {
            emp = new Employee();
            emp.setIdEmployee(rs.getInt("idEmployee"));
            emp.setName(rs.getString("Name"));
            emp.setSurname(rs.getString("Surname"));
            emp.setLogin(rs.getString("Login"));
            emp.setPassword(rs.getString("Password"));
        }
        return emp;
    }

    public static ObservableList<Employee> searchEmployees () throws SQLException {

        String selectStmt = "SELECT employee.idEmployee, employee.Name, employee.Surname, " +
                "employee.Login, employee.Password, acess.Type " +
                "FROM account JOIN employee ON account.idEmployee=employee.idEmployee " +
                "JOIN acess ON account.idAcess = acess.idAcess order by idEmployee";

        try {
            ResultSet rsEmployees = ConnectionManager.dbExecuteQuery(selectStmt);

            ObservableList<Employee> employeesList = getEmployeeList(rsEmployees);
            return employeesList;

        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private static ObservableList<Employee> getEmployeeList(ResultSet rs) throws SQLException {

        ObservableList<Employee> employeesList = FXCollections.observableArrayList();

        while (rs.next()) {
            Employee employee = new Employee(rs.getInt("idEmployee"), rs.getString("Name"), rs.getString("Surname"),
                    rs.getString("Login"), rs.getString("Password"), rs.getString("Type"));


            employeesList.add(employee);
        }
        return employeesList;
    }

    public static void deleteEmployeeWithId (int idEmployee) throws SQLException {
        String employeeStmt ="DELETE FROM employee WHERE idEmployee= "+ idEmployee;

        try {
            ConnectionManager.dbExecuteUpdate(employeeStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public static void insertEmployee (String name, String surname, int idAccess) throws SQLException {

        String insertEmployee = "INSERT INTO `databasetests`.`employee` (`Name`, `Surname`, `Login`, `Password`, `Seed`) VALUES " +
                "('"+name+"','"+surname+"','"+generateTemporyLoginData()+"','"+generateTemporyLoginData()+"','"+generateSeed()+"')";
        String insertAccount = "INSERT INTO `databasetests`.`account` (`idAcess`, `idEmployee`)VALUES" + "('"+idAccess+"','"+ConnectionManager.getId(insertEmployee)+"' )";
        try {
            ConnectionManager.dbExecuteUpdate(insertAccount);
            }
            catch (SQLException e) {
                System.out.print("Error occurred while insert Operation: " + e);
                throw e;
            }
    }

    private static int generateSeed(){
        Random random = new Random();
        int seed = random.nextInt(100)+1;
        return seed;
    }

    private static String generateTemporyLoginData() {
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";

        Random random = new Random();
        int length = random.nextInt(10)+5;

        String values = Capital_chars + Small_chars +
                numbers + symbols;

        String password = "";

        stringBuilder = new StringBuilder(password);

        for (int i = 0; i < length; i++)
        {
            stringBuilder.append(values.charAt(random.nextInt(values.length())));

        }
        password = stringBuilder.toString();
        return password;
    }

    public static void updateEmployee(int idEmployeeToEdit, String name, String surname, int access) {
        {
            String updateStmt = "UPDATE account join employee on account.idEmployee = employee.idEmployee join " +
                    "acess on account.idAcess = acess.idAcess set employee.Name = '" + name + "'" +
                    " , employee.Surname ='" + surname + "'" +
                    ", account.idAcess =" + access + " " +
                    "where employee.idEmployee =" + idEmployeeToEdit;
            try {
                ConnectionManager.dbExecuteUpdate(updateStmt);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void updateEmployeePasswordAndLogin(String login, String password){

        String updateStmt = "UPDATE employee SET Login = '"+login+"', Password = '"+password+"' WHERE idEmployee = '"+Employee.loggedEmployee.getIdEmployee()+"'";

        try {
            ConnectionManager.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEmployeeLogin(String login){

        String updateStmt = "UPDATE employee SET Login = '"+login+"' WHERE idEmployee = '"+Employee.loggedEmployee.getIdEmployee()+"'";

        try {
            ConnectionManager.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEmployeePassword(String password){

        String updateStmt = "UPDATE employee SET Password = '"+password+"' WHERE idEmployee = '"+Employee.loggedEmployee.getIdEmployee()+"'";

        try {
            ConnectionManager.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Employee> searchEmployeesNameSurnameId () throws SQLException {

        String selectStmt = "SELECT idEmployee, Name, Surname FROM employee";

        try {
            ResultSet rsEmployees = ConnectionManager.dbExecuteQuery(selectStmt);

            ObservableList<Employee> employeesList = getEmployeeListNameSurnameId(rsEmployees);
            return employeesList;

        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private static ObservableList<Employee> getEmployeeListNameSurnameId(ResultSet rs) throws SQLException {

        ObservableList<Employee> employeesList = FXCollections.observableArrayList();

        while (rs.next()) {
            Employee employee = new Employee(rs.getInt("idEmployee"), rs.getString("Name"), rs.getString("Surname"));
            employeesList.add(employee);
        }
        return employeesList;
    }
}
