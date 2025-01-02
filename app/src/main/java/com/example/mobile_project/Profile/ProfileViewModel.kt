package com.example.mobile_project.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val username: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileState(isLoading = true)
            try {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    _uiState.value = ProfileState(
                        username = user.displayName ?: "No Name",
                        email = user.email ?: "No Email",
                        isLoading = false
                    )
                } else {
                    _uiState.value = ProfileState(error = "User not logged in", isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileState(error = e.message, isLoading = false)
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}