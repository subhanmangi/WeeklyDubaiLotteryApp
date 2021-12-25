package com.example.fridaydubailottery.utils


import android.app.Activity
import android.content.Context

object SharedPreference {
    private const val SHARED_PREFERENCES_KEY = "UserSharedPrefs"
    @JvmStatic
    fun saveSimpleString(mContext: Context, key: String?, value: String?) {
        val userSharedPrefs =
            mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE)
        val edit = userSharedPrefs.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getSimpleString(mContext: Context, key: String?): String? {
        val userSharedPrefs =
            mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE)
        return userSharedPrefs.getString(key, "")
    }

    @JvmStatic
    fun saveBoolSharedPrefValue(mContext: Context, key: String?, value: Boolean) {
        val userSharedPrefs =
            mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE)
        val edit = userSharedPrefs.edit()
        edit.putBoolean(key, value)
        edit.apply()
    }

    fun getBoolSharedPrefValue(cxt: Context, key: String?, defaultValue: Boolean): Boolean {
        val userSharedPrefs =
            cxt.getSharedPreferences(SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE)
        return userSharedPrefs.getBoolean(key, defaultValue)
    }
}