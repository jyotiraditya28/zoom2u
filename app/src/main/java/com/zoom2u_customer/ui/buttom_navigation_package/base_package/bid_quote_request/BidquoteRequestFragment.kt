package com.zoom2u_customer.ui.buttom_navigation_package.base_package.bid_quote_request

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zoom2u_customer.R
import com.zoom2u_customer.ui.buttom_navigation_package.bid_request.BidRequestActivity
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.search_location.Location
import com.zoom2u_customer.databinding.FragmentBidquoteRequestBinding

class BidquoteRequestFragment : Fragment() , View.OnClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBidquoteRequestBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener(this)
        binding.button1.setOnClickListener(this)

    return binding.root
}

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button -> {
                val intent = Intent(activity, BidRequestActivity::class.java)
                startActivity(intent)
            }
            R.id.button1 -> {
                val intent = Intent(activity, Location::class.java)
                startActivity(intent)
            }
        }
    }

}