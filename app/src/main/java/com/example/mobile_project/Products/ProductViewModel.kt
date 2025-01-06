package com.example.mobile_project.Products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _categoryName = MutableStateFlow("N/A")
    val categoryName: StateFlow<String> = _categoryName

    fun fetchProduct(productId: String) {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection("products").document(productId)
                .get()
                .addOnSuccessListener { document ->
                    val fetchedProduct = document.toObject(Product::class.java)
                    fetchedProduct?.id = productId // Ensure productId is set
                    _product.value = fetchedProduct
                    Log.d("ProductViewModel", "Fetched product: $fetchedProduct")
                    fetchedProduct?.category?.let { categoryId ->
                        fetchCategory(categoryId)
                    }
                }
                .addOnFailureListener {
                    Log.d("ProductViewModel", "Failed to fetch product")
                }
        }
    }

    private fun fetchCategory(categoryId: String) {
        FirebaseFirestore.getInstance().collection("categories").document(categoryId)
            .get()
            .addOnSuccessListener { categoryDocument ->
                val category = categoryDocument.toObject(Category::class.java)
                _categoryName.value = category?.name ?: "N/A"
                Log.d("ProductViewModel", "Fetched category: $category")
            }
            .addOnFailureListener {
                _categoryName.value = "N/A"
                Log.d("ProductViewModel", "Failed to fetch category")
            }
    }
}