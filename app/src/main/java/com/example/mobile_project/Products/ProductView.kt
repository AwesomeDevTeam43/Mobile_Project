package com.example.mobile_project.Products

import ProductViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.mobile_project.Review.Review
import com.example.mobile_project.Review.ReviewsViewModel
import com.example.mobile_project.ui.theme.White01

@Composable
fun ProductView(
    modifier: Modifier = Modifier, productId: String?, navController: NavHostController
) {
    val viewModel: ProductViewModel = viewModel()
    val product by viewModel.product.collectAsState()
    val categoryName by viewModel.categoryName.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

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
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = product.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = product.description ?: "",
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Category: $categoryName",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Price: ${product.price?.formatPrice()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        if (isFavorite) {
                            viewModel.removeFromFavorites(product.id!!)
                        } else {
                            viewModel.addToFavorites(product.id!!)
                        }
                    }) {
                        Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
                    }
                    product.id?.let { ReviewsSection(productId = it, navController = navController) }
                }
            }
        }
    }
}

@Composable
fun ReviewsSection(productId: String, navController: NavHostController) {
    val viewModel: ReviewsViewModel = viewModel()
    val reviews by viewModel.getReviewsForProduct(productId).collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "AddReview",

                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate("review/$productId")
                    }
            )
        }

        if (reviews.isEmpty()) {
            Log.d("ReviewsSection", "No reviews found for product ID: $productId")
            Text(text = "No reviews yet.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reviews) { review ->
                    Log.d("ReviewsSection", "Displaying review: $review")
                    ReviewItem(review = review)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Row(modifier = Modifier.padding(8.dp)) {
        review.userIcon?.let { iconUrl ->
            AsyncImage(
                model = iconUrl,
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = review.userName ?: "Anonymous",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = "Rating: ${review.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.reviewText ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}