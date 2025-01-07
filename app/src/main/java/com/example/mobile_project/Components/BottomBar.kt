package com.example.mobile_project.Components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.ui.theme.Orange01
import com.example.mobile_project.ui.theme.Orange02
import com.example.mobile_project.ui.theme.White01

@Composable
fun BottomBar(navController: NavController = rememberNavController()) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = Orange01
    ) {
        var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                navController.navigate("home")
            },
            label = {
                Text("Home")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Orange02,
            )

        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                navController.navigate("profile")
            },
            label = {
                Text("Profile")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Orange02,
            )
        )
    }
}






@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    val navController = rememberNavController()
    BottomBar(navController = navController)
}
