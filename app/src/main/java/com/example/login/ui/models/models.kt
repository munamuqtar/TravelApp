package com.example.login.ui.models

data class Airport(
    val name: String,
    val code: String,
    val distanceKm: Int
)

data class City(
    val city: String,
    val country: String,
    val airports: List<Airport>
)
