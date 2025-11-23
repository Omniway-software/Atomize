package com.infinitysoftware.atomize.ui.paywall

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.paywall.composables.PaywallOptionCard
import com.infinitysoftware.atomize.ui.theme.ActivityLevel1
import com.infinitysoftware.atomize.ui.theme.Orange
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen
import com.infinitysoftware.atomize.ui.theme.White

enum class SubscriptionType { Monthly, Yearly, Lifetime }

@Composable
fun PaywallScreen(
    onSubscribeMonthly: () -> Unit = {},
    onSubscribeYearly: () -> Unit = {},
    onSubscribeLifetime: () -> Unit = {},
    onRestore: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    var selectedSubscription by remember { mutableStateOf(value = SubscriptionType.Lifetime) }

    val constants = Constants()
    val infiniteTransition = rememberInfiniteTransition()
    val xOffset by infiniteTransition.animateFloat(
        initialValue = constants.initialValue, targetValue = constants.targetValue, animationSpec = infiniteRepeatable(animation = tween(durationMillis = constants.gradientAnimationDuration, easing = LinearEasing), RepeatMode.Reverse)
    )
    val yOffset by infiniteTransition.animateFloat(
        initialValue = constants.initialValue, targetValue = constants.targetValue, animationSpec = infiniteRepeatable(animation = tween(durationMillis = constants.gradientAnimationDuration, easing = LinearEasing), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Orange, PrimaryGreen, ActivityLevel1),
                    start = Offset(x = xOffset, y = yOffset),
                    end = Offset(x = constants.endValue + xOffset, y = constants.endValue + yOffset)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(all = constants.defaultPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(all = constants.minPadding),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = stringResource(R.string.paywall_title), style = MaterialTheme.typography.titleLarge, color = White)
                Spacer(Modifier.size(size = constants.minSpacerSize))
                Text(text = stringResource(R.string.paywall_subtitle), style = MaterialTheme.typography.bodyLarge, color = White)
                Spacer(Modifier.size(size = constants.minSpacerSize))
                Text(text = stringResource(R.string.paywall_benefits_title), style = MaterialTheme.typography.titleLarge, color = White)
                Spacer(Modifier.size(size = constants.minSpacerSize))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = White)
                    Spacer(Modifier.size(size = constants.minSpacerSize))
                    Text(text = stringResource(R.string.paywall_benefit_slots), style = MaterialTheme.typography.bodyLarge, color = White)
                }
                Spacer(Modifier.size(size = constants.minSpacerSize))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = White)
                    Spacer(Modifier.size(size = constants.minSpacerSize))
                    Text(text = stringResource(R.string.paywall_benefit_notifications), style = MaterialTheme.typography.bodyLarge, color = White)
                }
                Spacer(Modifier.size(size = constants.minSpacerSize))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Block, contentDescription = null, tint = White)
                    Spacer(Modifier.size(size = constants.minSpacerSize))
                    Text(text = stringResource(R.string.paywall_benefit_no_ads), style = MaterialTheme.typography.bodyLarge, color = White)
                }
                Spacer(Modifier.size(size = constants.defaultSpacerSize))

                PaywallOptionCard(
                    title = stringResource(R.string.paywall_plan_monthly),
                    price = stringResource(R.string.paywall_price_monthly),
                    isSelected = selectedSubscription == SubscriptionType.Monthly
                ) {
                    selectedSubscription = SubscriptionType.Monthly
                    onSubscribeMonthly()
                }

                Spacer(Modifier.size(size = constants.defaultSpacerSize))

                PaywallOptionCard(
                    title = stringResource(R.string.paywall_plan_yearly),
                    price = stringResource(R.string.paywall_price_yearly),
                    isSelected = selectedSubscription == SubscriptionType.Yearly
                ) {
                    selectedSubscription = SubscriptionType.Yearly
                    onSubscribeYearly()
                }

                Spacer(Modifier.size(size = constants.defaultSpacerSize))

                PaywallOptionCard(
                    title = stringResource(R.string.paywall_plan_lifetime),
                    price = stringResource(R.string.paywall_price_lifetime),
                    tag = stringResource(R.string.paywall_tag_best_deal),
                    isSelected = selectedSubscription == SubscriptionType.Lifetime
                ) {
                    selectedSubscription = SubscriptionType.Lifetime
                    onSubscribeLifetime()
                }
            }
        }
    }
}