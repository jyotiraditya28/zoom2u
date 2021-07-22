package com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pricing_payment)


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
        binding.startTimer.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
    }

    private fun callApiForInterOrIntraState() {
        if (intent.hasExtra("IntraStateData")) {
            intraStateReq = intent.getParcelableExtra("IntraStateData")
            itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
            //saveDeliveryRequestReq=intent.getParcelableExtra("SaveDeliveryRequestReq")
            mainObjForMakeABooking = JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))
            viewModel.getIntraStatePrice(intraStateReq)
        } else if (intent.hasExtra("InterStateData")) {
            interStateReq = intent.getParcelableExtra("InterStateData")
            itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
            // saveDeliveryRequestReq=intent.getParcelableExtra("SaveDeliveryRequestReq")
            mainObjForMakeABooking = JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))
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
                binding.expiredQuote.visibility = View.GONE
                binding.expiredQuote1.visibility = View.GONE
                if (millisUntilFinished < 60000) {
                    binding.timer.setTextColor(Color.RED)
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.timer.setTextColor(Color.BLACK)
                binding.timer.text = "3:00"
                priceSelected = false
                binding.expiredQuote.visibility = View.VISIBLE
                binding.expiredQuote1.visibility = View.VISIBLE
                adapter?.updateRecords(Collections.emptyList())
            }
        }.start()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.next_btn -> {
                if (priceSelected == true) {
                    selectedQuoteOptionData(selectedQuoteOptionClass)
                    val bookingConfirmationIntent =
                        Intent(this, BookingConfirmationActivity::class.java)
                    bookingConfirmationIntent.putExtra(
                        "MainJsonForMakeABooking",
                        mainObjForMakeABooking.toString()
                    )
                    bookingConfirmationIntent.putParcelableArrayListExtra("IconList", itemDataList)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(bookingConfirmationIntent)
                } else {
                    DialogActivity.alertDialogSingleButton(this, "Oops!", "Please select a price.")
                }
            }
            R.id.start_timer -> {
                callApiForInterOrIntraState()
                priceSelected = true
                binding.expiredQuote.visibility = View.GONE
                binding.expiredQuote1.visibility = View.GONE
            }
            R.id.back_btn -> {
                finish()
            }
        }
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
                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .put("Distance", quoteOptionClass?.Distance)

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
}