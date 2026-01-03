package com.example.login.APIModels

data class DashboardFlight(
    val id: Int?,
    val flight_number: String?,
    val flight_date: String?,
    val flight_day: String?,
    val aircraft: String?,
    val from: String?,
    val to: String?,
    val arrival_time: String?,
    val departure_time: String?,
    val booking_deadline: String?,
    val expired_hours: Int?,
    val tax: String?,
    val payment_mode: String?,
    val status: String?,
    val freeze_schedule: String?
)

// Dashboard response
data class DashboardResponseV2(
    val status: String?,
    val data: DashboardFlightsData?,
    val message: String?,
    val error: String?
)

data class DashboardFlightsData(
    val flights: List<DashboardFlight>?
)
