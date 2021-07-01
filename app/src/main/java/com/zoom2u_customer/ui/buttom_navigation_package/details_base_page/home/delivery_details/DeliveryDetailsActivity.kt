package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.delivery_details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.pricing_payment.PricingPaymentActivity
import com.zoom2u_customer.databinding.ActivityDeliveryDatailsBinding

class DeliveryDetailsActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityDeliveryDatailsBinding
    lateinit var slidedwon: Animation
    lateinit var slideup: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        slidedwon = AnimationUtils.loadAnimation(
            this,
            R.anim.slide_down
        )
        slideup = AnimationUtils.loadAnimation(
            this,
            R.anim.slide_up
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_datails)
        binding.closePickup.setOnClickListener(this)
        binding.openPickup.setOnClickListener(this)
        binding.openDropoff.setOnClickListener(this)
        binding.closeDropOff.setOnClickListener(this)


        binding.nextBtn.setOnClickListener {
            val intent = Intent(this, PricingPaymentActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.close_pickup -> {
                binding.pickupView.startAnimation(slidedwon)

                binding.pickupView.visibility = View.GONE
                binding.dropOffView.visibility = View.VISIBLE

                binding.openDropOffView.visibility = View.GONE
                binding.openPickupView.visibility = View.VISIBLE

                binding.dropOffView.startAnimation(slideup)

            }
            R.id.close_drop_off -> {
                binding.dropOffView.startAnimation(slidedwon)

                binding.dropOffView.visibility = View.GONE
                binding.pickupView.visibility = View.VISIBLE

                binding.openDropOffView.visibility = View.VISIBLE
                binding.openPickupView.visibility = View.GONE

                binding.pickupView.startAnimation(slideup)
            }

            R.id.open_pickup -> {
                binding.dropOffView.startAnimation(slidedwon)

                binding.dropOffView.visibility = View.GONE
                binding.pickupView.visibility = View.VISIBLE

                binding.openDropOffView.visibility = View.VISIBLE
                binding.openPickupView.visibility = View.GONE

                binding.pickupView.startAnimation(slideup)
            }
            R.id.open_dropoff -> {
                binding.pickupView.startAnimation(slidedwon)

                binding.pickupView.visibility = View.GONE
                binding.dropOffView.visibility = View.VISIBLE

                binding.openDropOffView.visibility = View.GONE
                binding.openPickupView.visibility = View.VISIBLE

                binding.dropOffView.startAnimation(slideup)
            }

        }
    }


}