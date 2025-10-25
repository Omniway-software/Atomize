package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitysoftware.atomize.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarComposable(
    currentMonth: Int,
    currentYear: Int,
    currentCount: Int,
    canCreateHabit: Boolean,
    monthYearText: String,
    onMenuClick: () -> Unit,
    onMonthDecrement: () -> Unit,
    onMonthIncrement: () -> Unit,
    onAddHabitClick: () -> Unit,
    onResetToToday: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.cd_menu)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onResetToToday
                        )
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
                            .size(40.dp)
                            .background(Color.White, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onMonthDecrement) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_previous_month)
                            )
                        }
                    }

                    Spacer(Modifier.width(width = 8.dp))

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onMonthIncrement) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(R.string.cd_next_month)
                            )
                        }
                    }

                    Spacer(Modifier.width(width = 8.dp))

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (canCreateHabit && currentCount < 5) Color.White else Color(0xFFDDDDDD),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { if (canCreateHabit && currentCount < 5) onAddHabitClick() },
                            enabled = canCreateHabit
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.cd_add),
                                tint = if (canCreateHabit && currentCount < 5) Color.Black else Color.Gray
                            )
                        }
                    }
                    Spacer(Modifier.width(width = 8.dp))
                }
            }
        },
        windowInsets = WindowInsets(left = 0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFEEEEEE),
            titleContentColor = Color.Black,
            navigationIconContentColor = Color(0xFFDDDDDD)
        )
    )
}