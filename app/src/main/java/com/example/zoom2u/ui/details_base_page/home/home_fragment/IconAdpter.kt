package com.example.zoom2u.ui.details_base_page.home.home_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.databinding.IconItemBinding

class IconAdpter(val context :Context,private val dataList: List<Icon>) : RecyclerView.Adapter<IconAdpter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: IconItemBinding =
            IconItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
          if(position%5==0&&position!=0)
              holder.itemBinding.loadMoreIcon.visibility= View.VISIBLE
        else  holder.itemBinding.loadMoreIcon.visibility= View.GONE
        val icon : Icon = dataList[position]
        holder.itemBinding.icon = icon


    }


    class BindingViewHolder(val itemBinding: IconItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}