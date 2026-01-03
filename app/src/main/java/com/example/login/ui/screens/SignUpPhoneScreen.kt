package com.example.login.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.APIInterface.ApiClient
import com.example.login.APIModels.MobileSignupRequest
import com.example.login.ui.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpPhoneScreen(
    navController: NavController,
    next: String? = null   // 🔑 MUHIIM
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(80.dp))
        Text(
            text = "Travel Around Somalia",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(50.dp))

        AuthTab(
            selected = "Phone",
            onEmailClick = {
                navController.navigate(
                    "signupEmail?next=${Uri.encode(next ?: "")}"
                ) {
                    launchSingleTop = true
                }
            },
            onPhoneClick = {}
        )

        Spacer(Modifier.height(26.dp))

        NameTextField(value = name, onValueChange = { name = it })
        Spacer(Modifier.height(18.dp))
        PhoneNumberField(value = phoneNumber, onValueChange = { phoneNumber = it })

        Spacer(Modifier.height(30.dp))

        SignUpButton(onClick = {
            errorMessage = null

            if (name.isBlank() || phoneNumber.isBlank()) {
                errorMessage = "Please fill in all fields."
                return@SignUpButton
            }

            scope.launch(Dispatchers.IO) {
                try {
                    val response = ApiClient.authApi.signupWithMobile(
                        MobileSignupRequest(
                            name = name.trim(),
                            mobile = phoneNumber.trim()
                        )
                    )

                    val body = response.body()

                    if (response.isSuccessful && body?.status == "success") {

                        val identity = body.data?.identity

                        if (identity.isNullOrEmpty()) {
                            withContext(Dispatchers.Main) {
                                errorMessage = "Identity missing from server."
                            }
                            return@launch
                        }

                        // 🔑 encode identity & next
                        val encodedIdentity = Uri.encode(identity)
                        val encodedNext = Uri.encode(next ?: "")

                        withContext(Dispatchers.Main) {
                            navController.navigate(
                                "verification/$encodedIdentity?next=$encodedNext"
                            ) {
                                popUpTo("signupPhone") { inclusive = true }
                            }
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            errorMessage = body?.error ?: "Signup failed."
                        }
                    }

                } catch (e: Exception) {
                    Log.e("SignupPhone", "Exception", e)
                    withContext(Dispatchers.Main) {
                        errorMessage = "Network error: ${e.localizedMessage}"
                    }
                }
            }
        })

        errorMessage?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = Color.Red)
        }

        Spacer(Modifier.height(100.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account?", color = Color.Gray)
            TextButton(onClick = { navController.navigate("login") }) {
                Text(" Sign in", color = Color.Blue)
            }
        }
    }
}
