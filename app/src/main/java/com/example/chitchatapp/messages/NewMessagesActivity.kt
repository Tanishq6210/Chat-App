package com.example.chitchatapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chitchatapp.messages.ChatLogActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_messages.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessagesActivity : AppCompatActivity() {
    final val tag = "#DDDDDDDDD"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)

        //To set the Action Bar Title
        supportActionBar?.title = "Select User"


        fetchUsers()
    }

    //Fetching all users from the Firebase Database (path-> 'users')
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {
                    Log.d(tag, it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                
                adapter.setOnItemClickListener { item, view ->
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                rv_new_messages.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}

class UserItem(private val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // will be called in out list for each user object later on....
        viewHolder.itemView.apply {
            tv_user_name.text = user.userName
            Picasso.get().load(user.profileImageUrl).into(civ_user_image)
        }

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}