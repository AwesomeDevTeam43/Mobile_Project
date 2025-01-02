package com.example.mobile_project.Login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobile_project.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class RegisterState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)


class RegisterViewModel : ViewModel() {

    var state = mutableStateOf(RegisterState())
        private set

    fun onEmailChange(email: String) {
        state.value = state.value.copy(email = email)
    }

    fun onUsernameChange(username: String) {
        state.value = state.value.copy(username = username)
    }

    fun onPasswordChange(password: String) {
        state.value = state.value.copy(password = password)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        state.value = state.value.copy(confirmPassword = confirmPassword)
    }

    fun onRegisterClick(onRegisterSuccess: () -> Unit) {
        val currentState = state.value

        // Validation checks
        if (currentState.password != currentState.confirmPassword) {
            state.value = currentState.copy(error = "Passwords do not match")
            return
        }

        if (currentState.username.isBlank()) {
            state.value = currentState.copy(error = "Username cannot be empty")
            return
        }

        state.value = currentState.copy(isLoading = true)

        val auth: FirebaseAuth = Firebase.auth

        auth.createUserWithEmailAndPassword(currentState.email, currentState.password)
            .addOnCompleteListener { task ->
                state.value = state.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    onRegisterSuccess()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    state.value = state.value.copy(error = task.exception?.message ?: "Unknown error")
                }
            }
    }
}
