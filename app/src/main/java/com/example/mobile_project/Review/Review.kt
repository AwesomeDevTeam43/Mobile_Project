package com.example.mobile_project.Review

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Review(
    var productId: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var userIcon: String? = null,
    var reviewText: String? = null,
    var rating: Float? = null,
    var hasReviewed: Boolean = false
) {
    constructor() : this(null, null, null, null, null, null, false)
}