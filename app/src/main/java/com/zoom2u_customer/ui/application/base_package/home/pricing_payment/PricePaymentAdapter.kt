package com.zoom2u_customer.ui.application.base_package.home.pricing_payment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ItemPriceBinding
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.model.QuoteOptionClass



class PricePaymentAdapter(val context : Context, private var dataList: List<QuoteOptionClass>,
        private val onItemClick: (QuoteOptionClass) -> Unit) : RecyclerView.Adapter<PricePaymentAdapter.BindingViewHolder>() {


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
        holder.itemBinding.price.text = "$"+(dataList[position].BookingFee as Int+
                dataList[position].Price as Int).toString()


        holder.itemBinding.selectBtn.setOnClickListener {
            holder.itemBinding.rootView.setBackgroundResource(R.drawable.blankbox)
            onItemClick(dataList[position])
        }
    }


    class BindingViewHolder(val itemBinding: ItemPriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}