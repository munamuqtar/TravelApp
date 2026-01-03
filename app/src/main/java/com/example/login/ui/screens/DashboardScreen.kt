package com.example.login.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.APIInterface.ApiClient
import com.example.login.APIModels.DashboardFlight
import com.example.login.APIModels.DashboardResponse
import com.example.login.R
import com.example.login.ui.models.DestinationCity
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import com.example.login.SessionManager
import com.example.login.SessionManager.getFirstLetter
import java.util.*

// ---------------- SAMPLE DATA ----------------
private val bestCities = listOf(
    DestinationCity("Mugdisho", "from \$133.00 to \$138.00", R.drawable.botswana),
    DestinationCity("Pudland", "from \$148.00 to \$150.00", R.drawable.botswana),
    DestinationCity("Galgacyo", "from \$160.00 to \$163.00", R.drawable.botswana),
    DestinationCity("Busaaso", "from \$170.00 to \$178.00", R.drawable.botswana),
    DestinationCity("Somaliland", "from \$170.00 to \$178.00", R.drawable.botswana)
)

// ---------------- UI DATA CLASS ----------------
data class FlightUi(
    val id: Int?,
    val route: String,
    val date: String,
    val departureTime: String,
    val arrivalTime: String,
    val airline: String
)

// ---------------- BOTTOM NAV ----------------
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Home : BottomNavItem("dashboard", "Home", Icons.Default.Home)
    object Booking : BottomNavItem("booking", "Booking", Icons.Default.FlightTakeoff)
    object Payment : BottomNavItem("payment", "Payment", Icons.Default.CreditCard)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

// ---------------- FLIGHT CARD ----------------
@Composable
fun FlightCard(flight: FlightUi, navController: NavController) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val flightId = flight.id ?: 0
                navController.navigate("flightDetails/$flightId")
            }
    ) {
        Column(modifier = Modifier.background(Color.White)) {

            // ------------------ DATE ON TOP RIGHT ------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = flight.date,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)

            // ------------------ FLIGHT MAIN CONTENT ------------------
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    // ROUTE (London → NYC)
                    Text(
                        flight.route,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Departure / Arrival
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {
                            Text("Departure", color = Color.Gray, fontSize = 12.sp)
                            Text(
                                flight.departureTime,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Arrival", color = Color.Gray, fontSize = 12.sp)
                            Text(
                                flight.arrivalTime,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Airline
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Flight,
                            contentDescription = null,
                            tint = Color(0xFF1B73E8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = flight.airline,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))

                // ------------------ VIEW DETAILS BUTTON ------------------
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
            }
        }
    }
}

// ---------------- DASHBOARD SCREEN ----------------
@Composable
fun DashboardScreen(navController: NavController) {

    val identity = SessionManager.identity ?: ""
    val token = SessionManager.token ?: ""

    var searchQuery by remember { mutableStateOf("") }
    var flights by remember { mutableStateOf<List<FlightUi>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    val coroutineScope = rememberCoroutineScope()

    // -------------------- FETCH DASHBOARD DATA --------------------
    LaunchedEffect(identity, token) {
        coroutineScope.launch {
            isLoading = true
            try {
                val bearerToken = "Bearer $token"

                val dashboardResponse = ApiClient.authApi.getDashboard(
                    bearerToken,
                    identity
                )
                val dashboardFlights: List<DashboardFlight> =
                    dashboardResponse.body()?.data?.flights ?: emptyList()

                // save globally if needed
                flightsFromAPI = dashboardFlights

                val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                val outputTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                flights = dashboardFlights.map { flight ->
                    val departureDate = runCatching { inputFormat.parse(flight.departure_time ?: "") }.getOrNull()
                    val arrivalDate = runCatching { inputFormat.parse(flight.arrival_time ?: "") }.getOrNull()

                    FlightUi(
                        id = flight.id,
                        route = "${flight.from ?: "Unknown"} → ${flight.to ?: "Unknown"}",
                        date = flight.flight_date ?: "",
                        departureTime = departureDate?.let { outputTimeFormat.format(it) } ?: "",
                        arrivalTime = arrivalDate?.let { outputTimeFormat.format(it) } ?: "",
                        airline = flight.aircraft ?: "Unknown"
                    )
                }

            } catch (e: IOException) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } catch (e: HttpException) {
                errorMessage = "Server error: ${e.localizedMessage}"
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    // ... rest of UI

    // -------------------- UI --------------------
    Scaffold(
        topBar = {
            val userInitial = getFirstLetter(SessionManager.identity)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2B7AF5), Color(0xFF4EA1FF))
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // ---- LEFT TEXT ----
                        Column {
                            Text(
                                "Welcome back",
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                "Find your flight",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }

                        // ---- USER INITIAL (DYNAMIC) ----
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userInitial, // 🔥 HERE
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // ---- SEARCH BAR ----
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate("flightOneWay")
                            }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Search destination, flight number...",
                                color = Color.Gray
                            )
                        }
                    }


                }
            }
        },


    bottomBar = {
            BottomNavigation(backgroundColor = Color.White, contentColor = Color.Gray) {
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Booking,
                    BottomNavItem.Payment,
                    BottomNavItem.Profile
                )

                items.forEach { item ->
                    BottomNavigationItem(
                        selected = selectedTab == item,
                        onClick = {
                            selectedTab = item
                            when (item) {
                                BottomNavItem.Booking -> navController.navigate("booking")
                                BottomNavItem.Payment -> navController.navigate("payment")
                                BottomNavItem.Profile -> navController.navigate("profile")
                                else -> Unit
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selectedContentColor = Color(0xFF1B73E8),
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))
            Text("Recommended cities", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(bestCities) { city ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = city.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Text(city.name, fontWeight = FontWeight.Bold)
                        Text(
                            city.priceRange,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Upcoming flights for this week", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                errorMessage != null -> Text(errorMessage!!, color = MaterialTheme.colors.error)

                flights.isEmpty() -> Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No flights this week", color = Color.Gray)
                }

                else -> LazyColumn {
                    items(flights) { flight ->
                        FlightCard(flight, navController)
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

