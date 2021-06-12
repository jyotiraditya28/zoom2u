package com.example.zoom2u.ui.details_base_page.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zoom2u.R
import com.example.zoom2u.databinding.FragmentHomeBinding
import com.example.zoom2u.databinding.FragmentProfileBinding
import com.example.zoom2u.ui.chat.ChatActivity
import com.example.zoom2u.ui.details_base_page.home.map_page.MapActivity
import com.example.zoom2u.ui.details_base_page.profile.change_price.PriceChangeActivity

class ProfileFragment : Fragment() , View.OnClickListener{


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.changePrice.setOnClickListener(this)

        return binding.root
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.change_price -> {
                val intent = Intent(activity, PriceChangeActivity::class.java)
                startActivity(intent)
            }

        }
    }
}