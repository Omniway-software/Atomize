package com.example.atomize.ui.theme.home

import android.util.Log
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.atomize.R
import com.example.atomize.model.Habit
import com.example.atomize.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.YearMonth

// Home (Screen) Fragment.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFragment(navController: NavHostController) {

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
                            IconButton(onClick = { /* TODO: MAKE TRIGGER */ }) {
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
    val habitsForCurrentDate = calendarState.habitsByDate[currentDate] ?: emptyList()

    if (habitsForCurrentDate.isEmpty()) {
        val defaultHabits = listOf("Stretch", "Run", "Light workout", "Write", "Eat vegetables")
        defaultHabits.forEach { habitText ->
            viewModel.addHabitForDate(currentDate, habitText)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            itemsIndexed(items = habitsForCurrentDate) { index, habit ->
                ItemFragment(
                    habit = habit,
                    onToggle = { isChecked ->
                        viewModel.toggleHabit(currentDate, habitId = habit.id, isChecked)
                        if (isChecked) { viewModel.increaseStrike(currentDate, habitId = habit.id) }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemFragment(habit: Habit, onToggle: (Boolean) -> Unit) {
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
                        checkedColor = Color.Red,
                        uncheckedColor = Color.Red,
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
            StreakFragment(streak = habit.streak)
        }
    }
}

@Composable
fun StreakFragment(streak: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = { Log.i("StreakFragment: IconButton", "IconButton Has Been Clicked!") }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.fire_flame_64),
                contentDescription = "Menu",
                tint = Color.Red
            )
        }
        Text(text = "$streak", fontSize = 15.sp)
    }
}