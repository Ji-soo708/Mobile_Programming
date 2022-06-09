package com.example.kulendar.Login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.example.kulendar.DB.User
import com.example.kulendar.DB.UserDatabase
import com.example.kulendar.databinding.ActivityRegisterBinding
import com.example.kulendar.R
import com.example.kulendar.TableActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var adapter: ArrayAdapter<String>


    //////////////////리스트//////////////////
    val departure = mutableListOf<String>(
        "국어국문학과", "영어영문학과", "중어중문학과", "철학과", "사학과", "지리학과", "미디어커뮤니케이션학과", "문화콘텐츠학과",
        "수학과", "물리학과", "화학과", "건축학부",
        "사회환경공학부", "기계항공공학부", "전기전자공학부", "화학공학부", "컴퓨터공학부",
        "산업경영공학부 산업공학과", "산업경영공학부 신산업융합학과", "생물공학과", "K뷰티산업융합학과",
        "정치외교학과", "경제학과", "행정학과", "국제무역학과", "응용통계학과", "융합인재학과",
        "글로벌비즈니스학과", "경영학과", "기술경영학과", "부동산학과",
        "미래에너지공학과", "스마트운행체공학과", "스마트ICT융합공학과", "화장품공학과", "줄기세포재생공학과",
        "의생명공학과", "시스템생명공학과", "융합생명공학과", "생명과학특성학과", "동물자원과학과", "식량자원과학과",
        "축산식품생명공학과", "식품유통공학과", "환경보건과학과", "산림조경학과", "수의예과", "수의학과",
        "커뮤니케이션디자인학과", "산업디자인학과", "의상디자인학과", "리빙디자인학과", "현대미술학과", "영상영화학과",
        "일어교육과", "수학교육과", "체육교육과", "음악교육과", "교육공학과", "영어교육과", "교직과/대학원교육학과"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authbtn = findViewById<Button>(R.id.home_auth)
        val checkbtn = findViewById<Button>(R.id.home_authcheck)
        val randomnum = findViewById<EditText>(R.id.regedit_num)
        val sharedPreference = getSharedPreferences("key", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()

        // <보내기> 버튼 누르면
        authbtn.setOnClickListener {
            val email: String = binding.regeditId.text.toString() + "@konkuk.ac.kr"
            if (binding.regeditId.text.toString().isEmpty()) {
                Toast.makeText(this, "학교 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 이메일 보내기
                GMailSender().sendEmail(email)
                Toast.makeText(applicationContext, "인증번호를 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        var flag = false
        checkbtn.setOnClickListener {
            //val intent = Intent(this, GMailSender::class.java)
            //var random=intent.getIntExtra("random",0)
            var value = sharedPreference.getString("key", "데이터 없음")
            editor.commit()
            Log.d("key", "value: $value")
            if (randomnum.text.toString() == "$value") {
                Toast.makeText(applicationContext, "인증되었습니다.", Toast.LENGTH_SHORT).show()
                flag = true
            } else {
                Toast.makeText(applicationContext, "인증번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                flag = false
            }
        }

        initLayout()

        binding.btnRegister.setOnClickListener {
            if (flag) {
                signUp()
                finish()
            } else {
                Toast.makeText(applicationContext, "회원가입 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //자동완성검색 기능
    private fun initLayout() {
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.reg_autoComplete)

        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, departure)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            //i 포지션 정보
            val item = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this, "검색 완료", Toast.LENGTH_SHORT).show()
        }

        //text왓쳐는 인터페이스로 인터페이스를 구현한 객체가 들어가야 함
        //글자를 입력하면 입력에 따른 이벤트 처리


    }


    ///////////////회원가입 구현부분////////////////



    private fun getUSer(): User {
        val email: String = binding.regeditId.text.toString() + "@konkuk.ac.kr"
        val pw: String = binding.regeditPw.text.toString()
        val name: String = binding.regeditName.text.toString()
        val dep: String = binding.regAutoComplete.text.toString()

        return User(email, pw, name, dep)
    }




    private fun signUp() {
            if (binding.regeditId.text.toString().isEmpty()) {
                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
                return
            }
            if (binding.regeditPw.text.toString() != binding.regeditRech.text.toString()) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            if ((binding.regeditName.text.toString()).trim().length <= 0) {
                Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                return
            }
            if ((binding.regAutoComplete.text.toString()).trim().length <= 0) {
                Toast.makeText(this, "학과를 입력하세요.", Toast.LENGTH_SHORT).show()
                return
            }

            //val test_email = binding.regeditId.text.toString()

            val userDB = UserDatabase.getInstance(this)!!
            userDB.userDao().insert(getUSer())//회원가입

            val user = userDB.userDao().getUsers()//모든 정보 부름
            Log.d("SIGNUPACT", user.toString())

            //val test_email=binding.regIdtext.toString()

            //val intent = Intent(this, MainActivity2::class.java)
            //intent.putExtra("EMAIL1",test_email)
            //startActivity(intent)


        }
        //db저장되는거까지 확인함
}