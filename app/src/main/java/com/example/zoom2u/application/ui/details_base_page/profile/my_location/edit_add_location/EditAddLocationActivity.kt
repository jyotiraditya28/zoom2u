package com.example.zoom2u.application.ui.details_base_page.profile.my_location.edit_add_location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResponse
import com.example.zoom2u.application.ui.log_in.LoginRepository
import com.example.zoom2u.application.ui.log_in.LoginViewModel
import com.example.zoom2u.databinding.ActivityEditLocationBinding

class EditAddLocationActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEditLocationBinding
    private var  isEdit: Boolean =false
    private var myLocationResponse: MyLocationResponse? = null

    lateinit var viewModel: EditAddLocationViewModel
    private var repository: EditAddLocationRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location)


        if (intent.hasExtra("EditAddLocation")) {
           isEdit= intent.getBooleanExtra("EditAddLocation", false)
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
        repository = EditAddLocationRepository(serviceApi,this)
        viewModel.repository = repository


    }

    fun setEditDataview(myLocationResponse: MyLocationResponse?) {

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
            R.id.save_change_btn->{
               var location :MyLocationResponse.location= MyLocationResponse.location(ContactName = binding.name.text.toString().trim(),
                   Phone = binding.phone.text.toString().trim(),Email = binding.email.text.toString().trim(),
                    Address = binding.address.text.toString().trim())

                var myLocationResponse :MyLocationResponse= MyLocationResponse(DefaultDropoff = binding.dropOffCheckBox.isChecked,DefaultPickup = binding.pickupCheckBox.isChecked,
                     Location =location)


                if(isEdit){
                    viewModel.getEditAddLocation(myLocationResponse)
               }else{

               }
            }
        }

    }
}