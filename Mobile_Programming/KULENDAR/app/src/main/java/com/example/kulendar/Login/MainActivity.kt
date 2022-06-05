package com.example.kulendar.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.kulendar.DB.UserDatabase
import com.example.kulendar.R
import com.example.kulendar.databinding.ActivityMainBinding

///////////로그인 화면/////////////
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val register_intent = Intent(this, RegisterActivity::class.java)
        val register_btn = findViewById<Button>(R.id.btn_register)

        //로그인, 회원가입 화면 전환
        binding.btnLogin.setOnClickListener {
            login()
        }

        register_btn.setOnClickListener {
            startActivity(register_intent)
        }
    }

//////////로그인 기능///////////

    private fun login(){
        if(binding.homeId.text.toString().isEmpty()){
            Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.homePw.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.homeId.text.toString() + "@konkuk.ac.kr"
        val pw : String = binding.homePw.text.toString()

        val userDB = UserDatabase.getInstance(this)!!
        val user = userDB.userDao().getUser(email, pw)


        //로그
        user?.let {
            Log.d("LOGIN_ACT/GET_USER","userId : ${user.id}, $user")
            saveJwt(user.id)
            startActivity()
        }
        Toast.makeText(this, "로그인 시도", Toast.LENGTH_SHORT).show()
        binding.homeId.text=null
        binding.homePw.text=null
    }

    private fun saveJwt(jwt:Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }

    //로그인 되면 화면 전환
    private fun startActivity(){
        val login_intent = Intent(this, MainActivity2::class.java)
        startActivity(login_intent)
    }
}