package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository
import com.zoom2u_customer.apiclient.GetAddressFromGoogleAPI
import com.zoom2u_customer.apiclient.GoogleServiceApi
import com.zoom2u_customer.apiclient.ServiceApi

import com.zoom2u_customer.databinding.ActivityActiveBidBinding
import com.zoom2u_customer.utility.AppUtility

class ActiveBidActivity : AppCompatActivity() {
    lateinit var binding: ActivityActiveBidBinding
    private var quoteID:Int?=null
    private lateinit var viewpageradapter: BidViewPagerAdapter
    lateinit var viewModel: BidDetailsViewModel
    private var repositoryGoogleAddress: GoogleAddressRepository? = null
    private var repository: BidDetailsRepository? =null
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_bid)

       if(intent.hasExtra("QuoteId")){
           quoteID=intent.getStringExtra("QuoteId")?.toInt()
       }

            viewModel = ViewModelProvider(this).get(BidDetailsViewModel::class.java)
            val googleServiceApi: GoogleServiceApi = GetAddressFromGoogleAPI.getGoogleServices()
            val serviceApi: ServiceApi = ApiClient.getServices()
            repositoryGoogleAddress = GoogleAddressRepository(googleServiceApi, this)
            repository = BidDetailsRepository(serviceApi, this)
            viewModel.repository = repository
            viewModel.repositoryGoogleAddress = repositoryGoogleAddress

            viewModel.getBidDetails(quoteID)

            viewModel.getBidDetailsSuccess()?.observe(this) {
                if (it != null) {
                    AppUtility.progressBarDissMiss()
                    viewpageradapter= BidViewPagerAdapter(supportFragmentManager,it)
                    binding.pager.adapter=viewpageradapter
                    binding.tabLayout.setupWithViewPager(binding.pager)
                }
            }


    }
}