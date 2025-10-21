package com.infinitysoftware.atomize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.infinitysoftware.atomize.ui.auth.AuthFragment
import com.infinitysoftware.atomize.ui.home.HomeFragment
import com.infinitysoftware.atomize.ui.theme.AtomizeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtomizeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }
}

// Application Navigation.
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        // Defining Routes Section.
        // Home Screen.
        composable(route = "home") {
            HomeFragment(navController)
        }

        // Auth Screen.
        composable(route = "auth") {
            AuthFragment(navController)
        }
    }
}

// Authentication Section.
class AuthenticationManager {
    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
    }
}