package com.example.chitchatapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            registerUser()
        }

        tv_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_register_photo.setOnClickListener {
            // Passing request to give photo to gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    //This will hold uri of photo we chose
    var selectedPhotoUri: Uri? = null

    //When we choose image after that onActivityResult is executed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what the selected image was
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            civ_profile_image.setImageBitmap(bitmap)
            //val bitmapDrawable = BitmapDrawable(bitmap)
            //btn_register_photo.background = bitmapDrawable
            btn_register_photo.alpha = 0f
        }
    }

    //Function to register user to firebase authentication
    private fun registerUser() {
        // Site --> https://firebase.google.com/docs/auth/android/start#kotlin+ktx_1
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter valid Email and Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("#DDDDDDDDDD", "Email : ${et_email.text.toString()} , Password: ${et_password.text.toString()}")

        //Firebase authentication with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d("#DDDDDDDD", "Successfully created user with uid: ${it.result.user?.uid}")
                uploadImageInFirebaseStorage()
            }
            .addOnFailureListener(this) {
                Log.d("#DDDDDDD", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Can't register ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //Function to upload user image to firebase storage
    private fun uploadImageInFirebaseStorage() {
        if(selectedPhotoUri == null) return

        //Giving random unique filename for every new image
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("#DDDDDDDDDDD", "Successfully uploaded image: ${it.metadata?.path}")

                //Reference of the location of user image on google to use that for individual users
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.d("#DDDDDD", "File location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }

            }.addOnFailureListener {
                Log.d("#DDDDDDDD", "Cant upload image: ${it.message}")
            }
    }

    //Function to save User Data in Firebase Realtime Database
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        // uid which Firebase auth gives to every user
        val uid = FirebaseAuth.getInstance().uid ?: ""

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, et_name.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("#DDDDD Database: ", "Finally we saved user to database")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }.addOnFailureListener{
                Log.d("#DDDDDD Database: ", "User could register to Database ${it.message}")
            }
    }
}

class User(val uid: String, val userName: String, val profileImageUrl: String)