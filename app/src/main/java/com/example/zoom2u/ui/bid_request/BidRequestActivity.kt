package com.example.zoom2u.ui.bid_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityBidRequestBinding
import com.google.android.material.tabs.TabLayout

class BidRequestActivity : AppCompatActivity() {
    lateinit var binding: ActivityBidRequestBinding
    internal lateinit var viewpageradapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_bid_request)

        viewpageradapter= ViewPagerAdapter(supportFragmentManager)

        binding.pager.adapter=viewpageradapter
        binding.tabLayout.setupWithViewPager(binding.pager)

        binding.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager!!.currentItem = tab.position

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}