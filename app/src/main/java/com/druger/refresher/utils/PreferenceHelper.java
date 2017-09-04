package com.druger.refresher.utils;

import android.content.SharedPreferences;

/**
 * Created by druger on 11.09.2015.
 */
public class PreferenceHelper {

    public static final String SPLASH_IS_INVISIBLE = "splash_is_invisible";
    public static final String PREFERENCES_NAME = "preferences";

    private SharedPreferences preferences;

    public PreferenceHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return preferences.getBoolean(key, false);
    }
}
