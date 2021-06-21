package com.example.zoom2u.utility

import android.content.Context
import android.content.SharedPreferences
import com.example.zoom2u.application.ui.log_in.LoginResponce
import com.google.gson.Gson

object AppPreference : SharedPref {



        private val Preference_name :String = "zoom2u_pref"

        private var sp: SharedPreferences?=null

        private var editor: SharedPreferences.Editor? = null

        private val Login_Response :String= "login_response"



    fun getSharedPrefInstance(): SharedPref {
            return this
        }

    init {
        sp = Zoom2u.getInstance()?.getSharedPreferences(Preference_name, Context.MODE_PRIVATE)
        editor = sp?.edit()
    }

    override fun setLoginData() {
        TODO("Not yet implemented")
    }


    override fun setLoginResponse(res: String?) {
            editor?.putString(Login_Response, res)
            editor?.commit()
        }


        override fun getLoginResponse(): LoginResponce {
            val gson = Gson()
            val res: LoginResponce =
                gson.fromJson(sp?.getString(Login_Response, null), LoginResponce::class.java)
            return res
        }

    override fun removeLoginResponce() {
        editor?.remove(Login_Response)
        editor?.commit()
    }


}