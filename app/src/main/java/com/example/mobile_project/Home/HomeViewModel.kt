package com.example.mobile_project.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobile_project.Products.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProductsState (
    val products: ArrayList<Product> = arrayListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()

    fun fetchProducts() {
        _uiState.value = ProductsState(
            isLoading = true,
            error = null
        )

        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val productsResult = arrayListOf<Product>()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    productsResult.add(product)
                }
                _uiState.value = ProductsState(
                    products = productsResult,
                    isLoading = false,
                    error = null
                )
            }
            .addOnFailureListener { exception ->
                Log.e("HomeViewModel", "Error getting documents: ", exception)
                _uiState.value = ProductsState(
                    isLoading = false,
                    error = exception.message
                )
            }
    }
}