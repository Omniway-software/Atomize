package com.infinitysoftware.atomize.ui.home.composables

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.home.Constants

@Composable
fun AnimatedIcon(state: Boolean = false) {
    val constants = Constants()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fire))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = constants.animationSpeed
    )
    LottieAnimation(
        composition = composition,
        progress = if (state) progress else constants.animationProgress,
        modifier = Modifier.size(size = constants.animatedIconSize)
    )
}