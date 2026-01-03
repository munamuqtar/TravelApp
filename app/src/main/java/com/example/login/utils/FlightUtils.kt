package com.example.login.utils

import com.example.login.APIModels.DashboardFlight

fun extractFromCities(flights: List<DashboardFlight>): List<String> {
    return flights.mapNotNull { it.from }.distinct()
}

fun extractToCities(flights: List<DashboardFlight>): List<String> {
    return flights.mapNotNull { it.to }.distinct()
}
