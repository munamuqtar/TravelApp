package com.example.login.APIModels

data class MobileSignupRequest(
    val name: String,
    val mobile: String
)
data class MobileSignupResponse(
    val status: String,
    val data: SignupData?,
    val error: String?
)