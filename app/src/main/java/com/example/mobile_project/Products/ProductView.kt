package com.example.mobile_project.Products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mobile_project.ui.theme.White01

@Composable
fun ProductView(
    modifier: Modifier = Modifier, product: Product
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(White01.value))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(modifier = Modifier) {
            product.urlImage?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .height(240.dp)
                        .width(240.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(6.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically)
        {
            Column(
                modifier = Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,)

            {
                Text(
                    text = product.title ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = product.description ?: "",
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Category: ${product.category?.name ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Price: ${product.price}€",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductViewPreview() {
    ProductView(
        product = Product(
            "Name",
            "https://media.istockphoto",
            "Description",
            //"Category",
            price = 0.0
        )
    )
}