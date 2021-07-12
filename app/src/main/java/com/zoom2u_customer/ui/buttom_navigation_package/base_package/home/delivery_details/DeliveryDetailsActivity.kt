package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.delivery_details

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
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
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.home_fragment.Icon
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.pricing_payment.PricingPaymentActivity
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.MyLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.edit_add_location.EditAddLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.ui.buttom_navigation_package.get_location.GetLocationClass
import com.zoom2u_customer.utility.*
import com.zoom2u_customer.utility.utility_custom_class.MySpinnerAdapter
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DeliveryDetailsActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    var bookDeliveryAlertMsgStr = ""
    var intraStateModel: IntraStateReq?=null


    private var pickAutocompleteRequest = 1019
    private var dropAutocompleteRequest = 1018
    lateinit var binding: ActivityDeliveryDatailsBinding
    private var datePicker: DatePicker? = null
    private var timePicker: TimePicker? = null
    lateinit var viewModel: DeliveryDetailsViewModel
    private var categories: MutableList<String> = mutableListOf()
    private var repository: DeliveryDetailsRepository? = null
    private var repositoryEditAdd: EditAddLocationRepository? = null
    private var repositoryMyLoc: MyLocationRepository? = null
    private var getLocationClass: GetLocationClass? = null
    private var pickState: String? = null
    private var pickLat: String? = null
    private var pickLang: String? = null
    private var pickSuburb: String? = null

    private var dropState: String? = null
    private var dropLat: String? = null
    private var dropLang: String? = null
    private var dropSuburb: String? = null



    private lateinit var itemDataList: ArrayList<Icon>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtility.hideKeyboard(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_datails)

    /**get data from map Item*/
        val intent: Intent = intent
        itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>


        viewModel = ViewModelProvider(this).get(DeliveryDetailsViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        val googleServiceApi: GoogleServiceApi = GetAddressFromGoogleAPI.getGoogleServices()
        repositoryMyLoc = MyLocationRepository(serviceApi, this)
        repositoryEditAdd = EditAddLocationRepository(serviceApi, googleServiceApi, this)
        viewModel.repositoryMyLoc = repositoryMyLoc
        viewModel.repositoryEditAdd = repositoryEditAdd
        viewModel.getMyLocationList()


        val dateFormat: DateFormat = SimpleDateFormat("EEE dd MMM yyyy")
        val date = Date()
        binding.pickDate.text = dateFormat.format(date)

        val dateFormat1: DateFormat = SimpleDateFormat("hh:mm aaa")
        binding.pickTime.text = dateFormat1.format(date)
        datePicker = DatePicker()
        timePicker = TimePicker()


        categories.add("Front door")
        categories.add("Back door")
        categories.add("Side door")
        categories.add("Other")
        binding.spinner.adapter = MySpinnerAdapter(this, categories)

        getLocationClass = GetLocationClass(this)
        binding.spinner.onItemSelectedListener = this
        binding.authorityTo.setOnClickListener(this)
        binding.nextBtn.setOnClickListener(this)
        binding.pickTime.setOnClickListener(this)
        binding.pickDate.setOnClickListener(this)
        binding.pickTime.setOnClickListener(this)
        binding.pickFindMe.setOnClickListener(this)
        binding.dropFindMe.setOnClickListener(this)
        binding.pickAddress.setOnClickListener(this)
        binding.dropAddress.setOnClickListener(this)
        binding.itemWeNotSend.setOnClickListener(this)
        binding.dropChkTerms1.setOnClickListener(this)
        binding.dropChkTerms.setOnClickListener(this)

        if (!Places.isInitialized()) {
            val apiKey = getString(R.string.google_api_key)
            Places.initialize(applicationContext, apiKey)
        }


        binding.pickName.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedContact =
                    parent.adapter.getItem(position) as MyLocationResAndEditLocationReq?
                binding.pickName.setText(selectedContact?.Location?.ContactName)
                binding.pickEmail.setText(selectedContact?.Location?.Email)
                binding.pickPhone.setText(selectedContact?.Location?.Phone)
                binding.pickUnit.setText(selectedContact?.Location?.UnitNumber)
                binding.pickInstruction.setText(selectedContact?.Location?.Notes)
                binding.pickAddress.setText(selectedContact?.Location?.Address)

            }

        binding.dropName.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedContact =
                    parent.adapter.getItem(position) as MyLocationResAndEditLocationReq?
                binding.dropName.setText(selectedContact?.Location?.ContactName)
                binding.dropEmail.setText(selectedContact?.Location?.Email)
                binding.dropPhone.setText(selectedContact?.Location?.Phone)
                binding.dropUnit.setText(selectedContact?.Location?.UnitNumber)
                binding.dropInstruction.setText(selectedContact?.Location?.Notes)
                binding.dropAddress.setText(selectedContact?.Location?.Address)
            }






        viewModel.getMySuccess()?.observe(this) {
            if (it != null) {
                CustomProgressBar.dismissProgressBar()
                if (it.isNotEmpty()) {
                    setDataToContact(it)
                }

            }

        }

        viewModel.getDataFromGoogleSuccess()?.observe(this) { its ->
            if (!TextUtils.isEmpty(its)) {
                AppUtility.progressBarDissMiss()
                if (its != "") {
                    viewModel.getIsForPickup()?.observe(this) {
                        if (it != null) {
                            val jsonObj = JSONObject(its.toString())
                            val resultsJsonArray = jsonObj.getJSONArray("results")

                            if (it) {
                                pickState = resultsJsonArray.getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(3)
                                    .getString("short_name")
                                showHideWeight(pickState, dropState)
                                // myLocationResponse?.Location?.Country = address[0].countryName
                                // myLocationResponse?.Location?.State = address[0].locality
                                // myLocationResponse?.Location?.GPSX = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                                //myLocationResponse?.Location?.GPSY = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                                //  myLocationResponse?.Location?.Suburb =resultsJsonArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(0).getString("short_name")
                                // /*check again*/myLocationResponse?.Location?.Street =


                            } else {
                                dropState = resultsJsonArray.getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(3)
                                    .getString("short_name")
                                showHideWeight(pickState, dropState)
                                //  addLocationReq?.Location?.State = address[0].locality
                                //   addLocationReq?.Location?.Gpsx = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                                //   addLocationReq?.Location?.Gpsy = resultsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                                //   addLocationReq?.Location?.Suburb = resultsJsonArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(0).getString("short_name")
                                //   /*check again*/ addLocationReq?.Location?.Street = address[0].adminArea
                            }
                        }
                    }
                }
            }
        }


    }


    private fun showHideWeight(pickState: String?, dropState: String?) {
        if (pickState != null && dropState != null) {
            if (pickState != dropState) {
                binding.weightLl.visibility = View.VISIBLE
            } else {
                binding.weightLl.visibility = View.GONE
            }
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
                    pickState = myLocation.Location?.State.toString()
                    pickLat = myLocation.Location?.GPSX.toString()
                    pickLang = myLocation.Location?.GPSY.toString()
                    pickSuburb = myLocation.Location?.Suburb.toString()
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
                    dropState = myLocation.Location?.State.toString()
                    dropSuburb = myLocation.Location?.Suburb.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
        val pickAdapter = AutoCompleteTextViewAdapter(this, R.layout.autocomplete_text, contactList)
        binding.pickName.threshold = 0
        binding.pickName.setAdapter(pickAdapter)
        val dropAdapter = AutoCompleteTextViewAdapter(this, R.layout.autocomplete_text, contactList)
        binding.dropName.threshold = 0
        binding.dropName.setAdapter(dropAdapter)
        showHideWeight(pickState, dropState)
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
                  /**for Intra State**/
                    if (pickState == dropState) {
                        intraStateModel = getIntraState()

                        val intent = Intent(this, PricingPaymentActivity::class.java)
                        intent.putExtra("IntraStateData",intraStateModel)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    else{
                        val intent = Intent(this, PricingPaymentActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }

                } else
                    DialogActivity.alertDialogSingleButton(this, "Awaiting!", bookDeliveryAlertMsgStr)
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
                        .setCountry("AU")
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
                        .setCountry("AU")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this)
                    startActivityForResult(intent, dropAutocompleteRequest)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.pick_date -> {
                datePicker?.datePickerDialog(
                    this,
                    binding.pickDate.text.toString(),
                    onItemClick = ::onDateClick
                )
            }
            R.id.pick_time -> {
                timePicker?.timePickerDialog(
                    this,
                    binding.pickDate.text.toString(),
                    onItemClick = ::onTimeClick
                )
            }
            R.id.item_we_not_send -> {
                /*  val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse())
                  startActivity(browserIntent)*/
            }
            R.id.authority_to -> {
                binding.spinner.performClick()
            }
            R.id.drop_chk_terms -> {
                binding.dropChkTerms1.isChecked = true
                binding.authorityTo.visibility = View.VISIBLE
            }
            R.id.drop_chk_terms1 -> {
               if(binding.dropChkTerms1.isChecked)
                binding.authorityTo.visibility = View.VISIBLE
              else
                   binding.authorityTo.visibility = View.GONE
            }

        }
    }


    private fun getIntraState(): IntraStateReq? {
       var shipments :IntraStateReq.ShipmentsClass
       val shipmentsList: MutableList<IntraStateReq.ShipmentsClass> = ArrayList()
        for((i, item) in itemDataList.withIndex()) {

            shipments = IntraStateReq.ShipmentsClass(
                item.Category,
                item.quantity, item.Value, item.length, item.height,
                item.width, item.weight, item.weight.toString()
            )

             shipmentsList.add(i,shipments)
        }


        val dropLocation=IntraStateReq.DropLocationClass("","")

        val pickLocation=IntraStateReq.PickupLocationClass("","")


        intraStateModel=IntraStateReq("2021-07-12T17:43:39+05:30",
        0,binding.dropAddress.text.toString(),
            dropLocation,"",dropState,
            dropSuburb,false,false,
            binding.pickAddress.text.toString(),"2021-07-12T17:42:13+05:30",pickLocation,""
        ,pickState,pickSuburb,shipmentsList
        )

        return intraStateModel
    }

    private fun onTimeClick(hr: String?, min: String?, am_pm: String?) {
        binding.pickTime.text = "$hr:$min $am_pm"
    }

    fun onDateClick(s: String?) {
        binding.pickDate.text = s.toString()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickAutocompleteRequest) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.pickAddress.setText(place.address)
                viewModel.dataFromGoogle(binding.pickAddress.text.toString(), true)

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                binding.pickAddress.setText("")
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Place API Failure", "  -------------- User Cancelled -------------")
            }
        } else if (requestCode == dropAutocompleteRequest) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.pickAddress.setText(place.address)
                viewModel.dataFromGoogle(binding.dropAddress.text.toString(), false)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                binding.pickAddress.setText("")
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Place API Failure", "  -------------- User Cancelled -------------")
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val selectedText = categories[position]
        binding.authorityTo.setText(selectedText)
        if (selectedText == "Other")
            binding.other.visibility = View.VISIBLE
        else
            binding.other.visibility = View.GONE
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }




    private fun validateYourDeliveryField(
        pickName: String,
        pickPhone: String,
        pickAddress: String,
        pickEmail: String,
        dropName: String,
        dropPhone: String,
        dropAddress: String
    ): Int {
        var bookDeliveryAlertCount = 0
        bookDeliveryAlertMsgStr = ""


        /******************* Validation for pick up detail ******************/
        if (pickName == ""
            || pickPhone == ""
            || pickAddress == ""
            || !pickPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())
        ) {
            if (pickName == "") {
                AppUtility.validateEditTextField(binding.pickName, "Please enter contact name")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup name", bookDeliveryAlertCount)
            }
            if (pickPhone == "") {
                AppUtility.validateEditTextField(binding.pickPhone, "Please enter mobile number")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's number", bookDeliveryAlertCount)
            } else if (!pickPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {

                AppUtility.validateEditTextField(
                    binding.pickPhone,
                    "Please enter valid mobile number"
                )
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
            || !dropPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())
        ) {
            if (dropName == "") {
                AppUtility.validateEditTextField(binding.dropName, "Please enter contact name")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff name", bookDeliveryAlertCount)
            }
            if (dropPhone == "") {
                AppUtility.validateEditTextField(binding.dropPhone, "Please enter mobile number")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff's number", bookDeliveryAlertCount)
            } else if (!dropPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {

                AppUtility.validateEditTextField(
                    binding.pickPhone,
                    "Please enter valid mobile number"
                )
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


    private fun addTextToAlertDialog(addAlertMsgStr: String, count: Int){
        if (bookDeliveryAlertMsgStr == "") bookDeliveryAlertMsgStr =
            "$bookDeliveryAlertMsgStr$count) $addAlertMsgStr" else bookDeliveryAlertMsgStr =
            "$bookDeliveryAlertMsgStr\n$count) $addAlertMsgStr"

    }

}


