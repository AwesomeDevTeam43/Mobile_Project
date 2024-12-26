package com.example.mobile_project.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobile_project.Products.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

data class ProductsState (
    val products: ArrayList<Product> = arrayListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState.asStateFlow()

    fun fetchProducts() {
        _uiState.value = ProductsState(
            isLoading = true,
            error = null
        )

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://fakestoreapi.com/products")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HomeViewModel", "API call failed", e)
                _uiState.value = ProductsState(
                    isLoading = false,
                    error = e.message
                )
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.e("HomeViewModel", "Unexpected response code: ${response.code}")
                        _uiState.value = ProductsState(
                            isLoading = false,
                            error = "Unexpected response code: ${response.code}"
                        )
                        return
                    }

                    val result = response.body?.string() ?: ""
                    Log.d("HomeViewModel", "API response: $result")

                    try {
                        val productsJson = JSONArray(result)
                        val productsResult = arrayListOf<Product>()
                        for (index in 0 until productsJson.length()) {
                            val productJson = productsJson.getJSONObject(index)
                            val product = Product.fromJson(productJson)
                            productsResult.add(product)
                        }
                        _uiState.value = ProductsState(
                            products = productsResult,
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error parsing response", e)
                        _uiState.value = ProductsState(
                            isLoading = false,
                            error = "Error parsing response: ${e.message}"
                        )
                    }
                }
            }
        })
    }
}