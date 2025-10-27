package com.infinitysoftware.atomize.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.settings.composables.SettingCard
import com.infinitysoftware.atomize.ui.theme.Black

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
                color = Black,
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