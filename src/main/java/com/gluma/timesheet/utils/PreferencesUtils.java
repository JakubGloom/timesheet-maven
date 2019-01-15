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
            preferences.put("login",login.getText().trim());
        }
        else{
            try {
                preferences.clear();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        }
        try {
            preferences.exportNode(new FileOutputStream("MyFile.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

    }
    public static void loggedUserData(int id, String name, String surname,String password){
        preferences.putInt("idEmployee",id);
        preferences.put("userName", name);
        preferences.put("userLastName", surname);
        preferences.put("password",password);


    }
    public static boolean checkIfRemembered(){
        return PreferencesUtils.preferences.getBoolean("rememberMe", false);
    }

    public static String getText(String key){
        String textToReturn = preferences.get(key,"error");
        if (textToReturn.isEmpty()){
            return "error";
        }
        return textToReturn;
    }

    public static int getNumber(String key){
        int toReturn = preferences.getInt(key,-1);
        return toReturn;
    }
}
