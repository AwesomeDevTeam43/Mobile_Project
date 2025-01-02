package com.example.mobile_project.Products

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Category(
    var id: String = "",
    var name: String = "",
    var image: String = ""
) {
    constructor() : this("", "", "")
}