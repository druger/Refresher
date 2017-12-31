package com.druger.refresher.utils

import android.content.SharedPreferences

/**
* Created by druger on 11.09.2015.
*/
class PreferenceHelper(var preferences: SharedPreferences) {

    companion object {
        const val SPLASH_IS_INVISIBLE = "splash_is_invisible"
        const val PREFERENCES_NAME = "preferences"
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }
}
