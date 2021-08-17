package com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.quotes_req.quote_confirmation_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityQuoteConfirmationBinding
import com.zoom2u_customer.databinding.ActivityUploadQuotesBinding
import com.zoom2u_customer.ui.application.bottom_navigation.base_page.BasePageActivity
import com.zoom2u_customer.ui.application.bottom_navigation.history.history_details.HistoryDetailsActivity
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.CustomTypefaceSpan
import com.zoom2u_customer.utility.DialogActivity

class QuoteConfirmationActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuoteConfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quote_confirmation)

        val importantTxtStr =
            "Status: Awaiting quotes from couriers. These should arrive within 10mins."
        val ss = SpannableStringBuilder(importantTxtStr)
        ss.setSpan(CustomTypefaceSpan("", ResourcesCompat.getFont(this, R.font.arimo_bold)!!), 0, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        ss.setSpan(
            CustomTypefaceSpan("", ResourcesCompat.getFont(this, R.font.arimo_regular)!!), 10,
            importantTxtStr.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.txtAwaitingQuotes.text=ss

        binding.close.setOnClickListener(){
            val intent = Intent(this, BasePageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }
    }

    private fun newBooking(){
        val intent = Intent(this, BasePageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        DialogActivity.logoutDialog(this, "Are you sure!", "Are you want make a new Booking?",
            "Ok","Cancel",
            onCancelClick=::onCancelClick,
            onOkClick = ::onOkClick)
    }
    private fun onCancelClick(){}
    private fun onOkClick() {
        newBooking()
    }
}