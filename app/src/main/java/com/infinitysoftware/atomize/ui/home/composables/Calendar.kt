package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.util.Calendar
import java.util.Locale
import kotlin.collections.count

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

    @Composable
    fun getActivityColor(level: Int): Color {
        return when (level) {
            0 -> MaterialTheme.colorScheme.surface
            1 -> Color(0xFFC8E6C9)
            2 -> Color(0xFFA5D6A7)
            3 -> Color(0xFF81C784)
            4 -> Color(0xFF66BB6A)
            5 -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEachIndexed { index, dayName ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(2f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = if (index == 0) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
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
                val dayColor = getActivityColor(activityLevel)
                val calendar = Calendar.getInstance().apply {
                    if (day.isNotEmpty()) {
                        set(Calendar.YEAR, currentYear)
                        set(Calendar.MONTH, currentMonth)
                        set(Calendar.DAY_OF_MONTH, day.toIntOrNull() ?: 1)
                    }
                }
                val isSunday = day.isNotEmpty() && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

                Box(
                    modifier = Modifier
                        .aspectRatio(1.2f)
                        .background(dayColor, shape = RoundedCornerShape(4.dp))
                        .then(
                            if (isSelected) Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
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
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                day,
                                textAlign = TextAlign.Center,
                                color = if (isSunday) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Text(
                            day,
                            textAlign = TextAlign.Center,
                            color = if (isSunday) {
                                Color.Red
                            } else if (activityLevel == 0) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    }

                    if (habitsForDay.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                                .size(6.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}