package com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details

import android.annotation.SuppressLint
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
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository
import com.zoom2u_customer.apiclient.GetAddressFromGoogleAPI
import com.zoom2u_customer.apiclient.GoogleServiceApi
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityDeliveryDatailsBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.InterStateReq
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.SaveDeliveryRequestReq
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.ShipmentsClass
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.quotes_req.UploadQuotesActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.PricingPaymentActivity
import com.zoom2u_customer.ui.application.bottom_navigation.profile.my_location.MyLocationRepository
import com.zoom2u_customer.ui.application.bottom_navigation.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.ui.application.get_location.GetLocationClass
import com.zoom2u_customer.utility.*
import com.zoom2u_customer.utility.utility_custom_class.MySpinnerAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DeliveryDetailsActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    var bookDeliveryAlertMsgStr = ""
    private var intraStateReq: IntraStateReq? = null
    private var interStateReq: InterStateReq? = null

    private var pickAutocompleteRequest = 1019
    private var dropAutocompleteRequest = 1018
    lateinit var binding: ActivityDeliveryDatailsBinding
    private var datePicker: DatePicker? = null
    private var timePicker: TimePicker? = null
    lateinit var viewModel: DeliveryDetailsViewModel
    private var categories: MutableList<String> = mutableListOf()
    private var repositoryGoogleAddress: GoogleAddressRepository? = null
    private var repositoryMyLoc: MyLocationRepository? = null
    private var getLocationClass: GetLocationClass? = null

    private var pickState: String? = null
    private var pickStreetNumber: String? = null
    private var pickStreet: String? = null
    private var pickGpx: String? = null
    private var pickGpy: String? = null
    private var pickSuburb: String? = null
    private var pickPostCode: String? = null
    private var pickCountry: String? = null
    private var pickPremisesType: String? = "House"

    private var dropState: String? = null
    private var dropStreetNumber: String? = null
    private var dropStreet: String? = null
    private var dropGpx: String? = null
    private var dropGpy: String? = null
    private var dropSuburb: String? = null
    private var dropPostCode: String? = null
    private var dropCountry: String? = null
    private var dropPremisesType: String? = "House"
    private var leaveAt: Int? = null
    private var isInterstate: Boolean? = null
    private var isLaptopOrMobile: String? = "laptopOrMobileNo"
    private lateinit var itemDataList: ArrayList<Icon>
    var isQuotesRequest:Boolean?=null
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtility.hideKeyboard(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_datails)

        /**get data from map Item*/
        val intent: Intent = intent
        itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
        isQuotesRequest=intent.getBooleanExtra("isQuotesRequest",false)

        viewModel = ViewModelProvider(this).get(DeliveryDetailsViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        val googleServiceApi: GoogleServiceApi = GetAddressFromGoogleAPI.getGoogleServices()
        repositoryMyLoc = MyLocationRepository(serviceApi, this)
        repositoryGoogleAddress = GoogleAddressRepository(googleServiceApi, this)
        viewModel.repositoryMyLoc = repositoryMyLoc
        viewModel.repositoryGoogleAddress = repositoryGoogleAddress
        viewModel.getMyLocationList()

      /**for request quotes*/
       if(isQuotesRequest as Boolean){
        binding.pickSendSms.visibility=View.GONE
        binding.nextBtn.text="Request Quotes"
        binding.parcelBeforeCl.visibility=View.VISIBLE
       }else{
           binding.pickSendSms.visibility=View.VISIBLE
           binding.nextBtn.text="Next"
           binding.parcelBeforeCl.visibility=View.GONE
       }

      /**drop and pick date*/
        val dateFormat: DateFormat = SimpleDateFormat("EEE dd MMM yyyy")
        val date = Date()
        binding.pickDate.text = dateFormat.format(date)

        /** pick time*/
        val timeFormat: DateFormat = SimpleDateFormat("hh:mm aaa")
        binding.pickTime.text = timeFormat.format(date)

        /**drop time*/
        val c = Calendar.getInstance()
        c.add(Calendar.HOUR, 3)
        val d = c.time
        val dropTime = timeFormat.format(d)
        binding.dropTime.text = dropTime

        datePicker = DatePicker()
        timePicker = TimePicker()

        val timeFrom="09:00 PM"
        val current:String=timeFormat.format(date)

        val time9PM: Date = timeFormat.parse(timeFrom)
        val currentTime: Date = timeFormat.parse(current)

        if(time9PM.before(currentTime)){
            val c = Calendar.getInstance()
            c.add(Calendar.DATE, 1)
            val d = c.time
            val dropTime = dateFormat.format(d)
            binding.dropDate.text = dropTime
        }else
            binding.dropDate.text = dateFormat.format(date)

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
        binding.pickFindMe.setOnClickListener(this)
        binding.dropFindMe.setOnClickListener(this)
        binding.pickAddress.setOnClickListener(this)
        binding.dropAddress.setOnClickListener(this)
        binding.itemWeNotSend.setOnClickListener(this)
        binding.noContactDrop.setOnClickListener(this)
        binding.authorityToLeave.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.pickDateCl.setOnClickListener(this)
        binding.pickTimeCl.setOnClickListener(this)
        binding.dropDateCl.setOnClickListener(this)
        binding.dropTimeCl.setOnClickListener(this)
        binding.dropTime.setOnClickListener(this)
        binding.dropDate.setOnClickListener(this)
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
                pickState = selectedContact?.Location?.State
                showHideWeight(pickState, dropState)
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
                dropState = selectedContact?.Location?.State
                showHideWeight(pickState, dropState)
            }




        viewModel.getMySuccess()?.observe(this) {
            if (it != null) {
                CustomProgressBar.dismissProgressBar()
                if (it.isNotEmpty()) {
                    setDataToContact(it)
                }

            }

        }

        viewModel.googleSuccessUsingAdd()?.observe(this) { isGoogleAdd ->
            if (isGoogleAdd.isNotEmpty()) {
                AppUtility.progressBarDissMiss()
                val getAddress: HashMap<String, Any>? = isGoogleAdd
                if (isGoogleAdd["isTrue"] == "true") {

                    pickGpx =
                        getAddress?.get("latitude").toString()

                    pickGpy =
                        getAddress?.get("longitude").toString()

                    pickState = getAddress?.get("state")?.toString()
                    showHideWeight(pickState, dropState)

                    pickSuburb =
                        getAddress?.get("suburb")?.toString()

                    pickPostCode =
                        getAddress?.get("postcode")?.toString()

                    pickStreet =
                        getAddress?.get("address")?.toString()

                    pickStreetNumber =
                        getAddress?.get("streetNumber")?.toString()

                    pickCountry =
                        getAddress?.get("country")?.toString()


                } else {

                    dropGpx =
                        getAddress?.get("latitude").toString()

                    dropGpy =
                        getAddress?.get("longitude").toString()

                    dropState = getAddress?.get("state")?.toString()
                    showHideWeight(pickState, dropState)

                    dropSuburb =
                        getAddress?.get("suburb")?.toString()

                    dropPostCode =
                        getAddress?.get("postcode")?.toString()

                    dropStreet =
                        getAddress?.get("address")?.toString()

                    dropStreetNumber =
                        getAddress?.get("streetNumber")?.toString()

                    dropCountry =
                        getAddress?.get("country")?.toString()

                }
            }
        }
        viewModel.googleSuccessUsingLatLang()?.observe(this) { isGoogleAdd ->
            if (isGoogleAdd.isNotEmpty()) {
                AppUtility.progressBarDissMiss()
                val getAddress: HashMap<String, Any>? = isGoogleAdd
                if (isGoogleAdd["isTrue"] == "true") {

                    pickGpx =
                        getAddress?.get("latitude").toString()

                    pickGpy =
                        getAddress?.get("longitude").toString()

                    pickState = getAddress?.get("state")?.toString()
                    showHideWeight(pickState, dropState)

                    pickSuburb =
                        getAddress?.get("suburb")?.toString()

                    pickPostCode =
                        getAddress?.get("postcode")?.toString()

                    pickStreet =
                        getAddress?.get("address")?.toString()

                    pickStreetNumber =
                        getAddress?.get("streetNumber")?.toString()

                    pickCountry =
                        getAddress?.get("country")?.toString()

                } else {
                    dropGpx =
                        getAddress?.get("latitude").toString()

                    dropGpy =
                        getAddress?.get("longitude").toString()

                    dropState = getAddress?.get("state")?.toString()
                    showHideWeight(pickState, dropState)

                    dropSuburb =
                        getAddress?.get("suburb")?.toString()

                    dropPostCode =
                        getAddress?.get("postcode")?.toString()

                    dropStreet =
                        getAddress?.get("address")?.toString()

                    dropStreetNumber =
                        getAddress?.get("streetNumber")?.toString()

                    dropCountry =
                        getAddress?.get("country")?.toString()
                }
            }
        }

        binding.pickHouseCom.setOnCheckedChangeListener { _, checkedId ->
            pickPremisesType = if (R.id.pick_house == checkedId)
                "House"
            else "Commercial"

        }

        binding.dropHouseCom.setOnCheckedChangeListener { _, checkedId ->
            dropPremisesType = if (R.id.drop_house == checkedId)
                "House"
            else "Commercial"

        }


        binding.yesNo.setOnCheckedChangeListener { _, checkedId ->
            isLaptopOrMobile = if (R.id.yes == checkedId)
                "laptopOrMobileYes"
            else "laptopOrMobileNo"

        }

        binding.weight.text = countTotalWeight() + "Kg"
    }


    private fun showHideWeight(pickState: String?, dropState: String?) {
        if (pickState != null && dropState != null) {
            if (pickState != dropState) {
                binding.packageType.visibility = View.VISIBLE
                binding.yesNo.visibility = View.VISIBLE
                binding.itemWeNotSend.visibility = View.VISIBLE
                // binding.weightCl.visibility = View.VISIBLE
            } else {
                binding.packageType.visibility = View.GONE
                binding.yesNo.visibility = View.GONE
                binding.itemWeNotSend.visibility = View.GONE
                // binding.weightCl.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToContact(it: List<MyLocationResAndEditLocationReq>) {
        val contactList: MutableList<MyLocationResAndEditLocationReq> = mutableListOf()
        for (myLocation in it) {
            contactList.add(myLocation)
            if (myLocation.DefaultPickup == true) {
                try {
                    binding.pickName.setText(myLocation.Location?.ContactName.toString())
                    binding.pickEmail.setText(myLocation.Location?.Email.toString())
                    binding.pickPhone.setText(myLocation.Location?.Phone.toString())

                    if (TextUtils.isEmpty(myLocation.Location?.UnitNumber.toString()))
                        binding.pickUnit.setText(myLocation.Location?.UnitNumber.toString())
                    if (TextUtils.isEmpty(myLocation.Location?.CompanyName.toString()))
                        binding.pickCompany.setText(myLocation.Location?.CompanyName.toString())
                    binding.pickAddress.setText(myLocation.Location?.Street.toString())
                    pickState = myLocation.Location?.State.toString()
                    pickGpx = myLocation.Location?.GPSX.toString()
                    pickGpy = myLocation.Location?.GPSY.toString()
                    pickSuburb = myLocation.Location?.Suburb.toString()
                    pickPostCode = myLocation.Location?.Postcode.toString()
                    pickStreet = myLocation.Location?.Street.toString()
                    pickStreetNumber = myLocation.Location?.StreetNumber.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (myLocation.DefaultDropoff == true) {
                try {
                    binding.dropName.setText(myLocation.Location?.ContactName.toString())
                    binding.dropEmail.setText(myLocation.Location?.Email.toString())
                    binding.dropPhone.setText(myLocation.Location?.Phone.toString())
                    if (TextUtils.isEmpty(myLocation.Location?.UnitNumber.toString()))
                        binding.dropUnit.setText(myLocation.Location?.UnitNumber.toString())
                    if (TextUtils.isEmpty(myLocation.Location?.CompanyName.toString()))
                        binding.dropCompany.setText(myLocation.Location?.CompanyName.toString())
                    binding.dropAddress.setText(myLocation.Location?.Street.toString())
                    dropState = myLocation.Location?.State.toString()
                    dropGpx = myLocation.Location?.GPSX.toString()
                    dropGpy = myLocation.Location?.GPSY.toString()
                    dropSuburb = myLocation.Location?.Suburb.toString()
                    dropPostCode = myLocation.Location?.Postcode.toString()
                    dropStreet = myLocation.Location?.Street.toString()
                    dropStreetNumber = myLocation.Location?.StreetNumber.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
        val pickAdapter = ContactNameAdapter(this, R.layout.autocomplete_text, contactList)
        binding.pickName.threshold = 0
        binding.pickName.setAdapter(pickAdapter)
        val dropAdapter = ContactNameAdapter(this, R.layout.autocomplete_text, contactList)
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
                    if(isQuotesRequest as Boolean) {
                        val intent = Intent(this, UploadQuotesActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        startActivity(intent)
                    }
                    else {
                        /**for Intra State**/
                        if (pickState == dropState) {
                            isInterstate = false
                            intraStateReq = getIntraState()
                            val intent = Intent(this, PricingPaymentActivity::class.java)
                            intent.putExtra("IntraStateData", intraStateReq)
                            intent.putExtra(
                                "SaveDeliveryRequestReq",
                                createJsonForSaveRequest().toString()
                            )
                            intent.putParcelableArrayListExtra("IconList", itemDataList)
                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        /**for Inter State**/
                        else {
                            isInterstate = true
                            interStateReq = getInterState()
                            val intent = Intent(this, PricingPaymentActivity::class.java)
                            intent.putExtra("InterStateData", interStateReq)
                            intent.putExtra(
                                "SaveDeliveryRequestReq",
                                createJsonForSaveRequest().toString()
                            )
                            intent.putParcelableArrayListExtra("IconList", itemDataList)
                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
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
                        listOf(
                            Place.Field.ADDRESS, Place.Field.LAT_LNG,
                        )
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
                        listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
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
                    onItemClick = ::onPickDateClick
                )
            }
            R.id.pick_date_cl -> {
                datePicker?.datePickerDialog(
                    this,
                    binding.pickDate.text.toString(),
                    onItemClick = ::onPickDateClick
                )
            }
            R.id.pick_time -> {
                timePicker?.timePickerDialog(
                    this,false,
                    binding.pickTime.text.toString(),
                    onItemClick = ::onPickTimeClick
                )
            }
            R.id.pick_time_cl -> {
                timePicker?.timePickerDialog(
                    this,false,
                    binding.pickTime.text.toString(),
                    onItemClick = ::onPickTimeClick
                )
            }
            R.id.drop_date -> {
                datePicker?.datePickerDialog(
                    this,
                    binding.dropDate.text.toString(),
                    onItemClick = ::onDropDateClick
                )
            }
            R.id.drop_date_cl -> {
                datePicker?.datePickerDialog(
                    this,
                    binding.dropDate.text.toString(),
                    onItemClick = ::onDropDateClick
                )
            }
            R.id.drop_time -> {
                timePicker?.timePickerDialog(
                    this,true,
                    binding.dropTime.text.toString(),
                    onItemClick = ::onDropTimeClick
                )
            }
            R.id.drop_time_cl -> {
                timePicker?.timePickerDialog(
                    this,true,
                    binding.dropTime.text.toString(),
                    onItemClick = ::onDropTimeClick
                )
            }
            R.id.item_we_not_send -> {
            /* val pdfUri= Uri.fromFile( File("raw/item_not_send.pdf"))
                 val browserIntent=Intent(Intent.ACTION_VIEW, pdfUri)
                 startActivity(browserIntent)*/
            }
            R.id.authority_to -> {
                binding.spinner.performClick()

            }
            R.id.no_contact_drop -> {
                binding.authorityToLeave.isChecked = true
                binding.authorityTo.visibility = View.VISIBLE
            }
            R.id.authority_to_leave -> {
                if (binding.authorityToLeave.isChecked)
                    binding.authorityTo.visibility = View.VISIBLE
                else
                    binding.authorityTo.visibility = View.GONE
            }
            R.id.back_btn -> {
                finish()
            }

        }
    }

    private fun getShipmentsList(): MutableList<ShipmentsClass> {
        var shipments: ShipmentsClass
        val shipmentsList: MutableList<ShipmentsClass> = ArrayList()
        for ((i, item) in itemDataList.withIndex()) {

            shipments = ShipmentsClass(
                item.Category,
                item.quantity, item.Value, item.length, item.height,
                item.width, item.weight, item.weight.toString()
            )
            shipmentsList.add(i, shipments)
        }
        return shipmentsList
    }

    private fun getPickDateAndTimeInEta(): String {
        return DateTimeUtil.getDateTimeFromDeviceForDeliveryETA(
            binding.pickDate.text.toString() + " " +
                    binding.pickTime.text.toString()
        ).toString()

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAndTimeInEta(): String {
        val date = Date()
        val dateFormat: DateFormat = SimpleDateFormat("EEE dd MMM yyyy")
        val timeFormat: DateFormat = SimpleDateFormat("hh:mm aaa")

        return DateTimeUtil.getDateTimeFromDeviceForDeliveryETA(
            dateFormat.format(date) + " " +
                    timeFormat.format(date)
        ).toString()

    }

    private fun getIntraState(): IntraStateReq? {
        val dropLocation = IntraStateReq.DropLocationClass(dropGpx, dropGpy)
        val pickLocation = IntraStateReq.PickupLocationClass(pickGpx, pickGpy)
        intraStateReq = IntraStateReq(
            getCurrentDateAndTimeInEta(),
            0,
            binding.dropAddress.text.toString(),
            dropLocation,
            dropPostCode,
            dropState,
            dropSuburb,
            false,
            false,
            binding.pickAddress.text.toString(),
            getPickDateAndTimeInEta(),
            pickLocation,
            pickPostCode,
            pickState,
            pickSuburb,
            getShipmentsList()
        )

        return intraStateReq
    }

    private fun getInterState(): InterStateReq? {
        val dropLocation = InterStateReq.DropLocationClass(dropGpx, dropGpy)
        val pickLocation = InterStateReq.PickupLocationClass(pickGpx, pickGpy)
        interStateReq = InterStateReq(
            getCurrentDateAndTimeInEta(),
            binding.dropAddress.text.toString(),
            dropLocation,
            dropPostCode,
            dropState,
            dropStreet,
            dropStreetNumber,
            dropSuburb,
            false,
            false,
            binding.pickAddress.text.toString(),
            getPickDateAndTimeInEta(),
            pickLocation,
            pickPostCode,
            pickState,
            pickStreet,
            pickStreetNumber,
            pickSuburb,
            getShipmentsList(),
            countTotalWeight()
        )

        return interStateReq
    }

    private fun onPickTimeClick(time: String?) {
        if (!TextUtils.isEmpty(time)) {
            if (checkPickFutureTime(time)) {
                binding.pickTime.text = time
            }
        }
    }
    private fun onPickDateClick(s: String?) {
        if (!TextUtils.isEmpty(s))
            binding.pickDate.text = s.toString()
    }

    private fun onDropTimeClick(time: String?) {
        if (!TextUtils.isEmpty(time)) {
            if (checkDropFutureTime(time)) {
                if (check3HourTime(time)) {
                    binding.dropTime.text = time
                }
            }
        }
    }
    private fun onDropDateClick(s: String?) {
        if (!TextUtils.isEmpty(s))
            binding.dropDate.text = s.toString()
    }

    fun add3HourInPickTime(time: String?){
        val serverDateTimeValue = binding.pickDate.text.toString() + " " +
                time
        val millisToAdd:Long = 7_200_000
        val converter = SimpleDateFormat("hh:mm a")
        val convertedDate: Date?
        try {
            convertedDate = converter.parse(serverDateTimeValue)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun checkPickFutureTime(selectedTime:String?): Boolean {
        val serverDateTimeValue = binding.pickDate.text.toString() + " " +
                selectedTime
        val converter = SimpleDateFormat("EEE dd MMM yyyy hh:mm a")
        val convertedDate: Date?
        try {
            convertedDate = converter.parse(serverDateTimeValue)
            if (System.currentTimeMillis() > convertedDate.time) {
                DialogActivity.alertDialogSingleButton(
                    this,
                    "Oops!",
                    "Please enter a pickup date/time in the future."
                )
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    private fun checkDropFutureTime(selectedTime:String?): Boolean {
        val serverDateTimeValue = binding.dropDate.text.toString() + " " +
                selectedTime
        val converter = SimpleDateFormat("EEE dd MMM yyyy hh:mm a")
        val convertedDate: Date?
        try {
            convertedDate = converter.parse(serverDateTimeValue)
            if (System.currentTimeMillis() > convertedDate.time) {
                DialogActivity.alertDialogSingleButton(
                        this,
                        "Oops!",
                        "Please enter a drop off date/time in the future."
                    )
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    private fun check3HourTime(selectedTime:String?): Boolean {
        val serverDateTimeValue = binding.dropDate.text.toString() + " " +
                selectedTime
        val converter = SimpleDateFormat("EEE dd MMM yyyy hh:mm a")
        val convertedDate: Date?
        try {
            convertedDate = converter.parse(serverDateTimeValue)

            val c = Calendar.getInstance()
            c.add(Calendar.HOUR, 3)
            val d = c.time
            val currentTime= converter.format(d)
            val currentTimePlus3Hour: Date = converter.parse(currentTime)
            if (currentTimePlus3Hour.time > convertedDate.time) {
                DialogActivity.alertDialogSingleButton(
                    this,
                    "Oops!",
                    "Please enter a dropoff date/time 3 hours more than the pickup date/time."
                )
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }


    fun countTotalWeight(): String {
        var totalWeight = 0.0
        for (item in itemDataList) {
            totalWeight += item.weight * item.quantity
        }
        return totalWeight.toString()
    }



    fun createJsonForSaveRequest(): JSONObject {

        val pickLocation: SaveDeliveryRequestReq.DeliveryRequestModel.PickupLocationClass =
            SaveDeliveryRequestReq.DeliveryRequestModel.PickupLocationClass(
                binding.pickName.text.toString(),
                binding.pickPhone.text.toString(),
                binding.pickEmail.text.toString(),
                binding.pickAddress.text.toString(),
                binding.pickInstruction.text.toString(),
                pickGpx, pickGpy, binding.pickUnit.text.toString(),
                pickStreetNumber, pickStreet, pickSuburb,
                pickState, pickPostCode, pickPremisesType, true,
                binding.pickCompany.text.toString(), pickCountry
            )


        val dropLocation: SaveDeliveryRequestReq.DeliveryRequestModel.DropLocationClass =
            SaveDeliveryRequestReq.DeliveryRequestModel.DropLocationClass(
                binding.dropName.text.toString(),
                binding.dropPhone.text.toString(),
                binding.dropEmail.text.toString(),
                binding.dropAddress.text.toString(),
                binding.dropInstruction.text.toString(),
                dropGpx, dropGpy, binding.dropUnit.text.toString(),
                dropStreetNumber, dropStreet, dropSuburb,
                dropState, dropPostCode, dropPremisesType, true,
                binding.dropCompany.text.toString(), dropCountry
            )

        val jObjOfQuotesItem = JSONObject()
        val deliveryRequest = JSONObject()
        val forInterstate = JSONObject()
        val authorityToLeaveForm = JSONObject()
        try {
            deliveryRequest.put("IsInterstate", isInterstate)
            deliveryRequest.put("PickUpDateTime", getPickDateAndTimeInEta())
            deliveryRequest.put("Notes", "")
            deliveryRequest.put("ParentId", 0)
            deliveryRequest.put("Source", 9)
            deliveryRequest.put("DropLocation", JSONObject(Gson().toJson(dropLocation).toString()))
            deliveryRequest.put(
                "PickupLocation",
                JSONObject(Gson().toJson(pickLocation).toString())
            )
            deliveryRequest.put("Weight", countTotalWeight())
            deliveryRequest.put("sendSmsToPickupPerson", binding.pickSendSms.isChecked)
            deliveryRequest.put("IsNoContactPickup", binding.isNoContactPickup.isChecked)
            deliveryRequest.put("IsNoContactDrop", binding.noContactDrop.isChecked)
            deliveryRequest.put("RequestedDropDateTimeWindowEnd", getCurrentDateAndTimeInEta())
            deliveryRequest.put("RequestedDropDateTimeWindowStart", getCurrentDateAndTimeInEta())
            deliveryRequest.put("RequestedPickupDateTimeWindowEnd", getCurrentDateAndTimeInEta())
            deliveryRequest.put("RequestedPickupDateTimeWindowStart", getCurrentDateAndTimeInEta())
            deliveryRequest.put("isLaptopOrMobile", isLaptopOrMobile)




            if (binding.authorityTo.text.toString() == "Other") {
                authorityToLeaveForm.put("NoContact", binding.noContactDrop.isChecked)
                authorityToLeaveForm.put("LeaveAt", leaveAt.toString())
                authorityToLeaveForm.put("Instructions", binding.other.text.toString())
                jObjOfQuotesItem.put("_authorityToLeaveForm", authorityToLeaveForm)

            }

            jObjOfQuotesItem.put("_deliveryRequestModel", deliveryRequest)
            jObjOfQuotesItem.put("_interstateModel", forInterstate)
            jObjOfQuotesItem.put(
                "_shipmentModel",
                JSONArray(Gson().toJson(getShipmentsList()).toString())
            )


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jObjOfQuotesItem
    }


    private fun getPickAddress(lat: Double, lang: Double) {

        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, lang, 1)
        binding.pickAddress.setText(address[0].getAddressLine(0))
        viewModel.addFromGoogleLatLang(lat.toString(), lang.toString(), true)
    }

    private fun getDropAddress(lat: Double, lang: Double) {

        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, lang, 1)
        binding.dropAddress.setText(address[0].getAddressLine(0))
        viewModel.addFromGoogleLatLang(lat.toString(), lang.toString(), false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickAutocompleteRequest) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    binding.pickAddress.setText(place.address)
                    viewModel.addFromGoogleAdd(place.address, true)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    binding.pickAddress.setText("")
                }
                RESULT_CANCELED -> {
                    Log.i("Place API Failure", "  -------------- User Cancelled -------------")
                }
            }
        } else if (requestCode == dropAutocompleteRequest) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    binding.dropAddress.setText(place.address)
                    viewModel.addFromGoogleAdd(place.address, false)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    binding.pickAddress.setText("")
                }
                RESULT_CANCELED -> {
                    Log.i("Place API Failure", "  -------------- User Cancelled -------------")
                }
            }
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val selectedText = categories[position]
        binding.authorityTo.setText(selectedText)
        leaveAt = position
        if (selectedText == "Other") {
            binding.other.visibility = View.VISIBLE
        } else {
            binding.other.visibility = View.GONE

        }
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
                AppUtility.validateEditTextField(binding.pickName, "Please enter contact name.")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup name.", bookDeliveryAlertCount)
            }
            if (pickPhone == "") {
                AppUtility.validateEditTextField(binding.pickPhone, "Please enter mobile number.")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's number.", bookDeliveryAlertCount)
            } else if (!pickPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {

                AppUtility.validateEditTextField(
                    binding.pickPhone,
                    "Please enter valid mobile number."
                )
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's number.", bookDeliveryAlertCount)
            }
            if (pickAddress == "") {
                AppUtility.validateEditTextField(binding.pickAddress, "Please enter address.")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at pickup's address.", bookDeliveryAlertCount)
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
                AppUtility.validateEditTextField(binding.dropName, "Please enter contact name.")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff name.", bookDeliveryAlertCount)
            }
            if (dropPhone == "") {
                AppUtility.validateEditTextField(binding.dropPhone, "Please enter mobile number.")
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff's number.", bookDeliveryAlertCount)
            } else if (!dropPhone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {

                AppUtility.validateEditTextField(
                    binding.pickPhone,
                    "Please enter valid mobile number."
                )
                bookDeliveryAlertCount++
                addTextToAlertDialog("Contact at dropoff's number.", bookDeliveryAlertCount)
            }
            if (dropAddress == "") {
                AppUtility.validateEditTextField(binding.dropAddress, "Please enter address.")
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


        /**for no contact pickup*/
        if (binding.isNoContactPickup.isChecked) {
            if (binding.pickInstruction.text.toString() == "") {
                AppUtility.validateEditTextField(
                    binding.pickInstruction,
                    "Notes are required for the pickup location."
                )
                bookDeliveryAlertCount++
                addTextToAlertDialog(
                    "Notes are required for the pickup location when requesting a no contact delivery.",
                    bookDeliveryAlertCount
                )
            }
        }

        /**for drop authority to leave*/
        if (binding.authorityToLeave.isChecked) {
            if (binding.authorityTo.text.toString() == "Other") {
                if (binding.other.text.toString() == "") {
                    AppUtility.validateEditTextField(
                        binding.other,
                        "Please enter a safe place on where to leave your parcel."
                    )
                    bookDeliveryAlertCount++
                    addTextToAlertDialog(
                        "Please enter a safe place on where to leave your parcel.",
                        bookDeliveryAlertCount
                    )
                }
            }
        }
        return bookDeliveryAlertCount
    }


    private fun addTextToAlertDialog(addAlertMsgStr: String, count: Int) {
        if (bookDeliveryAlertMsgStr == "") bookDeliveryAlertMsgStr =
            "$bookDeliveryAlertMsgStr$count) $addAlertMsgStr" else bookDeliveryAlertMsgStr =
            "$bookDeliveryAlertMsgStr\n$count) $addAlertMsgStr"

    }


}


