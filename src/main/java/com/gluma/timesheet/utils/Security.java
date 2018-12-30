package com.gluma.timesheet.utils;

import org.mindrot.jbcrypt.BCrypt;


public class Security {
    public static String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt(12));
    }
    public static Boolean checkPassword(String passwordToCheck, String password){return BCrypt.checkpw(passwordToCheck, password);}
}
