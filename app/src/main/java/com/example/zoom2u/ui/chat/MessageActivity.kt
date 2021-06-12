package com.example.zoom2u.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.R

import com.example.zoom2u.databinding.ActivityMessageBinding
import com.example.zoom2u.ui.chat.message.Message
import com.example.zoom2u.ui.chat.message.MessageAdapter

class MessageActivity : AppCompatActivity(), View.OnClickListener {
    var messageItem: MutableList<Message> = ArrayList()
    lateinit var adapter :MessageAdapter
    lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        setAdpterView()
        binding.sendMsgImg.setOnClickListener(this)


    }

    fun setAdpterView() {
        var layoutManager = GridLayoutManager(this, 1)
        binding.messageRecyclerView.layoutManager = layoutManager
        adapter = MessageAdapter(this,messageItem)
        binding.messageRecyclerView.adapter=adapter

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.send_msg_Img -> {
               if(binding.msgText.text.toString().trim().isNotEmpty()){
                   val item = Message(binding.msgText.text.toString().trim())
                   messageItem.add(item)
                   binding.msgText.setText("")
                   adapter.setList(messageItem)
               }
            }
        }
    }
}