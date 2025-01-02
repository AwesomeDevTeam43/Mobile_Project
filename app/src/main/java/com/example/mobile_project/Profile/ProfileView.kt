package com.example.mobile_project.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme

@Composable
fun ProfileView(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White),
    navController: NavController = rememberNavController()
) {
    val viewModel: ProfileViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBar(title = "Profile") },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        ProfileViewContent(
            modifier = modifier.padding(paddingValues),
            uiState = uiState,
            onLogout = {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun ProfileViewContent(
    modifier: Modifier = Modifier,
    uiState: ProfileState,
    onLogout: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Username: ${uiState.username}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${uiState.email}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onLogout) {
                    Text("Logout")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    Mobile_ProjectTheme {
        ProfileViewContent(
            uiState = ProfileState(
                username = "John Doe",
                email = "john.doe@example.com",
                isLoading = false,
                error = null
            ),
            onLogout = {}
        )
    }
}