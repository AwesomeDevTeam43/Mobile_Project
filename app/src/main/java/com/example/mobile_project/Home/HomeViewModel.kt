package com.example.mobile_project.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobile_project.Products.Product
import com.example.mobile_project.Products.Category
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
                    fetchCategoryForProduct(product) { category ->
                        product.category = category?.name
                        productsResult.add(product)
                        if (productsResult.size == result.size()) {
                            productsResult.sortBy { it.title }
                            _uiState.value = ProductsState(
                                products = productsResult,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("HomeViewModel", "Error getting documents: ", exception)
                _uiState.value = ProductsState(
                    isLoading = false,
                    error = exception.message
                )
            }
    }

    private fun fetchCategoryForProduct(product: Product, callback: (Category?) -> Unit) {
        product.category?.let { categoryId ->
            firestore.collection("categories").document(categoryId)
                .get()
                .addOnSuccessListener { document ->
                    val category = document.toObject(Category::class.java)
                    callback(category)
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeViewModel", "Error getting category: ", exception)
                    callback(null)
                }
        } ?: callback(null)
    }
}