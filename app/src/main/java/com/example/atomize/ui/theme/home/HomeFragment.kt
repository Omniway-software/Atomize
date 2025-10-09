package com.example.atomize.ui.theme.home

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.atomize.R
import com.example.atomize.model.Habit
import com.example.atomize.ui.theme.ActivityLevel1
import com.example.atomize.ui.theme.ActivityLevel2
import com.example.atomize.ui.theme.ActivityLevel3
import com.example.atomize.ui.theme.ActivityLevel4
import com.example.atomize.ui.theme.ActivityLevel5
import com.example.atomize.ui.theme.DarkGray
import com.example.atomize.ui.theme.LightGreen
import com.example.atomize.ui.theme.MediumGray
import com.example.atomize.ui.theme.PrimaryGreen
import com.example.atomize.ui.theme.White
import com.example.atomize.viewmodel.HabitViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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
    val currentDate = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)}-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
    }
    val calendarState by viewModel.calendarState.collectAsState()
    val currentCount = calendarState.habitsByDate[currentDate]?.size ?: 0

    LaunchedEffect(currentCount) {
        if (currentCount < 1) {
            delay(1000)
            showDialog = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        Log.i("TopAppBar: IconButton", "Menu Has Been Clicked!")
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
                            .clickable {
                                val today = Calendar.getInstance()
                                currentMonth = today.get(Calendar.MONTH)
                                currentYear = today.get(Calendar.YEAR)
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
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = { showDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add"
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
            CalendarFragment(currentMonth = currentMonth, currentYear = currentYear)
        }
        Box {
            ListFragment()
            if (showDialog) {
                CreateNewHabitFragment(onDismiss = { showDialog = false })
            }
        }
    }
}

// Calendar Fragment.
@Composable
fun CalendarFragment(
    viewModel: HabitViewModel = viewModel(),
    currentMonth: Int,
    currentYear: Int
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
                val isToday = (day.toIntOrNull() == today && currentMonth == thisMonth && currentYear == thisYear)

                val dateString = if (day != "") {
                    "${currentYear}-${String.format(Locale.US, "%02d", currentMonth + 1)}-${String.format(Locale.US, "%02d", day.toIntOrNull() ?: 1)}"
                } else ""

                var activityLevel by remember(dateString) { mutableIntStateOf(0) }

                LaunchedEffect(dateString) {
                    if (dateString.isNotEmpty()) {
                        activityLevel = viewModel.getCompletedHabitsCountForDate(dateString)
                    }
                }

                val dayColor = when (activityLevel.coerceIn(0, 5)) {
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
                        .background(dayColor, shape = RoundedCornerShape(4.dp)),
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
fun ListFragment() {
    val viewModel: HabitViewModel = viewModel()
    val calendarState by viewModel.calendarState.collectAsState()
    val currentDate = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)}-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
    }
    viewModel.ensureRecurringHabitsForDate(currentDate)
    viewModel.observeHabitsForDate(currentDate)
    val habitsForCurrentDate = calendarState.habitsByDate[currentDate] ?: emptyList()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 0.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            itemsIndexed(items = habitsForCurrentDate) { index, habit ->
                var showEdit by remember { mutableStateOf(value = false) }
                var showDelete by remember { mutableStateOf(value = false) }
                if (showEdit) {
                    EditHabitDialog(habit = habit, onDismiss = { showEdit = false }) { text, days, time, enabled ->
                        viewModel.updateHabitPersisted(habit.id, text, days, time, enabled)
                    }
                }
                if (showDelete) {
                    DeleteHabitDialog(onDismiss = { showDelete = false }, onConfirm = { viewModel.deleteHabit(habit.id) })
                }
                ItemFragment(
                    habit = habit,
                    onToggle = { isChecked ->
                        viewModel.toggleHabit(currentDate, habitId = habit.id, isChecked)
                        if (isChecked) viewModel.increaseStrike(currentDate, habitId = habit.id)
                    },
                    onEdit = { showEdit = true },
                    onDelete = { showDelete = true }
                )
            }
        }
    }
}

@Composable
fun ItemFragment(
    habit: Habit,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isChecked by remember { mutableStateOf(habit.isChecked) }

    LaunchedEffect(key1 = habit.id) {
        while (true) {
            val now = Calendar.getInstance()
            val midnight = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val delayMillis = midnight.timeInMillis - now.timeInMillis
            kotlinx.coroutines.delay(delayMillis)
            if (isChecked) {
                isChecked = false
                onToggle(false)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .background(Color.White, shape = RoundedCornerShape(size = 8.dp))
            .padding(all = 8.dp),
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
                    onCheckedChange = { newValue ->
                        if (!isChecked && newValue) {
                            isChecked = true
                            onToggle(true)
                        }
                    },
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
                    color = if (isChecked) Color.Gray else Color.Unspecified,
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
                if (habit.streak > 0) {
                    StreakFragment(streak = habit.streak, tint = Color.Red)
                } else {
                    StreakFragment(streak = habit.streak, tint = Color(0x3CFF0000))
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { onEdit() }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = PrimaryGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    IconButton(onClick = { onDelete() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
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
fun DeleteHabitDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
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
                    text = "Do you want to delete habit?",
                    style = MaterialTheme.typography.bodyLarge
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
fun StreakFragment(streak: Int, tint: Color = Color.Red) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(modifier = Modifier.size(size = 40.dp),
            painter = painterResource(id = R.drawable.fire_flame_64),
            contentDescription = "Menu",
            tint = tint
        )
        Text(text = "$streak", fontSize = 15.sp)
    }
}

@Composable
fun CreateNewHabitFragment(onDismiss: () -> Unit) {
    var text by remember { mutableStateOf(value = "") }
    val viewModel: HabitViewModel = viewModel()
    val currentDate = Calendar.getInstance().let { cal ->
        "${cal.get(Calendar.YEAR)}-${String.format(Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)}-${String.format(Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))}"
    }
    val calendarState by viewModel.calendarState.collectAsState()
    val currentCount = calendarState.habitsByDate[currentDate]?.size ?: 0
    val context = LocalContext.current
    val dayCodes = listOf("sun","mon","tue","wed","thu","fri","sat")
    val dayLabels = listOf("S","M","T","W","T","F","S")

    var notificationsEnabled by remember { mutableStateOf(true) }
    val selectedDays = remember { mutableStateListOf(false, false, false, false, false, false, false) }
    //val days = listOf("S", "M", "T", "W", "T", "F", "S")
    var selectedHour by remember { mutableIntStateOf(14) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    val timeText = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)

    // Dialog Section.
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
                            Toast.makeText(context, "Maximum 5 habits per day", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val selected = dayCodes.filterIndexed { index, _ -> selectedDays[index] }
                        val time: String? = if (notificationsEnabled) timeText else null
                        viewModel.createHabitPersisted(
                            date = currentDate,
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
                }
            }
        }
    }
}