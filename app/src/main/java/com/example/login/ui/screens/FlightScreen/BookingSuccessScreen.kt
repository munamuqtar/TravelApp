package com.example.login.ui.screens.FlightScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.R

@Composable
fun BookingSuccessScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top airplane image
        Image(
            painter = painterResource(id = R.drawable.botswana), // replace with your top image
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        // White Card below image
        Card(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier
                .fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Success Icon
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )

                // Title
                Text(
                    "Booking successful",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                )

                // Flight route info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LCY", style = MaterialTheme.typography.titleLarge)
                        Text("Tue, Jul 14", style = MaterialTheme.typography.bodySmall)
                    }
                    Text("⇄", style = MaterialTheme.typography.titleLarge)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("JFK", style = MaterialTheme.typography.titleLarge)
                        Text("Fri, Jul 17", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Divider()

                // Traveller details row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Traveller", style = MaterialTheme.typography.bodySmall)
                        Text("Pedro Moreno", style = MaterialTheme.typography.bodyMedium)
                    }
                    Column {
                        Text("Class", style = MaterialTheme.typography.bodySmall)
                        Text("Economy", style = MaterialTheme.typography.bodyMedium)
                    }
                    Column {
                        Text("Flight", style = MaterialTheme.typography.bodySmall)
                        Text("Round-trip", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Price
                Text(
                    "$811.56",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Booking Detail button
                Button(
                    onClick = { /* TODO: Go to booking detail */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Booking detail")
                }

                // Home button (text only, like screenshot)
                TextButton(
                    onClick = {
                        navController.navigate("DashboardScreen")
                    }
                     ) {
                    Text("Home")
                }
            }
        }
    }
}
