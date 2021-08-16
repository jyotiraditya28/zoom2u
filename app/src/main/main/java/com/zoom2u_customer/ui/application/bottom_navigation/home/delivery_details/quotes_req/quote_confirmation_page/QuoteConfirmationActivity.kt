package com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.quotes_req.quote_confirmation_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityQuoteConfirmationBinding
import com.zoom2u_customer.databinding.ActivityUploadQuotesBinding
import com.zoom2u_customer.utility.CustomTypefaceSpan

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
    }
}