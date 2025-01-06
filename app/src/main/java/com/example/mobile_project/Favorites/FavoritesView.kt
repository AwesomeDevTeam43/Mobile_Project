package com.example.mobile_project.Favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobile_project.Components.BottomBar
import com.example.mobile_project.Components.TopBar

@Composable
fun FavoritesView(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: FavoritesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Favorites",
                actions = {}
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    uiState.favoriteProducts.forEach { product ->
                        Text(text = product.title ?: "", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}