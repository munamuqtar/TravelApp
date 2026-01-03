package com.example.login.ui.screens.FlightScreen

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.APIModels.DashboardFlight
import com.example.login.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FlightDetailsScreen(
    navController: NavController,
    flight: DashboardFlight
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    var travellersCount by remember { mutableStateOf(1) }
    var travellerSummary by remember { mutableStateOf("1 traveller • Economy • One-way") }

    val basePrice = flight.tax?.toDoubleOrNull() ?: 0.0
    val totalPrice = basePrice * travellersCount

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            TravellerOptionsSheet(
                initialText = travellerSummary,
                onDone = {
                    travellerSummary = it
                    travellersCount = it.substringBefore(" ").toIntOrNull() ?: 1
                    scope.launch { sheetState.hide() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Flight details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$${"%.2f".format(totalPrice)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            val nextRoute = Uri.encode(
                                "travellerInfo/$travellersCount/$totalPrice?identity=&token="
                            )

                            if (SessionManager.isLoggedIn()) {
                                navController.navigate(
                                    "travellerInfo/$travellersCount/$totalPrice" +
                                            "?identity=${SessionManager.identity}&token=${SessionManager.token}"
                                )
                            } else {
                                navController.navigate("login?next=$nextRoute")
                            }

                        },
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF2962FF))
                    ) {
                        Text("BOOK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        "Your trip to ${flight.to}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("from ${flight.from}", color = Color.Gray)

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.clickable {
                            scope.launch { sheetState.show() }
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Flight, null)
                        Spacer(Modifier.width(8.dp))
                        Text(travellerSummary)
                    }

                    Spacer(Modifier.height(20.dp))
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "${flight.from} - ${flight.to}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {

                                Column {
                                    Text("Departure", color = Color.Gray)
                                    Text(
                                        flight.departure_time ?: "",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(Modifier.weight(1f))

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Arrival", color = Color.Gray)
                                    Text(
                                        flight.arrival_time ?: "",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Divider()
                            Spacer(Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Flight,
                                    contentDescription = null,
                                    tint = Color.Blue
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(flight.aircraft ?: "")
                            }

                            Spacer(Modifier.height(14.dp))

                            Text(
                                text = "Booking Deadline",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = flight.booking_deadline ?: "N/A",
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
