package com.example.zoom2u.ui.details_base_page.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.databinding.DeliveryHistoryItemBinding

class HistoryItemAdpter(val context: Context, private val dataList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryItemAdpter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: DeliveryHistoryItemBinding =
            DeliveryHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val historyItem: HistoryItem= dataList[position]
        holder.itemBinding.historyitem = historyItem


    }


    class BindingViewHolder(val itemBinding: DeliveryHistoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}