package com.example.login.ui.screens.FlightScreen

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Calendar
import androidx.compose.material.icons.filled.DateRange


// -------- DATA MODEL --------
data class TravellerForm(
    var passengerTitle: String = "",
    var name: String = "",
    var birthDate: String = "",
    var mobileNumber: String = "",
    var passport: String = "",
    var country: String = ""
)

@Composable
fun TravellerInformationScreen(
    navController: NavController,
    travellers: Int,
    price: Double,
    identity: String,
    token: String
) {

    val formData = remember {
        mutableStateListOf<TravellerForm>().apply {
            repeat(travellers) { add(TravellerForm()) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Traveller information") })
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "$${"%.2f".format(price)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        navController.navigate(
                            "payment/$travellers/$price?identity=$identity&token=$token"
                        )
                    },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF2962FF))
                ) {
                    Text("Payment", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            itemsIndexed(formData) { index, traveller ->

                // -------- HEADER --------
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Traveller ${index + 1}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                // -------- PASSENGER TITLE (DROPDOWN) --------
                PassengerTitleDropdown(traveller.passengerTitle) {
                    formData[index] = formData[index].copy(passengerTitle = it)
                }

                // -------- NAME --------
                RoundedInput("Full name", traveller.name) {
                    formData[index] = formData[index].copy(name = it)
                }

                // -------- BIRTH DATE (DATE PICKER) --------
                BirthDatePicker(traveller.birthDate) {
                    formData[index] = formData[index].copy(birthDate = it)
                }

                // -------- MOBILE NUMBER --------
                RoundedInput("Mobile number", traveller.mobileNumber) {
                    formData[index] = formData[index].copy(mobileNumber = it)
                }

                // -------- PASSPORT --------
                RoundedInput("Passport number", traveller.passport) {
                    formData[index] = formData[index].copy(passport = it)
                }

                // -------- COUNTRY --------
                RoundedInput("Country", traveller.country) {
                    formData[index] = formData[index].copy(country = it)
                }

                Spacer(Modifier.height(25.dp))
                Divider()
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}

// -------- PASSENGER TITLE DROPDOWN --------
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PassengerTitleDropdown(
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Mr", "Ms", "Mrs")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Passenger title") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { title ->
                DropdownMenuItem(onClick = {
                    onSelect(title)
                    expanded = false
                }) {
                    Text(title)
                }
            }
        }
    }
}

// -------- BIRTH DATE PICKER --------
@Composable
fun BirthDatePicker(
    value: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val formattedDate =
                "%04d-%02d-%02d".format(year, month + 1, day)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text("Birth date") },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { datePickerDialog.show() }
    )
}

// -------- NORMAL INPUT --------
@Composable
fun RoundedInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}
