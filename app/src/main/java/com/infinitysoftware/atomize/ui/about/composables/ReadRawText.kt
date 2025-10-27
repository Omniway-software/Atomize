package com.infinitysoftware.atomize.ui.about.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun readRawText(resId: Int): String {
    val context = LocalContext.current
    return remember(resId) {
        context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }
}