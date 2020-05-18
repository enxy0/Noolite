package com.enxy.noolite.core.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

/**
 * [FileManager] saves and retrieves data from SharedPreferences.
 */
class FileManager(private val appContext: Context) {
    fun getStringFromPrefs(preferencesName: String, key: String): String? {
        val prefs =
            appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getString(key, null)
        else
            null
    }

    fun getBooleanFromPrefs(preferencesName: String, key: String): Boolean {
        val prefs =
            appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getBoolean(key, true)
        else
            true
    }

    fun getIntFromPrefs(preferencesName: String, key: String): Int {
        val prefs =
            appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getInt(key, -1)
        else
            -1
    }

    fun getLongFromPrefs(preferencesName: String, key: String): Long {
        val prefs =
            appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getLong(key, -1L)
        else
            -1L
    }

    fun getFloatFromPrefs(preferencesName: String, key: String): Float {
        val prefs =
            appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getFloat(key, -1f)
        else
            -1f
    }

    fun saveStringToPrefs(preferencesName: String, key: String, value: String) {
        appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
            .edit()
            .putString(key, value)
            .apply()
    }

    fun saveBooleanToPrefs(preferencesName: String, key: String, value: Boolean) {
        appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    fun saveIntToPrefs(preferencesName: String, key: String, value: Int) {
        appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
            .edit()
            .putInt(key, value)
            .apply()
    }

    fun saveLongToPrefs(preferencesName: String, key: String, value: Long) {
        appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
            .edit()
            .putLong(key, value)
            .apply()
    }

    fun saveFloatToPrefs(preferencesName: String, key: String, value: Float) {
        appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
            .edit()
            .putFloat(key, value)
            .apply()
    }

    fun removeString(preferencesName: String, key: String) {
        appContext.applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
            .edit()
            .remove(key)
            .apply()
    }
}