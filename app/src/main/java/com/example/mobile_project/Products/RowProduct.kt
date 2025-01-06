package com.example.mobile_project.Products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_project.R
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme
import java.util.Date
import coil.compose.AsyncImage
import com.example.mobile_project.Components.formatPrice
import com.example.mobile_project.ui.theme.Blue02


@Composable
fun RowProduct(
    modifier: Modifier = Modifier,
    product: Product
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Imagem em tela cheia horizontal
        product.images?.firstOrNull()?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Descrição
        Text(
            text = product.title ?: "No Title",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = product.description ?: "No Description",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Price: ${product.price?.formatPrice()}",
            style = MaterialTheme.typography.bodyLarge,
            color = Blue02
        )
    }
}




@Preview(showBackground = true)
@Composable
fun RowArticlePreview() {
    Mobile_ProjectTheme() {
        RowProduct(
            product = Product(
                title = "Product Name",
                images = listOf("https://media.istockphoto"),
                description = "Description",
                price = 0.0
            )
        )
    }
}