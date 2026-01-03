package com.example.login.ui.screens.FlightScreen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login.utils.extractToCities
import com.example.login.APIModels.DashboardFlight

@Composable
fun SelectToScreen(
    navController: NavController,
    flights: List<DashboardFlight>,
    onSelected: (String) -> Unit
) {
    val toCities = extractToCities(flights)

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(90.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Where to?", style = MaterialTheme.typography.h6, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(28.dp).clickable { navController.popBackStack() }
            )
        }
        Divider()
        LazyColumn {
            items(toCities) { city ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selectedTo", city)
                            navController.popBackStack()
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

