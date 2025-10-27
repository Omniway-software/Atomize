package com.infinitysoftware.atomize.ui.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.infinitysoftware.atomize.ui.settings.Constants
import com.infinitysoftware.atomize.ui.theme.*


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
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(constants.descriptionSpacing))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Black.copy(alpha = constants.descriptionAlpha),
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
                    uncheckedThumbColor = MediumGray,
                    uncheckedTrackColor = MediumGray.copy(alpha = constants.uncheckedTrackAlpha)
                )
            )
        }
    }
}