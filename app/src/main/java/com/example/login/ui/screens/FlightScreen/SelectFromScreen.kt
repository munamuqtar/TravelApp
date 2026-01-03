package com.example.login.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login.utils.extractFromCities
import com.example.login.APIModels.DashboardFlight



@Composable
fun SelectFromScreen(
    navController: NavController,
    flights: List<DashboardFlight>,
    onSelected: (String) -> Unit
) {
    val fromCities = extractFromCities(flights)

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(90.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Where from?", style = MaterialTheme.typography.h6, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(28.dp).clickable { navController.popBackStack() }
            )
        }
        Divider()
        LazyColumn {
            items(fromCities) { city ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Pass selected city to FlightOneWayScreen
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selectedFrom", city)
                            navController.popBackStack() // Go back to FlightOneWayScreen
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "", tint = Color.Blue)
                    Spacer(Modifier.width(12.dp))
                    Text(city, style = MaterialTheme.typography.subtitle1)
                }
                Divider()
            }
        }
    }
}

