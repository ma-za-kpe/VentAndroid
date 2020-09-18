package com.makumatthew.vent

import android.content.Context
import android.content.SharedPreferences
import com.makumatthew.vent.Constants.FIREBASE_TOKEN
import com.makumatthew.vent.Constants.PREF_FULLNAME
import com.makumatthew.vent.Constants.PREF_USER_EMAIL
import com.makumatthew.vent.Constants.PREF_USER_PIC

/*
*
* This is the main class handling all local cache for the App
*
* */

class PrefManager(val context: Context) {

    companion  object {
        private var INSTANCE: PrefManager? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): PrefManager? {
            if (INSTANCE == null) {
                INSTANCE = PrefManager(context)
            }
            return INSTANCE
        }
    }

    private val pref: SharedPreferences = context.getSharedPreferences(
        Constants.MyPREFERENCES,
        Context.MODE_PRIVATE
    )
    private var editor: SharedPreferences.Editor


    init {
        editor = pref.edit()
    }

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(Constants.IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(Constants.IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.apply()
        }

    fun setFireBaseToken(token: String?) {
        editor.putString(FIREBASE_TOKEN, token)
        editor.apply()
    }

    var firebaseToken: String?
        get() = pref.getString(FIREBASE_TOKEN, "")
        set(firebaseToken) = editor.putString(FIREBASE_TOKEN, firebaseToken).apply()

    var phoneNumber: String?
        get() = pref.getString(Constants.PREF_PHONE_NUMBER, "")
        set(phoneNumber) {
            editor = pref.edit()
            editor.putString(Constants.PREF_PHONE_NUMBER, phoneNumber)
            editor.apply()
        }

    var fullName: String?
        get() = pref.getString(PREF_FULLNAME, "")
        set(fullName) = editor.putString(PREF_FULLNAME, fullName).apply()

    var email: String?
        get() = pref.getString(PREF_USER_EMAIL, "")
        set(fullName) = editor.putString(PREF_USER_EMAIL, fullName).apply()

    var profpic: String?
        get() = pref.getString(PREF_USER_PIC, "")
        set(fullName) = editor.putString(PREF_USER_PIC, fullName).apply()

    fun DELETESHAREDPREFS() { //this one doesn't work 50/50
        editor.clear().apply()
        editor.clear().commit()
        //doing this to at least recreate the file & disable intro from rerunning
        isFirstTimeLaunch = true
    }

}
