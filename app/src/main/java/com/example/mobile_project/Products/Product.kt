package com.example.mobile_project.Products

import org.json.JSONObject

data class Product(
    var id: Int? = null,
    var title: String? = null,
    var price: Double? = 0.0,
    var description: String? = null,
    var category: Category? = null,
    var images: List<String>? = null,
    var url: String? = null,
) {
    companion object {
        fun fromJson(json: JSONObject): Product {
            val categoryJson = json.getJSONObject("category")
            val category = Category(
                id = categoryJson.getInt("id"),
                name = categoryJson.getString("name"),
                image = categoryJson.getString("image")
            )

            val imagesJsonArray = json.getJSONArray("images")
            val imagesList = mutableListOf<String>()
            for (i in 0 until imagesJsonArray.length()) {
                imagesList.add(imagesJsonArray.getString(i))
            }

            return Product(
                id = json.optInt("id"),
                title = json.optString("title", null),
                price = json.optDouble("price", 0.0),
                description = json.optString("description", null),
                category = category,
                images = imagesList,
                url = json.optString("url", null),
            )
        }
    }
}

data class Category(
    val id: Int,
    val name: String,
    val image: String
)