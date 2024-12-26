package com.example.mobile_project.Home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.Products.RowProduct
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mobile_project.Products.Product
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
        navController = navController,
        uiState = uiState
    )

    LaunchedEffect(Unit) {
        viewModel.fetchArticles()
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
            Text("Loading articles...")
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else if (uiState.products.isEmpty()) {
            Text("No articles found!")
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
//                                Log.d("dailynews",article.url ?:"none")
//                                navController.navigate(
//                                    Screen.ArticleDetail.route
//                                        .replace("{articleUrl}", article.url?.encodeURL()?:"")
//                                )
                                Log.d("HomeView", "Product clicked: ${product.name}")
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
            name = "Capacete",
            urlImage = "https://s1.medias-norauto.pt/images_produits/3501369716290_1/900x900/capacete-de-moto-wayscral-integral-road-vision-preto-xl--2631932.jpg",
            description = "Protetor de cabeça para motociclistas"
        ),
        Product(
            name = "Casaca para MotoCiclistas",
            urlImage="https://www.lojamotard.pt/facebook/imgs/5145.png",
            description = "Casaca Protetora para motociclistas"
        )
    )

    //val articles = arrayListOf<Article>()
    Mobile_ProjectTheme() {
        HomeViewContent(
            uiState = ProductsState(
                products = products,
                isLoading = false,
                error = null
            )
        )
    }
}