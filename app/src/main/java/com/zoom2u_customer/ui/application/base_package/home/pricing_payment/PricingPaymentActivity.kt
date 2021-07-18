package com.zoom2u_customer.ui.application.base_package.home.pricing_payment

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
import com.zoom2u_customer.ui.application.base_package.home.booking_confirmation.BookingConfirmationActivity
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.InterStateReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.SaveDeliveryRequestReq
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.model.InterStateRes
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.model.IntraStateRes
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.model.QuoteOptionClass
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DateTimeUtil
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class PricingPaymentActivity : AppCompatActivity(), View.OnClickListener {
    var mainObjForMakeABooking: JSONObject? = null

    lateinit var binding: ActivityPricingPaymentBinding
    private var intraStateReq: IntraStateReq? = null
    private var interStateReq: InterStateReq? = null
    lateinit var viewModel: PricingPaymentViewModel
    private var repository: PricingPaymentRepository? = null
    var adapter : PricePaymentAdapter? =null
    private lateinit var itemDataList: ArrayList<Icon>
     private var saveDeliveryRequestReq: SaveDeliveryRequestReq?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_pricing_payment)



        viewModel = ViewModelProvider(this).get(PricingPaymentViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = PricingPaymentRepository(serviceApi, this)
        viewModel.repository = repository

        callApiForInterOrIntraState()

        setAdapterView(binding)

    /** get price using intra state*/
        viewModel.intraStateSuccess()?.observe(this) {
            if(it!=null){
                AppUtility.progressBarDissMiss()
                startTimer()
                val intraStateRes:IntraStateRes = Gson().fromJson(it, IntraStateRes::class.java)
                val quoteOptionList =intraStateRes.QuoteOptions
                if (quoteOptionList != null) {
                    adapter?.updateRecords(quoteOptionList)
                }
            }
        }

        /** get price using inter state*/
        viewModel.interStateSuccess()?.observe(this) {
            if(it!=null){
                AppUtility.progressBarDissMiss()
                startTimer()
                binding.priceView.visibility=View.VISIBLE
                val interStateRes: InterStateRes = Gson().fromJson(it, InterStateRes::class.java)
                val quoteOptionList =interStateRes.QuoteOptions
                if (quoteOptionList != null) {
                    adapter?.updateRecords(quoteOptionList)
                }
            }
        }


     binding.nextBtn.setOnClickListener(this)
     binding.startTimer.setOnClickListener(this)
    }

    private fun callApiForInterOrIntraState() {
        if (intent.hasExtra("IntraStateData")) {
            intraStateReq=intent.getParcelableExtra("IntraStateData")
            itemDataList =  intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
            //saveDeliveryRequestReq=intent.getParcelableExtra("SaveDeliveryRequestReq")
            mainObjForMakeABooking = JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))
            viewModel.getIntraStatePrice(intraStateReq)
        }else if(intent.hasExtra("InterStateData")){
            interStateReq=intent.getParcelableExtra("InterStateData")
            itemDataList =  intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
           // saveDeliveryRequestReq=intent.getParcelableExtra("SaveDeliveryRequestReq")
            mainObjForMakeABooking = JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))
            viewModel.getInterStatePrice(interStateReq)
        }
    }


    fun setAdapterView(binding: ActivityPricingPaymentBinding) {
        val layoutManager = GridLayoutManager(this, 1)
        binding.priceView.layoutManager = layoutManager
        adapter = PricePaymentAdapter(this, Collections.emptyList(),onItemClick = ::onPriceSelect)
        binding.priceView.adapter=adapter

    }
    private fun onPriceSelect(quoteOptionClass: QuoteOptionClass) {
        try {
           /* if (mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                    .getBoolean("IsInterstate")
            ) {
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "pickupDistance",
                    jsonArrayQuoteOptions.getJSONObject(+index).getString("PickupDistance")
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "dropDistance",
                    jsonArrayQuoteOptions.getJSONObject(+index).getString("DropDistance")
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "pickupState",
                    mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                        .getJSONObject("PickupLocation").getString("state")
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "dropState",
                    mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                        .getJSONObject("DropLocation").getString("state")
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "routePickupPrice",
                    jsonArrayQuoteOptions.getJSONObject(+index).getInt("PickupPrice")
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "routeAirPrice",
                    jsonArrayQuoteOptions.getJSONObject(+index).getInt("InterstatePrice")
                )
                mainObjForMakeABooking!!.getJSONObject("_interstateModel").put(
                    "routeDropPrice",
                    jsonArrayQuoteOptions.getJSONObject(+index).getInt("DropPrice")
                )
                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                    "Distance",
                    jsonArrayQuoteOptions.getJSONObject(+index).getString("Distance")
                )
            } else*/
                mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                .put("Distance", quoteOptionClass.Distance)
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                "DeliverySpeed",
                quoteOptionClass.DeliverySpeed
            )
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                "bookingFee",
                quoteOptionClass.BookingFee
            )
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                .put("Price",quoteOptionClass.Price)
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put(
                "DropDateTime",
                DateTimeUtil.getServerFormatFromServerFormat(quoteOptionClass.DropDateTime)
                )
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel").put("Source", 9)
            mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                .put("ETA", quoteOptionClass.ETA)


            callBookingConfirmationPage()


           /* if (quoteID != 0) mainObjForMakeABooking!!.getJSONObject("_deliveryRequestModel")
                .put("quoteID", quoteID)
            if (intent.getIntExtra(
                    "quoteFromMakeABooking",
                    0
                ) != 1
            ) callBookingConfirmationPage() else callMakeABookingView()*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callBookingConfirmationPage() {
        val bookingConfirmationIntent = Intent(this, BookingConfirmationActivity::class.java)
        bookingConfirmationIntent.putExtra(
            "MainJsonForMakeABooking",
            mainObjForMakeABooking.toString()
        )
        bookingConfirmationIntent.putParcelableArrayListExtra("IconList", itemDataList)
        startActivity(bookingConfirmationIntent)
    }

    private fun startTimer() {
        object : CountDownTimer(180000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.timer.text = f.format(min) + ":" + f.format(sec)
                binding.expiredQuote.visibility=View.GONE
                binding.expiredQuote1.visibility=View.GONE
                if( millisUntilFinished <60000){
                    binding.timer.setTextColor(Color.RED)
                }
            }

            override fun onFinish() {
                binding.timer.setTextColor(Color.BLACK)
                binding.timer.text = "3:00"
                binding.expiredQuote.visibility=View.VISIBLE
                binding.expiredQuote1.visibility=View.VISIBLE
                adapter?.updateRecords(Collections.emptyList())
            }
        }.start()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.next_btn -> {
                val intent = Intent(this, BookingConfirmationActivity::class.java)
                intent.putExtra("SaveDeliveryRequestReq",saveDeliveryRequestReq)
                intent.putParcelableArrayListExtra("IconList", itemDataList)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.start_timer->{
                callApiForInterOrIntraState()
                binding.expiredQuote.visibility=View.GONE
                binding.expiredQuote1.visibility=View.GONE
            }
        }
    }
}