package com.example.zoom2u.application.ui.details_base_page.profile.my_location.edit_add_location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.example.zoom2u.databinding.ActivityEditLocationBinding
import com.example.zoom2u.utility.AppUtility

class EditAddLocationActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEditLocationBinding
    private var isEdit: Boolean = false
    private var myLocationResponse: MyLocationResAndEditLocationReq? = null

    lateinit var viewModel: EditAddLocationViewModel
    private var repository: EditAddLocationRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location)


        if (intent.hasExtra("EditAddLocation")) {
            isEdit = intent.getBooleanExtra("EditAddLocation", false)
            if (isEdit) {
                binding.header.text = "Edit Location"
                binding.removeCl.visibility = View.VISIBLE
                myLocationResponse = intent.getParcelableExtra("EditLocation")
                setEditDataview(myLocationResponse)
            } else {
                binding.removeCl.visibility = View.GONE
                binding.header.text = "Add Location"

            }
        }


        binding.saveBtn.setOnClickListener(this)
        binding.saveChangeBtn.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(EditAddLocationViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = EditAddLocationRepository(serviceApi, this)
        viewModel.repository = repository


        viewModel.getEditAddLocSuccess()?.observe(this) {
            if (!TextUtils.isEmpty(it)) {
                AppUtility.progressBarDissMiss()
                if (it != "") {


                }
            }


        }

    }

    fun setEditDataview(myLocationResponse: MyLocationResAndEditLocationReq?) {

        binding.name.setText(myLocationResponse?.Location?.ContactName)
        binding.email.setText(myLocationResponse?.Location?.Email)
        binding.phone.setText(myLocationResponse?.Location?.Phone)
        binding.address.setText(myLocationResponse?.Location?.Address)
        binding.pickupCheckBox.isChecked = myLocationResponse?.DefaultPickup == true
        binding.dropOffCheckBox.isChecked = myLocationResponse?.DefaultDropoff == true

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.save_btn -> {


            }
            R.id.save_change_btn -> {
                myLocationResponse?.DefaultPickup = binding.pickupCheckBox.isChecked
                myLocationResponse?.DefaultDropoff = binding.dropOffCheckBox.isChecked

                myLocationResponse?.Location?.Address = binding.address.text.toString().trim()
                myLocationResponse?.Location?.ContactName = binding.name.text.toString().trim()
                myLocationResponse?.Location?.Email = binding.email.text.toString().trim()
                myLocationResponse?.Location?.Phone = binding.phone.text.toString().trim()

                if (isEdit) {
                    viewModel.getEditAddLocation(myLocationResponse)
                } else {

                }
            }
        }

    }
}