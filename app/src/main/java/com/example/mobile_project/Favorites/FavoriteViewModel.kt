package com.example.mobile_project.Favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_project.Products.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FavoritesState(
    val favoriteProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavoritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesState())
    val uiState: StateFlow<FavoritesState> = _uiState

    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesState(isLoading = true)
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                firestore.collection("users").document(user.uid).collection("favorites")
                    .get()
                    .addOnSuccessListener { result ->
                        val favoriteProducts = result.toObjects(Product::class.java)
                        _uiState.value = FavoritesState(favoriteProducts = favoriteProducts, isLoading = false)
                    }
                    .addOnFailureListener { exception ->
                        _uiState.value = FavoritesState(error = exception.message, isLoading = false)
                    }
            } else {
                _uiState.value = FavoritesState(error = "User not logged in", isLoading = false)
            }
        }
    }
}