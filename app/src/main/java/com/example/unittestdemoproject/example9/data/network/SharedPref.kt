package com.example.unittestdemoproject.example9.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.unittestdemoproject.example9.utils.AppConstant
import com.example.unittestdemoproject.example9.utils.AppConstant.Companion.PREFERENCE_NAME

class SharedPref(context: Context) : AppConstant {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, 0)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    init {
        editor = sharedPreferences.edit()
    }


    var token: String = sharedPreferences.getString(AppPref.PREFERENCE_AUTH_TOKEN.toString(), "")!!
        set(value: String) {
            editor.putString(AppPref.PREFERENCE_AUTH_TOKEN.toString(), value)
            editor.apply()
        }

    private enum class AppPref {
        PREFERENCE_AUTH_TOKEN
    }

}