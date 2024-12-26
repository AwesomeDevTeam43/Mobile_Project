package com.example.mobile_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.Home.HomeView
import com.example.mobile_project.Login.LoginView
import com.example.mobile_project.Login.RegisterView
import com.example.mobile_project.Products.ProductDetail
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

const val TAG = "MobileProject"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mobile_ProjectTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Login.route
                    ) {
                        composable(Screen.Login.route) {
                            LoginView(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route)
                                }
                            )
                        }
                        composable(Screen.Register.route) {
                            RegisterView(
                                onRegisterSuccess = {
                                    navController.navigate(Screen.Login.route)
                                },
                                navController = navController
                            )
                        }
                        composable(Screen.Home.route) {
                            HomeView(navController = navController)
                        }
                        composable(Screen.ProductDetail.route){
                            val url = it.arguments?.getString("articleUrl")
                            ProductDetail(
                                modifier = Modifier.padding(innerPadding),
                                url = url ?: ""
                            )
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    val auth = Firebase.auth
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        navController.navigate(Screen.Home.route)
                    }
                }
            }
        }
    }
}

sealed class Screen (val route:String){
    object Login : Screen("login")
    object Home : Screen("home")
    object Register : Screen("register")
    object ProductDetail : Screen("productDetail/{productUrl}")
}