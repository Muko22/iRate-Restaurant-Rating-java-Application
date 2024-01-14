package com.example.irate.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = _context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String fullName, String email, String phoneNumber) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);

        editor.commit();

    }

    public HashMap<String, String> getUsersDetailsFromSession() {

        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONE_NUMBER, usersSession.getString(KEY_PHONE_NUMBER, null));

        return userData;

    }

    public boolean checkLogin() {

        if (usersSession.getBoolean(IS_LOGIN, true)) {
            return true;
        }
        else
            return false;

    }

    public void logout() {

        editor.clear();
        editor.commit();

    }

}
