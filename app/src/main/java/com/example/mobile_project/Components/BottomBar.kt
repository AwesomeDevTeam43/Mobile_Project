package com.example.mobile_project.Components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.ui.theme.Orange01

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp), // Remove qualquer padding
        containerColor = Orange01
    ) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navController.navigate("favorites") }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorites",
                tint = Color.White
            )
        }
        IconButton(onClick = { navController.navigate("profile") }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White
            )
        }
    }
}






@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    val navController = rememberNavController()
    BottomBar(navController = navController)
}
