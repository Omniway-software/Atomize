package com.infinitysoftware.atomize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.infinitysoftware.atomize.ui.NavDrawer
import com.infinitysoftware.atomize.ui.theme.AtomizeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtomizeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDrawer(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }
}