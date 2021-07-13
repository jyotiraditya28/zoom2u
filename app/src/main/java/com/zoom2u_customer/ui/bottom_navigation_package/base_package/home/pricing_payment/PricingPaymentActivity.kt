package com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.pricing_payment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityPricingPaymentBinding
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.booking_confirmation.BookingConfirmationActivity
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.delivery_details.model.InterStateReq
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.pricing_payment.model.InterStateRes
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.pricing_payment.model.IntraStateRes
import com.zoom2u_customer.utility.AppUtility
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class PricingPaymentActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityPricingPaymentBinding
    private var intraStateReq: IntraStateReq? = null
    private var interStateReq: InterStateReq? = null
    lateinit var viewModel: PricingPaymentViewModel
    private var repository: PricingPaymentRepository? = null
    var adapter : PricePaymentAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_pricing_payment)



        viewModel = ViewModelProvider(this).get(PricingPaymentViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = PricingPaymentRepository(serviceApi, this)
        viewModel.repository = repository


        if (intent.hasExtra("IntraStateData")) {
            intraStateReq=intent.getParcelableExtra("IntraStateData")
            viewModel.getIntraStatePrice(intraStateReq)
        }else if(intent.hasExtra("InterStateData")){
            interStateReq=intent.getParcelableExtra("InterStateData")
            viewModel.getInterStatePrice(interStateReq)
        }

        Timmer()
        setAdapterView(binding)

    /** get price using intra state*/
        viewModel.intraStateSuccess()?.observe(this) {
            if(it!=null){
                AppUtility.progressBarDissMiss()
                val intraStateRes:IntraStateRes = Gson().fromJson(it, IntraStateRes::class.java)
                val quoteOptionList =intraStateRes.QuoteOptions
                if (quoteOptionList != null) {
                    adapter?.updateRecords(quoteOptionList)
                }
            }
        }

        /** get price using inter state*/
        viewModel.interStateSuccess()?.observe(this) {
            if(it!=null){
                AppUtility.progressBarDissMiss()
                val interStateRes: InterStateRes = Gson().fromJson(it, InterStateRes::class.java)
                val quoteOptionList =interStateRes.QuoteOptions
                if (quoteOptionList != null) {
                    adapter?.updateRecords(quoteOptionList)
                }
            }
        }


     binding.nextBtn.setOnClickListener(this)
     binding.startTimmer.setOnClickListener(this)
    }


    fun setAdapterView(binding: ActivityPricingPaymentBinding) {
        val layoutManager = GridLayoutManager(this, 1)
        binding.priceView.layoutManager = layoutManager
        adapter = PricePaymentAdapter(this, Collections.emptyList())
        binding.priceView.adapter=adapter

    }


    private fun Timmer() {
        object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.timer.text = f.format(min) + ":" + f.format(sec)
                binding.expiredQuote.visibility=View.GONE
                binding.expiredQuote1.visibility=View.GONE
            }

            override fun onFinish() {
                binding.timer.text = "3:00"
                binding.expiredQuote.visibility=View.VISIBLE
                binding.expiredQuote1.visibility=View.VISIBLE
            }
        }.start()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.next_btn -> {
                val intent = Intent(this, BookingConfirmationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.start_time->{
                Timmer()
            }
        }
    }
}