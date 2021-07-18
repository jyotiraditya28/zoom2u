package com.zoom2u_customer.ui.application.base_package.home.delivery_details

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
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.InterStateReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.SaveDeliveryRequestReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.ShipmentsClass
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.PricingPaymentActivity
import com.zoom2u_customer.ui.application.base_package.profile.my_location.MyLocationRepository
import com.zoom2u_customer.ui.application.base_package.profile.my_location.model.MyLocationResAndEditLocationReq
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
    private var intraStateReq: IntraStateReq?=null
    private var interStateReq: InterStateReq?=null

    private var pickAutocompleteRequest = 1019
    private var dropAutocompleteRequest = 1018
    lateinit var binding: ActivityDeliveryDatailsBinding
    private var datePicker: DatePicker? = null
    private var timePicker: TimePicker? = null
    lateinit var viewModel: DeliveryDetailsViewModel
    private var categories: MutableList<String> = mutableListOf()
    private var repository: DeliveryDetailsRepository? = null
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
    private var pickPremisesType:String? =null

    private var dropState: String? = null
    private var dropStreetNumber: String? = null
    private var dropStreet: String? = null
    private var dropGpx: String? = null
    private var dropGpy: String? = null
    private var dropSuburb: String? = null
    private var dropPostCode: String? = null
    private var dropCountry: String? = null
    private var dropPremisesType:String? =null

    private var leaveAt:Int?=null
    private var isInterstate:Boolean?=null
    private var otherInstructions:String?=null

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
        repositoryGoogleAddress = GoogleAddressRepository(googleServiceApi, this)
        viewModel.repositoryMyLoc = repositoryMyLoc
        viewModel.repositoryGoogelAddress = repositoryGoogleAddress
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
                dropState= selectedContact?.Location?.State
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
                     if(isGoogleAdd["isTrue"] =="true"){


                           /* myLocationResponse?.Location?.GPSX =
                                getAddress?.get("latitude") as Double

                            myLocationResponse?.Location?.GPSY =
                                getAddress["longitude"] as Double
*/
                            pickState = getAddress?.get("state")?.toString()
                             showHideWeight(pickState,dropState)
                         /*   myLocationResponse?.Location?.Suburb =
                                getAddress["suburb"].toString()


                            myLocationResponse?.Location?.Postcode =
                                getAddress["postcode"].toString()

                            myLocationResponse?.Location?.Street =
                                getAddress["suburb"].toString()


                            myLocationResponse?.Location?.StreetNumber =
                                getAddress["streetNumber"].toString()
*/

                        } else {

                          /*  lattitude = getAddress?.get("latitude") as Double

                            longitude = getAddress["longitude"] as Double
*/
                            dropState = getAddress?.get("state")?.toString()
                             showHideWeight(pickState,dropState)
                         /*   suburb =  getAddress["suburb"].toString()

                            country =  getAddress["country"].toString()

                            postCode =   getAddress["postcode"].toString()

                            street =  getAddress["suburb"].toString()*/


                    }  }
        }

    }


    private fun showHideWeight(pickState: String?, dropState: String?) {
        if (pickState != null && dropState != null) {
            if (pickState != dropState) {
                binding.packageType.visibility = View.VISIBLE
                binding.yesNo.visibility = View.VISIBLE
                binding.weightLl.visibility = View.VISIBLE
            } else {
                binding.packageType.visibility = View.GONE
                binding.yesNo.visibility = View.GONE
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
                    if(TextUtils.isEmpty(myLocation.Location?.UnitNumber.toString()))
                    binding.pickUnit.setText(myLocation.Location?.UnitNumber.toString())
                    if(TextUtils.isEmpty(myLocation.Location?.CompanyName.toString()))
                    binding.pickCompany.setText(myLocation.Location?.CompanyName.toString())
                    binding.pickAddress.setText(myLocation.Location?.Address.toString())
                    pickState = myLocation.Location?.State.toString()
                    pickGpx = myLocation.Location?.GPSX.toString()
                    pickGpy = myLocation.Location?.GPSY.toString()
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
                    if(TextUtils.isEmpty(myLocation.Location?.UnitNumber.toString()))
                    binding.dropUnit.setText(myLocation.Location?.UnitNumber.toString())
                    if(TextUtils.isEmpty(myLocation.Location?.CompanyName.toString()))
                    binding.dropCompany.setText(myLocation.Location?.CompanyName.toString())
                    binding.dropAddress.setText(myLocation.Location?.Address.toString())
                    dropState = myLocation.Location?.State.toString()
                    dropSuburb = myLocation.Location?.Suburb.toString()

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
                  /**for Intra State**/
                    if (pickState == dropState) {
                        isInterstate=false
                        intraStateReq = getIntraState()
                        val intent = Intent(this, PricingPaymentActivity::class.java)
                        intent.putExtra("IntraStateData",intraStateReq)
                        intent.putExtra("SaveDeliveryRequestReq",createJsonForSaveRequest().toString())
                        intent.putParcelableArrayListExtra("IconList", itemDataList)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    /**for Inter State**/
                    else{
                        isInterstate = true
                        interStateReq = getInterState()
                        val intent = Intent(this, PricingPaymentActivity::class.java)
                        intent.putExtra("InterStateData",interStateReq)
                        intent.putExtra("SaveDeliveryRequestReq",createJsonForSaveRequest().toString())
                        intent.putParcelableArrayListExtra("IconList", itemDataList)
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

    private fun getShipmentsList():MutableList<ShipmentsClass>{
        var shipments : ShipmentsClass
        val shipmentsList: MutableList<ShipmentsClass> = ArrayList()
        for((i, item) in itemDataList.withIndex()) {

            shipments = ShipmentsClass(
                item.Category,
                item.quantity, item.Value, item.length, item.height,
                item.width, item.weight, item.weight.toString()
            )
            shipmentsList.add(i,shipments)
        }
     return shipmentsList
    }

    private fun getPickDateAndTimeInEta() :String{
      return DateTimeUtil.getDateTimeFromDeviceForDeliveryETA(
          binding.pickDate.text.toString()+" "+
                  binding.pickTime.text.toString()
      ).toString()

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAndTimeInEta() :String{
        val date = Date()
        val dateFormat: DateFormat = SimpleDateFormat("EEE dd MMM yyyy")
        val timeFormat: DateFormat = SimpleDateFormat("hh:mm aaa")

        return DateTimeUtil.getDateTimeFromDeviceForDeliveryETA(
            dateFormat.format(date)+" "+
                    timeFormat.format(date)
        ).toString()

    }

    private fun getIntraState(): IntraStateReq? {
        val dropLocation=IntraStateReq.DropLocationClass("","")
        val pickLocation=IntraStateReq.PickupLocationClass("","")
        intraStateReq=IntraStateReq(getCurrentDateAndTimeInEta(),
        0,binding.dropAddress.text.toString(),
            dropLocation,"",dropState,
            dropSuburb,false,false,
            binding.pickAddress.text.toString(),getPickDateAndTimeInEta(),pickLocation,""
        ,pickState,pickSuburb,getShipmentsList()
        )

        return intraStateReq
    }

    private fun getInterState(): InterStateReq? {
        val dropLocation=InterStateReq.DropLocationClass("","")
        val pickLocation=InterStateReq.PickupLocationClass("","")
        interStateReq=InterStateReq(getCurrentDateAndTimeInEta(),
            binding.dropAddress.text.toString(),
            dropLocation,"",dropState,"","",
            dropSuburb,false,false,
            binding.pickAddress.text.toString(),getPickDateAndTimeInEta(),pickLocation,""
            ,pickState,"","",pickSuburb,getShipmentsList(),binding.totalWeight.text.toString().trim()
        )

        return interStateReq
    }

    private fun deliveryDetailsModel(): SaveDeliveryRequestReq {


        val authorityToLeaveForm = SaveDeliveryRequestReq.AuthorityToLeaveForm(
            leaveAt.toString(), otherInstructions, "",
            "", true
        )
        val itemList:MutableList<SaveDeliveryRequestReq.DeliveryRequestModel.Item> = ArrayList()

        val item =SaveDeliveryRequestReq.DeliveryRequestModel.Item(
            "","","","","",
            "","",""
        )
        itemList.add(item)


        val pickLocation: SaveDeliveryRequestReq.DeliveryRequestModel.PickupLocationClass =
            SaveDeliveryRequestReq.DeliveryRequestModel.PickupLocationClass(
                binding.pickName.text.toString(),
                binding.pickPhone.text.toString(),
                binding.pickEmail.text.toString(),
                binding.pickAddress.text.toString(),
                binding.pickInstruction.text.toString(),
                "-33.8709706", "-33.8709706", binding.pickUnit.text.toString(),
                "", "","",
                pickState, "", "House", true,
                binding.pickCompany.text.toString(), ""
            )


        val dropLocation: SaveDeliveryRequestReq.DeliveryRequestModel.DropLocationClass =
            SaveDeliveryRequestReq.DeliveryRequestModel.DropLocationClass(
                binding.dropName.text.toString(),
                binding.dropPhone.text.toString(),
                binding.dropEmail.text.toString(),
                binding.dropAddress.text.toString(),
                binding.dropInstruction.text.toString(),
                dropGpx, dropGpy, binding.dropUnit.text.toString(),
                "", "", "",
                dropState, "","House", true,
                binding.dropCompany.text.toString(), ""
            )


        val deliveryRequestReqModel = SaveDeliveryRequestReq.DeliveryRequestModel(
            itemList, "", "", "",
            "FullLoad", "", "",
            "y", "y",
            "None", "None",
            "", "",
            "Active", "9", 10,
            "", pickLocation, dropLocation, "Bike",
            "2021-07-16T04:44:13.932Z", "2021-07-16T04:44:13.932Z",
            "2021-07-16T04:44:13.932Z",
            "2021-07-16T04:44:13.932Z",
            "2021-07-16T04:44:13.932Z",
            "2021-07-16T04:44:13.932Z",
            "", 0, "", "",
            isInterstate, 0, false,
            false, binding.pickSendSms.isChecked,
            "", "",
            binding.dropChkTerms.isChecked,
            binding.dropChkTerms1.isChecked,
            leaveAt.toString(), "",
            false, "",
            "", "0.5",
            binding.isNoContactPickup.isChecked, false,
            false, false,
            false, false, true,
            "", "", "laptopOrMobileNo", false,
            "", "Standard", 1,
            "400ccf7b-e0ac-0aab-134d-3ffc7fac5142"
        )
        //val interstateModel= SaveDeliveryRequestReq.InterstateModel(null,"")
        return SaveDeliveryRequestReq(
            authorityToLeaveForm,
            deliveryRequestReqModel, null, getShipmentsList()
        )
    }

    private fun onTimeClick(hr: String?, min: String?,am_pm: String?) {
        binding.pickTime.text = "$hr:$min $am_pm"
    }

    fun onDateClick(s: String?) {
        if(!TextUtils.isEmpty(s))
        binding.pickDate.text = s.toString()
    }


    fun createJsonForSaveRequest() :JSONObject {

        val pickLocation: SaveDeliveryRequestReq.DeliveryRequestModel.PickupLocationClass =
                SaveDeliveryRequestReq.DeliveryRequestModel.PickupLocationClass(
                    binding.pickName.text.toString(),
                    binding.pickPhone.text.toString(),
                    binding.pickEmail.text.toString(),
                    binding.pickAddress.text.toString(),
                    binding.pickInstruction.text.toString(),
                    "-33.8709706", "-33.8709706", binding.pickUnit.text.toString(),
                    "", "","",
                    pickState, "", "House", true,
                    binding.pickCompany.text.toString(), ""
                )


            val dropLocation: SaveDeliveryRequestReq.DeliveryRequestModel.DropLocationClass =
                SaveDeliveryRequestReq.DeliveryRequestModel.DropLocationClass(
                    binding.dropName.text.toString(),
                    binding.dropPhone.text.toString(),
                    binding.dropEmail.text.toString(),
                    binding.dropAddress.text.toString(),
                    binding.dropInstruction.text.toString(),
                    "-33.8646627", "151.2043709", binding.dropUnit.text.toString(),
                    "", "", "",
                    dropState, "","House", true,
                    binding.dropCompany.text.toString(), ""
                )

            val jObjOfQuotesItem = JSONObject()
            val jObjOfQuotesItemForDeliveryRequest = JSONObject()
            val jObjOfQuotesItemForInterstate = JSONObject()
            try {


                jObjOfQuotesItemForDeliveryRequest.put(
                    "IsInterstate",
                    false
                )
                jObjOfQuotesItemForDeliveryRequest.put("PickUpDateTime",getPickDateAndTimeInEta())
                jObjOfQuotesItemForDeliveryRequest.put(
                    "Notes",
                   ""
                )
                jObjOfQuotesItemForDeliveryRequest.put("ParentId", 0)
                jObjOfQuotesItemForDeliveryRequest.put("Source", 9)
                jObjOfQuotesItemForDeliveryRequest.put("DropLocation",
                    JSONObject( Gson().toJson(dropLocation).toString())
                )
                jObjOfQuotesItemForDeliveryRequest.put( "PickupLocation", JSONObject( Gson().toJson(pickLocation).toString()))
                /*if (pickOrDropView_Handler.isInterState) jObjOfQuotesItemForDeliveryRequest.put(
                    "Weight",
                    pickOrDropView_Handler.selectWeightYourdelivery.getText().toString().toInt()
                )*/
                jObjOfQuotesItem.put("_deliveryRequestModel", jObjOfQuotesItemForDeliveryRequest)
                jObjOfQuotesItem.put("_interstateModel", jObjOfQuotesItemForInterstate)
                jObjOfQuotesItem.put("_shipmentModel", JSONArray( Gson().toJson(getShipmentsList()).toString()))

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        return jObjOfQuotesItem
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
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    binding.pickAddress.setText(place.address)
                    viewModel.dataFromGoogle(place.address,true)

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
                    viewModel.dataFromGoogle(place.address,false)
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
        leaveAt=position
        if (selectedText == "Other") {
            binding.other.visibility = View.VISIBLE
            otherInstructions = binding.other.text.toString()
        }else {
            binding.other.visibility = View.GONE
            otherInstructions = ""
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


