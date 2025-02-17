package com.example.mobile_project.Review

import RatingBar
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_project.ui.theme.Black01
import com.example.mobile_project.ui.theme.Orange01
import com.example.mobile_project.ui.theme.White01
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(productId: String, onReviewAdded: () -> Unit) {
    val viewModel: AddReviewScreenModel = viewModel()
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: ""
    val userName = user?.displayName ?: "Anonymous"
    val userIcon = user?.photoUrl?.toString() ?: ""

    Log.d("AddReviewScreen", "User ID: $userId, User Name: $userName, User Icon: $userIcon")

    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }
    var hasReviewed by remember { mutableStateOf(false) }
    var hasBeenPressed by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        viewModel.hasUserReviewed(productId, userId) { reviewed ->
            hasReviewed = reviewed
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White01),
        contentAlignment = Alignment.Center
    ) {
        if (hasReviewed) {
            Text("You have already reviewed this product.")
        } else {
            Column(modifier = Modifier
                .padding(16.dp)
                .background(color = White01)) {
                TextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Review") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White01),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = White01,
                        cursorColor = Black01,
                        focusedIndicatorColor = Black01,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(25.dp))
                RatingBar(
                    rating = rating,
                    onRatingChanged = { rating = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    onClick = {
                        if (!hasBeenPressed) {
                            hasBeenPressed = true
                            val review = Review(
                                productId = productId,
                                userId = userId,
                                userName = userName,
                                userIcon = userIcon,
                                reviewText = reviewText,
                                rating = rating,
                                hasReviewed = true
                            )
                            Log.d("AddReviewScreen", "Submitting review: $review")
                            viewModel.addReview(
                                review,
                                onSuccess = onReviewAdded,
                                onFailure = { exception ->
                                    Log.e(
                                        "AddReviewScreen",
                                        "Failed to add review: ${exception.message}"
                                    )
                                    hasBeenPressed = false
                                })
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange01,
                        contentColor = White01
                    )
                ) {
                    Text("Submit Review")
                }
            }
        }
    }
}