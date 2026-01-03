package com.example.login.ui.screens.FlightScreen

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




@Composable
fun TravellerOptionsSheet(
    initialText: String,
    onDone: (String) -> Unit
) {
    var adults by remember { mutableStateOf(1) }
    var children by remember { mutableStateOf(0) }
    var infants by remember { mutableStateOf(0) }

    var cabinClass by remember { mutableStateOf("Economy") }
    var flightType by remember { mutableStateOf("One-way") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Text("Options", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        // Traveller
        Text("Traveller", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))

        TravellerRow("Adults", "12+ years", adults, { if (adults > 1) adults-- }, { adults++ })
        TravellerRow("Children", "2-12 years", children, { if (children > 0) children-- }, { children++ })
        TravellerRow("Infants", "Under 2 years", infants, { if (infants > 0) infants-- }, { infants++ })

        Spacer(Modifier.height(20.dp))

        // Cabin class
        Text("Cabin Class", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))

        CabinOption("Economy", cabinClass == "Economy") { cabinClass = "Economy" }
        CabinOption("Business", cabinClass == "Business") { cabinClass = "Business" }
        CabinOption("First", cabinClass == "First") { cabinClass = "First" }

        Spacer(Modifier.height(20.dp))

        // Flight type
        Text("Flight Type", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))

        CabinOption("One-way", flightType == "One-way") { flightType = "One-way" }
        CabinOption("Round-trip", flightType == "Round-trip") { flightType = "Round-trip" }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                val totalTravellers = adults + children + infants
                val newSummary = "${totalTravellers} traveller • $cabinClass • $flightType"
                onDone(newSummary)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF2962FF))
        ) {
            Text("Done", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.height(40.dp))
    }
}
@Composable
fun TravellerRow(
    title: String,
    subtitle: String,
    count: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(Modifier.weight(1f))

        IconButton(onClick = onMinus) {
            Icon(Icons.Default.Remove, contentDescription = null)
        }

        Text("$count", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        IconButton(onClick = onPlus) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}
@Composable
fun CabinOption(title: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp)

        Spacer(Modifier.weight(1f))

        if (selected) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF2962FF)
            )
        }
    }
}
@Composable
fun BottomBookingBar(
    price: String,
    paymentMode: String
) {
    Surface(elevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text("Total price", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "$$price",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Payment: $paymentMode", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(50.dp)
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF2962FF))
            ) {
                Text(text = "BOOK", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
