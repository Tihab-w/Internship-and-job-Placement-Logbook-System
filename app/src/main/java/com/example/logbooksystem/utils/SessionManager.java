package com.example.logbooksystem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.logbooksystem.models.User;

public class SessionManager {
    private static final String PREF_NAME = "LogbookPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ROLE = "userRole";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "student");
    }

    public User getUserDetails() {
        User user = new User();
        user.setId(getUserId());
        user.setName(getUserName());
        user.setEmail(getUserEmail());
        user.setRole(getUserRole());
        return user;
    }

    public void updateUserData(User user) {
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.commit();
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}