package com.infinitysoftware.atomize.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinitysoftware.atomize.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val settings by viewModel.settings.collectAsState()
    val constants = Constants()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = constants.surfaceVariantAlpha)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(all = constants.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = constants.titlePadding)
            )
            Spacer(modifier = Modifier.height(constants.titleBottomSpacing))
            SettingCard(
                icon = Icons.Outlined.Visibility,
                title = stringResource(R.string.settings_show_streak_title),
                description = stringResource(R.string.settings_show_streak_description),
                checked = settings.showStreak,
                onCheckedChange = { viewModel.updateShowStreak(it) }
            )
            Spacer(modifier = Modifier.height(constants.cardSpacing))
            SettingCard(
                icon = Icons.Outlined.LocalFireDepartment,
                title = stringResource(R.string.settings_animated_icon_title),
                description = stringResource(R.string.settings_animated_icon_description),
                checked = settings.animatedIcon,
                onCheckedChange = { viewModel.updateAnimatedIcon(it) }
            )
            Spacer(modifier = Modifier.height(constants.bottomSpacing))
        }
    }
}

@Composable
fun SettingCard(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val constants = Constants()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = constants.cardElevation,
                shape = RoundedCornerShape(constants.cardCornerRadius),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = constants.shadowAlpha)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(constants.cardCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(constants.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(constants.iconBoxSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(constants.iconSize)
                    )
                }
                Spacer(modifier = Modifier.width(constants.iconSpacing))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(constants.descriptionSpacing))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = constants.descriptionAlpha),
                        lineHeight = constants.descriptionLineHeight
                    )
                }
            }
            Spacer(modifier = Modifier.width(constants.switchSpacing))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = constants.trackAlpha),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Gray.copy(alpha = constants.uncheckedTrackAlpha)
                )
            )
        }
    }
}