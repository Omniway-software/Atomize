package com.infinitysoftware.atomize.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen

@Composable
fun SignInFragment(navController: NavHostController){
    var emailTextInput by remember { mutableStateOf(value = "") }
    var passwordTextInput by remember { mutableStateOf(value = "") }
    var showPassword by remember { mutableStateOf(value = false) }

    Box(modifier = Modifier.fillMaxSize().padding(all = 32.dp), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Sign-in", fontSize = 32.sp)
            Spacer(modifier = Modifier.size(size = 8.dp))
            Text(text = "Please fill the form to sign-in!", fontSize = 20 .sp)
            Spacer(modifier = Modifier.size(size = 8.dp))
            OutlinedTextField(
                value = emailTextInput,
                onValueChange = { emailTextInput = it },
                label = { Text(text = "Email") },
                singleLine = true,
                shape = RoundedCornerShape(size = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email icon"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = passwordTextInput,
                onValueChange = { passwordTextInput = it },
                label = { Text(text = "Password") },
                singleLine = true,
                shape = RoundedCornerShape(size = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        val visibilityIcon = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(imageVector = visibilityIcon, contentDescription = "Toggle password visibility")
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(size = 8.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { TODO() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(text = "Sign-in")
            }
        }
    }
}