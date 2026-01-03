package com.example.login.APIModels


data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)

data class SignupResponse(
    val status: String,
    val data: SignupData?,
    val error: String?
)

data class SignupData(
    val identity: String
)