package com.zoom2u_customer.application.ui.details_base_page.profile.my_location.edit_add_location

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.internal.ContextUtils.getActivity
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.AddLocationReq
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.databinding.ActivityEditLocationBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import java.util.*

class EditAddLocationActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010
    var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1019

    lateinit var binding: ActivityEditLocationBinding
    private var isEdit: Boolean = false
    private var myLocationResponse: MyLocationResAndEditLocationReq? = null
    lateinit var viewModel: EditAddLocationViewModel
    private var repository: EditAddLocationRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location)
        AppUtility.hideKeyboard(this)

        if (intent.hasExtra("EditAddLocation")) {
            isEdit = intent.getBooleanExtra("EditAddLocation", false)
            if (isEdit) {
                binding.header.text = "Edit Location"
                binding.removeCl.visibility = View.VISIBLE
                myLocationResponse = intent.getParcelableExtra("EditLocation")
                setEditDataView(myLocationResponse)
            } else {
                binding.removeCl.visibility = View.GONE
                binding.header.text = "Add Location"

            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.findMe.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)
        binding.saveChangeBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.removeTxt.setOnClickListener(this)
        binding.address.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(EditAddLocationViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = EditAddLocationRepository(serviceApi, this)
        viewModel.repository = repository


        viewModel.getEditLocationSuccess()?.observe(this) {
            if (!TextUtils.isEmpty(it)) {
                AppUtility.progressBarDissMiss()
                if (it != "") {
                    val intent = Intent()
                    setResult(3, intent)
                    Toast.makeText(
                        this,
                        "My Location details updated successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
            viewModel.getAddLocationSuccess()?.observe(this) {
                if (!TextUtils.isEmpty(it)) {
                    AppUtility.progressBarDissMiss()
                    if (it != "") {
                        val intent = Intent()
                        setResult(4, intent)
                        Toast.makeText(
                            this,
                            "My Location details Added successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }

            }

            viewModel.getDeleteLocationSuccess()?.observe(this) {
                if (!TextUtils.isEmpty(it)) {
                    AppUtility.progressBarDissMiss()
                    if (it != "") {
                        val intent = Intent()
                        setResult(3, intent)
                        Toast.makeText(
                            this,
                            "Location delete successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }

            }
        }
    }





    fun setEditDataView(myLocationResponse: MyLocationResAndEditLocationReq?) {

        binding.name.setText(myLocationResponse?.Location?.ContactName)
        binding.email.setText(myLocationResponse?.Location?.Email)
        binding.phone.setText(myLocationResponse?.Location?.Phone)
        binding.address.setText(myLocationResponse?.Location?.Address)
        binding.pickupCheckBox.isChecked = myLocationResponse?.DefaultPickup == true
        binding.dropOffCheckBox.isChecked = myLocationResponse?.DefaultDropoff == true

    }

    private fun onItemClick() {
        viewModel.deleteLocation(myLocationResponse?.PreferredLocationId)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
          R.id.address->{
              try {
                  val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
                  val intent = Autocomplete.IntentBuilder(
                      AutocompleteActivityMode.OVERLAY, fields
                  )
                      .setCountry("AU")
                      .setTypeFilter(TypeFilter.ADDRESS)
                      .build(this)

                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
              } catch (e: Exception) {
                  e.printStackTrace()
              }
          }
            R.id.remove_txt -> {
                DialogActivity.alertDialogDoubleButton(
                    this,
                    "Confirmation !",
                    "Are you sure you want to delete this location?",
                    onItemClick = ::onItemClick
                )

            }
            R.id.find_me -> {
                getCurrentLocation()
            }
            R.id.back_btn -> {
                finish()
            }
            R.id.save_change_btn -> {
                if (isEdit) {
                    myLocationResponse?.DefaultPickup = binding.pickupCheckBox.isChecked
                    myLocationResponse?.DefaultDropoff = binding.dropOffCheckBox.isChecked

                    myLocationResponse?.Location?.Address = binding.address.text.toString().trim()
                    myLocationResponse?.Location?.ContactName = binding.name.text.toString().trim()
                    myLocationResponse?.Location?.Email = binding.email.text.toString().trim()
                    myLocationResponse?.Location?.Phone = binding.phone.text.toString().trim()
                    viewModel.editLocation(myLocationResponse)
                } else {

                    val location = AddLocationReq.Location1(
                        binding.address.text.toString().trim(),
                        binding.name.text.toString().trim(), "Australia", -25.274398, 133.775136,
                        binding.phone.text.toString().trim(), ""
                    )

                    val location2 = AddLocationReq.Location2(
                        binding.email.text.toString().trim(), "", "", "" +
                                "", "", "", "", "", "", "",
                        "", "", ""
                    )
                    val addLocationReq = AddLocationReq(
                        binding.pickupCheckBox.isChecked, binding.dropOffCheckBox.isChecked,
                        location, location2
                    )

                    viewModel.addLocation(addLocationReq)
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location:Location? = task.result
                    if(location == null){
                        var locationRequest =  LocationRequest()
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationRequest.interval = 0
                        locationRequest.fastestInterval = 0
                        locationRequest.numUpdates = 1
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient!!.requestLocationUpdates(
                            locationRequest,locationCallback, Looper.myLooper()
                        )
                    }else{
                        getAddress(location.latitude,location.longitude)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            requestPermission()
        }
    }

    private fun getAddress(lat: Double, long: Double){

        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        address[0].locality
        address[0].countryName
        binding.address.setText(address[0].getAddressLine(0))

        if (isEdit) {

            myLocationResponse?.Location?.Country=address[0].countryName
            myLocationResponse?.Location?.State =address[0].locality
            myLocationResponse?.Location?.GPSX =address[0].latitude
            myLocationResponse?.Location?.GPSY =address[0].longitude

            myLocationResponse?.Location?.Postcode =address[0].postalCode
        }

    }


    private fun checkPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false

    }


    private fun requestPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
        }
    }

}