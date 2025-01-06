package com.example.mobile_project.Products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mobile_project.Components.formatPrice
import com.example.mobile_project.ui.theme.White01

@Composable
fun ProductView(
    modifier: Modifier = Modifier, productId: String?, navController: NavHostController
) {
    val viewModel: ProductViewModel = viewModel()
    val product = viewModel.product.collectAsState().value
    val categoryName = viewModel.categoryName.collectAsState().value

    LaunchedEffect(productId) {
        if (productId == null) {
            Log.d("ProductView", "Product ID is null")
        } else {
            viewModel.fetchProduct(productId)
        }
    }

    product?.let { product ->
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
                        text = "Category: $categoryName",
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