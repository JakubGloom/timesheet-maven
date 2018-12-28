package com.gluma.timesheet.utils;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesUtils {
    public static Preferences preferences = Preferences.userNodeForPackage(PreferencesUtils.class);
    public static void rememberMe(JFXCheckBox isChecked, JFXTextField login){
        if(isChecked.isSelected()){
            System.out.println(isChecked.isSelected());
            preferences.putBoolean("rememberMe",true);
            preferences.put("username",login.getText().trim());
        }
        else{
            preferences.putBoolean("rememberMe",false);
        }
        try {
            preferences.exportNode(new FileOutputStream("MyFile.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

    }
    public static boolean checkIfRemembered(){
        return PreferencesUtils.preferences.getBoolean("rememberMe", true);
    }
}
