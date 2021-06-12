package com.example.zoom2u.ui.details_base_page.profile.change_price

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.databinding.ItemChnagePriceBinding
import com.example.zoom2u.databinding.ItemDeliveryHistoryBinding
import com.example.zoom2u.ui.details_base_page.history.HistoryItem
import com.example.zoom2u.ui.details_base_page.history.HistoryItemAdpter

class PriceChangeAdapter(val context: Context, private val dataList: List<PriceChange>) :
    RecyclerView.Adapter<PriceChangeAdapter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemChnagePriceBinding =
            ItemChnagePriceBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val priceChange: PriceChange = dataList[position]
        holder.itemBinding.priceChange= priceChange
        /*holder.itemBinding.root.setOnClickListener {
            onItemClick(historyItem)
        }*/

    }


    class BindingViewHolder(val itemBinding: ItemChnagePriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root)



}