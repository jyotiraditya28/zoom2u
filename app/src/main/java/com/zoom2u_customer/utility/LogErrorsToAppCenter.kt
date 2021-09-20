package com.zoom2u_customer.utility

import com.microsoft.appcenter.analytics.Analytics
import com.zoom2u_customer.BuildConfig
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse
import com.zoom2u_customer.ui.log_in.LoginResponce

class LogErrorsToAppCenter {

    fun addLogToAppCenterOnAPIFail(url: String, statusCode: Int, errorMsg: String?, screen: String,trackEvent:String) {

        try {
            val properties = HashMap<String, String>()
            val profileResponse: ProfileResponse? = AppPreference.getSharedPrefInstance().getProfileData()

            properties["userName"] = "${profileResponse?.FirstName} ${profileResponse?.LastName}"
            properties["userEmail"] = "${profileResponse?.Email}"
            properties["apiUrl"] = url
            properties["customerId"] = profileResponse?.CustomerId.toString()
            properties["errorCode"] = "$statusCode"
            properties["errorMessage"] = errorMsg?.let { it }?: kotlin.run { "" }
            properties["screen"] = screen
            properties["deviceId"] = profileResponse?.deviceId.toString()
            properties["deviceType"] = profileResponse?.DeviceType.toString()
            properties["AccountType"] = profileResponse?.AccountType.toString()
            properties["appVersion"] = BuildConfig.VERSION_NAME


            Analytics.trackEvent(trackEvent, properties)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}