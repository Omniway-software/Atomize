// Modified SignupScreen with password confirmation
package com.infinitysoftware.atomize.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.theme.*
import com.infinitysoftware.atomize.viewmodel.AuthState
import com.infinitysoftware.atomize.viewmodel.AuthViewModel

@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel) {
    val constants = Constants()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = constants.surfaceVariantAlpha)
                )
            )
        )) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(constants.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sign_up_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(constants.titlePadding)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(constants.authGlobalPadding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = stringResource(id = R.string.sign_up_title), fontSize = constants.authTitleFontSize)
                Spacer(modifier = Modifier.height(constants.authSpacerDefaultWidth))
                Text(text = stringResource(id = R.string.sign_up_subtitle), fontSize = constants.authSubtitleFontSize)
                Spacer(modifier = Modifier.height(constants.authSpacerLargeDefaultWidth))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.email_label)) },
                    singleLine = true,
                    shape = RoundedCornerShape(constants.authEntryFieldDefaultShape),
                    leadingIcon = { Icon(Icons.Default.Email, stringResource(id = R.string.cd_email)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(constants.authSpacerDefaultWidth))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = stringResource(id = R.string.password_label)) },
                    singleLine = true,
                    shape = RoundedCornerShape(constants.authEntryFieldDefaultShape),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(id = R.string.cd_password)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            val visibilityIcon = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            Icon(imageVector = visibilityIcon, contentDescription = stringResource(id = R.string.cd_toggle_password))
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(constants.authSpacerDefaultWidth))

                var confirmPassword by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(text = "Confirm Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(constants.authEntryFieldDefaultShape),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(id = R.string.cd_password))
                    },
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            val visibilityIcon = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            Icon(imageVector = visibilityIcon, contentDescription = stringResource(id = R.string.cd_toggle_password))
                        }
                    },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(constants.authSpacerLargeDefaultWidth))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.signup(email, password)
                            email = ""
                            password = ""
                            confirmPassword = ""
                        }
                    },
                    enabled = authState.value != AuthState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(stringResource(id = R.string.sign_up_button))
                }
            }
        }
    }
}
