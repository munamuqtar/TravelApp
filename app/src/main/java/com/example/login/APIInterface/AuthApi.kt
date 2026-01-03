package com.example.login.APIInterface

import com.example.login.APIModels.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/api/dashboard")
    suspend fun getDashboard(
        @Header("Authorization") token: String,
        @Header("Identity") identity: String
    ): Response<DashboardResponseV2>

    @GET("/api/flight-schedule/{id}")
    suspend fun getFlightSchedule(
        @Header("Authorization") token: String,
        @Path("id") flightId: String,
        @Query("identity") identity: String
    ): Response<FlightScheduleResponse>

    @POST("/api/signup/email")
    suspend fun signupUser(@Body request: SignupRequest): Response<SignupResponse>

    @POST("api/verify")
    suspend fun verifyEmailCode(
        @Body request: VerificationRequest
    ): Response<VerificationResponse>
    @POST("/api/sign/mobile")
    suspend fun signupWithMobile(@Body request: MobileSignupRequest): Response<MobileSignupResponse>
}
