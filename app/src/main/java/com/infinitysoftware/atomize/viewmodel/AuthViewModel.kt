package com.infinitysoftware.atomize.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    var onUserChanged: (() -> Unit)? = null

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus () {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error(message = "Empty Email Or Password")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    onUserChanged?.invoke()
                } else {
                    _authState.value = AuthState.Error(message = task.exception?.message?:"Error")
                }
            }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error(message = "Empty Email Or Password")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    onUserChanged?.invoke()
                } else {
                    _authState.value = AuthState.Error(message = task.exception?.message?:"Error")
                }
            }
    }

    fun signout () {
        viewModelScope.launch {
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
            onUserChanged?.invoke()
        }
    }

    fun getHabitLimit(): Int {
        return if (auth.currentUser != null) 10 else 5
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
    
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}

sealed class AuthState {
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}