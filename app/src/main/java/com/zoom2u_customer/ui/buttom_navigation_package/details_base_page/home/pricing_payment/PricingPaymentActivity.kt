package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.pricing_payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityPricingPaymentBinding


class PricingPaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPricingPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_pricing_payment)

        setAdpterView(binding)


    }


    fun setAdpterView(binding: ActivityPricingPaymentBinding) {
        var layoutManager = GridLayoutManager(this, 1)
        binding.priceView.layoutManager = layoutManager
        var adapter = PriceAdpter(this, PriceDataProvider.priceList.toList())
        binding.priceView.adapter=adapter

    }
}