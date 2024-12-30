package com.example.mobile_project.Components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home"
            )
        }
        Spacer(modifier = Modifier.weight(1f)) // Espaçamento flexível para centralizar os ícones
        IconButton(onClick = { navController.navigate("favorites") }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorites"
            )
        }
        IconButton(onClick = { navController.navigate("profile") }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    val navController = rememberNavController() // Criação de um NavController fictício
    BottomBar(navController = navController)
}
