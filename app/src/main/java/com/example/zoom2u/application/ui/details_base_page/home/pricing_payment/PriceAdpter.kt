package com.example.zoom2u.application.ui.details_base_page.home.pricing_payment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.databinding.ItemPriceBinding


class PriceAdpter(val context : Context, private val dataList: List<Price>) : RecyclerView.Adapter<PriceAdpter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemPriceBinding =
            ItemPriceBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val price : Price = dataList[position]
        holder.itemBinding.price= price


    }


    class BindingViewHolder(val itemBinding: ItemPriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}