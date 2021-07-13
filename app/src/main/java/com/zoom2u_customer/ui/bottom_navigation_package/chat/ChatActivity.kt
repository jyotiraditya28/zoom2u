package com.zoom2u_customer.ui.bottom_navigation_package.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_chat)

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