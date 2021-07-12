package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.pricing_payment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.databinding.ItemPriceBinding
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.pricing_payment.model.QuoteOptionClass



class PriceAdapter(val context : Context, private var dataList: List<QuoteOptionClass>) : RecyclerView.Adapter<PriceAdapter.BindingViewHolder>() {


    fun updateRecords(dataList: List<QuoteOptionClass>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemPriceBinding =
            ItemPriceBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val quoteOptionClass : QuoteOptionClass = dataList[position]
        holder.itemBinding.quoteOptionClass= quoteOptionClass


    }


    class BindingViewHolder(val itemBinding: ItemPriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}