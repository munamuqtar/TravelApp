package com.example.login.APIModels

// ---------------- FLIGHT SCHEDULE RESPONSE ----------------
data class FlightScheduleResponse(
    val status: String?,
    val data: FlightScheduleWrapper?,
    val message: String?,
    val error: String?
)

data class FlightScheduleWrapper(
    val message: String?,
    val data: DashboardFlight?   // SINGLE FLIGHT
)


