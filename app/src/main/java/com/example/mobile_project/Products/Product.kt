package com.example.mobile_project.Products

import com.example.mobile_project.Home.parseDate
import org.json.JSONObject
import java.util.Date

data class Product(
    var title: String? = null,
    var urlImage: String? = null,
    var description: String? = null,
    var category: String? = null,
    var price: Double? = 0.0,
    var url: String? = null,
) {
    companion object {
        fun fromJson(json: JSONObject): Product {
            return Product(
                title = json.optString("title", null),
                urlImage = json.optJSONArray("images")?.optString(0, null),
                description = json.optString("description", null),
                category = json.optString("category", null),
                price = json.optDouble("price", 0.0),
                url = json.optString("url", null),
            )
        }
    }
}