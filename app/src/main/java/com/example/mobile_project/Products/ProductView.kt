package com.example.mobile_project.Products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mobile_project.Components.formatPrice
import com.example.mobile_project.ui.theme.White01
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProductView(
    modifier: Modifier = Modifier, productId: String?, navController: NavHostController
) {
    val product = remember { mutableStateOf<Product?>(null) }
    val categoryName = remember { mutableStateOf("N/A") }

    LaunchedEffect(productId) {
        if (productId == null) {
            Log.d("ProductView", "Product ID is null")
            return@LaunchedEffect
        } else {
            productId?.let {
                Log.d("ProductView", "Fetching product with ID: $it")
                FirebaseFirestore.getInstance().collection("products").document(it)
                    .get()
                    .addOnSuccessListener { document ->
                        val fetchedProduct = document.toObject(Product::class.java)
                        product.value = fetchedProduct
                        Log.d("ProductView", "Fetched product: $fetchedProduct")
                        fetchedProduct?.category?.let { categoryId ->
                            Log.d("ProductView", "Fetching category with ID: $categoryId")
                            FirebaseFirestore.getInstance().collection("categories")
                                .document(categoryId)
                                .get()
                                .addOnSuccessListener { categoryDocument ->
                                    val category = categoryDocument.toObject(Category::class.java)
                                    categoryName.value = category?.name ?: "N/A"
                                    Log.d("ProductView", "Fetched category: $category")
                                }
                                .addOnFailureListener {
                                    categoryName.value = "N/A"
                                    Log.d("ProductView", "Failed to fetch category")
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.d("ProductView", "Failed to fetch product")
                    }
            }
        }
    }

    product.value?.let { product ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(White01.value))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier) {
                product.images?.firstOrNull()?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .height(240.dp)
                            .width(240.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(6.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically)
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = product.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = product.description ?: "",
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Category: ${categoryName.value}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Price: ${product.price?.formatPrice()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


