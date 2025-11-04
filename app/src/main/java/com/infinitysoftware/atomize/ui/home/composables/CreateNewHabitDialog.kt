package com.infinitysoftware.atomize.ui.home.composables

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.home.Constants
import com.infinitysoftware.atomize.ui.theme.*
import com.infinitysoftware.atomize.viewmodel.AuthViewModel
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.util.Locale

@Composable
fun CreateNewHabitDialog(selectedDate: String, authViewModel: AuthViewModel, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf(value = "") }
    val viewModel: HabitViewModel = viewModel()
    val calendarState by viewModel.calendarState.collectAsState()
    val currentCount = calendarState.habitsByDate[selectedDate]?.size ?: 0
    val context = LocalContext.current
    val dayCodes = listOf("sun","mon","tue","wed","thu","fri","sat")
    val dayLabels = listOf(
        stringResource(R.string.day_sun),
        stringResource(R.string.day_mon),
        stringResource(R.string.day_tue),
        stringResource(R.string.day_wed),
        stringResource(R.string.day_thu),
        stringResource(R.string.day_fri),
        stringResource(R.string.day_sat)
    )
    var notificationsEnabled by remember { mutableStateOf(true) }
    val selectedDays = remember { mutableStateListOf(false, false, false, false, false, false, false) }
    var selectedHour by remember { mutableIntStateOf(14) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    val constants = Constants()
    val habitLimit = authViewModel.getHabitLimit()
    val timeText = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(size = constants.dialogCornerRadius),
            modifier = Modifier
                .padding(all = constants.dialogPadding)
                .fillMaxWidth()
                .heightIn(min = constants.dialogMinHeight),
            elevation = CardDefaults.cardElevation(constants.dialogCardElevation),
            colors = CardDefaults.cardColors(
                containerColor = White
            )) {
            Column(
                modifier = Modifier
                    .padding(all = constants.dialogInnerPadding)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.dialog_create_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = constants.spacerSmall))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = stringResource(R.string.dialog_enter_habit)) },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(height = constants.spacerMedium))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        dayLabels.forEachIndexed { index, day ->
                            OutlinedButton(
                                onClick = { selectedDays[index] = !selectedDays[index] },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selectedDays[index]) LightGreen else Color.Transparent),
                                shape = CircleShape,
                                contentPadding = PaddingValues(all = 0.dp),
                                modifier = Modifier.size(size = constants.dayButtonSize),
                                border = BorderStroke(width = constants.dayButtonBorderWidth, color = MediumGray)
                            ) { Text(text = day, color = if (selectedDays[index]) White else DarkGray) }
                        }
                    }
                    Spacer(modifier = Modifier.height(height = constants.spacerMedium))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.dialog_notifications), style = MaterialTheme.typography.bodyLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    uncheckedThumbColor = PrimaryGreen,
                                    uncheckedTrackColor = White,
                                    uncheckedBorderColor = PrimaryGreen
                                ))
                            Text(
                                text = timeText,
                                modifier = Modifier
                                    .padding(start = constants.spacerSmall)
                                    .clickable(enabled = notificationsEnabled) {
                                        TimePickerDialog(
                                            context,
                                            { _, hourOfDay, minute ->
                                                selectedHour = hourOfDay
                                                selectedMinute = minute
                                            },
                                            selectedHour,
                                            selectedMinute,
                                            true
                                        ).show()
                                    },
                                color = if (notificationsEnabled) DarkGray else MediumGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(height = constants.spacerMedium))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = stringResource(R.string.dialog_dismiss))
                        }
                        Spacer(modifier = Modifier.width(width = constants.spacerSmall))
                        Button(onClick = {
                            if (text.isBlank()) return@Button
                            if (currentCount >= habitLimit) {
                                Toast.makeText(context, context.getString(R.string.toast_max_habits), Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val selected = dayCodes.filterIndexed { index, _ -> selectedDays[index] }
                            val time: String? = if (notificationsEnabled) timeText else null
                            viewModel.createHabitPersisted(
                                date = selectedDate,
                                habitText = text,
                                days = selected,
                                notifyTime = time,
                                notificationsEnabled = notificationsEnabled,
                                maxLimit = habitLimit
                            )
                            onDismiss()
                        }) {
                            Text(text = stringResource(R.string.dialog_create))
                        }
                    }
                }
            }
        }
    }
}