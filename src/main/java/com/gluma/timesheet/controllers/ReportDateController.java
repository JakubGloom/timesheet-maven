package com.gluma.timesheet.controllers;

import com.gluma.timesheet.datamdodel.Employee;
import com.gluma.timesheet.datamdodel.Event;
import com.gluma.timesheet.datamdodel.Task;
import com.gluma.timesheet.services.dao.EmployeeDAO;
import com.gluma.timesheet.services.dao.EventDAO;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ReportDateController implements Initializable {

    @FXML
    private JFXTreeTableView<Event> treeTableViewReport;

    @FXML
    private TreeTableColumn<Event, String> columnEmployee;

    @FXML
    private TreeTableColumn<Event, Task> columnTask;

    @FXML
    private TreeTableColumn<Event , Integer> columnMinutes;


    @FXML
    private JFXDatePicker datePickerFromDate;

    @FXML
    private JFXDatePicker datePickerToDate;


    @FXML
    private TableView<Employee> tableViewEmployeeToPick;

    @FXML
    private TableColumn<Employee, String> columnName;

    @FXML
    private TableColumn<Employee, String> columnSurname;

    @FXML
    private TableColumn<Employee, Integer> columnIdEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        columnIdEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("Surname"));

        columnEmployee.setCellValueFactory(new TreeItemPropertyValueFactory<>("fullName"));
        columnTask.setCellValueFactory(new TreeItemPropertyValueFactory<>("task"));
        columnMinutes.setCellValueFactory(new TreeItemPropertyValueFactory<>("time"));

        tableViewEmployeeToPick.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewEmployeeToPick.getSelectionModel().setCellSelectionEnabled(true);

        try {
            loadEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public  void openWorkdayScene(ActionEvent event) {
        WorkdayController workdayController = new WorkdayController();
        workdayController.openWorkdayScene(event);
    }

    @FXML
    private void loadEmployees() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Employee> empData = EmployeeDAO.searchEmployeesNameSurnameId();
            tableViewEmployeeToPick.setItems(empData);
        } catch (SQLException e){
            System.out.println("Error occurred while getting employees information from DB" + e);
            throw e;
        }
    }

    @FXML
    public void reportForOnePickedEmployee(){

        TreeItem<Event> itemRoot = new TreeItem<>();
        ObservableList<Employee> selectedEmployees = tableViewEmployeeToPick.getSelectionModel().getSelectedItems();

        for (Employee selectedEmployee: selectedEmployees) {
            Event eventsFromEmployee = new Event(selectedEmployee);
            TreeItem<Event> itemBranch = new TreeItem<>(eventsFromEmployee);

            HashMap<String,Event> employeeEventsToSum = new HashMap<>();

            ArrayList<TreeItem<Event>> eventsToInsert = new ArrayList<>();
            ObservableList<Event> employeeEvents = null;

            try {
                employeeEvents = EventDAO.singleDateToDateReport(selectedEmployee, datePickerFromDate.getValue(),datePickerToDate.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            for (Event employeeEventsToInsert : employeeEvents){
                if (employeeEventsToSum.containsKey(employeeEventsToInsert.getTask().getName())){
                    employeeEventsToSum.get(employeeEventsToInsert.getTask().getName()).addTime(employeeEventsToInsert.getTime());
                }

                else {
                    employeeEventsToSum.put(employeeEventsToInsert.getTask().getName(), employeeEventsToInsert);
                }
            }

            addingItems(employeeEventsToSum, eventsToInsert);

            itemBranch.getChildren().setAll(eventsToInsert);
            itemRoot.getChildren().add(itemBranch);
        }

        treeTableViewReport.setRoot(itemRoot);
        treeTableViewReport.setShowRoot(false);
    }

    @FXML
    public void reportForAll(){
        ObservableList<Employee> employeesEvents = tableViewEmployeeToPick.getItems();

        TreeItem<Event> itemRoot = new TreeItem<>();
        for (Employee employeeToInsert: employeesEvents) {
            Event eventsFromEmployee = new Event(employeeToInsert);

            HashMap<String,Event> employeeEventsToSum = new HashMap<>();

            ArrayList<TreeItem<Event>> eventsToInsert = new ArrayList<>();
            TreeItem<Event> itemBranch = new TreeItem<>(eventsFromEmployee);
            ObservableList<Event> employeeEvents = null;

            try {
                employeeEvents = EventDAO.singleDateToDateReport(employeeToInsert,datePickerFromDate.getValue(),datePickerToDate.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            for (Event employeeEventsToInsert : employeeEvents){
                if (employeeEventsToSum.containsKey(employeeEventsToInsert.getTask().getName())){
                    employeeEventsToSum.get(employeeEventsToInsert.getTask().getName()).addTime(employeeEventsToInsert.getTime());
                }

                else {
                    employeeEventsToSum.put(employeeEventsToInsert.getTask().getName(), employeeEventsToInsert);
                }
            }

            addingItems(employeeEventsToSum, eventsToInsert);

            itemBranch.getChildren().setAll(eventsToInsert);
            itemRoot.getChildren().add(itemBranch);

        }
        treeTableViewReport.setRoot(itemRoot);
        treeTableViewReport.setShowRoot(false);
    }

    public void openDateReport(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ReportDate.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageReportDaily = new Stage();
            stageReportDaily.setTitle("Periodical report");
            stageReportDaily.setScene(new Scene(root1));
            stageReportDaily.show();
            stageReportDaily.setResizable(false);

            stageReportDaily.setOnCloseRequest(eventClose -> openWorkdayScene(event));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addingItems(HashMap<String, Event> employeeEventsToSum, ArrayList<TreeItem<Event>> eventsToInsert) {
        for (Event event : employeeEventsToSum.values()) {
            TreeItem<Event> events;
            events = new TreeItem<>(event);
            System.out.println(events.toString());
            eventsToInsert.add(events);
        }
    }


}
