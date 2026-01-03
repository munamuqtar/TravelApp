package com.example.login.ui.screens

import android.net.Uri
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
import com.example.login.APIModels.LoginRequest
import com.example.login.SessionManager
import com.example.login.ui.components.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@Composable
fun LoginScreen(
    navController: NavController,
    next: String = ""
) {
    var identity by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(100.dp))

        Text(
            text = "Welcome Back",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(40.dp))

        AuthTab(
            selected = "Email",
            onEmailClick = {
                navController.navigate("signupEmail?next=$next")
            },
            onPhoneClick = {
                navController.navigate("signupPhone?next=$next")
            }
        )

        Spacer(Modifier.height(30.dp))

        IdentityTextField(
            value = identity,
            onValueChange = { identity = it }
        )

        Spacer(Modifier.height(20.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it }
        )

        Spacer(Modifier.height(40.dp))



        errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = Color.Red)
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = { /* forgot password */ }
            ) {
                Text("Forgot Password?", color = Color.Blue)
            }
        }

        Spacer(Modifier.height(80.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don’t have an account?", color = Color.Gray)
            TextButton(
                onClick = {
                    navController.navigate("signupEmail?next=$next")
                }
            ) {
                Text(" Sign up", color = Color.Blue)
            }
        }
    }
}

