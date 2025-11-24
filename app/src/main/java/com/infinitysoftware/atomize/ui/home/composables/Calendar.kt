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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.theme.*
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.util.Calendar
import java.util.Locale
import kotlin.collections.count

@Composable
fun CalendarComposable(
    viewModel: HabitViewModel = viewModel(),
    currentMonth: Int,
    currentYear: Int,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val constants = Constants()
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

    val weekDays = listOf(
        stringResource(R.string.day_sun),
        stringResource(R.string.day_mon),
        stringResource(R.string.day_tue),
        stringResource(R.string.day_wed),
        stringResource(R.string.day_thu),
        stringResource(R.string.day_fri),
        stringResource(R.string.day_sat)
    )

    val calendarState by viewModel.calendarState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(currentMonth, currentYear) {
        val datesInMonth = (1..daysInMonth).map { day ->
            "${currentYear}-${String.format(Locale.US, "%02d", currentMonth + 1)}-${String.format(Locale.US, "%02d", day)}"
        }

        datesInMonth.forEach { dateString ->
            viewModel.ensureRecurringHabitsForDate(dateString)
        }

        datesInMonth.forEach { dateString ->
            viewModel.observeHabitsForDate(dateString)
        }
    }

    androidx.compose.runtime.LaunchedEffect(calendarState) {
        for (day in 1..daysInMonth) {
            val dateString = "${currentYear}-${String.format(Locale.US, "%02d", currentMonth + 1)}-${String.format(Locale.US, "%02d", day)}"
            viewModel.ensureRecurringHabitsForDate(dateString)
        }
    }

    @Composable
    fun getActivityColor(level: Int): Color {
        return when (level) {
            0 -> ActivityLevel0
            1 -> ActivityLevel1
            2 -> ActivityLevel2
            3 -> ActivityLevel3
            4 -> ActivityLevel4
            5 -> ActivityLevel5
            6 -> ActivityLevel6
            7 -> ActivityLevel7
            8 -> ActivityLevel8
            9 -> ActivityLevel9
            10 -> ActivityLevel10
            else -> ActivityLevel0
        }
    }

    Column(modifier = Modifier.padding(constants.calendarPadding)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEachIndexed { index, dayName ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(constants.calendarWeekdayAspectRatio),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = if (index == 0) Orange else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(constants.calendarWeekdaySpacing))
        LazyVerticalGrid(
            columns = GridCells.Fixed(constants.calendarGridColumns),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(constants.calendarSpacing),
            verticalArrangement = Arrangement.spacedBy(constants.calendarSpacing)
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
                val activityLevel = completedCount.coerceIn(constants.minActivityLevel, constants.maxActivityLevel)
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
                        .aspectRatio(constants.calendarDayAspectRatio)
                        .background(dayColor, shape = RoundedCornerShape(constants.calendarCornerRadius))
                        .then(
                            if (isSelected) Modifier.border(
                                width = constants.calendarSelectedBorderWidth,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(constants.calendarCornerRadius)
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
                                .size(constants.calendarTodayCircleSize)
                                .border(
                                    constants.calendarTodayBorderWidth,
                                    MaterialTheme.colorScheme.onSurface,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                day,
                                textAlign = TextAlign.Center,
                                color = if (isSunday) Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Text(
                            day,
                            textAlign = TextAlign.Center,
                            color = if (isSunday) {
                                Orange
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
                                .padding(constants.calendarIndicatorPadding)
                                .size(constants.calendarIndicatorSize)
                                .background(
                                    color = Orange,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}