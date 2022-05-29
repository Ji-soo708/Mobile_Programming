package com.example.kulendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MainActivity2::class.java)
        val button = findViewById<Button>(R.id.startbtn)
        button.setOnClickListener {
            startActivity(intent)

        }
    }


}