package com.infinitysoftware.atomize.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.home.composables.AppBarComposable
import com.infinitysoftware.atomize.ui.home.composables.CalendarComposable
import com.infinitysoftware.atomize.ui.home.composables.CreateNewHabitDialog
import com.infinitysoftware.atomize.ui.home.composables.ListComposable
import com.infinitysoftware.atomize.ui.settings.SettingsViewModel
import com.infinitysoftware.atomize.ui.theme.*
import com.infinitysoftware.atomize.viewmodel.AuthState
import com.infinitysoftware.atomize.viewmodel.AuthViewModel
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
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

    val habitLimit = authViewModel.getHabitLimit()
    val canCreateHabit = !isPastDate && currentCount < habitLimit

    val constants = Constants()

//    val authState = authViewModel.authState.observeAsState()
//
//    LaunchedEffect(authState.value) {
//        if (authState.value == AuthState.Unauthenticated) {
//            navController.navigate("login")
//        } else Unit
//    }

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
            habitLimit = habitLimit,
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
                        authViewModel = authViewModel,
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
                            Text("+", fontSize = constants.createHabitButtonTextSize, color = White)
                        }
                        Spacer(modifier = Modifier.size(constants.spacerSmall))
                        Text(stringResource(R.string.home_create_new_habit), fontSize = constants.createHabitLabelTextSize)
                    } else {
                        Text(
                            stringResource(R.string.home_no_habits),
                            fontSize = constants.noHabitsTextSize,
                            color = MediumGray
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
                        authViewModel = authViewModel,
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}