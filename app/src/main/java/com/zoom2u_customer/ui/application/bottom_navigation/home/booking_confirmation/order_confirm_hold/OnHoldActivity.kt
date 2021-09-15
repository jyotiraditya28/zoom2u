package com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.braintreepayments.api.BraintreePaymentActivity
import com.braintreepayments.api.models.PaymentMethodNonce
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityOnHoldBinding
import com.zoom2u_customer.databinding.ActivityOrderConfirmBinding
import com.zoom2u_customer.getBrainTree.GetBrainTreeClientTokenOrBookDeliveryRequest
import com.zoom2u_customer.ui.application.bottom_navigation.base_page.BasePageActivity
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse
import com.zoom2u_customer.ui.application.bottom_navigation.history.history_details.HistoryDetailsRepository
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingConfirmationRepository
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingConfirmationViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONException

class OnHoldActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnHoldBinding
    private var request_Code = 1004
    var historyResponse: HistoryResponse?=null
    private var bookingResponse: BookingResponse? = null
    lateinit var viewModel: OnHoldViewModel
    private var getBrainTreeClientToken: GetBrainTreeClientTokenOrBookDeliveryRequest? = null
    private var repository: OnHoldRepository? = null
    var repositoryHistory: HistoryDetailsRepository? = null
    private var nonce: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_hold)
        getBrainTreeClientToken =
            GetBrainTreeClientTokenOrBookDeliveryRequest(this, request_Code)

        if (intent.hasExtra("BookingResponse")) {
            bookingResponse = intent.getParcelableExtra("BookingResponse")
            setData(bookingResponse)
        }else if (intent.hasExtra("historyResponse")) {
            historyResponse = intent.getParcelableExtra("historyResponse")
            binding.txtBookingRefrence.text = historyResponse?.BookingRef
        }
        viewModel = ViewModelProvider(this).get(OnHoldViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = OnHoldRepository(serviceApi, this)
        repositoryHistory = HistoryDetailsRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.repositoryHistory = repositoryHistory

        binding.acceptBtn.setOnClickListener {
            binding.acceptBtn.isClickable=false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.acceptBtn.isClickable=true

            }, 3000)

            getBrainTreeClientToken?.callServiceForGetClientToken()
        }

        binding.cancelBtn.setOnClickListener(){
            DialogActivity.logoutDialog(
                this,
                "Confirm!",
                "Are you sure you want to cancel your booking?",
                "Yes", "No",
                onCancelClick = ::onNoClick,
                onOkClick = ::onYesClick)
        }

        binding.backIcon.setOnClickListener{
            if (intent.hasExtra("BookingResponse")){
            DialogActivity.logoutDialog(
                this, "Are you sure!", "Are you want make a new Booking?",
                "Ok", "Cancel",
                onCancelClick = ::onCancelClick,
                onOkClick = ::onOkClick
            )}
            else
                finish()
        }


        viewModel.getActivateSuccess()?.observe(this) {
            if (it != null) {
                if (it == "true") {
                    AppUtility.progressBarDissMiss()
                    val loginPage = Intent(this, OrderConfirmActivity::class.java)
                    if (intent.hasExtra("BookingResponse"))
                    loginPage.putExtra("BookingResponse", bookingResponse)
                   else
                        loginPage.putExtra("historyResponse", historyResponse)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(loginPage)
                    finish()

                }
            }
        }
        viewModel.getCancelBooking().observe(this) {
            if (it != null) {
                if (it == "true") {
                    AppUtility.progressBarDissMiss()

                    if (intent.hasExtra("BookingResponse")) {
                        val intent = Intent(this, BasePageActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        Toast.makeText(
                            this,
                            "Booking cancellation successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(intent)
                        finish()
                    }
                    /**call when cancel from history on Hold*/
                    else{
                        val intent = Intent()
                        historyResponse?.IsCancel = true
                        intent.putExtra("historyItem", historyResponse)
                        setResult(85, intent)
                        Toast.makeText(this, "Booking cancellation successfully.", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                }
            }
        }
    }

    private fun setData(bookingResponse: BookingResponse?) {
        binding.txtBookingRefrence.text = bookingResponse?.BookingRef

    }

    private fun onNoClick() {}

    private fun onYesClick() {
        if (intent.hasExtra("BookingResponse"))
          viewModel.cancelBooking(bookingResponse?.BookingId)
       else
            viewModel.cancelBooking(historyResponse?.BookingId.toString())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_Code) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                val paymentMethodNonce: PaymentMethodNonce? =
                    data?.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE)
                val nonce = paymentMethodNonce?.nonce
                try {
                    if (intent.hasExtra("BookingResponse"))
                        viewModel.activateRequest(bookingResponse?.BookingRef, nonce)
                    else
                        viewModel.activateRequest(historyResponse?.BookingRef, nonce)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (intent.hasExtra("BookingResponse")){
        DialogActivity.logoutDialog(
            this, "Are you sure!", "Are you want make a new Booking?",
            "Ok", "Cancel",
            onCancelClick = ::onCancelClick,
            onOkClick = ::onOkClick
        )
        }
        else{
            finish()
        }
    }

    private fun onCancelClick() {}
    private fun onOkClick() {
        newBooking()
    }

    private fun newBooking() {
        val intent = Intent(this, BasePageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

}