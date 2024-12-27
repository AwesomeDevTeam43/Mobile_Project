package com.example.mobile_project.Products

import org.json.JSONObject

data class Product(
    var title: String? = null,
    var urlImage: String? = null,
    var description: String? = null,
    var category: Category? = null,
    var price: Double? = 0.0,
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

            return Product(
                title = json.optString("title", null),
                urlImage = json.optJSONArray("images")?.optString(0, null),
                description = json.optString("description", null),
                category = category,
                price = json.optDouble("price", 0.0),
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