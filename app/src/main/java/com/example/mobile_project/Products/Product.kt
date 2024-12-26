package com.example.mobile_project.Products

import com.example.mobile_project.Home.parseDate
import org.json.JSONObject
import java.util.Date

data class Product(
   // var id: Int? = 0,
    var name: String? = null,
    //var url: String? = null,
    var urlImage: String? = null,
    var imagesUrl: Array<String>? = null,
    var description: String? = null,
    var category: String? = null,
    var price: Double? = 0.0,
    var stock: Int? = 0,
    var rating: Double? = 0.0,
    var publishedAt: Date? = null
) {
    companion object {
        fun fromJson(json: JSONObject): Product {
            return Product(
                name = json.getString("title"),
                description = json.getString("description"),
                urlImage = json.getString("urlToImage"),
                publishedAt = json.getString("publishedAt").parseDate()
            )
        }
    }
}