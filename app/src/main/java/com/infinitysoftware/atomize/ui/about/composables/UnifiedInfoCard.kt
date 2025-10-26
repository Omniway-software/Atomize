package com.infinitysoftware.atomize.ui.about.composables

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.infinitysoftware.atomize.ui.about.Constants

@Composable
fun UnifiedInfoCard(
    icon: ImageVector,
    title: String,
    content: String,
    iconBackgroundColor: Color,
    isLongText: Boolean = false
) {
    val constants = Constants()

    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(
                elevation = constants.cardElevation,
                shape = RoundedCornerShape(constants.cardCornerRadius),
                spotColor = iconBackgroundColor.copy(alpha = constants.shadowAlpha)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(constants.cardCornerRadius)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(constants.cardPadding),
            verticalAlignment = if (isLongText) Alignment.Top else Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(constants.iconBoxSize).clip(CircleShape)
                    .background(iconBackgroundColor),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(constants.contentTopSpacing))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    lineHeight = constants.creditsLineHeight
                )
            }
        }
    }
}