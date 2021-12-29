package com.example.kotlincountries.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPeferences {

    companion object {
        private val PREFERENCES_TIME="time"
        private var sharedPrefences:SharedPreferences?=null

        @Volatile private var instance :CustomSharedPeferences?=null
        private val lock=Any()

        operator fun invoke(context:Context):CustomSharedPeferences= instance?: synchronized(lock){
            instance?: makeCustomSharedPReferences(context).also {
                instance= it
            }
        }

        private fun makeCustomSharedPReferences(context: Context):CustomSharedPeferences{
            sharedPrefences=PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPeferences()
        }
    }

    fun saveTime(time:Long){
        sharedPrefences?.edit(commit=true){
            putLong(PREFERENCES_TIME,time)
        }
    }

    fun getTime()= sharedPrefences?.getLong(PREFERENCES_TIME,0)
}