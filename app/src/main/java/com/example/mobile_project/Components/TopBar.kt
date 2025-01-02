package com.example.mobile_project.Components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile_project.ui.theme.Orange01

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(text = title, color = Color.White) },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange01
        ),
        windowInsets = WindowInsets(0, 0, 0, 0) // Remove qualquer margem do sistema
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        title = "Home",
        actions = {
            IconButton(onClick = { /* Ação para pesquisar */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Ação para configurações */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, // Ícone de 3 pontos
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        }
    )
}
