package com.example.mobile_project.Favorites

import android.util.Log
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
                        val favoriteProductIds = result.documents.map { it.id }
                        Log.d("FavoritesViewModel", "Favorite product IDs: $favoriteProductIds")
                        fetchFavoriteProducts(favoriteProductIds)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FavoritesViewModel", "Error fetching favorites: ", exception)
                        _uiState.value = FavoritesState(error = exception.message, isLoading = false)
                    }
            } else {
                Log.e("FavoritesViewModel", "User not logged in")
                _uiState.value = FavoritesState(error = "User not logged in", isLoading = false)
            }
        }
    }

    private fun fetchFavoriteProducts(productIds: List<String>) {
        if (productIds.isEmpty()) {
            Log.d("FavoritesViewModel", "No favorite products found")
            _uiState.value = FavoritesState(favoriteProducts = emptyList(), isLoading = false)
            return
        }

        val favoriteProducts = mutableListOf<Product>()
        var fetchedCount = 0

        productIds.forEach { productId ->
            firestore.collection("products").document(productId)
                .get()
                .addOnSuccessListener { document ->
                    val product = document.toObject(Product::class.java)
                    if (product != null) {
                        product.id = document.id
                        favoriteProducts.add(product)
                    }
                    fetchedCount++
                    if (fetchedCount == productIds.size) {
                        _uiState.value = FavoritesState(favoriteProducts = favoriteProducts, isLoading = false)
                        Log.d("FavoritesViewModel", "Fetched favorite products: $favoriteProducts")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FavoritesViewModel", "Error fetching product with ID $productId: ", exception)
                    fetchedCount++
                    if (fetchedCount == productIds.size) {
                        _uiState.value = FavoritesState(favoriteProducts = favoriteProducts, isLoading = false)
                    }
                }
        }
    }

    fun sortProductsByPriceAscending() {
        _uiState.value = _uiState.value.copy(
            favoriteProducts = _uiState.value.favoriteProducts.sortedBy { it.price }
        )
    }

    fun sortProductsByPriceDescending() {
        _uiState.value = _uiState.value.copy(
            favoriteProducts = _uiState.value.favoriteProducts.sortedByDescending { it.price }
        )
    }

    fun sortProductsByNameAscending() {
        _uiState.value = _uiState.value.copy(
            favoriteProducts = _uiState.value.favoriteProducts.sortedBy { it.title }
        )
    }

    fun sortProductsByNameDescending() {
        _uiState.value = _uiState.value.copy(
            favoriteProducts = _uiState.value.favoriteProducts.sortedByDescending { it.title }
        )
    }
}