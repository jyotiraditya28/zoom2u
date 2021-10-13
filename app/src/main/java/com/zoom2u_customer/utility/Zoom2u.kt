package com.zoom2u_customer.utility

import android.app.Application
import com.google.firebase.FirebaseApp
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class Zoom2u : Application() {


    companion object {

        private var mInstance: Zoom2u? = null


        fun getInstance(): Zoom2u? {
            return mInstance
        }

    }
    /*mahedra sir app center key="7af9dd8b-f9b2-4c28-909d-99faeafd4cbb"*/
    /*my app center key="c1415913-68da-4159-98a4-33f49149c349"*/
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppCenter.start(this,"7af9dd8b-f9b2-4c28-909d-99faeafd4cbb",
            Analytics::class.java, Crashes::class.java)
        mInstance = this
    }

}