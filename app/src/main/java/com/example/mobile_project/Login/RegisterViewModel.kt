package com.example.mobile_project.Login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobile_project.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {

    var state = mutableStateOf(RegisterState())
        private set

    fun onUsernameChange(username: String) {
        state.value = state.value.copy(username = username)
    }

    fun onEmailChange(email: String) {
        state.value = state.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        state.value = state.value.copy(password = password)
    }

    fun onRegisterClick(onRegisterSuccess: () -> Unit) {
        state.value = state.value.copy(isLoading = true)

        val auth: FirebaseAuth = Firebase.auth
        val firestore = FirebaseFirestore.getInstance()

        // Create user with email and password
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                state.value = state.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    // Sign-in success, now save the username to Firestore
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "email" to state.value.email,
                        "username" to state.value.username
                    )

                    // Save username to Firestore under a collection "users"
                    firestore.collection("users").document(user?.uid ?: "")
                        .set(userData)
                        .addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful) {
                                Log.d(TAG, "Username saved to Firestore")
                                onRegisterSuccess()
                            } else {
                                Log.w(TAG, "Error saving username to Firestore", firestoreTask.exception)
                                state.value = state.value.copy(error = firestoreTask.exception?.message)
                            }
                        }
                } else {
                    // Registration failed
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    state.value = state.value.copy(error = task.exception?.message ?: "Unknown error")
                }
            }
    }
}