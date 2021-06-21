package com.example.zoom2u.application.ui.bid_request.active_bid_request.active_bid_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityActiveBidBinding

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