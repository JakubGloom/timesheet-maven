package com.gluma.timesheet.services;

import com.gluma.timesheet.datamdodel.Employee;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EmployeeService extends Service<ObservableList<Employee>>
{
    @Override
    protected Task<ObservableList<Employee>> createTask() {
        return new Task<ObservableList<Employee>>() {
            @Override
            protected ObservableList<Employee> call() {
                ObservableList<Employee> employees = null;
                return employees;
            }
        };
    }
}
