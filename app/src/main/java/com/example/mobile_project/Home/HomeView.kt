package com.example.mobile_project.Home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.Products.Product
import com.example.mobile_project.Products.RowProduct
import com.example.mobile_project.Screen
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    HomeViewContent(
        modifier = modifier,
        uiState = uiState,
        navController = navController
    )
    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }
}

@Composable
fun HomeViewContent(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uiState: ProductsState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            Text("Loading products...")
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else if (uiState.products.isEmpty()) {
            Text("No products found!")
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(
                    items = uiState.products,
                ) { index, product ->
                    RowProduct(
                        modifier = Modifier
                            .clickable {
                                Log.d("mobile_Project", product.url ?: "none")
//                                navController.navigate(
//                                    Screen.ProductDetail.route
//                                        .replace("{product}", product.url?.encodeURL()?:"")
//                                )
                            },
                        product = product
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    val products = arrayListOf(
        Product(
            "Name 1",
            "https://media.istockphoto",
            "Description",
            //"Category",
            price = 0.0
        ),

    Product(
        "Name 2",
        "https://media.istockphoto",
        "Description",
        //"Category",
        price = 0.0
    )
    )

    Mobile_ProjectTheme {
        HomeViewContent(
            uiState = ProductsState(
                products = products,
                isLoading = false,
                error = null
            )
        )
    }
}