package com.example.zoom2u.application.ui.log_in

import java.io.Serializable

data class LoginRequest(val grant_type : String="password",
                        val username: String,
                        val password : String,
                        val isDeliveriesPortal : String="false"): Serializable
