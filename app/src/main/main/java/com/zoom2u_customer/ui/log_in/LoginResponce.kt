package com.zoom2u_customer.ui.log_in

import java.io.Serializable

data class LoginResponce(val CustomerHash : String?,
                         var access_token: String?,
                         val firstName : String?,
                         val mobileNumber : String?,
                         val roles : String?,
                         val token_type : String?): Serializable