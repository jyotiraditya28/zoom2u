package com.example.zoom2u.application.ui.details_base_page.bid_quote_request

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zoom2u.R
import com.example.zoom2u.databinding.FragmentBidquoteRequestBinding
import com.example.zoom2u.application.ui.bid_request.BidRequestActivity
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.search_location.Location
import com.example.zoom2u.application.ui.qutote_request.QuoteRequestActivity

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