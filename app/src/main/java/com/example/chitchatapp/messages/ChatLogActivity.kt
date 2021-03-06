package com.example.chitchatapp.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chitchatapp.NewMessagesActivity.Companion.USER_KEY
import com.example.chitchatapp.R
import com.example.chitchatapp.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //val username = intent.getStringExtra(NewMessagesActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(USER_KEY)
        supportActionBar?.title =user?.userName

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        rv_chat_log.adapter = adapter
    }
}

class ChatFromItem: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChatToItem: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
