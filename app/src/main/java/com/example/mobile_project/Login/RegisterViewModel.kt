package com.example.mobile_project.Login

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val profileImageUri: Uri? = null
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

    fun onProfileImageChange(uri: Uri) {
        state.value = state.value.copy(profileImageUri = uri)
    }

    fun onRegisterClick(onRegisterSuccess: () -> Unit) {
        state.value = state.value.copy(isLoading = true)

        val auth: FirebaseAuth = Firebase.auth
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        // Create user with email and password
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "email" to state.value.email,
                        "username" to state.value.username
                    )

                    // Upload profile image to Firebase Storage
                    state.value.profileImageUri?.let { uri ->
                        val storageRef = storage.reference.child("profile_images/${user?.uid}")
                        storageRef.putFile(uri)
                            .addOnSuccessListener { taskSnapshot ->
                                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                    userData["profileImageUrl"] = downloadUri.toString()
                                    saveUserDataToFirestore(user, userData, onRegisterSuccess)
                                }
                            }
                            .addOnFailureListener { exception ->
                                state.value = state.value.copy(error = exception.message, isLoading = false)
                            }
                    } ?: saveUserDataToFirestore(user, userData, onRegisterSuccess)
                } else {
                    state.value = state.value.copy(error = task.exception?.message, isLoading = false)
                }
            }
    }

    private fun saveUserDataToFirestore(user: FirebaseUser?, userData: HashMap<String, String>, onRegisterSuccess: () -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(user?.uid ?: "")
            .set(userData)
            .addOnCompleteListener { firestoreTask ->
                state.value = state.value.copy(isLoading = false)
                if (firestoreTask.isSuccessful) {
                    onRegisterSuccess()
                } else {
                    state.value = state.value.copy(error = firestoreTask.exception?.message)
                }
            }
    }
}