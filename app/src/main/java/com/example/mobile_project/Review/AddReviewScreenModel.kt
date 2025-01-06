package com.example.mobile_project.Review

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AddReviewScreenModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    fun addReview(review: Review, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("reviews")
            .add(review)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun hasUserReviewed(productId: String, userId: String, callback: (Boolean) -> Unit) {
        firestore.collection("reviews")
            .whereEqualTo("productId", productId)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                callback(!result.isEmpty)
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }
}