package com.infinitysoftware.atomize.ui.about.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.about.Constants

@Composable
fun ExpandablePrivacyPolicyCard() {
    var expanded by remember { mutableStateOf(false) }
    val constants = Constants()
    val privacyTitle = stringResource(R.string.about_privacy_policy_title)
    val privacyText = readRawText(R.raw.privacy_policy)

    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(
                elevation = constants.cardElevation,
                shape = RoundedCornerShape(constants.cardCornerRadius),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = constants.shadowAlpha)
            )
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(constants.cardCornerRadius)
    ) {
        Row(modifier = Modifier.padding(constants.cardPadding), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(constants.iconBoxSize).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = privacyTitle,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(constants.iconSize)
                )
            }

            Spacer(modifier = Modifier.width(constants.iconSpacing))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = privacyTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(constants.contentTopSpacing))

                val previewText = if (expanded) privacyText else privacyText.lines().take(constants.privacyPreviewLines).joinToString("\n")
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = constants.privacyLineHeight),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}