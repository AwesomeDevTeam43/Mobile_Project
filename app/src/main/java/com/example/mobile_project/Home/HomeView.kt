package com.example.mobile_project.Home


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.Components.BottomBar
import com.example.mobile_project.Components.TopBar
import com.example.mobile_project.Products.RowProduct
import com.example.mobile_project.Products.Product
import com.example.mobile_project.Profile.ProfileViewModel
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme

@Composable
fun HomeView(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White),
    navController: NavController = rememberNavController()
) {
    val viewModel: HomeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val showLogoutDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(title = "Home") },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        HomeViewContent(
            modifier = modifier.padding(paddingValues),
            uiState = uiState,
            navController = navController
        )
    }

    BackHandler(enabled = true) {
        showLogoutDialog.value = true
    }

    if (showLogoutDialog.value) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog.value = false
                profileViewModel.logout() // Perform logout
                navController.popBackStack() // Navigate back to the login screen
            },
            onDismiss = {
                showLogoutDialog.value = false
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }
}
@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Confirm Logout") },
        text = { Text("Are you sure you want to logout?") },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("Logout")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HomeViewContent(
    modifier: Modifier = Modifier
        .fillMaxSize(),
    navController: NavController = rememberNavController(),
    uiState: ProductsState
) {
    Box(
        modifier = modifier,
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.products) { product ->
                    RowProduct(
                        modifier = modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                Log.d("Product Clicked", product.url ?: "none")
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
            title = "Name 1",
            images = listOf("https://media.istockphoto"),
            description = "Description",
            price = 0.0
        ),

        Product(
            title = "Name 2",
            images = listOf("https://media.istockphoto"),
            description = "Description",
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