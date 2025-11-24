package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinitysoftware.atomize.model.habit.Habit
import com.infinitysoftware.atomize.ui.theme.*
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun ListComposable(
    selectedDate: String,
    showStreak: Boolean = true,
    animatedIcon: Boolean = true
) {
    val constants = Constants()
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
                verticalArrangement = Arrangement.spacedBy(space = constants.listItemSpacing)
            ) {
                itemsIndexed(items = habitsForSelectedDate) { index, habit ->
                    ItemComposable(
                        habit = habit,
                        selectedDate = selectedDate,
                        isEditable = isEditable,
                        showStreak = showStreak,
                        animatedIcon = animatedIcon,
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
fun ItemComposable(
    habit: Habit,
    selectedDate: String,
    isEditable: Boolean,
    showStreak: Boolean = true,
    animatedIcon: Boolean = true,
    onToggle: (Boolean) -> Unit
) {
    val constants = Constants()
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
        EditHabitDialog(
            habit = habit,
            onDismiss = { showEdit = false },
            viewModel = viewModel,
            onConfirm = { text, days, time, enabled ->
                viewModel.updateHabitPersisted(id = habit.id, text, days, notifyTime = time, notificationsEnabled = enabled)
            }
        )
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
            .height(constants.listItemHeight)
            .background(
                color = White,
                shape = RoundedCornerShape(size = constants.listItemCornerRadius)
            )
            .padding(horizontal = constants.listItemHorizontalPadding)
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
                        checkmarkColor = White,
                        disabledCheckedColor = MediumGray,
                        disabledUncheckedColor = MediumGray,
                        disabledIndeterminateColor = MediumGray
                    )
                )
                Spacer(modifier = Modifier.width(constants.listItemCheckboxSpacing))
                Text(
                    text = habit.text,
                    fontSize = constants.listItemTextSize,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isChecked) MediumGray else if (canCheck) Color.Unspecified else DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                Spacer(modifier = Modifier.width(width = constants.listItemStreakSpacing))

                if (showStreak && currentStreak != 0) {
                    Streak(
                        streak = currentStreak,
                        state = true,
                        animatedIcon = animatedIcon
                    )
                } else if (showStreak && currentStreak == 0) {
                    Streak(
                        streak = currentStreak,
                        state = true,
                        animatedIcon = animatedIcon
                    )
                }

                if (canEditOrDelete) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MoreOptionsItemMenu(
                            onEdit = { showEdit = true },
                            onDelete = { showDelete = true }
                        )
                    }
                }
            }
        }
    }
}