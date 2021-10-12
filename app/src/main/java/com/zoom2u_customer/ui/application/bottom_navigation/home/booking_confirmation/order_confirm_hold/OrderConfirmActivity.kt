package com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityOrderConfirmBinding
import com.zoom2u_customer.ui.application.bottom_navigation.base_page.BasePageActivity
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse
import com.zoom2u_customer.ui.application.bottom_navigation.history.history_details.HistoryDetailsActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingResponse
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.interstate_booking.InterStateSecondActivity
import com.zoom2u_customer.utility.CustomTypefaceSpan
import com.zoom2u_customer.utility.DialogActivity

class OrderConfirmActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityOrderConfirmBinding
    private var bookingResponse: BookingResponse? = null
    var historyResponse: HistoryResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_confirm)


        if (intent.hasExtra("BookingResponse")) {
            bookingResponse = intent.getParcelableExtra("BookingResponse")
            setData(bookingResponse)
        } else if (intent.hasExtra("BookingRefFromBid")) {
            binding.yourBookingNumberTxt.text = intent.getStringExtra("BookingRefFromBid")
        } else if (intent.hasExtra("historyResponse")) {
            historyResponse = intent.getParcelableExtra("historyResponse")
            binding.yourBookingNumberTxt.text = historyResponse?.BookingRef
            historyResponse?.IsOnHold = false
            binding.close.visibility = View.GONE


        }
        val importantTxtStr =
            "Important: Please make sure your parcel is ready to go at your designated pickup time as you could be charged $1 a minute waiting time. This includes time waiting in the loading dock or reception."
        val ss = SpannableStringBuilder(importantTxtStr)
        ss.setSpan(
            CustomTypefaceSpan("", ResourcesCompat.getFont(this, R.font.arimo_bold)!!),
            0,
            10,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        ss.setSpan(
            CustomTypefaceSpan("", ResourcesCompat.getFont(this, R.font.arimo_regular)!!), 10,
            importantTxtStr.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.ImportantText.text = ss
        binding.viewDeliveryDetails.setOnClickListener(this)
        binding.close.setOnClickListener(this)

    }

    private fun setData(bookingResponse: BookingResponse?) {
        binding.yourBookingNumberTxt.text = bookingResponse?.BookingRef
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.view_delivery_details -> {
                if (intent.hasExtra("historyResponse")) {
                    val intent = Intent()
                    intent.putExtra("historyItem", historyResponse)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    setResult(23, intent)
                    finish()
                } else
                    newBooking()
            }
            R.id.close -> {
                newBooking()
            }

        }
    }

    private fun newBooking() {

        val intent1 = Intent("home_page")
        intent1.putExtra("message","from_booking_confirmation")
        LocalBroadcastManager.getInstance(this@OrderConfirmActivity).sendBroadcast(intent1)

        if (intent.hasExtra("BookingRefFromBid")){
            val intent1 = Intent("open_home_from_bid")
            LocalBroadcastManager.getInstance(this@OrderConfirmActivity).sendBroadcast(intent1)
            finish()
        }
        else {
            val intent = Intent(this, BasePageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (intent.hasExtra("historyResponse")) {
            val intent = Intent()
            intent.putExtra("historyItem", historyResponse)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            setResult(23, intent)
            finish()
        } else
            newBooking()
    }



}