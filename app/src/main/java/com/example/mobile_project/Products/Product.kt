package com.example.mobile_project.Products

import com.example.mobile_project.Home.parseDate
import org.json.JSONObject
import java.util.Date

data class Product(
    var name: String? = null,
    var urlImage: String? = null,
    var description: String? = null,
    var category: String? = null,
    var price: Double? = 0.0,
    var stock: Int? = 0,
    var rating: Double? = 0.0,
    var publishedAt: Date? = null,
    var url: String? = null,
) {
    companion object {
        fun fromJson(json: JSONObject): Product {
            val ratingObject = json.getJSONObject("rating")
            return Product(
                name = json.getString("title"),
                urlImage = json.getString("image"),
                description = json.getString("description"),
                category = json.getString("category"),
                price = json.getDouble("price"),
                stock = ratingObject.getInt("count"), // Supondo que 'count' representa o estoque
                rating = ratingObject.getDouble("rate"),
                publishedAt = null, // Defina uma data padrão ou omita este campo
                url = json.getString("url"),
            )
        }
    }
}