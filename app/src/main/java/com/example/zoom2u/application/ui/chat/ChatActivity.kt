package com.example.zoom2u.application.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat)

        setAdpterView(binding)

    }


    fun setAdpterView(binding: ActivityChatBinding) {
        var layoutManager = GridLayoutManager(this, 1)
        binding.chatView.layoutManager = layoutManager
        var adapter = ChatListAdapter(this, ChatDataProvider.chatItem.toList(),onItemClick = ::onItemClick)
        binding.chatView.adapter=adapter

    }

    private fun onItemClick(chat:Chat){
       /* val intent = Intent(this, MessageActivity::class.java)
        startActivity(intent)*/
    }

}