package com.example.smartshopv2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartshopv2.auth.SignUpScreen
import com.example.smartshopv2.auth.LoginScreen
import com.example.smartshopv2.data.local.AppDatabase
import com.example.smartshopv2.data.repository.ProductRepository
import com.example.smartshopv2.ui.screens.ProductFormScreen
import com.example.smartshopv2.ui.screens.ProductListScreen
import com.example.smartshopv2.ui.viewmodel.ProductViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Navigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Dependency Injection
    val database = AppDatabase.getDatabase(context)
    val repository = ProductRepository(
        dao = database.productDao(),
        firestore = FirebaseFirestore.getInstance()
    )
    
    val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

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
        composable("productList") {
            val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
            ProductListScreen(
                viewModel = productViewModel,
                onAddClick = { navController.navigate("productForm") },
                onEditClick = { product -> navController.navigate("productForm?productId=${product.id}") },
                onChartClick = { navController.navigate("chart") }
            )
        }
        composable(
            route = "productForm?productId={productId}",
            arguments = listOf(navArgument("productId") { 
                nullable = true
                defaultValue = null
                type = NavType.StringType 
            })
        ) { backStackEntry ->
            val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
            val productId = backStackEntry.arguments?.getString("productId")
             ProductFormScreen(
                viewModel = productViewModel,
                productId = productId,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
//        composable("chart") {
//            val productViewModel: ProductViewModel = viewModel(factory = viewModelFactory)
//            ChartScreen(
//                viewModel = productViewModel,
//                onBack = { navController.popBackStack() }
//            )
//        }
    }
}
