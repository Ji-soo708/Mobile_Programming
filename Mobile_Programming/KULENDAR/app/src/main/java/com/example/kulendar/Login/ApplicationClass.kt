package com.example.kulendar.Login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class ApplicationClass: Application() {
    /////난수 참조하기 위한 클래스/////

    companion object{
        lateinit var mSharedPreferences: SharedPreferences

    }

    override fun onCreate() {
        super.onCreate()
        mSharedPreferences = applicationContext.getSharedPreferences("key", Context.MODE_PRIVATE)
    }
}