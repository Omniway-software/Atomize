package com.infinitysoftware.atomize.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen

@Composable
fun SignInFragment(navController: NavHostController){
    var emailTextInput by remember { mutableStateOf("") }
    var passwordTextInput by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.sign_in_title),
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.sign_in_subtitle),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailTextInput,
                onValueChange = { emailTextInput = it },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(id = R.string.cd_email)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordTextInput,
                onValueChange = { passwordTextInput = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(id = R.string.cd_password)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        val visibilityIcon =
                            if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            imageVector = visibilityIcon,
                            contentDescription = stringResource(id = R.string.cd_toggle_password)
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: Handle sign-in */ },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(text = stringResource(id = R.string.sign_in_button))
            }
        }
    }
}