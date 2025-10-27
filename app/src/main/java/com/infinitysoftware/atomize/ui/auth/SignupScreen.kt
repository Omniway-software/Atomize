package com.infinitysoftware.atomize.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.theme.*

@Composable
fun SignupScreen(navController: NavController) {
    val constants = Constants()

    var emailTextInput by remember { mutableStateOf("") }
    var passwordTextInput by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

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
                .padding(all = constants.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sign_up_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = constants.titlePadding)
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(all = constants.authGlobalPadding),
            contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = stringResource(id = R.string.sign_up_title),
                    fontSize = constants.authTitleFontSize
                )
                Spacer(modifier = Modifier.height(constants.authSpacerDefaultWidth))
                Text(
                    text = stringResource(id = R.string.sign_up_subtitle),
                    fontSize = constants.authSubtitleFontSize
                )
                Spacer(modifier = Modifier.height(constants.authSpacerLargeDefaultWidth))

                OutlinedTextField(
                    value = emailTextInput,
                    onValueChange = { emailTextInput = it },
                    label = { Text(text = stringResource(id = R.string.email_label)) },
                    singleLine = true,
                    shape = RoundedCornerShape(constants.authEntryFieldDefaultShape),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = stringResource(id = R.string.cd_email)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(constants.authSpacerDefaultWidth))

                OutlinedTextField(
                    value = passwordTextInput,
                    onValueChange = { passwordTextInput = it },
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

                Spacer(modifier = Modifier.height(constants.authSpacerLargeDefaultWidth))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO: Handle sign-in */ },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(text = stringResource(id = R.string.sign_up_button))
                }
            }
        }
    }
}