package com.example.mobile_project.Products

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {

    private val _productState = MutableStateFlow<Product?>(null)
    val productState: StateFlow<Product?> = _productState.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()

    fun getProduct(productId: String) {
        firestore.collection("products").document(productId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val product = document.toObject(Product::class.java)
                    _productState.value = product
                } else {
                    Log.e("ProductViewModel", "No such document")
                    _productState.value = null
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ProductViewModel", "Error fetching product: ", exception)
                _productState.value = null
            }
    }
}
