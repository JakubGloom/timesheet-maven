package com.gluma.timesheet.controllers;

import com.gluma.timesheet.dao.EmployeeDAO;
import com.gluma.timesheet.utils.Actions;
import com.gluma.timesheet.utils.PreferencesUtils;
import com.gluma.timesheet.utils.Security;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;


public class ChangeLoginDataController {
    @FXML
    private JFXTextField textFieldNewLogin;

    @FXML
    private JFXPasswordField passwordFieldOldPassword;

    @FXML
    private JFXPasswordField passwordFieldNewPassword;

    @FXML
    private JFXPasswordField passwordFieldRepeat;

    @FXML
    private void change(){
        if (validateOldPassword()&& validateNewPasswords()) {
            if (isNewLogin()) {
                EmployeeDAO.updateEmployeePasswordAndLogin(textFieldNewLogin.getText(), Security.hashPassword(passwordFieldNewPassword.getText()));
                Actions.showInfo("Successfully changed to a new password and login");
            }else{
                EmployeeDAO.updateEmployeePassword(Security.hashPassword(passwordFieldNewPassword.getText()));
                Actions.showInfo("Successfully changed to a new password");
            }
        }
        else{
            if (isNewLogin()){
                EmployeeDAO.updateEmployeeLogin(textFieldNewLogin.getText());
                Actions.showInfo("Successfully changed to a new login");
            }
        }
    }

    private boolean validateOldPassword(){
       if (!passwordFieldOldPassword.getText().isEmpty()) {
           System.out.println(PreferencesUtils.preferences.get("password",null));
           System.out.println(passwordFieldOldPassword.getText());
           if (Security.checkPassword(passwordFieldOldPassword.getText(),PreferencesUtils.getText("password"))) {
               return true;
           }
           else{
               Actions.showAlert("Old password don't match");
               return false;
           }
       }
       Actions.showAlert("Password field cannot be empty");
       return false;
    }

    private boolean validateNewPasswords(){
        if (!passwordFieldNewPassword.getText().isEmpty()&&!passwordFieldRepeat.getText().isEmpty()) {
            if (passwordFieldNewPassword.getText().equals(passwordFieldRepeat.getText())) {
                return true;
            }
            else{
                Actions.showAlert("Password field cannot be empty");
            }
        }
        Actions.showAlert("New passwords don't match");
        return false;
    }

    private boolean isNewLogin(){
        return !textFieldNewLogin.getText().isEmpty();
    }
}
