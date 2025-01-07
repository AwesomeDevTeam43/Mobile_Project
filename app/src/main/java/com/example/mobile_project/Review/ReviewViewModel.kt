package com.example.mobile_project.Review

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ReviewsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    fun getReviewsForProduct(productId: String): Flow<List<Review>> = flow {
        try {
            Log.d("ReviewsViewModel", "Fetching reviews for product ID: $productId")
            val snapshot = firestore.collection("reviews")
                .whereEqualTo("productId", productId)
                .get()
                .await()
            val reviews = snapshot.toObjects(Review::class.java)

            val reviewsWithUserDetails = reviews.map { review ->
                val userSnapshot = firestore.collection("users").document(review.userId!!).get().await()
                val userName = userSnapshot.getString("username") ?: "Anonymous"
                val userIcon = userSnapshot.getString("icon") ?: ""
                Log.d("ReviewsViewModel", "Fetched user details: User ID: ${review.userId}, User Name: $userName, User Icon: $userIcon")
                review.copy(userName = userName, userIcon = userIcon)
            }

            Log.d("ReviewsViewModel", "Fetched ${reviewsWithUserDetails.size} reviews for product ID: $productId")
            emit(reviewsWithUserDetails)
        } catch (e: Exception) {
            Log.e("ReviewsViewModel", "Error fetching reviews: ${e.message}")
            emit(emptyList())
        }
    }
}
