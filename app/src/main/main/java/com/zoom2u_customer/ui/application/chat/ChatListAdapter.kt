package com.zoom2u_customer.ui.application.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.databinding.ItemChatBinding


class ChatListAdapter (val context : Context, private val dataList: List<Chat>,private val onItemClick:(Chat) -> Unit) : RecyclerView.Adapter<ChatListAdapter.BindingViewHolder>() {


    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemChatBinding =
            ItemChatBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val chat: Chat = dataList[position]
        holder.itemBinding.chat = chat

        holder.itemBinding.root.setOnClickListener {
            onItemClick(chat)
        }
    }


    class BindingViewHolder(val itemBinding: ItemChatBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}