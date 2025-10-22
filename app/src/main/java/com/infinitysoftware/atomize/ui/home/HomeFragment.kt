package com.infinitysoftware.atomize.ui.home

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.model.Habit
import com.infinitysoftware.atomize.ui.theme.ActivityLevel1
import com.infinitysoftware.atomize.ui.theme.ActivityLevel2
import com.infinitysoftware.atomize.ui.theme.ActivityLevel3
import com.infinitysoftware.atomize.ui.theme.ActivityLevel4
import com.infinitysoftware.atomize.ui.theme.ActivityLevel5
import com.infinitysoftware.atomize.ui.theme.DarkGray
import com.infinitysoftware.atomize.ui.theme.LightGreen
import com.infinitysoftware.atomize.ui.theme.MediumGray
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen
import com.infinitysoftware.atomize.ui.theme.White
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Home (Screen) Fragment.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFragment(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(value = false) }
    var currentMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var currentYear by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    val todayString = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)}-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
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

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.navigate("auth") {
                            popUpTo("home") { inclusive = false }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(weight = 1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val today = Calendar.getInstance()
                                currentMonth = today.get(Calendar.MONTH)
                                currentYear = today.get(Calendar.YEAR)
                                selectedDate = todayString
                            }
                    ) {
                        Text(
                            text = monthYearText,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Row {
                        Box(
                            modifier = Modifier
                                .size(size = 40.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {
                                if (currentMonth == 0) {
                                    currentMonth = 11
                                    currentYear -= 1
                                } else {
                                    currentMonth -= 1
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                        Spacer(Modifier.size(size = 8.dp))
                        Box(
                            modifier = Modifier
                                .size(size = 40.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {
                                if (currentMonth == 11) {
                                    currentMonth = 0
                                    currentYear += 1
                                } else {
                                    currentMonth += 1
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next"
                                )
                            }
                        }
                        Spacer(Modifier.size(size = 8.dp))
                        Box(
                            modifier = Modifier
                                .size(size = 40.dp)
                                .background(
                                    color = if (canCreateHabit && currentCount < 5) Color.White else Color(color = 0xFFDDDDDD),
                                    shape = RoundedCornerShape(size = 4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    if (canCreateHabit && currentCount < 5) {
                                        showDialog = true
                                    }
                                },
                                enabled = canCreateHabit
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = if (canCreateHabit && currentCount < 5) Color.Black else Color.Gray
                                )
                            }
                        }
                        Spacer(Modifier.size(size = 8.dp))
                    }
                }
            },
            windowInsets = WindowInsets(left = 0.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFEEEEEE),
                titleContentColor = Color(0xFF000000),
                navigationIconContentColor = Color(0xFFDDDDDD)
            )
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CalendarFragment(
                currentMonth = currentMonth,
                currentYear = currentYear,
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date }
            )
        }

        if (currentCount < 1) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ListFragment(selectedDate = selectedDate)
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
                                .size(size = 60.dp)
                                .background(
                                    color = PrimaryGreen,
                                    shape = RoundedCornerShape(size = 4.dp)
                                )
                                .clickable(onClick = { showDialog = true }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 45.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.size(size = 8.dp))
                        Text("Create New Habit", fontSize = 20.sp)
                    } else {
                        Text(
                            "No Habits For This Day",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            Box {
                ListFragment(selectedDate = selectedDate)
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

// Calendar Fragment.
@Composable
fun CalendarFragment(
    viewModel: HabitViewModel = viewModel(),
    currentMonth: Int,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
    val thisYear = Calendar.getInstance().get(Calendar.YEAR)

    val firstDayOfMonth = Calendar.getInstance().apply {
        set(Calendar.YEAR, currentYear)
        set(Calendar.MONTH, currentMonth)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val dayOfWeekOffset = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1

    val days = buildList {
        repeat(dayOfWeekOffset) { add("") }
        for (day in 1..daysInMonth) add(day.toString())
    }

    val weekDays = listOf("S", "M", "T", "W", "T", "F", "S")
    val calendarState by viewModel.calendarState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(currentMonth, currentYear) {
        for (day in 1..daysInMonth) {
            val dateString = "${currentYear}-${String.format(Locale.US, "%02d", currentMonth + 1)}-${String.format(Locale.US, "%02d", day)}"
            viewModel.ensureRecurringHabitsForDate(dateString)
            viewModel.observeHabitsForDate(dateString)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEach { dayName ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(2f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = dayName, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { day ->
                val isToday =
                    (day.toIntOrNull() == today && currentMonth == thisMonth && currentYear == thisYear)

                val dateString = if (day != "") {
                    "${currentYear}-${
                        String.format(
                            Locale.US,
                            "%02d",
                            currentMonth + 1
                        )
                    }-${String.format(Locale.US, "%02d", day.toIntOrNull() ?: 1)}"
                } else ""

                val isSelected = dateString == selectedDate
                val habitsForDay = calendarState.habitsByDate[dateString] ?: emptyList()
                val completedCount = habitsForDay.count { it.isChecked }

                val activityLevel = completedCount.coerceIn(0, 5)

                val dayColor = when (activityLevel) {
                    0 -> Color.White
                    1 -> ActivityLevel1
                    2 -> ActivityLevel2
                    3 -> ActivityLevel3
                    4 -> ActivityLevel4
                    5 -> ActivityLevel5
                    else -> Color.White
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1.2f)
                        .background(dayColor, shape = RoundedCornerShape(4.dp))
                        .then(
                            if (isSelected) Modifier.border(
                                width = 2.dp,
                                color = PrimaryGreen,
                                shape = RoundedCornerShape(4.dp)
                            ) else Modifier
                        )
                        .clickable(enabled = day.isNotEmpty()) {
                            onDateSelected(dateString)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isToday) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .border(1.dp, Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day, textAlign = TextAlign.Center)
                        }
                    } else {
                        Text(day, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// List Of Items Fragment.
@Composable
fun ListFragment(selectedDate: String) {
    val viewModel: HabitViewModel = viewModel()
    val calendarState by viewModel.calendarState.collectAsState()

    viewModel.ensureRecurringHabitsForDate(selectedDate)
    viewModel.observeHabitsForDate(selectedDate)
    val habitsForSelectedDate = calendarState.habitsByDate[selectedDate] ?: emptyList()

    val todayString = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)}-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
    }
    val isPast = selectedDate < todayString
    val isEditable = !isPast

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(all = 0.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                itemsIndexed(items = habitsForSelectedDate) { index, habit ->
                    ItemFragment(
                        habit = habit,
                        selectedDate = selectedDate,
                        isEditable = isEditable,
                        onToggle = { isChecked ->
                            if (isEditable) {
                                viewModel.toggleHabit(selectedDate, habitId = habit.id, isChecked)
                                if (isChecked) {
                                    viewModel.increaseStrike(selectedDate, habitId = habit.id)
                                } else {
                                    viewModel.decreaseStrike(selectedDate, habitId = habit.id)
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ItemFragment(
    habit: Habit,
    selectedDate: String,
    isEditable: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var isChecked by remember(key1 = habit.id, key2 = habit.isChecked) { mutableStateOf(value = habit.isChecked) }
    var showEdit by remember { mutableStateOf(value = false) }
    var showDelete by remember { mutableStateOf(value = false) }
    val viewModel: HabitViewModel = viewModel()

    val todayString = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)}-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
    }
    val isFuture = selectedDate > todayString
    val canCheck = isEditable && !isFuture
    val canEditOrDelete = true

    val currentStreak = habit.streak

    if (showEdit) {
        EditHabitDialog(habit = habit, onDismiss = { showEdit = false }) { text, days, time, enabled ->
            viewModel.updateHabitPersisted(habit.id, text, days, time, enabled)
        }
    }

    if (showDelete) {
        DeleteHabitDialog(onDismiss = { showDelete = false }, habit = habit, onConfirm = { viewModel.deleteHabit(habit.id) })
    }

    val handleToggle: (Boolean) -> Unit = { newValue ->
        if (canCheck) {
            isChecked = newValue
            onToggle(newValue)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .background(
                color = if (canCheck) Color.White else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(horizontal = 8.dp)
            .clickable(enabled = canCheck) { handleToggle(!isChecked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = handleToggle,
                    enabled = canCheck,
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryGreen,
                        uncheckedColor = PrimaryGreen,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.LightGray,
                        disabledUncheckedColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = habit.text,
                    fontSize = 15.sp,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isChecked) Color.Gray else if (canCheck) Color.Unspecified else Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                Spacer(modifier = Modifier.width(width = 8.dp))

                if (currentStreak != 0) {
                    StreakFragment(streak = currentStreak, state = true)
                }

                if (canEditOrDelete) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MoreOptionsItemFragmentMenu(
                            onEdit = { showEdit = true },
                            onDelete = { showDelete = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoreOptionsItemFragmentMenu(onEdit: () -> Unit, onDelete: () -> Unit){
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                },
                onClick = {
                    onEdit()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
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
    val dayLabels = listOf("S","M","T","W","T","F","S")

    var notificationsEnabled by remember { mutableStateOf(true) }
    val selectedDays = remember { mutableStateListOf(false, false, false, false, false, false, false) }
    var selectedHour by remember { mutableIntStateOf(14) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    val timeText = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(size = 16.dp),
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .heightIn(min = 220.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )) {
            Column(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create New Habit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "Enter New Habit") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        dayLabels.forEachIndexed { index, day ->
                            OutlinedButton(
                                onClick = { selectedDays[index] = !selectedDays[index] },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedDays[index]) LightGreen else Color.Transparent
                                ),
                                shape = CircleShape,
                                contentPadding = PaddingValues(all = 0.dp),
                                modifier = Modifier.size(size = 30.dp),
                                border = BorderStroke(width = 1.dp, color = Color(color = 0xFFAAAAAA))
                            ) {
                                Text(text = day, color = if (selectedDays[index]) White else DarkGray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Notifications", style = MaterialTheme.typography.bodyLarge)
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
                                    .padding(start = 8.dp)
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
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = "Dismiss")
                        }
                        Spacer(modifier = Modifier.width(width = 8.dp))
                        Button(onClick = {
                            if (text.isBlank()) return@Button
                            if (currentCount >= 5) {
                                Toast.makeText(context, "Maximum 5 Habits!", Toast.LENGTH_SHORT).show()
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
                            Text(text = "Create")
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
    val dayCodes = listOf("sun","mon","tue","wed","thu","fri","sat")
    val dayLabels = listOf("S","M","T","W","T","F","S")
    val selectedDays = remember { mutableStateListOf<Boolean>().apply { addAll(dayCodes.map { habit.days.contains(it) }) } }
    val context = LocalContext.current
    val initialHourMinute = remember(habit.notifyTime) {
        val parts = habit.notifyTime?.split(":")
        val h = parts?.getOrNull(0)?.toIntOrNull() ?: 14
        val m = parts?.getOrNull(1)?.toIntOrNull() ?: 0
        h to m
    }
    var selectedHour by remember { mutableIntStateOf(initialHourMinute.first) }
    var selectedMinute by remember { mutableIntStateOf(initialHourMinute.second) }
    val timeText = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(size = 16.dp),
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Edit Habit", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(height = 8.dp))
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text(text = "Habit Name") }, singleLine = true)
                Spacer(modifier = Modifier.height(height = 16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    dayLabels.forEachIndexed { index, day ->
                        OutlinedButton(
                            onClick = { selectedDays[index] = !selectedDays[index] },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selectedDays[index]) LightGreen else Color.Transparent),
                            shape = CircleShape,
                            contentPadding = PaddingValues(all = 0.dp),
                            modifier = Modifier.size(size = 30.dp),
                            border = BorderStroke(width = 1.dp, color = Color(color = 0xFFAAAAAA))
                        ) { Text(text = day, color = if (selectedDays[index]) White else DarkGray) }
                    }
                }
                Spacer(modifier = Modifier.height(height = 16.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Notifications", style = MaterialTheme.typography.bodyLarge)
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
                                .padding(start = 8.dp)
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
                Spacer(modifier = Modifier.height(height = 16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) { Text(text = "Dismiss") }
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Button(onClick = {
                        val selected = dayCodes.filterIndexed { index, _ -> selectedDays[index] }
                        onConfirm(text, selected, if (notificationsEnabled) timeText else null, notificationsEnabled)
                        onDismiss()
                    }) { Text(text = "Save") }
                }
            }
        }
    }
}

@Composable
fun DeleteHabitDialog(habit: Habit, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Delete Habit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${habit.text}?",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                        Text("Dismiss")
                    }
                    Spacer(modifier = Modifier.width(width = 8.dp))
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
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedIcon(state: Boolean = false) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fire))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 0.75F
    )

    LottieAnimation(
        composition = composition,
        progress = if (state) progress else 0F,
        modifier = Modifier.size(size = 48.dp)
    )
}

@Composable
fun StreakFragment(streak: Int, state: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedIcon(state)
        Text(text = "$streak", fontSize = 15.sp)
    }
}