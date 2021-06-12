package com.example.zoom2u.ui.details_base_page.history

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ItemDeliveryHistoryBinding

class HistoryItemAdpter(val context: Context, private val dataList: List<HistoryItem>,private val onItemClick:(HistoryItem) -> Unit) :
    RecyclerView.Adapter<HistoryItemAdpter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemDeliveryHistoryBinding =
            ItemDeliveryHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
       if(position==0){
           holder.itemBinding.heading.visibility= View.VISIBLE
           holder.itemBinding.heading.setText("Ongoing(1)")
       }else if(position==1){
           holder.itemBinding.heading.visibility= View.VISIBLE
           holder.itemBinding.heading.setText("Past")
       }else
           holder.itemBinding.heading.visibility= View.GONE



        if(position==0){
            holder.itemBinding.status.setText("Picked Up")
            holder.itemBinding.status.setBackgroundColor(Color.parseColor("#FFD100"))
            holder.itemBinding.status.setTextColor(Color.BLACK)
        }else{
            holder.itemBinding.status.setText("Delivery")
            holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
            holder.itemBinding.status.setTextColor(Color.WHITE)
        }



        if (position == itemCount - 1)
            holder.itemBinding.blankView.setVisibility(View.VISIBLE)
        else
            holder.itemBinding.blankView.setVisibility(View.GONE)
        val historyItem: HistoryItem= dataList[position]
        holder.itemBinding.historyitem= historyItem
        holder.itemBinding.root.setOnClickListener {
            onItemClick(historyItem)
        }

    }


    class BindingViewHolder(val itemBinding: ItemDeliveryHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}