package com.example.zoom2u.application.ui.qutote_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityQuoterequestBinding

class QuoteRequestActivity : AppCompatActivity() , View.OnClickListener{
    lateinit var binding: ActivityQuoterequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_quoterequest)
        binding.closePage.setOnClickListener(this)
        binding.chatBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.close_page -> {
                overridePendingTransition(0, R.anim.slide_down)
            }
        }
    }
}