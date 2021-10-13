package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_offers.CompletedBidOffersFragment
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_details.CompletedDetailsFragment

class CompletedViewPagerAdapter (fm: FragmentManager, var bidDetails: CompletedDetailsResponse?) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = CompletedDetailsFragment.newInstance(bidDetails)
        } else if (position == 1) {
            fragment = CompletedBidOffersFragment.newInstance(bidDetails)
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Bid Details"
        } else if (position == 1) {
            title = "Offers(${bidDetails?.Offers?.size})"
        }
        return title
    }
}