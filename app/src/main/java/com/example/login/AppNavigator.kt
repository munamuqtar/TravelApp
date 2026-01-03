package com.example.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.login.ui.screens.*
import com.example.login.ui.screens.FlightScreen.*
import com.example.login.ui.screens.FlightScreen.SelectDateScreen

@Composable
fun AppNavigator() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        // ---------------- DASHBOARD ----------------
        composable("dashboard") {
            DashboardScreen(navController)
        }

        // ---------------- LOGIN ----------------
        composable(
            route = "login?next={next}",
            arguments = listOf(
                navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val next = it.arguments?.getString("next") ?: ""
            LoginScreen(navController, next)
        }

        // ---------------- SIGNUP EMAIL ----------------
        composable(
            route = "signupEmail?next={next}",
            arguments = listOf(
                navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val next = it.arguments?.getString("next") ?: ""
            SignUpEmailScreen(navController, next)
        }

        // ---------------- SIGNUP PHONE ----------------
        composable(
            route = "signupPhone?next={next}",
            arguments = listOf(
                navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val next = it.arguments?.getString("next") ?: ""
            SignUpPhoneScreen(navController, next)
        }

        // ---------------- VERIFICATION ----------------
        composable(
            route = "verification/{identity}?next={next}",
                    arguments = listOf(
                navArgument("identity") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val identity = it.arguments?.getString("identity") ?: ""
            val next = it.arguments?.getString("next") ?: ""
            VerificationScreen(navController, identity, next)
        }

        // ---------------- FLIGHT ONE WAY ----------------
        composable(
            route = "flightOneWay?identity={identity}&token={token}",
            arguments = listOf(
                navArgument("identity") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("token") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val identity = it.arguments?.getString("identity") ?: ""
            val token = it.arguments?.getString("token") ?: ""
            FlightOneWayScreen(navController, token, identity)
        }

        // ---------------- TRAVELLER INFO ----------------
        composable(
            route = "travellerInfo/{travellers}/{price}?identity={identity}&token={token}",
            arguments = listOf(
                navArgument("travellers") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType },
                navArgument("identity") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("token") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val travellers =
                it.arguments?.getString("travellers")?.toIntOrNull() ?: 1
            val price =
                it.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
            val identity = it.arguments?.getString("identity") ?: ""
            val token = it.arguments?.getString("token") ?: ""

            TravellerInformationScreen(
                navController,
                travellers,
                price,
                identity,
                token
            )
        }

        // ---------------- PAYMENT ----------------
        composable(
            route = "payment/{count}/{price}?identity={identity}&token={token}",
            arguments = listOf(
                navArgument("count") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType },
                navArgument("identity") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("token") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val count = it.arguments?.getString("count")?.toInt() ?: 1
            val price = it.arguments?.getString("price")?.toDouble() ?: 0.0
            val identity = it.arguments?.getString("identity") ?: ""
            val token = it.arguments?.getString("token") ?: ""

            PaymentScreen(
                navController,
                count,
                price,
                identity,
                token
            )
        }

        // ---------------- flightOneWay ----------------
        composable("flightOneWay") {
            FlightOneWayScreen(
                navController = navController,
                token = SessionManager.token ?: "",
                identity = SessionManager.identity ?: ""
            )
        }

        // ---------------- SELECT FROM ----------------
        composable(Screens.SelectFrom.route) {
            SelectFromScreen(
                navController = navController,
                flights = flightsFromAPI
            ) { selected ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("selectedFrom", selected)
                navController.popBackStack()
            }
        }

        // ---------------- SELECT TO ----------------
        composable(Screens.SelectTo.route) {
            SelectToScreen(
                navController = navController,
                flights = flightsFromAPI
            ) { selected ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("selectedTo", selected)
                navController.popBackStack()
            }
        }

        // ---------------- SELECT DATE ----------------
        composable("selectDate") {
            SelectDateScreen(navController)
        }

        // ---------------- SEARCH RESULTS ----------------
        composable("searchResults/{from}/{to}/{date}") {
            val from = it.arguments?.getString("from") ?: ""
            val to = it.arguments?.getString("to") ?: ""
            val date = it.arguments?.getString("date") ?: ""

            SearchResultsScreen(
                navController,
                from,
                to,
                date,
                flightsFromAPI
            )
        }

        // ---------------- FLIGHT DETAILS ----------------
        composable("flight_details/{id}") {
            val id = it.arguments?.getString("id") ?: ""
            val flight = flightsFromAPI.find { f ->
                f.id.toString() == id
            }

            flight?.let {
                FlightDetailsScreen(navController, it)
            }
        }
    }
}
