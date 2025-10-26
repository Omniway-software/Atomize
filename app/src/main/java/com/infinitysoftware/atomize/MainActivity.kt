package com.infinitysoftware.atomize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.infinitysoftware.atomize.ui.NavDrawer
import com.infinitysoftware.atomize.ui.theme.AtomizeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AtomizeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDrawer(modifier = Modifier.padding(innerPadding))
//                     SignInFragment() // NE MORA SA POCETAK
                }
            }
        }
    }
}

// TODO KO SE ULOGUJE IMA FIRESTORE 10
// AKO SE REAZLIKUJE TRENUTNO SA ONIM NA FIRESTOREU
//

class AuthenticationManager {

    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(task.exception?.message ?: "Unknown error"))
                }
                close()
            }
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(task.exception?.message ?: "Unknown error"))
                }
                close()
            }
    }

    fun signOut() = auth.signOut()

    fun getCurrentUser() = auth.currentUser
}

sealed interface AuthResponse {
    object Success : AuthResponse
    data class Error(val message: String) : AuthResponse
}