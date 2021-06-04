package com.example.zoom2u.ui.details_base_page.home.delivery_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityDeliveryDatailsBinding
import com.example.zoom2u.ui.details_base_page.home.pricing_payment.PricingPaymentActivity

class DeliveryDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeliveryDatailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_datails)

        binding.headerTxt.setOnClickListener {
            val intent = Intent(this, PricingPaymentActivity::class.java)
            startActivity(intent)
        }

    }
}