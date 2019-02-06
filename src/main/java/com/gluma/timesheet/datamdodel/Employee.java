package com.gluma.timesheet.datamdodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Employee implements Serializable,BaseModel{

    private SimpleIntegerProperty idEmployee;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private SimpleStringProperty access;
    private SimpleStringProperty fullName;

    public Employee(){}
    public Employee(int idEmployee, String name, String surname, String login, String password) {
        this(idEmployee, name, surname);
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
    }

    public Employee(int idEmployee, String name, String surname, String login, String password, String access) {
        this(idEmployee, name, surname,login,password);
        this.access = new SimpleStringProperty(access);
    }

    public Employee(int idEmployee, String name, String surname) {
        this(name, surname);
        this.idEmployee = new SimpleIntegerProperty(idEmployee);
    }

    public Employee(String name, String surname) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.fullName = new SimpleStringProperty(name + " " + surname);
    }

    public int getIdEmployee() {
        return idEmployee.get();
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee.set(idEmployee);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getAccess() {
        return access.get();
    }

    public void setAccess(String access) {
        this.access.set(access);
    }

    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", nameame='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}
