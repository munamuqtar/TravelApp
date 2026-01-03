package com.example.login.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.ui.components.ClickableDateField
import com.example.login.ui.components.ClickableFlightField

@Composable
fun FlightOneWayScreen(
    navController: NavController,
    token: String,
    identity: String
) {

    // 🔹 Tabs state (FIXED)
    val tabs = listOf("One-way", "Round-trip")
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    // 🔹 Form state
    var from by rememberSaveable { mutableStateOf("") }
    var to by rememberSaveable { mutableStateOf("") }
    var departureDate by rememberSaveable { mutableStateOf("Select date") }

    // 🔹 Receive values from Select screens
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    savedStateHandle?.get<String>("selectedFrom")?.let {
        from = it
        savedStateHandle.remove<String>("selectedFrom")
    }

    savedStateHandle?.get<String>("selectedTo")?.let {
        to = it
        savedStateHandle.remove<String>("selectedTo")
    }

    savedStateHandle?.get<String>("selectedDate")?.let {
        departureDate = it
        savedStateHandle.remove<String>("selectedDate")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        // -------------------------------------------------------
        // 🔵 Top Bar
        // -------------------------------------------------------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Flight",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        // -------------------------------------------------------
        // 🔵 Tabs (NOW WORKING)
        // -------------------------------------------------------
        TabRow(
            selectedTabIndex = selectedTab,
            backgroundColor = Color.Transparent,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(2.dp)
                        .background(Color.Black)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index)
                                FontWeight.Bold
                            else
                                FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // -------------------------------------------------------
        // 🔵 FROM
        // -------------------------------------------------------
        ClickableFlightField(
            value = from,
            placeholder = "From",
            icon = Icons.Default.FlightTakeoff
        ) {
            navController.navigate("selectFrom")
        }

        Spacer(Modifier.height(12.dp))

        // -------------------------------------------------------
        // 🔵 TO
        // -------------------------------------------------------
        ClickableFlightField(
            value = to,
            placeholder = "To",
            icon = Icons.Default.FlightLand
        ) {
            navController.navigate("selectTo")
        }

        Spacer(Modifier.height(12.dp))

        // -------------------------------------------------------
        // 🔵 DATE
        // -------------------------------------------------------
        ClickableDateField(
            label = "Departure",
            date = departureDate,
            icon = Icons.Default.DateRange
        ) {
            navController.navigate("selectDate")
        }

        Spacer(Modifier.weight(1f))

        // -------------------------------------------------------
        // 🔵 SEARCH BUTTON
        // -------------------------------------------------------
        Button(
            onClick = {
                navController.navigate(
                    "searchResults/" +
                            "${Uri.encode(from)}/" +
                            "${Uri.encode(to)}/" +
                            "${Uri.encode(departureDate)}"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = from.isNotBlank() && to.isNotBlank()
        ) {
            Text("Search flights", fontSize = 16.sp)
        }

    }
}
