package com.example.smartshopv2

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartshopv2.auth.SignUpScreen
import com.example.smartshopv2.auth.LoginScreen
import com.example.smartshopv2.ui.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    
    // Navigation Host
    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") {
            SignUpScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}
