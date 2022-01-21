package com.example.chitchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = et_email.text.toString()
        val password = et_password.text.toString()

        btn_register.setOnClickListener {
            Log.d("#DDDDDDDDDD", "Email : ${et_email.text.toString()} , Password: ${et_password.text.toString()}")
        }
        tv_login.setOnClickListener {
            Log.d("#DDDDDDDDDD", "Try to Login!")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}