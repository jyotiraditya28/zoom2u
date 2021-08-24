package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.braintreepayments.api.BraintreePaymentActivity
import com.braintreepayments.api.models.PaymentMethodNonce
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi

import com.zoom2u_customer.databinding.FragmentBidOffersBinding
import com.zoom2u_customer.getBrainTree.GetBrainTreeClientTokenOrBookDeliveryRequest
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.ActiveBidListRepository
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.ActiveBidListViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.BidDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.Offer
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_offers.CompletedBidOffersAdapter
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OrderConfirmActivity
import com.zoom2u_customer.utility.AppUtility
import org.json.JSONException

class BidOffersFragment(private val bidDetails: BidDetailsResponse?) : Fragment() {
    lateinit var binding: FragmentBidOffersBinding
    lateinit var viewModel: ActiveBidOffersViewModel
    private var  repository: ActiveBidOffersRepository? = null
    private var getBrainTreeClientToken: GetBrainTreeClientTokenOrBookDeliveryRequest? = null
    private var request_Code =1002
    private var offers:Offer?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidOffersBinding.inflate(inflater, container, false)
        getBrainTreeClientToken = GetBrainTreeClientTokenOrBookDeliveryRequest(activity, request_Code)
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
                val intent = Intent(activity, OrderConfirmActivity::class.java)
                intent.putExtra("BookingRefFromBid", it)
                intent.flags=Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                activity?.finish()

            }
        }




        return binding.root
    }

    fun setAdapterView(binding: FragmentBidOffersBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.activeBidOffersRecycler.layoutManager = layoutManager
        val adapter =
            ActiveBidOffersAdapter(context, bidDetails?.Offers?.toList()!!, onItemClick = ::onBidOfferSelected)
        binding.activeBidOffersRecycler.adapter = adapter

    }
    private fun onBidOfferSelected(offer:Offer) {
        this.offers=offer
        getBrainTreeClientToken?.callServiceForGetClientToken()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_Code) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                val paymentMethodNonce: PaymentMethodNonce? =
                    data?.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE)
                val nonce = paymentMethodNonce?.nonce
                try {

                 viewModel.quotePayment(nonce.toString(),bidDetails?.Id.toString(),offers?.OfferId.toString())

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

}