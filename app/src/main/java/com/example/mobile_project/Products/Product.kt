package com.example.mobile_project.Products

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Product(
    var id: Int? = null,
    var title: String? = null,
    var price: Double? = 0.0,
    var description: String? = null,
    var category: Category? = null,
    var images: List<String>? = null,
    var url: String? = null,
) {
    constructor() : this(null, null, 0.0, null, null, null, null)
}

data class Category(
    val id: Int = 0,
    val name: String = "",
    val image: String = ""
)