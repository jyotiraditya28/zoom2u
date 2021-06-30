package com.zoom2u_customer.application.ui.details_base_page.history.history_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.application.ui.details_base_page.history.HistoryResponse
import com.zoom2u_customer.databinding.ActivityHistoryDetailsBinding
import com.zoom2u_customer.utility.AppUtility

class HistoryDetailsActivity : AppCompatActivity() {
    private var historyItem: HistoryResponse? = null
    lateinit var viewModel: HistoryDetailsViewModel
    private var repository: HistoryDetailsRepository? = null
    lateinit var binding: ActivityHistoryDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("HistoryItem") != null)
            historyItem = intent.getParcelableExtra("HistoryItem")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_details)

        viewModel = ViewModelProviders.of(this).get(HistoryDetailsViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = HistoryDetailsRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.setHistoryDetails(historyItem?.BookingRef)

        viewModel.getHistoryDetails()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                setDataToView(it)
            }
        }
    }

    private fun setDataToView(it: HistoryDetailsResponse) {

        binding.pickAdd.text = it.PickupAddress
        binding.pickName.text = it.PickupContactName
        binding.pickPhone.text = it.PickupPhone
        binding.pickEmail.text = it.PickupEmail


        binding.dropAdd.text = it.DropAddress
        binding.dropName.text = it.DropContactName
        binding.dropPhone.text = it.DropPhone
        binding.dropEmail.text = it.DropEmail
        binding.dropAdd.text = it.DropAddress


        if(!TextUtils.isEmpty(it.PackageNotes))
            binding.packageNote.text =it.PackageNotes
        else
            binding.packageNote.text = "Important Documents Please take care of those boxes."



    }

}