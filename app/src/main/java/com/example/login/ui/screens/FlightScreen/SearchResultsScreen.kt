package com.example.login.ui.screens.FlightScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.APIModels.DashboardFlight


// -------------------------------------------------------
// 🔵 SEARCH RESULTS SCREEN
// -------------------------------------------------------
@Composable
fun SearchResultsScreen(
    navController: NavController,
    from: String,
    to: String,
    date: String,
    flights: List<DashboardFlight>
) {
    Scaffold(
        topBar = {
            SearchHeader(
                from = from,
                to = to,
                date = date,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            items(flights.size) { index ->
                FlightCardItem(
                    flight = flights[index],
                    navController = navController
                )
            }
        }
    }
}

// -------------------------------------------------------
// 🔵 TOP HEADER (MATCHES SCREENSHOT)
// -------------------------------------------------------
@Composable
fun SearchHeader(
    from: String,
    to: String,
    date: String,
    onBack: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            // 🔙 Back Button
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🔍 Center Search Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .shadow(2.dp, RoundedCornerShape(14.dp))
                    .background(Color(0xFFF5F5F7), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = "$from - $to",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$date",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 🔔 Notification Icon
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Notifications",
                modifier = Modifier.size(26.dp)
            )
        }
    }
}


// -------------------------------------------------------
// 🔵 FLIGHT CARD (UNCHANGED)
// -------------------------------------------------------
@Composable
fun FlightCardItem(
    flight: DashboardFlight,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .padding(bottom = 30.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = flight.flight_date ?: "",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${flight.from ?: "--"} → ${flight.to ?: "--"}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(17.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            Column {
                Text("Departure", color = Color.Gray, fontSize = 12.sp)
                Text(
                    extractTime(flight.departure_time ?: "--"),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.End) {
                Text("Arrival", color = Color.Gray, fontSize = 12.sp)
                Text(
                    extractTime(flight.arrival_time ?: "--"),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    navController.navigate("flight_details/${flight.id}")
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF1ECFF)),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "View Details",
                    color = Color(0xFF6A4DFF),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Default.Flight,
                contentDescription = null,
                tint = Color(0xFF1B73E8),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = flight.aircraft ?: "-",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}


// -------------------------------------------------------
// 🔵 TIME FORMATTER (UNCHANGED)
// -------------------------------------------------------
fun extractTime(dateTime: String): String {
    return try {
        val p = dateTime.split(" ")
        if (p.size >= 3) "${p[1]} ${p[2]}" else dateTime
    } catch (e: Exception) {
        dateTime
    }
}
