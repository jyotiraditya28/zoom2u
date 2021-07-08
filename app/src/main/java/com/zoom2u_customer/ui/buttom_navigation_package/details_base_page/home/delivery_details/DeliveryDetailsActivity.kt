package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.delivery_details

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.GetAddressFromGoogleAPI
import com.zoom2u_customer.apiclient.GoogleServiceApi
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityDeliveryDatailsBinding
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.pricing_payment.PricingPaymentActivity
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.MyLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.edit_add_location.EditAddLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.edit_add_location.EditAddLocationViewModel
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.ui.buttom_navigation_package.get_location.GetLocationClass
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.CustomProgressBar
import com.zoom2u_customer.utility.DialogActivity
import java.util.*


class DeliveryDetailsActivity : AppCompatActivity(), View.OnClickListener{
    private var pickAutocompleteRequest = 1019
    private var dropAutocompleteRequest = 1019
    lateinit var binding: ActivityDeliveryDatailsBinding
    lateinit var viewModel: DeliveryDetailsViewModel
    private lateinit var viewModeLocation: EditAddLocationViewModel
    private var repositoryLocation: EditAddLocationRepository? = null
    private var repository: MyLocationRepository? = null
    var bookDeliveryAlertMsgStr = ""
    private var getLocationClass: GetLocationClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_datails)
        viewModel = ViewModelProvider(this).get(DeliveryDetailsViewModel::class.java)

        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = MyLocationRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.getMyLocationList()

        viewModeLocation = ViewModelProvider(this).get(EditAddLocationViewModel::class.java)
        val googleServiceApi: GoogleServiceApi = GetAddressFromGoogleAPI.getGoogleServices()
        repositoryLocation = EditAddLocationRepository(serviceApi, googleServiceApi, this)
        viewModel.repository = repository




        viewModel.getMySuccess()?.observe(this) {
            if (it != null) {
                CustomProgressBar.dismissProgressBar()
                if (it.isNotEmpty()) {
                    setDataToContact(it)
                }

            }

        }


        getLocationClass = GetLocationClass(this)
        binding.nextBtn.setOnClickListener(this)
        binding.pickFindMe.setOnClickListener(this)
        binding.dropFindMe.setOnClickListener(this)
        binding.pickAddress.setOnClickListener(this)
        binding.dropAddress.setOnClickListener(this)
        if (!Places.isInitialized()) {
            val apiKey = getString(R.string.google_api_key)
            Places.initialize(applicationContext, apiKey)
        }


        binding.pickName.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedContact = parent.adapter.getItem(position) as MyLocationResAndEditLocationReq?
                binding.pickName.setText(selectedContact?.Location?.ContactName)
                binding.pickEmail.setText (selectedContact?.Location?.Email)
                binding.pickPhone.setText(selectedContact?.Location?.Phone)
                binding.pickUnit.setText(selectedContact?.Location?.UnitNumber)
                binding.pickInstruction.setText(selectedContact?.Location?.Notes)
                binding.pickAddress.setText(selectedContact?.Location?.Address)

            }

        binding.dropName.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedContact = parent.adapter.getItem(position) as MyLocationResAndEditLocationReq?
                binding.dropName.setText(selectedContact?.Location?.ContactName)
                binding.dropEmail.setText (selectedContact?.Location?.Email)
                binding.dropPhone.setText(selectedContact?.Location?.Phone)
                binding.dropUnit.setText(selectedContact?.Location?.UnitNumber)
                binding.dropInstruction.setText(selectedContact?.Location?.Notes)
                binding.dropAddress.setText(selectedContact?.Location?.Address)
            }
    }

    private fun setDataToContact(it: List<MyLocationResAndEditLocationReq>) {
        val contactList: MutableList<MyLocationResAndEditLocationReq> = mutableListOf()
        for (myLocation in it) {
            contactList.add(myLocation)
            if (myLocation.DefaultPickup == true) {
                try {
                    binding.pickName.setText(myLocation.Location?.ContactName.toString())
                    binding.pickEmail.setText(myLocation.Location?.Email.toString())
                    binding.pickPhone.setText(myLocation.Location?.Phone.toString())
                    binding.pickUnit.setText(myLocation.Location?.UnitNumber.toString())
                    binding.pickCompany.setText(myLocation.Location?.CompanyName.toString())
                    binding.pickAddress.setText(myLocation.Location?.Address.toString())


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (myLocation.DefaultDropoff == true) {
                try {
                    binding.dropName.setText(myLocation.Location?.ContactName.toString())
                    binding.dropEmail.setText(myLocation.Location?.Email.toString())
                    binding.dropPhone.setText(myLocation.Location?.Phone.toString())
                    binding.dropUnit.setText(myLocation.Location?.UnitNumber.toString())
                    binding.dropCompany.setText(myLocation.Location?.CompanyName.toString())
                    binding.dropAddress.setText(myLocation.Location?.Address.toString())

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val pickadapter = AutoCompleteTextViewAdapter(this,  R.layout.autocomplete_text, contactList)
            binding.pickName.threshold = 0
            binding.pickName.setAdapter(pickadapter)
            val dropadapter = AutoCompleteTextViewAdapter(this,  R.layout.autocomplete_text, contactList)
            binding.dropName.threshold = 0
            binding.dropName.setAdapter(dropadapter)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.next_btn -> {
                val isBookingConfirm = validateYourDeliveryField(
                       binding.pickName.text.toString().trim(),
                        binding.pickPhone.text.toString().trim(),
                        binding.pickAddress.text.toString().trim(),
                        binding.pickEmail.text.toString().trim(),
                        binding.dropName.text.toString().trim(),
                        binding.dropPhone.text.toString().trim(),
                        binding.dropAddress.text.toString().trim()
                    )
                if (isBookingConfirm <= 0) {
                    val intent = Intent(this, PricingPaymentActivity::class.java)
                    startActivity(intent)
                } else
                    DialogActivity.alertDialogSingleButton(
                        this,
                        "Awaiting!",
                        bookDeliveryAlertMsgStr
                    )
            }
            R.id.pick_find_me -> {
                getLocationClass?.getCurrentLocation(onAddress = ::getPickAddress)
            }
            R.id.drop_find_me -> {
                getLocationClass?.getCurrentLocation(onAddress = ::getDropAddress)
            }
            R.id.pick_address -> {
                try {
                    val fields =
                        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields
                    )
                        //.setCountry("AU")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this)
                    startActivityForResult(intent, pickAutocompleteRequest)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.drop_address -> {
                try {
                    val fields =
                        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields
                    )
                        //.setCountry("AU")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this)
                    startActivityForResult(intent, dropAutocompleteRequest)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun getPickAddress(lat: Double, long: Double) {

        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        address[0].locality
        address[0].countryName
        binding.pickAddress.setText(address[0].getAddressLine(0))

    }

    private fun getDropAddress(lat: Double, long: Double) {

        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        address[0].locality
        address[0].countryName
        binding.dropAddress.setText(address[0].getAddressLine(0))

    }

    private fun validateYourDeliveryField(pickName: String,
                                          pickPhone: String,
                                          pickAddress: String,
                                          pickEmail :String,
                                          dropName: String,
                                          dropPhone: String,
                                          dropAddress: String):Int {
        var bookDeliveryAlertCount = 0
        bookDeliveryAlertMsgStr = ""


        /******************* Validation for pick up detail ******************/
        if (pickName == ""
            || pickPhone == ""
            || pickAddress == ""
            || !pickPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {
            if (pickName == "") {
                AppUtility.validateEditTextField(binding.pickName,"Please enter contact name")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup name", bookDeliveryAlertCount)
            }
            if (pickPhone == "") {
                AppUtility.validateEditTextField(binding.pickPhone, "Please enter mobile number")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's number", bookDeliveryAlertCount)
            } else if (!pickPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {

                AppUtility.validateEditTextField(binding.pickPhone, "Please enter valid mobile number")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's number", bookDeliveryAlertCount)
            }
            if (pickAddress == "") {
                AppUtility.validateEditTextField(binding.pickAddress, "Please enter address")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's address", bookDeliveryAlertCount)
            }
           /* if (!pickEmail.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex()))
             {
                 AppUtility.validateEditTextField(binding.pickEmail ,"Please enter valid email address")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's email", bookDeliveryAlertCount)
            }*/
        }



        /******************* Validation for drop off detail ******************/
        if (pickName == ""
            || dropPhone == ""
            || dropAddress == ""
            || !dropPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {
            if (dropName == "") {
                AppUtility.validateEditTextField(binding.dropName,"Please enter contact name")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff name", bookDeliveryAlertCount)
            }
            if (dropPhone == "") {
                AppUtility.validateEditTextField(binding.dropPhone, "Please enter mobile number")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff's number", bookDeliveryAlertCount)
            } else if (!dropPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {

                AppUtility.validateEditTextField(binding.pickPhone, "Please enter valid mobile number")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff's number", bookDeliveryAlertCount)
            }
            if (dropAddress == "") {
                AppUtility.validateEditTextField(binding.dropAddress, "Please enter address")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff's address", bookDeliveryAlertCount)
            }
           /* if (!dropEmail.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex()))
            {
                AppUtility.validateEditTextField(binding.pickEmail ,"Please enter valid email address")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's email", bookDeliveryAlertCount)
            }*/
        }




        return bookDeliveryAlertCount
    }


    private fun addTextToAlertDialog(addAlertMsgStr: String, count: Int) {
        if (bookDeliveryAlertMsgStr == "") bookDeliveryAlertMsgStr =
            "$bookDeliveryAlertMsgStr$count) $addAlertMsgStr" else bookDeliveryAlertMsgStr =
            "$bookDeliveryAlertMsgStr\n$count) $addAlertMsgStr"
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickAutocompleteRequest) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.pickAddress.setText(place.address)
                /*call google api for get data using selected address*/
                //viewModel.dataFromGoogle(binding.pickAddress.text.toString())
                Log.i("Place API Success", "  -------------- Place -------------" + place.name)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                binding.pickAddress.setText("")
                Log.i(
                    "Place API Failure",
                    "  -------------- Error -------------" + status.statusMessage
                )
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Place API Failure", "  -------------- User Cancelled -------------")
            }
        } else if (requestCode == dropAutocompleteRequest) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.pickAddress.setText(place.address)
                /*call google api for get data using selected address*/
                // viewModel.dataFromGoogle(binding.dropAddress.text.toString())
                Log.i("Place API Success", "  -------------- Place -------------" + place.name)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                binding.pickAddress.setText("")
                Log.i(
                    "Place API Failure",
                    "  -------------- Error -------------" + status.statusMessage
                )
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Place API Failure", "  -------------- User Cancelled -------------")
            }
        }
    }




}