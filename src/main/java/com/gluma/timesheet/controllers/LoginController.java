package com.gluma.timesheet.controllers;

import com.gluma.timesheet.conectivity.ConnectionManager;
import com.gluma.timesheet.dao.EmployeeDAO;
import com.gluma.timesheet.datamdodel.Employee;
import com.gluma.timesheet.utils.PreferencesUtils;
import com.gluma.timesheet.utils.Security;
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
import java.util.prefs.BackingStoreException;

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

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (PreferencesUtils.checkIfRemembered()) {
            checkBocRememberMe.setSelected(true);
            textFieldLogin.setText(PreferencesUtils.getText("login"));
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
        try (PreparedStatement loginQuery = ConnectionManager.dbConnect().prepareStatement("SELECT idEmployee,Password FROM employee WHERE Login=?")) {
            try {
                PreferencesUtils.preferences.clear();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
            loginQuery.setString(1, textFieldLogin.getText());
            result = loginQuery.executeQuery();
            if(result.next()) {
                logger.info(result.getString("Password"));
                if (Security.checkPassword(passwordFieldPassword.getText(),result.getString("Password"))) {
                    Employee employee = EmployeeDAO.searchEmployee(result.getInt("idEmployee"));
                    PreferencesUtils.loggedUserData(employee.getIdEmployee(), employee.getName() ,employee.getSurname(), employee.getPassword());
                    return true;
                }
            }
            } catch (SQLException e) {
                this.noConnection = true;
                e.printStackTrace();
            }
            finally{
            try{result.close();}catch(SQLException e){}
        }
        return false;
    }


    private void openWorkdayScene(ActionEvent event) {
        WorkdayController workdayController = new WorkdayController();
        workdayController.openWorkdayScene(event);
    }

}