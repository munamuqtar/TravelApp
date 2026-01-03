package com.example.login.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

data class Airport(val name: String)

@Composable
fun SelectFromScreen(navController: NavController, onAirportSelected: (Airport) -> Unit) {}

@Composable
fun SelectToScreen(navController: NavController, onAirportSelected: (Airport) -> Unit) {}

@Composable
fun SelectDateScreen(navController: NavController) {}

@Composable
fun SearchResultsScreen(navController: NavController, from: String, to: String, departureDate: String) {}

@Composable
fun FlightDetailsScreen(
    navController: NavController,
    from: String = "",
    to: String = "",
    departure: String = "",
    returnDate: String = ""
) {}

@Composable
fun TravellerInformationScreen(navController: NavController) {}

@Composable
fun BookingSuccessScreen(navController: NavController) {}


