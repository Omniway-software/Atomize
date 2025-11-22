package com.infinitysoftware.atomize.ui.paywall.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.infinitysoftware.atomize.ui.paywall.Constants
import com.infinitysoftware.atomize.ui.theme.ActivityLevel2
import com.infinitysoftware.atomize.ui.theme.ActivityLevel3
import com.infinitysoftware.atomize.ui.theme.LightGray
import com.infinitysoftware.atomize.ui.theme.Orange
import com.infinitysoftware.atomize.ui.theme.White


@Composable
fun PaywallOptionCard(
    title: String,
    price: String,
    tag: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Orange else LightGray
    val backgroundColor = if (isSelected) ActivityLevel3 else ActivityLevel2

    val constants = Constants()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = constants.paywallOptionCardBorderWidth, borderColor, shape = RoundedCornerShape(size = constants.paywallOptionCardShapeSize))
            .background(backgroundColor, shape = RoundedCornerShape(size = constants.paywallOptionCardShapeSize))
            .clickable { onClick() }
            .padding(all = constants.screenPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = White)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "$$price", fontSize = constants.priceFontSize, color = White)
                    Spacer(modifier = Modifier.size(size = constants.defaultSpacingSize))
                }
            }
            if (tag != null) {
                Box(
                    modifier = Modifier
                        .background(color = Orange, shape = RoundedCornerShape(size = constants.tagShapeSize))
                        .padding(horizontal = constants.tagPadding, vertical = constants.defaultSpacingSize)
                ) {
                    Text(text = tag, color = Color.White, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}