package com.gluma.timesheet.controllers;

import com.gluma.timesheet.dao.AccountDAO;
import com.gluma.timesheet.dao.EventDAO;
import com.gluma.timesheet.dao.TaskDAO;
import com.gluma.timesheet.datamdodel.Event;
import com.gluma.timesheet.datamdodel.Task;
import com.gluma.timesheet.utils.Actions;
import com.gluma.timesheet.utils.PreferencesUtils;
import com.gluma.timesheet.utils.StageManager;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class WorkdayController implements Initializable {

    @FXML
    private Label loggedUserName;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelToday;

    @FXML
    private Button buttonChange;

    @FXML
    private Button buttonReportDaily;

    @FXML
    private Button buttonTasks;

    @FXML
    private Button buttonEmployees;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonRemove;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnCheck;

    @FXML
    private TableView<Event> tableEvents;

    @FXML
    private TableColumn<Event,String> columnName;

    @FXML
    private TableColumn<Event,Timestamp> columnStart;

    @FXML
    private TableColumn<Event,Timestamp> columnEndDate;

    @FXML
    private TableColumn<Event,Integer> columnTime;

    @FXML
    private TableColumn<Event, Integer> columnIDEv;

    @FXML
    private TableView<Task> tableViewTasks;

    @FXML
    private TableColumn<Task, Integer> columnID;

    @FXML
    private TableColumn<Task, String> columnTaskName;

    @FXML
    private TableColumn<Task, String> columnDescirption;

    @FXML
    private JFXTimePicker timePickerStartTime;

    @FXML
    private JFXTimePicker timePickerEndTime;

    @FXML
    private JFXDatePicker datePickerAnotherDay;

    @FXML
    private TextArea textAreaDisplayDescription;

    private static final Logger logger = Logger.getLogger(WorkdayController.class.getName());
    private static LocalDate localDate;
    private Thread loadData;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        columnIDEv.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("task"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        columnEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        columnID.setCellValueFactory(new PropertyValueFactory<>("idTask"));
        columnTaskName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnDescirption.setCellValueFactory(new PropertyValueFactory<>("Descirption"));

        timePickerStartTime.setIs24HourView(true);
        timePickerStartTime.editableProperty().setValue(false);
        timePickerEndTime.setIs24HourView(true);
        timePickerEndTime.editableProperty().setValue(false);

        localDate = LocalDate.now();
        buttonsController(AccountDAO.validateEmployeeAcount(PreferencesUtils.getNumber("idEmployee")));
        datePickerAnotherDay.setValue(localDate);
        //loadData = new Thread(() -> loadEventData(localDate));
        //loadData.start();
        loadEventData(localDate);
        loadTasks();
        initializeLoggedEmployeeData();
    }

    private void loadEventData(LocalDate localDate) {
        try {
            System.out.println(Thread.currentThread());
            ObservableList<Event> eventData = EventDAO.searchEvents(localDate);
            tableEvents.setItems(eventData);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try {
            ObservableList<Task> taskData = TaskDAO.searchTasks();
            tableViewTasks.setItems(taskData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event){
        /*if (loadData.isAlive()){
            try {
                loadData.join();
                openLoginWindow(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        openLoginWindow(event);
    }

    private void openLoginWindow(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/views/login.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stageLogin = new Stage();

            stageLogin.setTitle("Login");
            stageLogin.setScene(new Scene(root1));
            stageLogin.setResizable(false);
            stageLogin.show();

            StageManager.stages.add(stageLogin);
            StageManager.closeStages(stageLogin);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEmployeesWindow(ActionEvent event){
        ManageEmployeesController manageEmployeesController = new ManageEmployeesController();
        manageEmployeesController.openEmployeesWindow(event);
    }

    @FXML
    public void openTasksWindow(ActionEvent event) {
        ManageTasksController tasksController = new ManageTasksController();
        tasksController.openTasksWindow(event);
    }

    @FXML
    private void changeLoginData(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ChangeLoginData.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stageChangeLoginData = new Stage();
            stageChangeLoginData.setTitle("Data");
            stageChangeLoginData.setScene(new Scene(root1));

            stageChangeLoginData.initModality(Modality.APPLICATION_MODAL);

            stageChangeLoginData.show();
            stageChangeLoginData.setResizable(false);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void add() {
        if (validateFields()){
            LocalTime startTime = timePickerStartTime.getValue();
            String insertStart = localDate.toString() + " " + startTime.toString() + ":" + startTime.getSecond();
            Timestamp start = Timestamp.valueOf(insertStart);

            LocalTime endTime = timePickerEndTime.getValue();
            String insertEnd = localDate.toString() + " " + endTime.toString() + ":" + endTime.getSecond();
            Timestamp end = Timestamp.valueOf(insertEnd);

            Timestamp lastAdded = start;

            if (!tableEvents.getItems().isEmpty()) {
                int lastIndex = tableEvents.getItems().size()-1;
                lastAdded = tableEvents.getItems().get(lastIndex).getEndDate();
                System.out.println(lastIndex);
            }

            if ( start.before(end)&&(start.after(lastAdded)||start.equals(lastAdded))&&(start.after(Timestamp.valueOf(localDate + " 00:00:00"))||end.before(Timestamp.valueOf(localDate + " 23:59:59")))) {
                int elapsedMinutes = (int) Duration.between(startTime, endTime).toMinutes();

                Task selectedTask = tableViewTasks.getSelectionModel().getSelectedItem();

                Event eventToInsert = new Event(start, end, elapsedMinutes, 0,1, selectedTask);
                System.out.println(eventToInsert);

                tableEvents.getItems().add(eventToInsert);
            }
            else{Actions.showAlert("Wrong time input");}
        }
    }

    @FXML
    private boolean showDescription() {
        if (Actions.validateSelections(tableViewTasks.getSelectionModel().getSelectedIndex())) {
            Task description = tableViewTasks.getSelectionModel().getSelectedItem();
            textAreaDisplayDescription.setText(description.getDescirption());
            return true;
        }
        return false;
    }

    @FXML
    private void deleteEvent() {
        int idEventToDelete = tableEvents.getSelectionModel().getSelectedItem().getIdEvent();
        try {
            System.out.println(idEventToDelete);
            EventDAO.deleteEvent(idEventToDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Event selectedEvent = tableEvents.getSelectionModel().getSelectedItem();
        tableEvents.getItems().remove(selectedEvent);
    }

    public void initializeLoggedEmployeeData() {
        String loggedUser = "Logged as: " + PreferencesUtils.getText("userName") + " " + PreferencesUtils.getText("userLastName");
        String today = "Today is: ";
        String date = String.valueOf(localDate);
        loggedUserName.setText(loggedUser);
        labelToday.setText(today);
        labelDate.setText(date);
    }

    private boolean validateFields(){
        if (timePickerStartTime.getValue()!=null&&timePickerEndTime.getValue()!=null&&showDescription()){
            return true;
        }
        Actions.showAlert("Fill the required data");
        return false;
    }

    @FXML
    private void checkEvents(){
        EventDAO.checkCurrentEvents(tableEvents);
    }

    @FXML
    private void loadAnotherDay(){
        localDate = datePickerAnotherDay.getValue();
        loadEventData(localDate);
    }

    @FXML
    public void send(){
            if (!tableEvents.getItems().isEmpty()) {
                try {
                    ObservableList<Event> toSend = tableEvents.getItems();
                    EventDAO.sendEvents(toSend);
                } catch (Exception e) {
                    Actions.showAlert(e.toString());
                }
                loadEventData(localDate);
            }
            else {
                Actions.showInfo("No events to send");
            }

    }

    @FXML
    private void openDailyReport(ActionEvent event){
        ReportDailyController reportDailyController = new ReportDailyController();
        reportDailyController.openDailyReport(event);
    }

    @FXML
    private void openDateReport(ActionEvent event){
        ReportDateController reportDateController = new ReportDateController();
        reportDateController.openDateReport(event);
    }

    private void buttonsController(int id){
        logger.info(String.valueOf(id));
        switch(id){
            case 1:
                buttonReportDaily.setVisible(false);
                buttonReportDaily.setDisable(true);
                buttonTasks.setVisible(false);
                buttonTasks.setDisable(true);
                buttonEmployees.setVisible(false);
                buttonEmployees.setDisable(true);
                break;
            case 2:
                buttonReportDaily.setVisible(false);
                buttonReportDaily.setDisable(true);
                buttonEmployees.setVisible(false);
                buttonEmployees.setDisable(true);
                break;
            case 3:
                break;
        }
    }

    private boolean validateTime(Timestamp start, Timestamp end){


        return false;
    }

    public  void openWorkdayScene(ActionEvent event) {
        try{ FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Workday.fxml"));
        Parent root = loader.load();
        Stage stageWorkday = new Stage();
        stageWorkday.setScene(new Scene(root));
        stageWorkday.setResizable(false);
        stageWorkday.show();


        ((Node) (event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

}
