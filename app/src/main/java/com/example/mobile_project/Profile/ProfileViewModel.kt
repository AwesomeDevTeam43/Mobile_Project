package com.example.mobile_project.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_project.Products.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val username: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val profileImageUrl: String? = null,
    val favoriteProducts: List<Product> = emptyList()
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileState(isLoading = true)
            try {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    firestore.collection("users").document(user.uid).get()
                        .addOnSuccessListener { document ->
                            val username = document.getString("username") ?: "No Name"
                            val profileImageUrl = document.getString("profileImageUrl")
                            _uiState.value = ProfileState(
                                username = username,
                                email = user.email ?: "No Email",
                                profileImageUrl = profileImageUrl,
                                isLoading = false
                            )
                            fetchFavorites(user.uid)
                        }
                        .addOnFailureListener { exception ->
                            _uiState.value = ProfileState(
                                error = exception.message ?: "Failed to load profile",
                                isLoading = false
                            )
                        }
                } else {
                    _uiState.value = ProfileState(error = "User not logged in", isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileState(error = e.message, isLoading = false)
            }
        }
    }

    private fun fetchFavorites(userId: String) {
        firestore.collection("users").document(userId).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val favoriteProducts = result.toObjects(Product::class.java)
                _uiState.value = _uiState.value.copy(favoriteProducts = favoriteProducts)
            }
            .addOnFailureListener { exception ->
                _uiState.value = _uiState.value.copy(error = exception.message)
            }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}