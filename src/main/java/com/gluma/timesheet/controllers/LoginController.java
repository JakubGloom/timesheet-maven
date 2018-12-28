package com.gluma.timesheet.controllers;

import com.gluma.timesheet.conectivity.ConnectionManager;
import com.gluma.timesheet.datamdodel.Employee;
import com.gluma.timesheet.services.dao.EmployeeDAO;
import com.gluma.timesheet.utils.PreferencesUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private boolean noConnection;

    @FXML
    private Label labelDisplayStatus;

    @FXML
    private JFXTextField textFieldLogin;

    @FXML
    private JFXPasswordField passwordFieldPassword;

    @FXML
    private JFXCheckBox checkBocRememberMe;

    @FXML
    private JFXButton loginButton;

    private ResultSet result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (PreferencesUtils.checkIfRemembered()) {
            checkBocRememberMe.setSelected(true);
            textFieldLogin.setText(PreferencesUtils.preferences.get("username", null));
        }else {
            checkBocRememberMe.setSelected(false);
        }
    }

    @FXML
    public void login(ActionEvent event) {
        try {
            if (isLoginValid()) {
                PreferencesUtils.rememberMe(checkBocRememberMe,textFieldLogin);
                System.out.println("Login success");
                openWorkdayScene(event);
            } else {
                textFieldLogin.clear();
                passwordFieldPassword.clear();
                if (noConnection)
                    labelDisplayStatus.setText("No connection to the Database");
                else
                    labelDisplayStatus.setText("Invalid username or password !");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean isLoginValid() {
        try (PreparedStatement loginQuery = ConnectionManager.dbConnect().prepareStatement("SELECT idEmployee FROM employee WHERE Login=? AND Password=?")) {
            loginQuery.setString(1, textFieldLogin.getText());
            loginQuery.setString(2, passwordFieldPassword.getText());
            result = loginQuery.executeQuery();
            if(result.next()) {
                Employee.loggedEmployee = EmployeeDAO.searchEmployee(result.getInt("idEmployee"));
                return true;
            }
            } catch (SQLException e) {
                this.noConnection = true;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{result.close();}catch(SQLException e){}
        }
        return false;
    }


    private void openWorkdayScene(ActionEvent event) {
        WorkdayController workdayController = new WorkdayController();
        workdayController.openWorkdayScene(event);
    }

}