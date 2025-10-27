package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.model.habit.Habit
import com.infinitysoftware.atomize.ui.home.Constants
import com.infinitysoftware.atomize.ui.theme.*

@Composable
fun DeleteHabitDialog(habit: Habit, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val constants = Constants()

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(constants.dialogCornerRadius),
            modifier = Modifier
                .padding(constants.dialogPadding)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(constants.dialogCardElevation),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .padding(constants.dialogInnerPadding)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.dialog_delete_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(constants.spacerSmall))
                Text(
                    text = "${habit.text}?",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Orange
                )
                Spacer(modifier = Modifier.height(constants.spacerMedium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = White,
                            contentColor = PrimaryGreen
                        )
                    ) {
                        Text(stringResource(R.string.dialog_dismiss))
                    }
                    Spacer(modifier = Modifier.width(width = constants.spacerSmall))
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = White
                        )
                    ) {
                        Text(stringResource(R.string.dialog_delete))
                    }
                }
            }
        }
    }
}