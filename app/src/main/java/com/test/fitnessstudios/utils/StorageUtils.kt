package com.test.fitnessstudios.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity

class StorageUtils {
    companion object {
        fun saveDataInPreferences(activity: FragmentActivity, key: String?, value: String) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString(key, value)
                apply()
            }
        }


        fun saveDataInPreferences(activity: FragmentActivity, key: String?, value: Boolean) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putBoolean(key, value)
                apply()
            }
        }

        fun saveDataInPreferences(activity: FragmentActivity, key: String?, value: Int) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putInt(key, value)
                apply()
            }
        }

        fun getDataFromPreferences(
            activity: FragmentActivity,
            key: String?,
            defaultValue: Boolean
        ): Boolean {
            val preferences = activity.getPreferences(Context.MODE_PRIVATE) ?: return defaultValue
            return preferences.getBoolean(key, defaultValue)
        }

        fun getDataFromPreferences(
            activity: FragmentActivity,
            key: String?,
            defaultValue: String?
        ): String? {
            val preferences = activity.getPreferences(Context.MODE_PRIVATE) ?: return defaultValue
            return preferences.getString(key, defaultValue)
        }


        fun getDataFromPreferences(
            activity: FragmentActivity,
            key: String?,
            defaultValue: Int
        ): Int {
            val preferences = activity.getPreferences(Context.MODE_PRIVATE) ?: return defaultValue
            return preferences.getInt(key, defaultValue)
        }
    }
}