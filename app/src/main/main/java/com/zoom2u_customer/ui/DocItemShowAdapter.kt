package com.zoom2u_customer.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zoom2u_customer.databinding.BidImagesShowBinding
import com.zoom2u_customer.databinding.ItemDocShowBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.ShowBidImageAdapter

class DocItemShowAdpater(val context : Context?, private val dataList: List<String>,
                         ) : RecyclerView.Adapter<DocItemShowAdpater.BindingViewHolder>() {


    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemDocShowBinding =
            ItemDocShowBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val imagePath: String = dataList[position]





    }


    class BindingViewHolder(val itemBinding: ItemDocShowBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}