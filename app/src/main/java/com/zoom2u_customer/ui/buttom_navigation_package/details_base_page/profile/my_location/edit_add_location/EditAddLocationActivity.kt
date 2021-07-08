package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.edit_add_location

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient.Companion.getServices
import com.zoom2u_customer.apiclient.GetAddressFromGoogleAPI.Companion.getGoogleServices
import com.zoom2u_customer.apiclient.GoogleServiceApi
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityEditLocationBinding
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.model.AddLocationReq
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.ui.buttom_navigation_package.get_location.GetLocationClass
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONObject
import java.util.*

class EditAddLocationActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionId = 1010
    private var placeAutocompleteRequest = 1019
    lateinit var binding: ActivityEditLocationBinding
    private var isEdit: Boolean = false
    private var myLocationResponse: MyLocationResAndEditLocationReq? = null
    private var addLocationReq: AddLocationReq? = null
    lateinit var viewModel: EditAddLocationViewModel
    private var repository: EditAddLocationRepository? = null
    private var getLocationClass : GetLocationClass?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location)
        AppUtility.hideKeyboard(this)

        getLocationClass = GetLocationClass(this)

        if (intent.hasExtra("EditAddLocation")) {
            isEdit = intent.getBooleanExtra("EditAddLocation", false)
            if (isEdit) {
                binding.header.text = "Edit Location"
                binding.removeCl.visibility = View.VISIBLE
                myLocationResponse = intent.getParcelableExtra("EditLocation")
                showEditDataView(myLocationResponse)
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

        viewModel = ViewModelProvider(this).get(EditAddLocationViewModel::class.java)
        val serviceApi: ServiceApi = getServices()
        val googleServiceApi: GoogleServiceApi = getGoogleServices()
        repository = EditAddLocationRepository(serviceApi, googleServiceApi, this)
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

            viewModel.getDataFromGoogleSuccess()?.observe(this) {
                if (!TextUtils.isEmpty(it)) {
                    AppUtility.progressBarDissMiss()
                    if (it != "") {

                        val jsonObj = JSONObject(it.toString())
                        val resultsJsonArray = jsonObj.getJSONArray("results")
                        if (isEdit) {
                           // myLocationResponse?.Location?.Country = address[0].countryName
                           // myLocationResponse?.Location?.State = address[0].locality
                            myLocationResponse?.Location?.GPSX = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                            myLocationResponse?.Location?.GPSY = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                            myLocationResponse?.Location?.Suburb =resultsJsonArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(0).getString("short_name")
                           // /*check again*/myLocationResponse?.Location?.Street =

                        } else {

                          //  addLocationReq?.Location?.State = address[0].locality
                            addLocationReq?.Location?.Gpsx = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                            addLocationReq?.Location?.Gpsy = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                           addLocationReq?.Location?.Suburb = resultsJsonArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(0).getString("short_name")
                         //   /*check again*/ addLocationReq?.Location?.Street = address[0].adminArea
                        }

                    }
                }

            }

        }
    }


    private fun showEditDataView(myLocationResponse: MyLocationResAndEditLocationReq?) {
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
            R.id.address -> {
                if (!Places.isInitialized()) {
                    val apiKey = getString(R.string.google_api_key)
                    Places.initialize(applicationContext, apiKey)
                }

                try {
                    val fields =
                        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields
                    )
                        //.setCountry("AU")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this)

                    startActivityForResult(intent, placeAutocompleteRequest)
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
                getLocationClass?.getCurrentLocation(onAddress = ::getAddress)
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
                    myLocationResponse?.Location?.Notes = binding.notes.text.toString().trim()
                    viewModel.editLocation(myLocationResponse)
                } else {

                    addLocationReq?.DefaultPickup = binding.pickupCheckBox.isChecked
                    addLocationReq?.DefaultDropoff = binding.dropOffCheckBox.isChecked

                    addLocationReq?.Location?.Address = binding.address.text.toString().trim()
                    addLocationReq?.Location?.CompanyName = ""
                    addLocationReq?.Location?.ContactName = binding.name.text.toString().trim()
                    addLocationReq?.Location?.Email = binding.email.text.toString().trim()
                    addLocationReq?.Location?.Notes = binding.notes.text.toString().trim()
                    addLocationReq?.Location?.Phone = binding.phone.text.toString().trim()
                    addLocationReq?.Location?.UnitNumber = ""
                    addLocationReq?.Location?.Suburb = ""


                    if (checkValidation(binding.name.text.toString().trim(), binding.phone.text.toString().trim(), binding.address.text.toString().trim()))
                        viewModel.addLocation(addLocationReq)
                }
            }
        }
    }

    private fun checkValidation(name: String, phone: String, address: String): Boolean {
        if (name == "" && phone == "" && address == "") {
            AppUtility.validateEditTextField(
                binding.name,
                "Please enter a contact name & business."
            )
            AppUtility.validateEditTextField(binding.phone, "Please enter a valid mobile number.")
            AppUtility.validateEditTextField(binding.address, "Please enter address.")
            return false
        } else if (name == "") {
            AppUtility.validateEditTextField(
                binding.name,
                "Please enter a contact name & business."
            )
            return false
        } else if (phone == "") {
            AppUtility.validateEditTextField(binding.phone, "Please enter a valid mobile number.")
            return false
        } else if (address == "") {
            AppUtility.validateEditTextField(binding.address, "Please enter address.")
            return false
        }
        return true
    }


    private fun getAddress(lat: Double, long: Double) {

        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        address[0].locality
        address[0].countryName
        binding.address.setText(address[0].getAddressLine(0))

        if (isEdit) {
           // myLocationResponse?.Location?.State = address[0].adminArea
            myLocationResponse?.Location?.GPSX = address[0].latitude
            myLocationResponse?.Location?.GPSY = address[0].longitude
            myLocationResponse?.Location?.Postcode = address[0].postalCode
            myLocationResponse?.Location?.Street = address[0].locality
            myLocationResponse?.Location?.Suburb = address[0].adminArea
        } else {
            addLocationReq?.Location?.Country = address[0].countryName
            addLocationReq?.Location?.State = address[0].locality
            addLocationReq?.Location?.Gpsx = address[0].latitude
            addLocationReq?.Location?.Gpsy = address[0].longitude
            addLocationReq?.Location?.Postcode = address[0].postalCode
            addLocationReq?.Location?.Street = address[0].adminArea
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == placeAutocompleteRequest) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.address.setText(place.address)
                /*call google api for get data using selected address*/
                viewModel.dataFromGoogle(binding.address.text.toString())

                Log.i("Place API Success", "  -------------- Place -------------" + place.name)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(
                    data!!
                )
                binding.address.setText("")
                Log.i(
                    "Place API Failure",
                    "  -------------- Error -------------" + status.statusMessage
                )
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Place API Failure", "  -------------- User Cancelled -------------")
            }
        }
    }

    private fun getAddressData() {


        /*try {
            if (binding.address.text.toString() != "") {
                var addressToGeocoderStr = ""
                addressToGeocoderStr = binding.address.text.toString()
                if (getaddresForNewLocations != null) getaddresForNewLocations = null
                getaddresForNewLocations =
                    GetAddressFromGoogleAPI.getAddressDetailGeoCoder(addressToGeocoderStr)
                try {
                    pickUplat = getaddresForNewLocations.get("latitude") as Double
                    pickUpLong = getaddresForNewLocations.get("longitude") as Double
                    countryStr = getaddresForNewLocations.get("country") as String
                    suburbStr = getaddresForNewLocations.get("suburb") as String
                    stateStr = getaddresForNewLocations.get("state") as String
                    postCodeStr = getaddresForNewLocations.get("postcode") as String
                    var setAddressTxt = getaddresForNewLocations.get("address") as String
                    if (getaddresForNewLocations.get("streetNumber") != null) {
                        if ((getaddresForNewLocations.get("address") as String).contains(
                                (getaddresForNewLocations.get(
                                    "streetNumber"
                                ) as String)
                            )
                        ) setAddressTxt = setAddressTxt.replace(
                            (getaddresForNewLocations.get("streetNumber") as String), ""
                        ).trim { it <= ' ' }
                    }
                    if (setAddressTxt != "") {
                        edtLocationdetails.setText(setAddressTxt)
                        if (getaddresForNewLocations.get("streetNumber") != null) edtStreetNo.setText(
                            getaddresForNewLocations.get("streetNumber") as String
                        )
                    }
                    if (edtStreetNo.getText().toString() != "") edtStreetNo.setError(null)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    edtLocationdetails.setText("")
                    edtStreetNo.setText("")
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
*/
    }




}