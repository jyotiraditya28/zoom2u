package com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ItemPriceBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.model.QuoteOptionClass


class PricePaymentAdapter(
    val context: Context, private var dataList: List<QuoteOptionClass>,
    private val onItemClick: (QuoteOptionClass) -> Unit,

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

        if(dataList[position].DeliverySpeed=="Interstate")
            holder.itemBinding.priceTag.text="Premium air"
        else
            holder.itemBinding.priceTag.text=dataList[position].DeliverySpeed

        holder.itemBinding.info.setOnClickListener{
            if(quoteOptionClass.DeliverySpeed=="Same day"){
                showPopup(
                    holder.itemBinding.info,
                    "Same day",
                    "Delivery will be picked up and delivered within the requested time frame.\n" +
                            "\n" +
                            "Please ensure that both locations will be open during these hours as we are not able to guarantee an earlier collection or delivery time on this service."
                )
            }
            if(quoteOptionClass.DeliverySpeed=="3 hour"){
                showPopup(
                    holder.itemBinding.info,
                    "3 hour",
                    "Delivery will be picked up and delivered within the 3 hour window of your choosing.\n" +
                            "\n" +
                            "Please be aware that there is no guaranteed collection time on this service."
                )
            }
            if(quoteOptionClass.DeliverySpeed=="VIP"){
                showPopup(
                    holder.itemBinding.info,
                    "ASAP",
                    "Delivery will be collected as close to the requested time as possible and delivered directly to the drop location."
                )
            }
            if(quoteOptionClass.DeliverySpeed=="Interstate"){
                showPopup(
                    holder.itemBinding.info,
                    "Premium air",
                    "Delivery will be picked up and delivered to the airport within 3 hours of the indicated ready time.\n" +
                            "\n" +
                            "The package will then be placed on the next available flight to the destination depot.\n" +
                            "\n" +
                            "Once the package is available for collection at the destination depot the driver will collect and deliver to the final drop location.\n" +
                            "\n"
                )
            }
        }
        if (rowIndex == position) {
            if (dataList[position].isPriceSelect == false) {
                holder.itemBinding.selectBtn.setBackgroundResource(R.drawable.selected_background)
                holder.itemBinding.selectBtn.text = "Selected"
                dataList[position].isPriceSelect = true
            }else{
                holder.itemBinding.selectBtn.setBackgroundResource(R.drawable.chip_background)
                holder.itemBinding.selectBtn.text = "Select"
                dataList[position].isPriceSelect = false
            }
        }else {
            holder.itemBinding.selectBtn.setBackgroundResource(R.drawable.chip_background)
            holder.itemBinding.selectBtn.text = "Select"
        }



    }


    class BindingViewHolder(val itemBinding: ItemPriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    @SuppressLint("ClickableViewAccessibility")
    fun showPopup(view: View, alertTitle: String, alertMsg: String) {

        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val popupView = inflater?.inflate(R.layout.price_info_dialog, null)

        val titleAlert: TextView? = popupView?.findViewById(R.id.price_type)
        titleAlert?.text = alertTitle

        val msgAlertDialog: TextView? = popupView?.findViewById(R.id.price_desc)
        msgAlertDialog?.text = alertMsg


        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, 500, height, focusable)


        popupWindow.showAsDropDown(view, -100, 0,Gravity.LEFT);

        popupView?.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }
    }
}