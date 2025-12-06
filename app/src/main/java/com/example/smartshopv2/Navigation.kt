package com.example.smartshopv2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartshopv2.auth.SignUpScreen
import com.example.smartshopv2.auth.LoginScreen
import com.example.smartshopv2.ui.HomeScreen

@Composable
fun Navigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    
    // Navigation Host
    NavHost(
        navController = navController, 
        startDestination = "signup",
        modifier = Modifier.padding(innerPadding)
    ) {
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
