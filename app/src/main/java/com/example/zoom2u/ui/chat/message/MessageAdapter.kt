package com.example.zoom2u.ui.chat.message

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.databinding.ItemChatBinding
import com.example.zoom2u.databinding.ItemMessageBinding
import com.example.zoom2u.ui.chat.Chat
import com.example.zoom2u.ui.chat.ChatListAdapter

class MessageAdapter (val context : Context,private var dataList: List<Message>) : RecyclerView.Adapter<MessageAdapter.BindingViewHolder>() {


    fun setList(dataList: List<Message>){
        this.dataList=dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemMessageBinding =
            ItemMessageBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val message: Message = dataList[position]
        holder.itemBinding.message = message


    }


    class BindingViewHolder(val itemBinding: ItemMessageBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}