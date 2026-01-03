package com.example.login.ui.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login.R
import androidx.compose.ui.graphics.vector.ImageVector




@Composable
fun AuthTab(
    selected: String,
    onEmailClick: () -> Unit,
    onPhoneClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F1F1)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem("Email", selected == "Email", onEmailClick)
        TabItem("Phone", selected == "Phone", onPhoneClick)
    }
}

@Composable
fun RowScope.TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color.White else Color.Transparent
    val textColor = if (isSelected) Color.Black else Color.Gray

    Box(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = textColor, modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun IdentityTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter your identity") },
        label = { Text("Identity (Email or Username)") },
        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = "Identity Icon") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun PasswordTextField(value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter your password") },
        label = { Text("Password") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
        trailingIcon = {
            val visibilityIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(visibilityIcon, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun PhoneNumberField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter your phone number") },
        label = { Text("Phone") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone Icon") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun NameTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter your name") },
        label = { Text("Name") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3B70D9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Lock, contentDescription = "Login Icon", tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text("Login", color = Color.White)
    }
}

@Composable
fun SignUpButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3B70D9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.PersonAdd, contentDescription = "Sign Up Icon", tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text("Sign Up", color = Color.White)
    }
}

@Composable
fun FlightInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(if (readOnly) Modifier.clickable { onClick() } else Modifier)
        )
    }
}




@Composable
fun RoundedDateField(
    label: String,
    date: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF0F0F0)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Icon(icon, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (date.isEmpty()) "" else date,
                fontSize = 16.sp,
                color = if (date.isEmpty()) Color.Gray else Color.Black
            )
        }
    }
}


@Composable
fun ClickableFlightField(
    value: String,
    placeholder: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            enabled = false, // 🔥 VERY IMPORTANT
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(icon, contentDescription = null)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledLeadingIconColor = Color.Gray,
                disabledPlaceholderColor = Color.Gray
            )
        )
    }
}

@Composable
fun ClickableDateField(
    label: String,
    date: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            leadingIcon = {
                Icon(icon, contentDescription = null)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledLeadingIconColor = Color.Gray
            )
        )
    }
}


