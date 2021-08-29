package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.home.getAccountType.AccountTypeModel
import com.zoom2u_customer.ui.application.bottom_navigation.home.getAccountType.GetAccountRepository
import com.zoom2u_customer.ui.application.bottom_navigation.profile.chnage_password.ChangePassRepository

class ActiveBidOffersViewModel : ViewModel() {
    private var success: MutableLiveData<String>? = MutableLiveData("")
    var repository: ActiveBidOffersRepository? = null
    var repositoryGetAccountType: GetAccountRepository? = null
    private var accountType: MutableLiveData<AccountTypeModel>?= MutableLiveData(null)
    fun quotePayment(nonce:String,requestId:String,offerId: String,orderNo:String) = repository?.quotePayment(nonce,requestId,offerId,orderNo,onSuccess = ::onSuccess)

    fun getAccountType() =
        repositoryGetAccountType?.getAccountType(onSuccess = ::getAccountSuccess)



    private fun onSuccess(msg:String){
        success?.value=msg

    }

    fun getAccountSuccess(account_Type: AccountTypeModel){
        accountType?.value=account_Type
    }
    fun getQuotePayment(): MutableLiveData<String>?{
        return success
    }

    fun accountTypeSuccess():MutableLiveData<AccountTypeModel>?{
        return accountType
    }
}