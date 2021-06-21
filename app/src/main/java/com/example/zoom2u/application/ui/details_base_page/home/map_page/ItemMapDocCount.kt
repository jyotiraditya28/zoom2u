package com.example.zoom2u.application.ui.details_base_page.home.map_page

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.databinding.ItemMapDocBinding
import com.example.zoom2u.application.ui.details_base_page.home.home_fragment.Icon

class ItemMapDocCount(val context : Context, private val dataList: List<Icon>) : RecyclerView.Adapter<ItemMapDocCount.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemMapDocBinding =
            ItemMapDocBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val icon : Icon = dataList[position]
        holder.itemBinding.icon= icon
        if(icon.count>0)
            holder.itemBinding.doc.visibility = View.VISIBLE
        else
            holder.itemBinding.doc.visibility = View.GONE

    }


    class BindingViewHolder(val itemBinding: ItemMapDocBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}