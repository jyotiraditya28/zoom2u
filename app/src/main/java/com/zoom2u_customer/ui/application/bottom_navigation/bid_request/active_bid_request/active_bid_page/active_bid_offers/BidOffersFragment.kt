package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.braintreepayments.api.BraintreePaymentActivity
import com.braintreepayments.api.models.PaymentMethodNonce
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi

import com.zoom2u_customer.databinding.FragmentBidOffersBinding
import com.zoom2u_customer.getBrainTree.GetBrainTreeClientTokenOrBookDeliveryRequest
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.BidDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.Offer
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_details.CompletedDetailsFragment
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingResponse
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OnHoldActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OrderConfirmActivity
import com.zoom2u_customer.utility.AppUtility
import org.json.JSONException

class BidOffersFragment() : Fragment() {
    lateinit var binding: FragmentBidOffersBinding
    lateinit var viewModel: ActiveBidOffersViewModel
    private var repository: ActiveBidOffersRepository? = null
    private var getBrainTreeClientToken: GetBrainTreeClientTokenOrBookDeliveryRequest? = null
    private var request_Code = 1002
    private var offers: Offer? = null
    private var purOrderNo: String? = null
    var bidDetails: BidDetailsResponse? = null

    companion object {

        var bidDetail1: BidDetailsResponse? = null

        fun newInstance(bidDetails: BidDetailsResponse?): BidOffersFragment{
            this.bidDetail1 = bidDetails
            return BidOffersFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidOffersBinding.inflate(inflater, container, false)
        this.bidDetails=bidDetail1

        getBrainTreeClientToken =
            GetBrainTreeClientTokenOrBookDeliveryRequest(activity, request_Code)
        if (container != null) {
            setAdapterView(binding, container.context)
        }
        viewModel = ViewModelProvider(this).get(ActiveBidOffersViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = ActiveBidOffersRepository(serviceApi, container?.context)
        viewModel.repository = repository


        viewModel.getQuotePayment()?.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                AppUtility.progressBarDissMiss()
                val bookingResponse: BookingResponse =
                    Gson().fromJson(it, BookingResponse::class.java)
                if (!bookingResponse.`$type`
                        .equals("System.Web.Http.HttpError, System.Web.Http", ignoreCase = true)
                ) {
                    if (bookingResponse.Verified == true) {
                        val intent = Intent(activity, OrderConfirmActivity::class.java)
                        intent.putExtra("BookingRefFromBid", bookingResponse.BookingRef)
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        startActivity(intent)
                        activity?.finish()
                    }else {
                        val intentOnHold = Intent(activity, OnHoldActivity::class.java)
                        intentOnHold.putExtra("BookingResponse", bookingResponse)
                        intentOnHold.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intentOnHold)
                        activity?.finish()
                    }
                }
            }
        }




        return binding.root
    }

    fun setAdapterView(binding: FragmentBidOffersBinding, context: Context) {
        if(bidDetails?.Offers?.isNotEmpty() == true) {
            val layoutManager = GridLayoutManager(activity, 1)
            binding.activeBidOffersRecycler.layoutManager = layoutManager
            val adapter = ActiveBidOffersAdapter(
                context,
                bidDetails?.Offers?.toList()!!,
                onItemClick = ::onBidOfferSelected
            )
            binding.activeBidOffersRecycler.adapter = adapter
        }else{
            binding.emptyView.visibility=View.VISIBLE
        }
    }

    private fun onBidOfferSelected(offer: Offer) {
        this.offers = offer

        val viewGroup = (context as Activity).findViewById<ViewGroup>(R.id.content)
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.purchase_dialogview, viewGroup, false)
        val builder = AlertDialog.Builder(context as Activity)
        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val orderNo: TextInputEditText = dialogView.findViewById(R.id.pur_no)


        val submitBtn: TextView = dialogView.findViewById(R.id.ok)
        submitBtn.setOnClickListener {
            if (orderNo.text.toString().trim() == "")
                AppUtility.validateEditTextField(orderNo, "Please enter purchase order number.")
            else {
                purOrderNo = orderNo.text.toString().trim()
              /**check account type for payment*/
                if (AppUtility.getAccountType() == "Standard") {
                    AppUtility.progressBarShow(activity)
                    getBrainTreeClientToken?.callServiceForGetClientToken()
                }else if(AppUtility.getAccountType() == "OnAccount"){
                    viewModel.quotePayment(
                        "",
                        bidDetails?.Id.toString(),
                        offers?.OfferId.toString(),
                        purOrderNo.toString()
                    )
                }
                alertDialog.dismiss()
            }
        }
        val cancelBtn: TextView = dialogView.findViewById(R.id.cancel)
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_Code) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                val paymentMethodNonce: PaymentMethodNonce? =
                    data?.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE)
                val nonce = paymentMethodNonce?.nonce
                try {

                    viewModel.quotePayment(
                        nonce.toString(),
                        bidDetails?.Id.toString(),
                        offers?.OfferId.toString(),
                        purOrderNo.toString()
                    )

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

}