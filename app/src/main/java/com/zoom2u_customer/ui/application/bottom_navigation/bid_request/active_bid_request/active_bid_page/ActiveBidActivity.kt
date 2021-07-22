package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R

import com.zoom2u_customer.databinding.ActivityActiveBidBinding

class ActiveBidActivity : AppCompatActivity() {
    lateinit var binding: ActivityActiveBidBinding
    internal lateinit var viewpageradapter: BidViewPagerAdpter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_bid)
        viewpageradapter= BidViewPagerAdpter(supportFragmentManager)

        binding.pager.adapter=viewpageradapter
        binding.tabLayout.setupWithViewPager(binding.pager)

    }
}