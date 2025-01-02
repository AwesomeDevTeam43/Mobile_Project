import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val state: MutableState<RegisterState> = mutableStateOf(RegisterState())

    fun registerUser(email: String, password: String, confirmPassword: String, username: String, onRegisterSuccess: () -> Unit) {
        // Validate the passwords match
        if (password != confirmPassword) {
            state.value = state.value.copy(error = "Passwords do not match")
            return
        }

        state.value = state.value.copy(isLoading = true)

        // Register user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                state.value = state.value.copy(isLoading = false)

                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser

                    // Update user's profile with the display name (username)
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Profile updated successfully
                                Log.d("RegisterViewModel", "User profile updated.")
                            } else {
                                // Handle error updating profile
                                Log.w("RegisterViewModel", "Profile update failed.", profileTask.exception)
                            }
                        }

                    // Save user information to Firestore
                    val userRef = db.collection("users").document(user?.uid ?: "")
                    userRef.set(mapOf("email" to email, "username" to username))

                    // Optionally, sign out the user to wait for email verification
                    auth.signOut()

                    onRegisterSuccess()  // Call the success callback
                } else {
                    // Handle registration failure
                    Log.w("RegisterViewModel", "Registration failed", task.exception)
                    state.value = state.value.copy(error = task.exception?.message ?: "Unknown error")
                }
            }
    }
}

// Data class to hold the state
data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null
)
