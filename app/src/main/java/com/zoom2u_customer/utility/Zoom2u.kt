package com.zoom2u_customer.utility

import android.app.Application
import com.google.firebase.FirebaseApp


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
        mInstance = this
    }

}