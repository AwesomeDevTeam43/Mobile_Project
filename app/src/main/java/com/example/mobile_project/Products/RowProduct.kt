package com.example.mobile_project.Products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_project.R
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme
import java.util.Date
import coil.compose.AsyncImage
import com.example.mobile_project.Home.NormalDate


@Composable
fun RowProduct(modifier: Modifier = Modifier, product: Product, errorMessage: String? = null) {
    if (errorMessage != null) {
        Text(
            text = "Error: $errorMessage",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Row(modifier = modifier) {
            product.urlImage?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "image article",
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(6.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Image(
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(6.dp),
                    painter = painterResource(id = R.drawable.no_img),
                    contentDescription = "product image",
                    contentScale = ContentScale.Crop,
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.description ?: "",
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(text = product.publishedAt?.NormalDate() ?: "")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RowArticlePreview() {
    Mobile_ProjectTheme() {
        RowProduct(product = Product(
            name = "Capacete de Moto",
            price = 100.0,
            urlImage = "https://s1.medias-norauto.pt/images_produits/3501369716290_1/900x900/capacete-de-moto-wayscral-integral-road-vision-preto-xl--2631932.jpg",
            description = "Protetor de cabeça para motociclistas",  
            category = "Motociclistas",
            stock = 10,
            rating = 4.5,
            publishedAt = Date(2023, 1, 1)
        ))
    }
}