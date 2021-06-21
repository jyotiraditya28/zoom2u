package com.example.zoom2u.utility

import android.app.Application


class Zoom2u : Application() {


    companion object {

        private var mInstance: Zoom2u? = null


        fun getInstance(): Zoom2u? {
            return mInstance
        }

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}