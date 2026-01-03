package com.example.login.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.APIInterface.ApiClient
import com.example.login.APIModels.VerificationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

@Composable
fun VerificationScreen(
    navController: NavController,
    identity: String,
    next: String? = null,
    travellers: Int = 1,
    price: Double = 0.0,
    token: String = ""
) {
    val decodedIdentity = remember(identity) { Uri.decode(identity) }
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        IconButton(
            enabled = !isLoading,
            onClick = { navController.popBackStack() }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Verify OTP",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6 && it.all(Char::isDigit)) {
                    otp = it
                }
            },
            label = { Text("OTP") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF3B70D9)
            ),
            onClick = {

                if (otp.length != 6) {
                    errorMessage = "OTP must be 6 digits"
                    return@Button
                }

                isLoading = true
                errorMessage = null

                scope.launch(Dispatchers.IO) {
                    try {
                        val response = ApiClient.authApi.verifyEmailCode(
                            VerificationRequest(
                                identity = decodedIdentity,
                                otp = otp
                            )
                        )

                        val body = response.body()

                        withContext(Dispatchers.Main) {
                            isLoading = false

                            if (response.isSuccessful && body?.status == "success") {

                                val token = body.data?.token?.toString()
                                val verifiedIdentity = body.data?.identity?.toString()

                                if (!token.isNullOrBlank() && !verifiedIdentity.isNullOrBlank()) {

                                    val encodedToken = Uri.encode(token)
                                    val encodedIdentity = Uri.encode(verifiedIdentity)

                                    val finalRoute = Uri.decode(next ?: "")

                                    navController.navigate(
                                        finalRoute.replace(
                                            "identity=&token=",
                                            "identity=$encodedIdentity&token=$encodedToken"
                                        )
                                    ) {
                                        popUpTo("verification/{identity}?next={next}") {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }


                                } else {
                                    errorMessage = "Missing token or identity from server"
                                }

                            } else {
                                errorMessage = body?.error ?: "Invalid or expired OTP"
                            }
                        }

                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Unexpected error"
                    }
                }
            }
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Continue", color = Color.White)
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, color = Color.Red)
        }
    }
}
