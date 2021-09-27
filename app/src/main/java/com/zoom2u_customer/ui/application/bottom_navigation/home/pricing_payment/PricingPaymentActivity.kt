package com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityPricingPaymentBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingConfirmationActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.InterStateReq
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.model.InterStateRes
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.model.IntraStateRes
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.model.QuoteOptionClass
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DateTimeUtil
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import androidx.appcompat.app.AppCompatActivity.LAYOUT_INFLATER_SERVICE as LAYOUT_INFLATER_SERVICE1


class PricingPaymentActivity : AppCompatActivity(), View.OnClickListener {
    var mainObjForMakeABooking: JSONObject? = null
    private var priceSelected: Boolean? = false
    lateinit var binding: ActivityPricingPaymentBinding
    private var intraStateReq: IntraStateReq? = null
    private var interStateReq: InterStateReq? = null
    lateinit var viewModel: PricingPaymentViewModel
    private var repository: PricingPaymentRepository? = null
    var adapter: PricePaymentAdapter? = null
    private lateinit var itemDataList: ArrayList<Icon>
    private var selectedQuoteOptionClass: QuoteOptionClass? = null
    private var isGenerateQuotesBtn:Boolean?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pricing_payment)
        AppUtility.hideKeyboardActivityLunched(this)
        AppUtility.hideKeyBoardClickOutside(binding.parentCl,this)

        viewModel = ViewModelProvider(this).get(PricingPaymentViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = PricingPaymentRepository(serviceApi, this)
        viewModel.repository = repository

        callApiForInterOrIntraState()

        setAdapterView(binding)

        /** get price using intra state*/
        viewModel.intraStateSuccess()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                startTimer()
                val intraStateRes: IntraStateRes = Gson().fromJson(it, IntraStateRes::class.java)

                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .put("Vehicle", intraStateRes.Vehicle)

                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                    "Distance",
                    intraStateRes.Distance
                )


                val quoteOptionList = intraStateRes.QuoteOptions
                if (quoteOptionList != null) {
                    adapter?.updateRecords(quoteOptionList)
                }
            }
        }

        /** get price using inter state*/
        viewModel.interStateSuccess()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                startTimer()
                binding.priceView.visibility = View.VISIBLE
                val interStateRes: InterStateRes = Gson().fromJson(it, InterStateRes::class.java)

                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .put("Vehicle", interStateRes.Vehicle)

                val quoteOptionList = interStateRes.QuoteOptions
                if (quoteOptionList != null) {
                    adapter?.updateRecords(quoteOptionList)
                }
            }
        }


        binding.nextBtn.setOnClickListener(this)
        binding.zoom2uHeader.backBtn.setOnClickListener(this)


        val text =  getString(R.string.expired_quote)
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color=resources.getColor(R.color.base_color)
                ds.underlineColor=resources.getColor(R.color.base_color)
                ds.isUnderlineText = true
            }

            override fun onClick(p0: View) {
              regenerateQuotes()
            }
        }
        spannableString.setSpan(clickableSpan, 39, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.quotesExpiered.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.quotesExpiered.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun callApiForInterOrIntraState() {
        if (intent.hasExtra("IntraStateData")) {
            intraStateReq = intent.getParcelableExtra("IntraStateData")
            intraStateReq?.CurrentDateTime =AppUtility.getCurrentDateAndTimeInEta()
            itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
            mainObjForMakeABooking = JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))
            if(!mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").getBoolean("isPickTimeSelectedFromTimeWindow"))
                intraStateReq?.PickupDateTime = AppUtility.getCurrentDateAndTimeInEta()
            viewModel.getIntraStatePrice(intraStateReq)
        } else if (intent.hasExtra("InterStateData")) {
            interStateReq = intent.getParcelableExtra("InterStateData")
            interStateReq?.CurrentDateTime = AppUtility.getCurrentDateAndTimeInEta()
            itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
            mainObjForMakeABooking = JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))
            if(!mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").getBoolean("isPickTimeSelectedFromTimeWindow"))
                interStateReq?.PickupDateTime = AppUtility.getCurrentDateAndTimeInEta()
            viewModel.getInterStatePrice(interStateReq)
        }
    }


    fun setAdapterView(binding: ActivityPricingPaymentBinding) {
        val layoutManager = GridLayoutManager(this, 1)
        binding.priceView.layoutManager = layoutManager
        adapter = PricePaymentAdapter(this, Collections.emptyList(), onItemClick = ::onPriceSelect)
        binding.priceView.adapter = adapter

    }


    private fun onPriceSelect(quoteOptionClass: QuoteOptionClass) {

        if (quoteOptionClass.isPriceSelect == false) {
            selectedQuoteOptionClass = quoteOptionClass
            priceSelected = true
        } else {
            priceSelected = false
        }
    }

    private fun startTimer() {
        object : CountDownTimer(180000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.timer.text = f.format(min) + ":" + f.format(sec)
                binding.quotesExpiered.visibility = View.GONE
                if (millisUntilFinished < 60000) {
                    binding.timer.setTextColor(Color.RED)
                    binding.secMin.text="seconds."
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.secMin.text="minutes."
                binding.timer.setTextColor(Color.BLACK)
                binding.timer.text = "3:00"
                isGenerateQuotesBtn=true
                priceSelected = false
                binding.nextBtn.text="Regenerate Quotes"
                binding.quotesExpiered.visibility = View.VISIBLE
                adapter?.updateRecords(Collections.emptyList())
            }
        }.start()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.next_btn -> {
                binding.nextBtn.isClickable=false
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.nextBtn.isClickable=true

                }, 1000)

                if(isGenerateQuotesBtn==true){
                    regenerateQuotes()
                }
                else {
                    if (priceSelected == true) {
                        selectedQuoteOptionData(selectedQuoteOptionClass)
                        val bookingConfirmationIntent =
                            Intent(this, BookingConfirmationActivity::class.java)
                        bookingConfirmationIntent.putExtra(
                            "MainJsonForMakeABooking",
                            mainObjForMakeABooking.toString()
                        )
                        bookingConfirmationIntent.putParcelableArrayListExtra(
                            "IconList",
                            itemDataList
                        )
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(bookingConfirmationIntent)
                    } else {
                        binding.nextBtn.isClickable = true
                        DialogActivity.alertDialogSingleButton(
                            this,
                            "Oops!",
                            "Please select a price."
                        )
                    }
                }
            }
            R.id.back_btn -> {
                val intent = Intent()
                setResult(3, intent)
                finish()
            }
        }
    }

    private fun regenerateQuotes() {
        isGenerateQuotesBtn=false
        callApiForInterOrIntraState()
        priceSelected = false
        binding.quotesExpiered.visibility = View.GONE
        binding.nextBtn.text="Next"
    }

    private fun selectedQuoteOptionData(quoteOptionClass: QuoteOptionClass?) {
        try {
            if (mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .getBoolean("IsInterstate")
            ) {


                /**for InterstateModel*/
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "pickupDistance",
                    quoteOptionClass?.PickupDistance
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "dropDistance",
                    quoteOptionClass?.DropDistance
                )

                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "routeDropPrice",
                    quoteOptionClass?.DropPrice
                )

                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "routePickupPrice",
                    quoteOptionClass?.PickupPrice
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "routeAirPrice",
                    quoteOptionClass?.InterstatePrice
                )

                /*for interstateodel*/




                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                    "DeliverySpeed",
                    quoteOptionClass?.DeliverySpeed
                )


                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                    "BookingFee",
                    quoteOptionClass?.BookingFee
                )

                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .put("Price", quoteOptionClass?.Price)


                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                    "Distance",
                    quoteOptionClass?.Distance
                )

                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                    "DropDateTime",
                    DateTimeUtil.getServerFormatFromServerFormat(quoteOptionClass?.DropDateTime)
                )

                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .put("ETA", quoteOptionClass?.ETA)

            } else


            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                "DeliverySpeed",
                quoteOptionClass?.DeliverySpeed
            )
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                "BookingFee",
                quoteOptionClass?.BookingFee
            )
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                .put("Price", quoteOptionClass?.Price)

            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                "DropDateTime",
                DateTimeUtil.getServerFormatFromServerFormat(quoteOptionClass?.DropDateTime)
            )


            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                .put("ETA", quoteOptionClass?.ETA)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(3, intent)
        finish()
    }
}