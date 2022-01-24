package com.example.chitchatapp

import android.content.Intent
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

        tv_forgot_password.setOnClickListener {
            resetPassword()
        }
    }

    //Function to login user using Firebase Authentication
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
                    Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Log.d("#DDDDDDD", "Couldn't Logged in Successfully")
                }
            }
            .addOnFailureListener(this) {
                Toast.makeText(this, "Failed to log in ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //Function to reset password using Firebase Authentication template
    private fun resetPassword() {
        val email = et_login_email.text.toString()

        if(email.isEmpty()) {
            if(email.isEmpty()) {
                Toast.makeText(this, "Please Enter valid Email", Toast.LENGTH_SHORT).show()
                return
            }
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener(this) {
                Toast.makeText(this, "Reset Password Email sent successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener(this) {
                Toast.makeText(this, "Reset Password not sent, ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
}
