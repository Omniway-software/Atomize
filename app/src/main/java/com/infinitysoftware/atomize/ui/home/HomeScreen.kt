package com.infinitysoftware.atomize.ui.home

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.model.habit.Habit
import com.infinitysoftware.atomize.ui.home.composables.AppBarComposable
import com.infinitysoftware.atomize.ui.home.composables.CalendarComposable
import com.infinitysoftware.atomize.ui.home.composables.ListComposable
import com.infinitysoftware.atomize.ui.settings.SettingsViewModel
import com.infinitysoftware.atomize.ui.theme.DarkGray
import com.infinitysoftware.atomize.ui.theme.LightGreen
import com.infinitysoftware.atomize.ui.theme.MediumGray
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen
import com.infinitysoftware.atomize.ui.theme.White
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    onMenuClick: () -> Unit = {}
) {
    val settings by settingsViewModel.settings.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var currentMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var currentYear by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    val todayString = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${
            String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)
        }-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
    }

    var selectedDate by remember { mutableStateOf(todayString) }

    val monthYearText by remember(currentMonth, currentYear) {
        mutableStateOf(
            Calendar.getInstance().apply {
                set(Calendar.MONTH, currentMonth)
                set(Calendar.YEAR, currentYear)
            }.let { cal ->
                SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)
            }
        )
    }

    val viewModel: HabitViewModel = viewModel()
    val calendarState by viewModel.calendarState.collectAsState()

    val currentCount = calendarState.habitsByDate[selectedDate]?.size ?: 0
    val isPastDate = selectedDate < todayString
    val canCreateHabit = !isPastDate

    val constants = Constants()

    val onMonthDecrement: () -> Unit = {
        if (currentMonth == 0) {
            currentMonth = 11
            currentYear -= 1
        } else {
            currentMonth -= 1
        }
    }

    val onMonthIncrement: () -> Unit = {
        if (currentMonth == 11) {
            currentMonth = 0
            currentYear += 1
        } else {
            currentMonth += 1
        }
    }

    val onAddHabitClick: () -> Unit = {
        showDialog = true
    }

    val onResetToToday: () -> Unit = {
        val today = Calendar.getInstance()
        currentMonth = today.get(Calendar.MONTH)
        currentYear = today.get(Calendar.YEAR)
        selectedDate = todayString
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBarComposable(
            currentMonth = currentMonth,
            currentYear = currentYear,
            currentCount = currentCount,
            canCreateHabit = canCreateHabit,
            monthYearText = monthYearText,
            onMenuClick = onMenuClick,
            onMonthDecrement = onMonthDecrement,
            onMonthIncrement = onMonthIncrement,
            onAddHabitClick = onAddHabitClick,
            onResetToToday = onResetToToday
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CalendarComposable(
                currentMonth = currentMonth,
                currentYear = currentYear,
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date }
            )
        }

        if (currentCount < 1) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ListComposable(
                    selectedDate = selectedDate,
                    showStreak = settings.showStreak,
                    animatedIcon = settings.animatedIcon
                )

                if (showDialog && canCreateHabit) {
                    CreateNewHabitDialog(
                        selectedDate = selectedDate,
                        onDismiss = { showDialog = false }
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (canCreateHabit) {
                        Box(
                            modifier = Modifier
                                .size(constants.createHabitButtonBoxSize)
                                .background(
                                    color = PrimaryGreen,
                                    shape = RoundedCornerShape(constants.createHabitButtonCornerRadius)
                                )
                                .clickable { showDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = constants.createHabitButtonTextSize, color = Color.White)
                        }
                        Spacer(modifier = Modifier.size(constants.spacerSmall))
                        Text(stringResource(R.string.home_create_new_habit), fontSize = constants.createHabitLabelTextSize)
                    } else {
                        Text(
                            stringResource(R.string.home_no_habits),
                            fontSize = constants.noHabitsTextSize,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            Box {
                ListComposable(
                    selectedDate = selectedDate,
                    showStreak = settings.showStreak,
                    animatedIcon = settings.animatedIcon
                )
                if (showDialog && canCreateHabit) {
                    CreateNewHabitDialog(
                        selectedDate = selectedDate,
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun MoreOptionsItemMenu(onEdit: () -> Unit, onDelete: () -> Unit){
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.cd_more_options)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dialog_edit)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.cd_edit)
                    )
                },
                onClick = {
                    onEdit()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dialog_delete)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cd_delete)
                    )
                },
                onClick = {
                    onDelete()
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun CreateNewHabitDialog(selectedDate: String, onDismiss: () -> Unit) {
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
    val habitLimit by remember { mutableIntStateOf(5) }
    val timeText = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)

    val constants = Constants()

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(size = constants.dialogCornerRadius),
            modifier = Modifier
                .padding(all = constants.dialogPadding)
                .fillMaxWidth()
                .heightIn(min = constants.dialogMinHeight),
            elevation = CardDefaults.cardElevation(constants.dialogCardElevation),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
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
                                border = BorderStroke(width = constants.dayButtonBorderWidth, color = Color(color = 0xFFAAAAAA))
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
                                    uncheckedTrackColor = Color.White,
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
                                notificationsEnabled = notificationsEnabled
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

@Composable
fun EditHabitDialog(habit: Habit, onDismiss: () -> Unit, onConfirm: (String, List<String>, String?, Boolean) -> Unit) {
    var text by remember { mutableStateOf(value = habit.text) }
    var notificationsEnabled by remember { mutableStateOf(habit.notificationsEnabled) }
    val constants = Constants()
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
    val selectedDays = remember { mutableStateListOf<Boolean>().apply { addAll(dayCodes.map { habit.days.contains(it) }) } }
    val context = LocalContext.current
    val initialHourMinute = remember(habit.notifyTime) {
        val parts = habit.notifyTime?.split(":")
        val h = parts?.getOrNull(0)?.toIntOrNull() ?: constants.notificationDefaultHour
        val m = parts?.getOrNull(1)?.toIntOrNull() ?: constants.notificationDefaultMinute
        h to m
    }
    var selectedHour by remember { mutableIntStateOf(initialHourMinute.first) }
    var selectedMinute by remember { mutableIntStateOf(initialHourMinute.second) }
    val timeText = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(size = constants.dialogCornerRadius),
            modifier = Modifier
                .padding(all = constants.dialogPadding)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(constants.dialogCardElevation),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = constants.dialogInnerPadding)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.dialog_edit_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(height = constants.spacerSmall))
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text(text = stringResource(R.string.dialog_habit_name)) }, singleLine = true)
                Spacer(modifier = Modifier.height(height = constants.spacerMedium))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    dayLabels.forEachIndexed { index, day ->
                        OutlinedButton(
                            onClick = { selectedDays[index] = !selectedDays[index] },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selectedDays[index]) LightGreen else Color.Transparent),
                            shape = CircleShape,
                            contentPadding = PaddingValues(all = 0.dp),
                            modifier = Modifier.size(size = constants.dayButtonSize),
                            border = BorderStroke(width = constants.dayButtonBorderWidth, color = Color(color = 0xFFAAAAAA))
                        ) { Text(text = day, color = if (selectedDays[index]) White else DarkGray) }
                    }
                }
                Spacer(modifier = Modifier.height(height = constants.spacerMedium))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.dialog_notifications), style = MaterialTheme.typography.bodyLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                uncheckedThumbColor = PrimaryGreen,
                                uncheckedTrackColor = Color.White,
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) { Text(text = stringResource(R.string.dialog_dismiss)) }
                    Spacer(modifier = Modifier.width(width = constants.spacerSmall))
                    Button(onClick = {
                        val selected = dayCodes.filterIndexed { index, _ -> selectedDays[index] }
                        onConfirm(text, selected, if (notificationsEnabled) timeText else null, notificationsEnabled)
                        onDismiss()
                    }) { Text(text = stringResource(R.string.dialog_save)) }
                }
            }
        }
    }
}

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
            colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(constants.spacerMedium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
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
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(stringResource(R.string.dialog_delete))
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedIcon(state: Boolean = false) {
    val constants = Constants()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fire))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = constants.animationSpeed
    )
    LottieAnimation(
        composition = composition,
        progress = if (state) progress else constants.animationProgress,
        modifier = Modifier.size(size = constants.animatedIconSize)
    )
}

@Composable
fun Streak(streak: Int, state: Boolean, animatedIcon: Boolean = true) {
    val constants = Constants()
    val streakThreshold by remember { mutableIntStateOf(0) }
    val tintColor = if (streak > streakThreshold) Color.Red else Color.Red.copy(alpha = constants.streakTintColorAlpha)
    if (animatedIcon && streak == streakThreshold) return
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when {
            animatedIcon && streak > 0 -> {
                AnimatedIcon(state)
            }
            !animatedIcon -> {
                Icon(
                    painter = painterResource(id = R.drawable.fire_flame_64),
                    contentDescription = stringResource(R.string.cd_streak),
                    modifier = Modifier.size(constants.streakIconSize),
                    tint = tintColor
                )
            }
        }
        Text(
            text = "$streak",
            fontSize = constants.streakTextSize
        )
    }
}