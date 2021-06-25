package com.example.zoom2u.application.ui.details_base_page.profile.my_location

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResponse
import com.example.zoom2u.databinding.ItemMyLocationBinding

class MyLocationAdapter (val context: Context, private var dataList: List<MyLocationResponse>, private val onItemClick:(MyLocationResponse) -> Unit) :
    RecyclerView.Adapter<MyLocationAdapter.BindingViewHolder>() {

    fun updateRecords(dataList: List<MyLocationResponse>) {
       this.dataList = dataList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemMyLocationBinding =
            ItemMyLocationBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val myLocation:MyLocationResponse = dataList[position]
        holder.itemBinding.mylocation= myLocation
        holder.itemBinding.editLocation.setOnClickListener {
            onItemClick(myLocation)
        }

    }


    class BindingViewHolder(val itemBinding: ItemMyLocationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}