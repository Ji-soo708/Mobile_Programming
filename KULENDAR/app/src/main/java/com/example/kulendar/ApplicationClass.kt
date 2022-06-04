package com.example.kulendar

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class ApplicationClass: Application() {

    companion object{
        lateinit var mSharedPreferences: SharedPreferences

    }

    override fun onCreate() {
        super.onCreate()
        mSharedPreferences = applicationContext.getSharedPreferences("key", Context.MODE_PRIVATE)
    }
}