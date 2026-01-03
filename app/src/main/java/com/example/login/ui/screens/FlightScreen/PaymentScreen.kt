package com.example.login.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun PaymentScreen(
    navController: NavController,
    count: Int,
    price: Double,
    identity: String,
    token: String
) {

    var selectedMethod by remember { mutableStateOf("Card") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {

        // ---------- Header ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Payment",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ------------ Fare Summary ------------
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Fare Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Flight Price")
                    Text("$120")
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Taxes & Fees")
                    Text("$18")
                }

                Divider(Modifier.padding(vertical = 12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("$138", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ------------ Payment Methods ------------
        Text("Select Payment Method", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PaymentOptionItem(
                icon = Icons.Default.CreditCard,
                label = "Card",
                selected = selectedMethod == "Card"
            ) { selectedMethod = "Card" }

            PaymentOptionItem(
                icon = Icons.Default.PhoneAndroid,
                label = "Mobile Money",
                selected = selectedMethod == "Mobile"
            ) { selectedMethod = "Mobile" }
        }

        Spacer(Modifier.height(24.dp))

        // ------------ Dynamic Payment Fields ------------
        if (selectedMethod == "Card") {
            CardPaymentUI(
                cardNumber, { cardNumber = it },
                expiry, { expiry = it },
                cvv, { cvv = it }
            )
        } else {
            MobileMoneyUI(
                phoneNumber,
                onPhoneChange = { phoneNumber = it }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ------------ Pay Now Button ------------
        Button(
            onClick = {
                // Payment action
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0A84FF)),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Pay Now", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun PaymentOptionItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(130.dp)
            .clickable { onClick() }
            .background(
                if (selected) Color(0xFF0A84FF).copy(alpha = 0.1f) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color(0xFF0A84FF), modifier = Modifier.size(32.dp))
        Spacer(Modifier.height(8.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun CardPaymentUI(
    number: String, onNumberChange: (String) -> Unit,
    expiry: String, onExpiryChange: (String) -> Unit,
    cvv: String, onCvvChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = number,
            onValueChange = onNumberChange,
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = expiry,
                onValueChange = onExpiryChange,
                label = { Text("Expiry (MM/YY)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.width(12.dp))

            OutlinedTextField(
                value = cvv,
                onValueChange = onCvvChange,
                label = { Text("CVV") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun MobileMoneyUI(phone: String, onPhoneChange: (String) -> Unit) {
    Column {
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
    }
}
