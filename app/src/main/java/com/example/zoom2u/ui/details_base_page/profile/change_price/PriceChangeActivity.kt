package com.example.zoom2u.ui.details_base_page.profile.change_price

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityChangePriceBinding
import com.example.zoom2u.databinding.ActivityPricingPaymentBinding
import com.example.zoom2u.ui.details_base_page.home.pricing_payment.PriceAdpter
import com.example.zoom2u.ui.details_base_page.home.pricing_payment.PriceDataProvider

class PriceChangeActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePriceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_price)

        setAdpterView()
    }

    fun setAdpterView() {
        var layoutManager = GridLayoutManager(this, 1)
        binding.priceChangeView.layoutManager = layoutManager
        var adapter = PriceChangeAdapter(this, PriceChangeProvider.priceChangeList.toList())
        binding.priceChangeView.adapter=adapter

    }
}