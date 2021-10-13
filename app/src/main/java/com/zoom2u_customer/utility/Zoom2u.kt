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

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppCenter.start(this, "c1415913-68da-4159-98a4-33f49149c349",
            Analytics::class.java, Crashes::class.java)
        mInstance = this
    }

}