package com.example.chitchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = et_login_email.text.toString()
        val password = et_login_password.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter valid Email and Password", Toast.LENGTH_SHORT).show()
            return
        }

        //Login with Firebase
        // Site --> https://firebase.google.com/docs/auth/android/start#kotlin+ktx_1
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    Log.d("#DDDDDDD", "${it.result.user?.uid}")
                } else {
                    Log.d("#DDDDDDD", "Couldn't Logged in Successfully")
                }
            }
            .addOnFailureListener(this) {
                Toast.makeText(this, "Failed to log in ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
