package com.drughub.citizen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    public static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__";
    public static final String PREFS_LOGIN_PASSWORD_KEY = "__PASSWORD__";

    public static void saveToPrefs(Context context, String name, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveToLoginPrefs(Context context, String userName, String password) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_LOGIN_USERNAME_KEY, userName);
        editor.putString(PREFS_LOGIN_PASSWORD_KEY, password);
        editor.apply();
    }

    public static boolean getLoginPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString(PREFS_LOGIN_USERNAME_KEY, "");
        String userPassword = prefs.getString(PREFS_LOGIN_PASSWORD_KEY, "");
        if (username.length() > 0 && userPassword.length() > 0)
            return true;
        else
            return false;
    }

    public static String getUserName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFS_LOGIN_USERNAME_KEY, "");
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFS_LOGIN_PASSWORD_KEY, "");
    }

    public static String getFromPrefs(Context context, String name, String key, String defaultValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static void logoutPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().apply();
    }

    public static void removeFromPrefs(Context context, String name) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply();
    }
}