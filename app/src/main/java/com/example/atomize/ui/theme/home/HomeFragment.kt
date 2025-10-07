package com.example.atomize.ui.theme.home

import android.app.TimePickerDialog
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import android.widget.Toast
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.atomize.R
import com.example.atomize.model.Habit
import com.example.atomize.ui.theme.*
import com.example.atomize.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.YearMonth

// Home (Screen) Fragment.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFragment(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(value = false) }

    // TopAppBar
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
                        modifier = Modifier.weight(weight = 1f)
                    ) {
                        Text(
                            text = "September 2025",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Row {
                        Box(
                            modifier = Modifier
                                .size(size = 40.dp)
                                .background(
                                    color = Color(color = 0xFFFFFFFF),
                                    shape = RoundedCornerShape(size = 4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {
                                Log.i("TopAppBar: IconButton", "Back Has Been Clicked!")
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
                                    color = Color(color = 0xFFFFFFFF),
                                    shape = RoundedCornerShape(size = 4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {
                                Log.i("TopAppBar: IconButton", "Next Has Been Clicked!")
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
                                    color = Color(color = 0xFFFFFFFF),
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
                containerColor = Color(color = 0xFFEEEEEE),
                titleContentColor = Color(color = 0xFF000000),
                navigationIconContentColor = Color(color = 0xFFDDDDDD)
            )
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CalendarFragment()
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
fun CalendarFragment() {
    val today = LocalDate.now()
    val yearMonth = YearMonth.now()
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val dayOfWeekOffset = if (firstDayOfMonth.dayOfWeek.value == 7) 0 else firstDayOfMonth.dayOfWeek.value
    val days = buildList {
        repeat(times = dayOfWeekOffset) {
            add("")
        }
        for (day in 1..daysInMonth)
            add(day.toString())
    }
    val weekDays = listOf("S", "M", "T", "W", "T", "F", "S")
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEach { dayName ->
                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .aspectRatio(ratio = 2f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            items(items = days) { day ->
                val isToday = day == today.dayOfMonth.toString()
                Box(
                    modifier = Modifier
                        .aspectRatio(ratio = 1.2F)
                        .background(Color.White, shape = RoundedCornerShape(size = 4.dp))
                        .clickable {
                            if (day != "") {
                                Log.i("CalendarFragment: Item", "Day $day Has Been Clicked!")
                            } else {
                                Log.i("CalendarFragment: Item", "Empty Day Field Has Been Clicked!")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isToday) {
                        Box(
                            modifier = Modifier
                                .size(size = 40.dp)
                                .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = day, textAlign = TextAlign.Center)
                        }
                    } else {
                        Text(text = day, textAlign = TextAlign.Center)
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
    val currentDate = LocalDate.now().toString()
    // Ensure recurring habits are created for this date before observing
    viewModel.ensureRecurringHabitsForDate(currentDate)
    // Observe DB for current date
    viewModel.observeHabitsForDate(currentDate)
    val habitsForCurrentDate = calendarState.habitsByDate[currentDate] ?: emptyList()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            itemsIndexed(items = habitsForCurrentDate) { index, habit ->
                var showEdit by remember { mutableStateOf(false) }
                if (showEdit) {
                    EditHabitDialog(habit = habit, onDismiss = { showEdit = false }) { text, days, time, enabled ->
                        viewModel.updateHabitPersisted(habit.id, text, days, time, enabled)
                    }
                }
                ItemFragment(
                    habit = habit,
                    onToggle = { isChecked ->
                        viewModel.toggleHabit(currentDate, habitId = habit.id, isChecked)
                        if (isChecked) { viewModel.increaseStrike(currentDate, habitId = habit.id) }
                    },
                    onEdit = { showEdit = true },
                    onDelete = { viewModel.deleteHabit(habit.id) }
                )
            }
        }
    }
}

@Composable
fun ItemFragment(habit: Habit, onToggle: (Boolean) -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 115.dp)
            .background(Color.White, shape = RoundedCornerShape(size = 8.dp))
            .padding(all = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = habit.isChecked,
                    onCheckedChange = { isChecked ->
                        onToggle(isChecked)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryGreen,
                        uncheckedColor = PrimaryGreen,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.LightGray,
                        disabledUncheckedColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.size(size = 32.dp))
                Text(
                    text = habit.text,
                    fontSize = 15.sp,
                    textDecoration = if (habit.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (habit.isChecked) Color.Gray else Color.Unspecified
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier ) {
                Spacer(modifier = Modifier.size(size = 8.dp))
                if (habit.streak < 1) {
                    StreakFragment(streak = habit.streak, tint = Color(color = 0x3CFF0000))
                } else {
                    StreakFragment(streak = habit.streak)
                }
                Column {
                    IconButton(onClick = { onEdit() }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = PrimaryGreen
                        )
                    }
                    Spacer(modifier = Modifier.size(size = 5.dp))
                    IconButton(onClick = { onDelete() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = PrimaryGreen
                        )
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
    // Parse existing time or default to 14:00
    val initialHourMinute = remember(habit.notifyTime) {
        val parts = habit.notifyTime?.split(":")
        val h = parts?.getOrNull(0)?.toIntOrNull() ?: 14
        val m = parts?.getOrNull(1)?.toIntOrNull() ?: 0
        h to m
    }
    var selectedHour by remember { mutableStateOf(initialHourMinute.first) }
    var selectedMinute by remember { mutableStateOf(initialHourMinute.second) }
    val timeText = String.format("%02d:%02d", selectedHour, selectedMinute)

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
                        Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
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
fun StreakFragment(streak: Int, tint: Color = Color.Red) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = { Log.i("StreakFragment: IconButton", "IconButton Has Been Clicked!") }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.fire_flame_64),
                contentDescription = "Menu",
                tint = tint
            )
        }
        Text(text = "$streak", fontSize = 15.sp)
    }
}

@Composable
fun CreateNewHabitFragment(onDismiss: () -> Unit) {
    var text by remember { mutableStateOf(value = "") }
    val viewModel: HabitViewModel = viewModel()
    val currentDate = LocalDate.now().toString()
    val calendarState by viewModel.calendarState.collectAsState()
    val currentCount = calendarState.habitsByDate[currentDate]?.size ?: 0
    val context = LocalContext.current
    val dayCodes = listOf("sun","mon","tue","wed","thu","fri","sat")
    val dayLabels = listOf("S","M","T","W","T","F","S")

    var notificationsEnabled by remember { mutableStateOf(true) }
    val selectedDays = remember { mutableStateListOf(false, false, false, false, false, false, false) }
    val days = listOf("S", "M", "T", "W", "T", "F", "S")
    var selectedHour by remember { mutableStateOf(14) }
    var selectedMinute by remember { mutableStateOf(0) }
    val timeText = String.format("%02d:%02d", selectedHour, selectedMinute)

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
                                onCheckedChange = { notificationsEnabled = it }
                            )
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