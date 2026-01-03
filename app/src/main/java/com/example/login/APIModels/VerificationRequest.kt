package com.example.login.APIModels

data class VerificationResponse(
    val status: String,
    val data: VerificationData?,
    val error: String?
)

data class VerificationData(
    val token: String,
    val identity: Int, // ❗ USER ID
    val message: String
)
data class VerificationRequest(
    val identity: String,
    val otp: String
)