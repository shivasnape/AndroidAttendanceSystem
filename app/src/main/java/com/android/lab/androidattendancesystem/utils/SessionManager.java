package com.android.lab.androidattendancesystem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Shivichu on 05-09-2017.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "SessionManager";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_CLASS_ID = "class_id";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void setLoginData(int id, String userName, String userType, String classId) {

        editor.putInt(KEY_ID, id);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_TYPE, userType);
        editor.putString(KEY_CLASS_ID,classId);

        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logoutUser() {

        editor.clear();
        editor.commit();
    }
}