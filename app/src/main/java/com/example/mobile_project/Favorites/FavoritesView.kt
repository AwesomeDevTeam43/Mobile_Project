package com.example.mobile_project.Favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobile_project.Components.BottomBar
import com.example.mobile_project.Components.TopBar
import com.example.mobile_project.Products.RowProduct

@Composable
fun FavoritesView(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: FavoritesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Favorites",
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sort by Price: Low to High") },
                            onClick = {
                                viewModel.sortProductsByPriceAscending()
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Price: High to Low") },
                            onClick = {
                                viewModel.sortProductsByPriceDescending()
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Name: A-Z") },
                            onClick = {
                                viewModel.sortProductsByNameAscending()
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Name: Z-A") },
                            onClick = {
                                viewModel.sortProductsByNameDescending()
                                expanded = false
                            }
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Box(modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else if (uiState.favoriteProducts.isEmpty()) {
                Text(text = "No favorite products found!", color = Color.Black)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.favoriteProducts) { product ->
                        RowProduct(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("product/${product.id}")
                                    Log.d("FavoritesView", "Clicked on product: ${product.id}")
                                },
                            product = product
                        )
                    }
                }
            }
        }
    }
}