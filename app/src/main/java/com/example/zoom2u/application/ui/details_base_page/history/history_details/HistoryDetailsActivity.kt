package com.example.zoom2u.application.ui.details_base_page.history.history_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.details_base_page.history.HistoryRepository
import com.example.zoom2u.application.ui.details_base_page.history.HistoryResponse
import com.example.zoom2u.application.ui.details_base_page.history.HistoryViewModel
import com.example.zoom2u.application.ui.log_in.LoginRepository
import com.example.zoom2u.application.ui.log_in.LoginViewModel
import com.example.zoom2u.databinding.ActivityHistoryDetailsBinding
import com.example.zoom2u.databinding.FragmentHistoryBinding
import com.example.zoom2u.utility.AppUtility

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
        val serviceApi: ServiceApi = ApiClient.getServices()
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