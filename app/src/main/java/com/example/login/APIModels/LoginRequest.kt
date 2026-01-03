package com.example.login.APIModels

// LoginRequest.kt
data class LoginRequest(
    val identity: String,
    val password: String
)

// LoginResponse.kt
data class LoginResponse(
    val status: String,
    val data: LoginData?,
    val error: String?
)

data class LoginData(
    val token: String,
    val message: String,
    val identity: String
)
data class DashboardResponse(
    val status: String,
    val data: DashboardData?,
    val error: String?
)

data class DashboardData(
    val message: String
)
