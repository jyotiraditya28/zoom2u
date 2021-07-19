package com.zoom2u_customer.ui.application.base_package.home.pricing_payment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ItemPriceBinding
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.model.QuoteOptionClass


class PricePaymentAdapter(
    val context: Context, private var dataList: List<QuoteOptionClass>,
    private val onItemClick: (QuoteOptionClass) -> Unit
) : RecyclerView.Adapter<PricePaymentAdapter.BindingViewHolder>() {

    private var rowIndex: Int? = null

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


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val quoteOptionClass: QuoteOptionClass = dataList[position]
        holder.itemBinding.quoteOptionClass = quoteOptionClass
        holder.itemBinding.price.text = "$" + (dataList[position].BookingFee as Int +
                dataList[position].Price as Int).toString()


        holder.itemBinding.selectBtn.setOnClickListener {
            rowIndex = position
            onItemClick(dataList[position])
            notifyDataSetChanged()
        }


        if (rowIndex == position) {
            holder.itemBinding.selectBtn.setBackgroundResource(R.drawable.selected_background)
            holder.itemBinding.selectBtn.text = "Selected"
        } else{
            holder.itemBinding.selectBtn.setBackgroundResource(R.drawable.chip_background)
                holder.itemBinding.selectBtn.text = "Select"
            }
    }


    class BindingViewHolder(val itemBinding: ItemPriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}