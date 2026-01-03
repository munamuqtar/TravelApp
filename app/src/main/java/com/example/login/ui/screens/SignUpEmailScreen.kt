package com.example.login.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login.APIInterface.ApiClient
import com.example.login.APIModels.SignupRequest
import com.example.login.ui.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.net.Uri

@Composable
fun SignUpEmailScreen(
    navController: NavController,
    next: String? = null   // 🔑 MUHIIM
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(80.dp))
        Text("Travel Around Somalia", fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(50.dp))

        AuthTab(
            selected = "Email",
            onEmailClick = {},
            onPhoneClick = {
                navController.navigate("signupPhone?next=${Uri.encode(next ?: "")}")
            }
        )

        Spacer(Modifier.height(26.dp))
        NameTextField(value = name, onValueChange = { name = it })
        Spacer(Modifier.height(18.dp))
        EmailTextField(value = email, onValueChange = { email = it })
        Spacer(Modifier.height(18.dp))
        PasswordTextField(value = password, onValueChange = { password = it })
        Spacer(Modifier.height(30.dp))

        SignUpButton(onClick = {
            errorMessage = null

            // ✅ Validation
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                errorMessage = "Please fill all fields"
                return@SignUpButton
            }
            if (!email.contains("@")) {
                errorMessage = "Invalid email address"
                return@SignUpButton
            }
            if (password.length < 8) {
                errorMessage = "Password must be at least 8 characters"
                return@SignUpButton
            }

            scope.launch(Dispatchers.IO) {
                try {
                    val response = ApiClient.authApi.signupUser(
                        SignupRequest(name, email, password)
                    )

                    val body = response.body()

                    if (response.isSuccessful && body?.status == "success") {

                        val identity = body.data?.identity

                        if (identity.isNullOrEmpty()) {
                            withContext(Dispatchers.Main) {
                                errorMessage = "Identity missing from server"
                            }
                            return@launch
                        }

                        // 🔑 encode identity + next
                        val encodedIdentity = Uri.encode(identity)
                        val encodedNext = Uri.encode(next ?: "")

                        withContext(Dispatchers.Main) {
                            navController.navigate(
                                "verification/$encodedIdentity?next=$encodedNext"
                            ) {
                                popUpTo("signupEmail") { inclusive = true }
                            }
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            errorMessage = body?.error ?: "Signup failed"
                        }
                    }

                } catch (e: Exception) {
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

@Composable
fun EmailTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("you@example.com") },
        label = { Text("Email Address") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
