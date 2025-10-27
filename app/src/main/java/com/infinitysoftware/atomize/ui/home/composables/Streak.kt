package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.theme.*

@Composable
fun Streak(streak: Int, state: Boolean, animatedIcon: Boolean = true) {
    val constants = Constants()
    val streakThreshold by remember { mutableIntStateOf(0) }
    val tintColor = if (streak > streakThreshold) Orange else Orange.copy(alpha = constants.streakTintColorAlpha)
    if (animatedIcon && streak == streakThreshold) return
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when {
            animatedIcon && streak > 0 -> {
                AnimatedIcon(state)
            }
            !animatedIcon -> {
                Icon(
                    painter = painterResource(id = R.drawable.fire_flame_64),
                    contentDescription = stringResource(R.string.cd_streak),
                    modifier = Modifier.size(constants.streakIconSize),
                    tint = tintColor
                )
            }
        }
        Text(
            text = "$streak",
            fontSize = constants.streakTextSize
        )
    }
}