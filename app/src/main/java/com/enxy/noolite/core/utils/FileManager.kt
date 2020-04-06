package com.enxy.noolite.core.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManager @Inject constructor(private val appContext: Context) {
    fun getStringFromPrefs(preferencesName: String, key: String): String? {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getString(key, null)
        else
            null
    }

    fun getBooleanFromPrefs(preferencesName: String, key: String): Boolean {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getBoolean(key, true)
        else
            true
    }

    fun getIntFromPrefs(preferencesName: String, key: String): Int {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getInt(key, -1)
        else
            -1
    }

    fun getLongFromPrefs(preferencesName: String, key: String): Long {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getLong(key, -1L)
        else
            -1L
    }

    fun getFloatFromPrefs(preferencesName: String, key: String): Float {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        return if (prefs.contains(key))
            prefs.getFloat(key, -1f)
        else
            -1f
    }

    fun saveStringToPrefs(preferencesName: String, key: String, value: String) {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun saveBooleanToPrefs(preferencesName: String, key: String, value: Boolean) {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun saveIntToPrefs(preferencesName: String, key: String, value: Int) {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        with(prefs.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun saveLongToPrefs(preferencesName: String, key: String, value: Long) {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        with(prefs.edit()) {
            putLong(key, value)
            apply()
        }
    }

    fun saveFloatToPrefs(preferencesName: String, key: String, value: Float) {
        val prefs = appContext.getSharedPreferences(preferencesName, MODE_PRIVATE)
        with(prefs.edit()) {
            putFloat(key, value)
            apply()
        }
    }
}