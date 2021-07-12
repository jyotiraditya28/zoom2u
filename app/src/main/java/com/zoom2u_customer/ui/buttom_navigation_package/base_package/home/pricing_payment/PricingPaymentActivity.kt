package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.pricing_payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityPricingPaymentBinding
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.pricing_payment.model.IntraStateRes
import com.zoom2u_customer.utility.AppUtility
import java.util.*


class PricingPaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPricingPaymentBinding
    private var intraStateReq: IntraStateReq? = null
    private var intraStateRes: IntraStateRes? = null
    lateinit var viewModel: PricingPaymentViewModel
    private var repository: PricingPaymentRepository? = null
    var adapter : PriceAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_pricing_payment)

        if (intent.hasExtra("IntraStateData")) {
            intraStateReq=intent.getParcelableExtra("IntraStateData")
        }

        viewModel = ViewModelProvider(this).get(PricingPaymentViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = PricingPaymentRepository(serviceApi, this)
        viewModel.repository = repository

        viewModel.getIntraStatePrice(intraStateReq)

        setAdapterView(binding)
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




    }


    fun setAdapterView(binding: ActivityPricingPaymentBinding) {
        val layoutManager = GridLayoutManager(this, 1)
        binding.priceView.layoutManager = layoutManager
        adapter = PriceAdapter(this, Collections.emptyList())
        binding.priceView.adapter=adapter

    }
}