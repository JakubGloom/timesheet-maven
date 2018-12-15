package com.gluma.timesheet.datamdodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee{

    private IntegerProperty idEmployee = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty access = new SimpleStringProperty();
    private IntegerProperty seed = new SimpleIntegerProperty();
    private  StringProperty fullName = new SimpleStringProperty();

    public static Employee loggedEmployee;

    public Employee(){}
    public Employee(int idEmployee, String name, String surname, String login, String password) {
        this.idEmployee.set(idEmployee);
        this.name.set(name);
        this.surname.set(surname);
        this.login.set(login);
        this.password.set(password);
    }

    public Employee(int idEmployee, String name, String surname, String login, String password, String access) {
        this.idEmployee.set(idEmployee);
        this.name.set(name);
        this.surname.set(surname);
        this.login.set(login);
        this.password.set(password);
        this.access.set(access);
    }

    public Employee(int idEmployee, String name, String surname) {
        this.idEmployee.set(idEmployee);
        this.name.set(name);
        this.surname.set(surname);
        this.fullName.set(name.concat(" ".concat(surname)));
    }

    public Employee(String name, String surname) {
        this.name.set(name);
        this.surname.set(surname);
        this.fullName.set(name.concat(" ".concat(surname)));
    }

    public int getIdEmployee() {
        return idEmployee.get();
    }

    public IntegerProperty idEmployeeProperty() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee.set(idEmployee);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getAccess() {
        return access.get();
    }

    public StringProperty accessProperty() {
        return access;
    }

    public void setAccess(String access) {
        this.access.set(access);
    }

    public int getSeed() {
        return seed.get();
    }

    public IntegerProperty seedProperty() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed.set(seed);
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }
}
