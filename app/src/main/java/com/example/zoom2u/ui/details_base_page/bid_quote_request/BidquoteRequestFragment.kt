package com.example.zoom2u.ui.details_base_page.bid_quote_request

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zoom2u.R
import com.example.zoom2u.databinding.FragmentBidquoteRequestBinding
import com.example.zoom2u.databinding.FragmentHomeBinding
import com.example.zoom2u.ui.bid_request.BidRequestActivity
import com.example.zoom2u.ui.details_base_page.home.pricing_payment.PricingPaymentActivity
import com.example.zoom2u.ui.qutote_request.QuoteRequestActivity

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
                val intent = Intent(activity, QuoteRequestActivity::class.java)
                startActivity(intent)
            }
        }
    }

}