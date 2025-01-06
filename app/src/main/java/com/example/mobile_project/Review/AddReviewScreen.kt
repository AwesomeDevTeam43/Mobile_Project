package com.example.mobile_project.Review

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddReviewScreen(productId: String, onReviewAdded: () -> Unit) {
    val viewModel: AddReviewScreenModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }
    var hasReviewed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.hasUserReviewed(productId, userId) { reviewed ->
            hasReviewed = reviewed
        }
    }

    if (hasReviewed) {
        Text("You have already reviewed this product.")
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Review") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 0f..5f,
                steps = 4,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val review = Review(
                    productId = productId,
                    userId = userId,
                    reviewText = reviewText,
                    rating = rating,
                    hasReviewed = true
                )
                viewModel.addReview(review, onSuccess = onReviewAdded, onFailure = { /* Handle error */ })
            }) {
                Text("Submit Review")
            }
        }
    }
}