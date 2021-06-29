package com.example.zoom2u.application.ui.details_base_page.profile.my_location.search_location

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.R

class Adapter_AddAddress_n_Fav  (private val mContext: Context, private val addressNFavList: java.util.ArrayList<Model_AddAddress_n_Fav>,
                                 val itemClickListner : OnItemClickListener) : RecyclerView.Adapter<Adapter_AddAddress_n_Fav.SimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_address_n_fav_list, parent, false)
        return SimpleViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Model_AddAddress_n_Fav)
    }

    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
        val item = addressNFavList[position]
        viewHolder.bind(item, itemClickListner)
        viewHolder.placeTxt_AddAddressTitleView.text = item.getPlaceName()
        viewHolder.placeTxt_AddAddressView.text = /*item.getAddressName()*/
            ArrayList<String>(item.getAddressName()!!.split(",").map { it.trim() }).apply {
                removeAt(size - 1)
            }.joinToString()
    }

    override fun getItemCount(): Int {
        return addressNFavList.size
    }

    fun distinctNotifyDataChanged() {
        val temp = addressNFavList.distinctBy { it.getPlaceId() }
        addressNFavList.clear()
        addressNFavList.addAll(temp)
        notifyDataSetChanged()
    }

    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Model_AddAddress_n_Fav, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }

        internal var placeTxt_AddAddressTitleView: TextView = itemView.findViewById(R.id.placeTxt_AddAddressTitleView) as TextView
        internal var placeTxt_AddAddressView: TextView = itemView.findViewById(R.id.placeTxt_AddAddressView) as TextView
    }
}