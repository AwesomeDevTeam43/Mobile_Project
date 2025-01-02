package com.example.mobile_project.Login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobile_project.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
        state.value = state.value.copy(isLoading = true)

        // Validate if password and confirm password match
        if (state.value.password != state.value.confirmPassword) {
            state.value = state.value.copy(isLoading = false, error = "Passwords do not match")
            return
        }

        // Create the user with Firebase Authentication
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                state.value = state.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    // Store the username in Firestore
                    val user = auth.currentUser
                    val userProfile = hashMapOf(
                        "username" to state.value.username,
                        "email" to state.value.email
                    )

                    // Save username to Firestore
                    user?.uid?.let { userId ->
                        db.collection("users").document(userId)
                            .set(userProfile)
                            .addOnSuccessListener {
                                Log.d(TAG, "User profile created for ${user.email}")
                                onRegisterSuccess()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error adding document", exception)
                                state.value = state.value.copy(error = "Failed to save username")
                            }
                    }
                } else {
                    // If sign-in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    state.value = state.value.copy(error = task.exception?.message ?: "Unknown error")
                }
            }
    }
}
