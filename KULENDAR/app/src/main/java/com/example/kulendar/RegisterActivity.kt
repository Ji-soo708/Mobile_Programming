package com.example.kulendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kulendar.DB.MYDBHelper_User
import com.example.kulendar.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDBHelperUser: MYDBHelper_User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}