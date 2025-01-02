import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
<<<<<<< HEAD
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
=======
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
>>>>>>> bb740228f711803d775da55ab62193a73cfd306c

    val state: MutableState<RegisterState> = mutableStateOf(RegisterState())

<<<<<<< HEAD
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
=======
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
>>>>>>> bb740228f711803d775da55ab62193a73cfd306c
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
