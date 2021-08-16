package com.zoom2u_customer.ui.application.bottom_navigation.bid_request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.zoom2u_customer.databinding.FragmentBidRequestBinding

class BidRequestFragment : Fragment() {
    lateinit var binding: FragmentBidRequestBinding
    private lateinit var viewpageradapter: ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidRequestBinding.inflate(inflater, container, false)

        viewpageradapter= ViewPagerAdapter(activity?.supportFragmentManager)

        binding.pager.adapter=viewpageradapter
        binding.tabLayout.setupWithViewPager(binding.pager)


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager.currentItem = tab.position

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        return binding.root
    }


}